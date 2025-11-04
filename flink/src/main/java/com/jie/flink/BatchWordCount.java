package com.jie.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
/**
 *  这种代码的实现方式，是基于DataSet API的
 *  从Flink 1.12开始，官方推荐的做法是直接使用DataStream API
 *
 * @Author ZhuPo
 * @date 2025/11/4 21:24
 */
public class BatchWordCount {
    public static void main(String[] args) throws Exception {
        // 创建执行环境
        ExecutionEnvironment executionEnvironment = ExecutionEnvironment.getExecutionEnvironment();
        // 从文件读取数据
        DataSource<String> lineDS = executionEnvironment.readTextFile("input/words.txt");
        AggregateOperator<Tuple2<String, Long>> sum = lineDS.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public void flatMap(String word, Collector<Tuple2<String, Long>> collector) throws Exception {
                        String[] words = word.split(" ");
                        for (String s : words) {
                            collector.collect(Tuple2.of(s, 1L));
                        }
                    }
                })
                .groupBy(0)
                .sum(1);

        sum.print();

    }
}
