package com.jie.flink.shuffle;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ZhuPo
 * @date 2025/11/5 16:06
 */
public class ShuffleExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        env.socketTextStream("127.0.0.1",9999)
//                .shuffle()    // 随机分区:随机将数据进行分发
//                .rebalance()  // 轮训分区:按照先后顺序将数据做依次分发
//                .rescale()    // 重缩放分区:其实底层也是使用Round-Robin算法进行轮询
//                .broadcast()  // 广播:数据会在不同的分区都保留一份
//                .global()       // 全局分区:这就相当于强行让下游任务并行度变成了1
                .partitionCustom(new MyPartitioner(), value -> value) // 自定义分区
                .print();

        env.execute();
    }


    public static class MyPartitioner implements Partitioner<String> {

        @Override
        public int partition(String key, int numPartitions) {
            return Integer.parseInt(key) % numPartitions;
        }
    }
}
