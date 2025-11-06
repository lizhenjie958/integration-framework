package com.jie.flink.aggregation;

import com.jie.flink.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * keyBy得到的结果将不再是DataStream，而是会将DataStream转换为KeyedStream。
 * KeyedStream可以认为是“分区流”或者“键控流”，它是对DataStream按照key的一个逻辑分区
 * @author ZhuPo
 * @date 2025/11/5 14:13
 */
public class TransKeyBy {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<WaterSensor> lineDS = environment.fromElements(
                new WaterSensor("sensor_1", 1L, 1),
                new WaterSensor("sensor_1", 2L, 2),
                new WaterSensor("sensor_2", 3L, 3),
                new WaterSensor("sensor_2", 4L, 4),
                new WaterSensor("sensor_2", 5L, 5)
        );

        KeyedStream<WaterSensor, String> keyStream = lineDS.keyBy(WaterSensor::getId);

        keyStream.print();

        environment.execute();
    }
}
