package com.jie.flink.state.backend;

import org.apache.flink.contrib.streaming.state.EmbeddedRocksDBStateBackend;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * HashMapStateBackend：默认状态后端，基于HashMap实现，基于内存，适合小数据量
 * EmbeddedRocksDBStateBackend：基于RocksDB实现，基于磁盘，适合大数据量
 *
 * @author ZhuPo
 * @date 2025/11/11 17:31
 */
public class StateBackendDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        // 默认的hashmap状态后端
//        environment.setStateBackend(new HashMapStateBackend());
        // IDE中使用EmbeddedRocksDBStateBackend, 添加依赖flink-statebackend-rocksdb
        environment.setStateBackend(new EmbeddedRocksDBStateBackend());

        environment.execute();
    }
}
