package com.jie.flink.udf;

import com.jie.flink.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ZhuPo
 * @date 2025/11/5 15:42
 */
public class TransFunctionUDF {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<WaterSensor> stream = env.fromElements(
                new WaterSensor("sensor_1", 1L, 1),
                new WaterSensor("sensor_1", 2L, 2),
                new WaterSensor("sensor_2", 2L, 2),
                new WaterSensor("sensor_3", 3L, 3)
        );

        SingleOutputStreamOperator<WaterSensor> filter = stream.filter(sensor -> "sensor_1".equals(sensor.getId()));

        filter.print();
        env.execute();
    }
}
