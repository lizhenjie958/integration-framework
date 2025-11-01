package com.jie.sparkcore.base;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 注意：distinct会存在shuffle过程。
 */
public class RddFunctionOfSortBy {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("RddFunctionOfSortBy");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<Integer> integerJavaRDD = sc.parallelize(Arrays.asList(8, 1, 5, 2, 6, 3, 7, 4),2);

        // (1)泛型为以谁作为标准排序  (2) true为正序  (3) 排序之后的分区个数
        JavaRDD<Integer> sortRDD  = integerJavaRDD.sortBy(new Function<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) throws Exception {
                return integer;
            }
        }, true, 4);
        sortRDD .collect().forEach(System.out::println);

        TimeUnit.HOURS.sleep(1);
        sc.stop();
    }
}
