package com.jie.pair;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 在不影响业务逻辑的前提下，优先选用reduceByKey
 * 1）reduceByKey：按照key进行聚合，在shuffle之前有combine（预聚合）操作，返回结果是RDD[K,V]。
 * 2）groupByKey：按照key进行分组，直接进行shuffle。
 */
public class RddFunctionOfReduceByKey {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("RddFunctionOfDistinct");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // 3. 编写代码
        JavaRDD<String> integerJavaRDD = sc.parallelize(Arrays.asList("hi","hi","hello","spark" ),2);

        // 统计单词出现次数
        JavaPairRDD<String, Integer> pairRDD = integerJavaRDD.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        });

        // 按照Key分组
        JavaPairRDD<String, Integer> reduceByKey = pairRDD.reduceByKey(Integer::sum);


        reduceByKey.collect().forEach(System.out::println);

        TimeUnit.HOURS.sleep(1);
        sc.stop();
    }
}
