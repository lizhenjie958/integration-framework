package com.jie.flink.baseoperate;

import com.jie.flink.bean.WaterSensor;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ZhuPo
 * @date 2025/11/5 13:59
 */
public class TransFilter {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<WaterSensor> lineDS = environment.fromElements(
                new WaterSensor("sensor_1", 1L, 1),
                new WaterSensor("sensor_1", 2L, 2),
                new WaterSensor("sensor_2", 2L, 2),
                new WaterSensor("sensor_3", 3L, 3)
        );
        lineDS.filter(new FilterFunction<WaterSensor>() {
            @Override
            public boolean filter(WaterSensor waterSensor) throws Exception {
                return "sensor_1".equals(waterSensor.getId());
            }
        }).print();
        environment.execute();
    }
}
