package com.jie.flink.state.key;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * 计算每种传感器的水位和
 *
 * 归约不是在状态列表里添加元素，而是直接把新数据和之前的状态进行归约，并用得到的结果更新状态
 *
 * @author ZhuPo
 * @date 2025/11/11 15:54
 */
public class KeyedReduceStateDemo {
    public static void main(String[] args) throws Exception { // 主函数入口
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment(); // 创建流执行环境
        environment.setParallelism(1); // 设置并行度为1

        environment.socketTextStream("localhost", 9999) // 从本地9999端口读取socket数据流
                .map(new WaterSensorMapFunction()) // 使用WaterSensorMapFunction将文本映射为WaterSensor对象
                .assignTimestampsAndWatermarks( // 分配时间戳和水印
                        WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3)) // 设置最多3秒的乱序处理
                                .withTimestampAssigner((value, ts) -> value.getTs() * 1000L) // 设置时间戳分配器，将秒转换为毫秒
                )
                .keyBy(WaterSensor::getId) // 按照传感器ID进行分组
                .process(new KeyedProcessFunction<String, WaterSensor, Integer>() {
                    private ReducingState<Integer> sumVcState;
                    @Override
                    public void open(Configuration parameters) throws Exception {
                        sumVcState = this
                                .getRuntimeContext()
                                .getReducingState(new ReducingStateDescriptor<Integer>("sumVcState", Integer::sum, Integer.class));
                    }

                    @Override
                    public void processElement(WaterSensor value, Context ctx, Collector<Integer> out) throws Exception {
                        sumVcState.add(value.getVc());
                        out.collect(sumVcState.get());
                    }
                })
                .print(); // 打印输出结果

        environment.execute(); // 执行Flink作业
    }
}
