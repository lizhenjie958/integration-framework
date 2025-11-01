package com.jie.base;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;

public class UseRddFromList {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("UseRddFromList");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> rdd = sc.parallelize(Arrays.asList("hello", "spark"));
        rdd.collect().forEach(System.out::println);
        sc.stop();
    }
}
