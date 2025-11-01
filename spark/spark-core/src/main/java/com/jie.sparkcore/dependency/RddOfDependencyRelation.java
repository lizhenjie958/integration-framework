package com.jie.dependency;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 圆括号中的数字表示RDD的并行度，也就是有几个分区
 *
 * 窄依赖表示每一个父RDD的Partition最多被子RDD的一个Partition使用,（一对一or多对一）
 * 宽依赖表示同一个父RDD的Partition被多个子RDD的Partition依赖,（只能是一对多），会引起Shuffle
 *      具有宽依赖的transformations包括：sort、reduceByKey、groupByKey、join和调用rePartition函数的任何操作
 *
 * 宽依赖对Spark去评估一个transformations有更加重要的影响，比如对性能的影响。
 * 在不影响业务要求的情况下，要尽量避免使用有宽依赖的转换算子，因为有宽依赖，就一定会走shuffle，影响性能
 */
public class RddOfDependencyRelation {
    public static void main(String[] args) {

        // 1.创建配置对象
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("sparkCore");

        // 2. 创建sparkContext
        JavaSparkContext sc = new JavaSparkContext(conf);

        // 3. 编写代码
        JavaRDD<String> lineRDD = sc.textFile("input/2.txt");
        System.out.println(lineRDD.toDebugString());
        System.out.println("-------------------");

        JavaRDD<String> wordRDD = lineRDD.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                List<String> stringList = Arrays.asList(s.split(" "));
                return stringList.iterator();
            }
        });
        System.out.println(wordRDD.toDebugString());
        System.out.println("-------------------");

        JavaPairRDD<String, Integer> tupleRDD = wordRDD.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        });
        System.out.println(tupleRDD.toDebugString());
        System.out.println("-------------------");

        JavaPairRDD<String, Integer> wordCountRDD = tupleRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        System.out.println(wordCountRDD.toDebugString());
        System.out.println("-------------------");

        // 4. 关闭sc
        sc.stop();
    }
}
