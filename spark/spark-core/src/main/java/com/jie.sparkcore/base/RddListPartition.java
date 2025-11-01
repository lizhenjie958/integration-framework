package com.jie.sparkcore.base;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;

public class RddListPartition {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("UseRddFromList");
        JavaSparkContext sc = new JavaSparkContext(conf);
        // 默认环境的核数
        // 可以手动填写参数控制分区的个数
        JavaRDD<String> rdd = sc.parallelize(Arrays.asList("hello", "spark", "hello", "spark", "hello"), 2);
        rdd.saveAsTextFile("output");
        sc.stop();
    }
}
