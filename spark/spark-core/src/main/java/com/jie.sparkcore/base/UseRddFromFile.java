package com.jie.base;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class UseRddFromFile {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("UseRddFromFile");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> rdd = sc.textFile("C:\\Users\\35017\\Desktop\\other\\spark\\file\\input");
        rdd.collect().forEach(System.out::println);
        sc.stop();
    }
}
