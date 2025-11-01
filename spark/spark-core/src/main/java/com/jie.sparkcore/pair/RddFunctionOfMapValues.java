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
public class RddFunctionOfMapValues {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("RddFunctionOfDistinct");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<String, String> tuple2JavaRDD = sc.parallelizePairs(Arrays.asList(
                new Tuple2<>("k", "v"), new Tuple2<>("k1", "v1"), new Tuple2<>("k2", "v2")
        ), 2);

        // 只修改value 不修改key
        JavaPairRDD<String, String> mapValuesRDD  = tuple2JavaRDD.mapValues(v -> v + "||");
        mapValuesRDD .collect().forEach(System.out::println);

        TimeUnit.HOURS.sleep(1);
        sc.stop();
    }
}
