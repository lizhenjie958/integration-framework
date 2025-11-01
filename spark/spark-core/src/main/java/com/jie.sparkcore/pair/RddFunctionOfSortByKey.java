package com.jie.sparkcore.pair;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class RddFunctionOfSortByKey {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("RddFunctionOfDistinct");
        JavaSparkContext sc = new JavaSparkContext(conf);


        // 3. 编写代码
        JavaPairRDD<Integer, String> javaPairRDD = sc.parallelizePairs(Arrays.asList(new Tuple2<>(4, "a"), new Tuple2<>(3, "c"), new Tuple2<>(2, "d")));

        // 填写布尔类型选择正序倒序
        JavaPairRDD<Integer, String> sortByKeyRdd = javaPairRDD.sortByKey(true);


        sortByKeyRdd.collect().forEach(System.out::println);

        TimeUnit.HOURS.sleep(1);
        sc.stop();
    }
}
