package com.jie.flink;

import lombok.SneakyThrows;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @Author ZhuPo
 * @date 2025/11/4 21:35
 */
public class StreamWordCount {
    @SneakyThrows
    public static void main(String[] args) {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> lineDS = executionEnvironment.readTextFile("input/words.txt");
        lineDS.flatMap(new FlatMapFunction<String, Tuple2<String,Long>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Long>> collector) throws Exception {
                String[] words = s.split(" ");

                for (String word : words) {
                    collector.collect(Tuple2.of(word, 1L));
                }
            }
        })
                // 分组操作调用的是keyBy方法，可以传入一个匿名函数作为键选择器
                .keyBy(0)
                .sum(1).print();

        executionEnvironment.execute();

    }

}
