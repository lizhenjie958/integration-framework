package com.jie.sparkstreaming;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ZhuPo
 * @date 2025/11/1 20:42
 */
public class SimpleUseSparkStreaming {
    public static void main(String[] args) throws InterruptedException {
        // 创建流环境
        JavaStreamingContext streamingContext = new JavaStreamingContext("local[*]", "sparkStreaming", Duration.apply(3000));
        // 创建配置参数
        Map<String, Object> map = new HashMap<>();
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"121.43.102.232:9092");
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        map.put(ConsumerConfig.GROUP_ID_CONFIG,"spark-streaming-consumer");
        map.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");

        // 需要消费的主题
        ArrayList<String> strings = new ArrayList<>();
        strings.add("topic_db");

        JavaInputDStream<ConsumerRecord<String, String>> directStream = KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferBrokers(), ConsumerStrategies.<String, String>Subscribe(strings,map));

        directStream.map(new Function<ConsumerRecord<String, String>, String>() {
            @Override
            public String call(ConsumerRecord<String, String> v1) throws Exception {
                return v1.value();
            }
        }).print(100);

        // 执行流的任务
        streamingContext.start();
        streamingContext.awaitTermination();
    }
}
