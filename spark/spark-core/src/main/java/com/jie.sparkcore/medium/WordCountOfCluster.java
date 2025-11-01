package com.jie.medium;

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

public class WordCountOfCluster {
    public static void main(String[] args) {
        // 1.创建配置对象
        SparkConf conf = new SparkConf().setMaster("yarn").setAppName("sparkCore");

        // 2. 创建sparkContext
        JavaSparkContext sc = new JavaSparkContext(conf);

        // 3. 编写代码
        // 读取数据
        JavaRDD<String> javaRDD = sc.textFile(args[0]);

        // 长字符串切分为单个单词
        JavaRDD<String> flatMapRDD = javaRDD.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                List<String> strings = Arrays.asList(s.split(" "));
                return strings.iterator();
            }
        });

        // 转换格式为  (单词,1)
        JavaPairRDD<String, Integer> pairRDD = flatMapRDD.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        });

        // 合并相同单词
        JavaPairRDD<String, Integer> javaPairRDD = pairRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });

        javaPairRDD.saveAsTextFile(args[1]);

        // 4. 关闭sc
        sc.stop();
    }
}
