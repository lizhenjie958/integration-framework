package com.jie.cache;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RddOfCache {
    public static void main(String[] args) throws InterruptedException {

        // 1.创建配置对象
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("sparkCore");

        // 2. 创建sparkContext
        JavaSparkContext sc = new JavaSparkContext(conf);

        // 3. 编写代码
        JavaRDD<String> lineRDD = sc.textFile("input/2.txt");


        //3.1.业务逻辑
        JavaRDD<String> wordRDD = lineRDD.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                List<String> stringList = Arrays.asList(s.split(" "));
                return stringList.iterator();
            }
        });

        JavaRDD<Tuple2<String, Integer>> tuple2JavaRDD = wordRDD.map(new Function<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> call(String v1) throws Exception {
                System.out.println("*****************");
                return new Tuple2<>(v1, 1);
            }
        });

        //3.5 cache缓存前打印血缘关系
        System.out.println(tuple2JavaRDD.toDebugString());

        //3.4 数据缓存。
//cache底层调用的就是persist方法,缓存级别默认用的是MEMORY_ONLY
        tuple2JavaRDD.cache();

        //3.6 persist方法可以更改存储级别
//        tuple2JavaRDD.persist(StorageLevel.MEMORY_AND_DISK_2());

        //3.2 触发执行逻辑
        tuple2JavaRDD. collect().forEach(System.out::println);

        //3.5 cache缓存后打印血缘关系
        // cache操作会增加血缘关系，不改变原有的血缘关系
        System.out.println(tuple2JavaRDD.toDebugString());
        System.out.println("=====================");

        //3.3 再次触发执行逻辑
        tuple2JavaRDD. collect().forEach(System.out::println);

        Thread.sleep(1000000);

        // 4. 关闭sc
        sc.stop();
    }
}
