package com.jie.sparkcore.base;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RddFunctionOfFlatMap {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("RddFunctionOfMap");
        JavaSparkContext sc = new JavaSparkContext(conf);

        ArrayList<List<String>> arrayLists = new ArrayList<>();
        arrayLists.add(Arrays.asList("1","2","3"));
        arrayLists.add(Arrays.asList("4","5","6"));
        JavaRDD<List<String>> rdd = sc.parallelize(arrayLists, 2);

        JavaRDD<String> flatMapRdd = rdd.flatMap(List::iterator);
        flatMapRdd.collect().forEach(System.out::println);

        System.err.println("============================");

        JavaRDD<String> rddLine = sc.parallelize(Arrays.asList("1,3,5", "2,4,6"));
        JavaRDD<String> lineFlatMapRdd = rddLine.flatMap(line -> {
            String[] split = line.split(",");
            return Arrays.asList(split).iterator();
        });
        lineFlatMapRdd.collect().forEach(System.out::println);

        sc.stop();

    }
}
