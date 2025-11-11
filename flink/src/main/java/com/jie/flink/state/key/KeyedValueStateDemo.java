package com.jie.flink.state.key;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * 检测每种传感器的水位值，如果连续的两个水位值超过10，就输出报警
 *
 * @author ZhuPo
 * @date 2025/11/11 14:54
 */
public class KeyedValueStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS = environment.socketTextStream("localhost", 9999)
                .map(new WaterSensorMapFunction())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner((value, ts) -> value.getTs() * 1000L)
                );

        SingleOutputStreamOperator<String> process = sensorDS.keyBy(WaterSensor::getId)
                .process(new KeyedProcessFunction<String, WaterSensor, String>() {
                    // 1.定义状态
                    ValueState<Integer> lastVcState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        // 在open方法中，初始化状态；
                        // 状态描述器两个参数，第一个参数，起个名字，不重复；第二个参数，状态的数据类型
                        lastVcState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("lastVcState", Integer.class));
                    }

                    @Override
                    public void processElement(WaterSensor waterSensor, Context context, Collector<String> out) throws Exception {
//                                lastVcState.value();    // 取出本组的状态
//                                lastVcState.update(1);   // 更新状态
//                                lastVcState.clear();    //  清空状态
                        // 取出上一条数据的水位值
                        int lastVc = lastVcState.value() == null ? 0 : lastVcState.value();

                        // 求差值的绝对值，判断是否超过10
                        if (Math.abs(waterSensor.getVc() - lastVc) > 10) {
                            out.collect("传感器=" + waterSensor.getId() + "==>当前水位值=" + waterSensor.getVc() + ",与上一条水位值=" + lastVc + ",相差超过10！！！！");
                        }

                        // 3. 更新状态里的水位值
                        lastVcState.update(waterSensor.getVc());
                    }
                });

        process.print();

        environment.execute();
    }
}
