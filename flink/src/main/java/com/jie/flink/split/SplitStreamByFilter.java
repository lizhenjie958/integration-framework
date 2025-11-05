package com.jie.flink.split;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 最为原始的分流实现方式
 * @author ZhuPo
 * @date 2025/11/5 16:21
 */
public class SplitStreamByFilter {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<Integer> lineDS = env.socketTextStream("127.0.0.1", 9999)
                .map(Integer::valueOf);
        SingleOutputStreamOperator<Integer> ds1 = lineDS.filter(i -> i % 2 == 0);
        SingleOutputStreamOperator<Integer> ds2 = lineDS.filter(i -> i % 2 != 0);

        ds1.print("偶数");
        ds2.print("奇数");

        env.execute();
    }
}
