package com.jie.base;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class RddFunctionOfMap {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("RddFunctionOfMap");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lineRDD = sc.textFile("input/1.txt");
        // 需求:每行结尾拼接||
        // 两种写法  lambda表达式写法(匿名函数)
        JavaRDD<String> mapRdd = lineRDD.map(line -> line + "||");
        mapRdd.collect().forEach(System.out::println);
        sc.stop();
    }
}
