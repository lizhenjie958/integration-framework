package com.jie.sparkcore.partitioner;

import org.apache.spark.Partitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Spark目前支持Hash分区、Range分区和用户自定义分区
 *
 * 只有Key-Value类型的pairRDD才有分区器，非Key-Value类型的RDD分区的值是None
 * 每个RDD的分区ID范围：0~numPartitions-1，决定这个值是属于那个分区的
 *
 *  HashPartitioner分区弊端:可能导致每个分区中数据量的不均匀，极端情况下会导致某些分区拥有
 * RDD的全部数据。
 *  Range分区要求RDD中的KEY类型必须是可以排序的
 */
public class RddOfPartitioner {
    public static void main(String[] args) {

        // 1.创建配置对象
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("sparkCore");

        // 2. 创建sparkContext
        JavaSparkContext sc = new JavaSparkContext(conf);

        // 3. 编写代码
        JavaPairRDD<String, Integer> tupleRDD = sc.parallelizePairs(Arrays.asList(new Tuple2<>("s", 1), new Tuple2<>("a", 3), new Tuple2<>("d", 2)));

        // 获取分区器
        Optional<Partitioner> partitioner = tupleRDD.partitioner();

        System.out.println(partitioner);

        JavaPairRDD<String, Integer> reduceByKeyRDD = tupleRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });

        // 获取分区器
        Optional<Partitioner> partitioner1 = reduceByKeyRDD.partitioner();
        System.out.println(partitioner1);

        // 4. 关闭sc
        sc.stop();
    }
}
