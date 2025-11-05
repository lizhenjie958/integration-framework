package com.jie.flink.read;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Flink从1.11开始提供了一个内置的DataGen 连接器，
 * 主要是用于生成一些随机数，用于在没有数据源的时候，进行流任务的测试以及性能测试等
 * @author ZhuPo
 * @date 2025/11/5 13:45
 */
public class ReadDataFromDataGenerator {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataGeneratorSource<String> dataSource = new DataGeneratorSource<>(new GeneratorFunction<Long, String>() {
            @Override
            public String map(Long aLong) throws Exception {
                return "NUMBER:" + aLong;
            }
        }, Long.MAX_VALUE, RateLimiterStrategy.perSecond(10),
                Types.STRING
        );
        DataStreamSource<String> stream = env.fromSource(dataSource, WatermarkStrategy.noWatermarks(), "datagenerator");
        stream.print();
        env.execute();
    }
}
