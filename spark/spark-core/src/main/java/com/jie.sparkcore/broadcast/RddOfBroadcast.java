package com.jie.sparkcore.broadcast;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;

import java.util.Arrays;
import java.util.List;

public class RddOfBroadcast {
    public static void main(String[] args) {

        // 1.创建配置对象
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("sparkCore");

        // 2. 创建sparkContext
        JavaSparkContext sc = new JavaSparkContext(conf);

        // 3. 编写代码
        JavaRDD<Integer> intRDD = sc.parallelize(Arrays.asList(4, 56, 7, 8, 1, 2));

        // 幸运数字
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        // 找出幸运数字
        // 每一个task都会创建一个list浪费内存
        /*
        JavaRDD<Integer> result = intRDD.filter(new Function<Integer, Boolean>() {
            @Override
            public Boolean call(Integer v1) throws Exception {
                return list.contains(v1);
            }
        });
         */

        // 创建广播变量
        // 只发送一份数据到每一个executor
        Broadcast<List<Integer>> broadcast = sc.broadcast(list);

        JavaRDD<Integer> result = intRDD.filter(new Function<Integer, Boolean>() {
            @Override
            public Boolean call(Integer v1) throws Exception {
                return broadcast.value().contains(v1);
            }
        });

        result. collect().forEach(System.out::println);

        // 4. 关闭sc
        sc.stop();
    }
}
