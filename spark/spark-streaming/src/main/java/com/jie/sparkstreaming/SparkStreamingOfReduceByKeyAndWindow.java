package com.jie.sparkstreaming;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 推荐使用此
 * @Author ZhuPo
 * @date 2025/11/1 22:26
 */
public class SparkStreamingOfReduceByKeyAndWindow {
    public static void main(String[] args) throws InterruptedException {
        // 创建流环境
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext("local[*]", "window", Duration.apply(3000L));
        // 创建配置参数
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"121.43.102.232:9092");
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        map.put(ConsumerConfig.GROUP_ID_CONFIG,"spark-streaming-consumer");
        map.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");

        // 需要消费的主题
        ArrayList<String> strings = new ArrayList<>();
        strings.add("topicA");

        JavaInputDStream<ConsumerRecord<String, String>> directStream = KafkaUtils.createDirectStream(javaStreamingContext, LocationStrategies.PreferBrokers(), ConsumerStrategies.<String, String>Subscribe(strings,map));


        JavaDStream<String> stringJavaDStream = directStream.flatMap(new FlatMapFunction<ConsumerRecord<String, String>, String>() {
            @Override
            public Iterator<String> call(ConsumerRecord<String, String> stringStringConsumerRecord) throws Exception {
                String[] split = stringStringConsumerRecord.value().split(" ");
                return Arrays.asList(split).iterator();
            }
        });

        JavaPairDStream<String, Integer> javaPairDStream = stringJavaDStream.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        });

        // reduceByKeyAndWindow(func, windowLength, slideInterval, [numTasks])：当在一个(K,V)对的DStream上调用此函数，会返回一个新(K,V)对的DStream，
        // 此处通过对滑动窗口中批次数据使用reduce函数来整合每个key的value值
        javaPairDStream.reduceByKeyAndWindow(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        },Duration.apply(12000L),Duration.apply(6000L)).print();

        // 执行流的任务
        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();

    }
}
