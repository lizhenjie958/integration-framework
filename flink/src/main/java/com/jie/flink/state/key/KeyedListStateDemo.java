package com.jie.flink.state.key;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 针对每种传感器输出最高的3个水位值
 *
 * @author ZhuPo
 * @date 2025/11/11 15:15
 */
public class KeyedListStateDemo {
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


                    ListState<Integer> vcListState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);

                        vcListState = getRuntimeContext().getListState(new ListStateDescriptor<Integer>("vcListState", Integer.class));
                    }

                    @Override
                    public void processElement(WaterSensor waterSensor, Context context, Collector<String> out) throws Exception {
                        vcListState.add(waterSensor.getVc());

                        Iterable<Integer> vcListIt = vcListState.get();

                        List<Integer> vcList = new ArrayList<>();

                        for (Integer vc : vcListIt) {
                            vcList.add(vc);
                        }

                        // 降序排序
                        vcList = vcList.stream().sorted(((o1, o2) -> o2 - o1)).limit(3).collect(Collectors.toList());

                        out.collect("传感器id为" + waterSensor.getId() + ",最大的3个水位值=" + vcList.toString());

                        vcListState.update(vcList);
                    }
                });

        process.print();

        environment.execute();
    }
}
