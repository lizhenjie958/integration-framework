package com.jie.flink.baseoperate;

import com.jie.flink.bean.WaterSensor;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author ZhuPo
 * @date 2025/11/5 14:05
 */
public class TransFlatMap {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<WaterSensor> lineDS = environment.fromElements(
                new WaterSensor("sensor_1", 1L, 1),
                new WaterSensor("sensor_1", 2L, 2),
                new WaterSensor("sensor_2", 2L, 2),
                new WaterSensor("sensor_3", 3L, 3)
        );

        SingleOutputStreamOperator<String> flatMapStream = lineDS.flatMap(new FlatMapFunction<WaterSensor, String>() {
            @Override
            public void flatMap(WaterSensor waterSensor, Collector<String> collector) throws Exception {
                if ("sensor_1".equals(waterSensor.getId())) {
                    collector.collect(waterSensor.getVc() + "");
                } else {
                    collector.collect(waterSensor.getTs() + "");
                    collector.collect(waterSensor.getVc() + "");
                }
            }
        });

        flatMapStream.print();

        environment.execute();
    }
}