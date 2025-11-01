package com.jie.sparkcore.pair;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 求平均值
 */
public class RddFunctionOfReduceByKeyAvg {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("RddFunctionOfDistinct");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // 3. 编写代码
        JavaPairRDD<String, Integer> javaPairRDD = sc.parallelizePairs(Arrays.asList(new Tuple2<>("hi", 96), new Tuple2<>("hi", 97), new Tuple2<>("hello", 95), new Tuple2<>("hello", 195)));

        // ("hi",(96,1))
        JavaPairRDD<String, Tuple2<Integer, Integer>> tuple2JavaPairRDD = javaPairRDD.mapValues(new Function<Integer, Tuple2<Integer, Integer>>() {
            @Override
            public Tuple2<Integer, Integer> call(Integer v1) throws Exception {
                return new Tuple2<>(v1, 1);
            }
        });

        // 聚合RDD
        // ("hi",(193,2)
        JavaPairRDD<String, Tuple2<Integer, Integer>> reduceRDD = tuple2JavaPairRDD.reduceByKey(new Function2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>() {
            @Override
            public Tuple2<Integer, Integer> call(Tuple2<Integer, Integer> v1, Tuple2<Integer, Integer> v2) throws Exception {
                return new Tuple2<>(v1._1 + v2._1, v1._2 + v2._2);
            }
        });

        // ("hi",(96.5)
        JavaPairRDD<String, Double> mapValuesRdd = reduceRDD.mapValues(v -> new Double(v._1) / v._2);

        mapValuesRdd.collect().forEach(System.out::println);

        TimeUnit.HOURS.sleep(1);
        sc.stop();
    }
}
