package com.jie.flink.function;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * @Author ZhuPo
 * @date 2025/11/5 21:32
 */
public class MySinkFunction implements SinkFunction<String> {
    @Override
    public void invoke(String value, Context context) throws Exception {
        System.err.println(value);
    }
}
