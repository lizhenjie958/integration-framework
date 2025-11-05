package com.jie.flink.read;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

/**
 * @author ZhuPo
 * @date 2025/11/5 11:11
 */
public class ReadDataFromCollection {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Integer> lineDS = executionEnvironment.fromCollection(Arrays.asList(1, 2, 3, 4, 5));
        lineDS.print();
        executionEnvironment.execute();
    }
}
