package com.jie.base;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 注意：distinct会存在shuffle过程。
 */
public class RddFunctionOfDistinct {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("RddFunctionOfDistinct");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<Integer> integerJavaRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4),2);

        JavaRDD<Integer> distinctRDD  = integerJavaRDD.distinct();
        distinctRDD .collect().forEach(System.out::println);

        TimeUnit.HOURS.sleep(1);
        sc.stop();
    }
}
