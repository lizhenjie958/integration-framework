package com.jie.flink.sink;

import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

/**
 * @Author ZhuPo
 * @date 2025/11/5 20:00
 */
public class SinkKafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        // 如果是精准一次，必须开启checkpoint（后续章节介绍）
        environment.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);

        DataStreamSource<String> lineDS = environment.socketTextStream("127.0.0.1", 9999);
        /**
         * Kafka Sink:
         * TODO 注意：如果要使用 精准一次 写入Kafka，需要满足以下条件，缺一不可
         * 1、开启checkpoint（后续介绍）
         * 2、设置事务前缀
         * 3、设置事务超时时间：   checkpoint间隔 <  事务超时时间  < max的15分钟
         */
        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setBootstrapServers("121.43.102.232:9092")
                .setRecordSerializer(new KafkaRecordSerializationSchema<String>() {
                    @Nullable
                    @Override
                    public ProducerRecord<byte[], byte[]> serialize(String s, KafkaSinkContext kafkaSinkContext, Long aLong) {
                        String[] split = s.split(",");
                        byte[] key = split[0].getBytes(StandardCharsets.UTF_8);
                        byte[] value = split[1].getBytes(StandardCharsets.UTF_8);
                        return new ProducerRecord<>("ws", key, value);
                    }
                })
                .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                .setTransactionalIdPrefix("lizhenjie-")
                .setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 10 * 60 * 1000 + "")
                .build();

        lineDS.sinkTo(kafkaSink);

        environment.execute();
    }
}
