package com.jie.flink.simple;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @Author ZhuPo
 * @date 2025/11/4 21:41
 */
public class SocketStreamWordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        // 2. 读取文本流
        executionEnvironment.socketTextStream("localhost", 9999)
                .flatMap(new FlatMapFunction<String, Tuple2<String,Long>>() {
                    @Override
                    public void flatMap(String s, Collector<Tuple2<String, Long>> collector) throws Exception {
                        String[] words = s.split(" ");

                        for (String word : words) {
                            collector.collect(Tuple2.of(word, 1L));
                        }
                    }
                })
//                .returns(Types.TUPLE(Types.STRING, Types.LONG))
                .keyBy(0)
                .sum(1)
                .print();

        executionEnvironment.execute();
    }
}
