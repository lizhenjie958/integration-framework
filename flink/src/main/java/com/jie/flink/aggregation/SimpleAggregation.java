package com.jie.flink.aggregation;

import com.jie.flink.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 在输入流上，对指定的字段做叠加<>的操作
 * sum、min、max、minBy、maxBy
 * @author ZhuPo
 * @date 2025/11/5 14:19
 */
public class SimpleAggregation {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<WaterSensor> stream = environment.fromElements(
                new WaterSensor("sensor_1", 1L, 1),
                new WaterSensor("sensor_1", 2L, 2),
                new WaterSensor("sensor_2", 2L, 2),
                new WaterSensor("sensor_3", 3L, 3)
        );

        SingleOutputStreamOperator<WaterSensor> vcMax = stream.keyBy(WaterSensor::getId).max("vc");

        vcMax.print();

        environment.execute();
    }
    }
