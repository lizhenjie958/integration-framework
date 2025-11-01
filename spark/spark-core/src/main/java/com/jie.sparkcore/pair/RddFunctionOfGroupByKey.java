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
 * groupByKey对每个key进行操作，但只生成一个seq，并不进行聚合。
 * 该操作可以指定分区器或者分区数（默认使用HashPartitioner）
 */
public class RddFunctionOfGroupByKey {
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
        JavaPairRDD<String, Iterable<Integer>> groupRdd = pairRDD.groupByKey();

        JavaPairRDD<String, Integer> mapValuesRdd = groupRdd.mapValues(v -> {
            int sum = 0;
            for (Integer i : v) {
                sum += i;
            }
            return sum;
        });

        mapValuesRdd.collect().forEach(System.out::println);

        TimeUnit.HOURS.sleep(1);
        sc.stop();
    }
}
