package com.jie.base;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class RddFunctionOfGroupBy {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("RddFunctionOfGroupBy");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<Integer> integerJavaRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4),2);

        // groupBy会存在shuffle过程
        // shuffle：将不同的分区数据进行打乱重组的过程
        // shuffle一定会落盘。可以在local模式下执行程序，通过4040看效果。
        JavaPairRDD<Integer, Iterable<Integer>> groupByRDD = integerJavaRDD.groupBy(v -> v % 2);
        groupByRDD.collect().forEach(System.out::println);

        TimeUnit.HOURS.sleep(1);
        sc.stop();
    }
}
