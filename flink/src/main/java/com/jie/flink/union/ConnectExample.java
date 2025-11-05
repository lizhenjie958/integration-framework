package com.jie.flink.union;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * 连接流
 *
 * 一次只能连接2条流
 * 流的数据类型可以不一样
 * 连接后可以调用map，flatmap，process来处理，但各处理各的
 * @author ZhuPo
 * @date 2025/11/5 16:42
 */
public class ConnectExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Integer> lineDS1 = env.socketTextStream("127.0.0.1", 9999)
                .map(Integer::valueOf);

        DataStreamSource<String> lineDS2 = env.socketTextStream("127.0.0.1", 8888);

        ConnectedStreams<Integer, String> connectStream = lineDS1.connect(lineDS2);

        SingleOutputStreamOperator<String> result = connectStream.map(new CoMapFunction<Integer, String, String>() {
            @Override
            public String map1(Integer integer) throws Exception {
                return "来源于数字流：" + integer;
            }

            @Override
            public String map2(String s) throws Exception {
                return "来源于字符流" + s;
            }
        });

        result.print();

        env.execute();
    }
}
