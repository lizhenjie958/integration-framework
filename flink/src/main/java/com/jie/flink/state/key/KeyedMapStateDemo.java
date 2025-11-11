package com.jie.flink.state.key;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * 统计每种传感器每种水位值出现的次数
 * @author ZhuPo
 * @date 2025/11/11 15:40
 */
public class KeyedMapStateDemo {
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
                .process(new KeyedProcessFunction<String, WaterSensor, String>() { // 使用键控处理函数处理数据

                    MapState<Integer, Integer> vcCountMapState; // 声明MapState变量用于存储水位值及其出现次数

                    @Override
                    public void open(Configuration parameters) throws Exception { // 初始化方法，在函数实例创建时调用
                        super.open(parameters); // 调用父类的open方法
                        vcCountMapState = getRuntimeContext().getMapState(new MapStateDescriptor<Integer, Integer>("vcCountMapState", Integer.class, Integer.class)); // 初始化MapState，创建名为"vcCountMapState"的状态描述符
                    }

                    @Override
                    public void processElement(WaterSensor waterSensor, Context context, Collector<String> collector) throws Exception { // 处理每个元素的方法
                        Integer i = vcCountMapState.get(waterSensor.getVc()); // 从状态中获取当前水位值的计数
                        if (i == null) { // 如果该水位值第一次出现
                            vcCountMapState.put(waterSensor.getVc(), 1); // 将该水位值计数设为1
                        } else { // 如果该水位值已存在
                            vcCountMapState.put(waterSensor.getVc(), i + 1); // 将该水位值计数加1
                        }
                        collector.collect("传感器id为" + waterSensor.getId() + "， 水位值为" + waterSensor.getVc() + "， 水位值出现的次数为" + vcCountMapState.get(waterSensor.getVc())); // 收集并输出结果
                    }
                })
                .print(); // 打印输出结果


        environment.execute(); // 执行Flink作业
    }
}
