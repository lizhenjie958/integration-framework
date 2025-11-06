package com.jie.flink.baseoperate;

import com.jie.flink.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ZhuPo
 * @date 2025/11/5 13:53
 */
public class TransMap {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<WaterSensor> lineDS = executionEnvironment.fromElements(
                new WaterSensor("sensor_1", 1L, 1),
                new WaterSensor("sensor_2", 2L, 2)
        );
        lineDS.map(new MapFunction<WaterSensor, String>() {
            @Override
            public String map(WaterSensor waterSensor) throws Exception {
                return waterSensor.getId();
            }
        }).print();

        executionEnvironment.execute();
    }
}
