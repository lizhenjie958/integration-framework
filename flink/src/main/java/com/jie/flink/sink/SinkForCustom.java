package com.jie.flink.sink;

import com.jie.flink.function.MySinkFunction;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Author ZhuPo
 * @date 2025/11/5 21:35
 */
public class SinkForCustom {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        // 如果是精准一次，必须开启checkpoint（后续章节介绍）
        environment.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);

        DataStreamSource<String> lineDS = environment.socketTextStream("127.0.0.1", 9999);

        lineDS.addSink(new MySinkFunction());

        environment.execute();
    }
}
