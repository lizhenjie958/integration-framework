package com.jie.sparkcore.dependency;

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
 * RDD任务切分中间分为：Application、Job、Stage和Task
 * （1）Application：初始化一个SparkContext即生成一个Application；
 * （2）Job：一个Action算子就会生成一个Job；
 * （3）Stage：Stage等于宽依赖的个数加1；
 * （4）Task：一个Stage阶段中，最后一个RDD的分区个数就是Task的个数。
 * 注意：Application->Job->Stage->Task每一层都是1对n的关系。
 *
 *
 * 查看http://localhost:4040/jobs/，
 * Job -> 2
 * Stage -> 2 + 2
 * Task -> 4 + 1
 */
public class RddOfDag {
    public static void main(String[] args) throws InterruptedException {

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
        System.out.println(wordRDD);
        System.out.println("-------------------");

        JavaPairRDD<String, Integer> tupleRDD = wordRDD.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        });
        System.out.println(tupleRDD.toDebugString());
        System.out.println("-------------------");

        // 缩减分区
        JavaPairRDD<String, Integer> coalesceRDD = tupleRDD.coalesce(1);

        JavaPairRDD<String, Integer> wordCountRDD = coalesceRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        },4);
        System.out.println(wordCountRDD.toDebugString());
        System.out.println("-------------------");

        wordCountRDD.collect().forEach(System.out::println);
        wordCountRDD.collect().forEach(System.out::println);

        Thread.sleep(600000);

        // 4. 关闭sc
        sc.stop();
    }
}
