package com.jie.flink.state.key;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * 目前的TTL设置只支持处理时间
 *
 * @author ZhuPo
 * @date 2025/11/11 16:06
 */
public class StateTTLDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS = environment.socketTextStream("localhost", 9999)
                .map(new WaterSensorMapFunction())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner((value, ts) -> value.getTs() * 1000L)
                );

        sensorDS.keyBy(WaterSensor :: getId)
                        .process(new KeyedProcessFunction<String, WaterSensor, String>() {
                            ValueState<Integer> lastVcState;

                            @Override
                            public void open(Configuration parameters) throws Exception {
                                super.open(parameters);

                                // TODO 1.创建 StateTtlConfig
                                StateTtlConfig stateTtlConfig =
                                        // 过期时间5S
                                        StateTtlConfig.newBuilder(Time.seconds(5))
//                                      // 状态 创建和写入（更新） 更新 过期时间
                                        .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                                                // 不返回过期的状态值
                                                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                                                .build();

                                // TODO 2.状态描述器 启用 TTL
                                ValueStateDescriptor<Integer> stateDescriptor = new ValueStateDescriptor<>("lastVcState", Types.INT);
                                stateDescriptor.enableTimeToLive(stateTtlConfig);
                                this.lastVcState = getRuntimeContext().getState(stateDescriptor);
                            }

                            @Override
                            public void processElement(WaterSensor waterSensor, Context context, Collector<String> out) throws Exception {
                                // 先获取状态值，打印 ==》 读取状态
                                Integer lastVc = lastVcState.value();
                                out.collect("key=" + waterSensor.getId() + ",状态值=" + lastVc);

                                // 如果水位大于10，更新状态值 ===》 写入状态
                                if (waterSensor.getVc() > 10) {
                                    lastVcState.update(waterSensor.getVc());
                                }
                            }
                        }).print();

        environment.execute();
    }
}
