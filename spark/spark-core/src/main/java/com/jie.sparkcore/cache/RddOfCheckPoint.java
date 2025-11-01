package com.jie.cache;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

/**
 * Cache缓存只是将数据保存起来，不切断血缘依赖。Checkpoint检查点切断血缘依赖。
 * Cache缓存的数据通常存储在磁盘、内存等地方，可靠性低。Checkpoint的数据通常存储在HDFS等容错、高可用的文件系统，可靠性高。
 * 建议对checkpoint()的RDD使用Cache缓存，这样checkpoint的job只需从Cache缓存中读取数据即可，否则需要再从头计算一次RDD
 */
public class RddOfCheckPoint {
    public static void main(String[] args) {

        // 1.创建配置对象
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("sparkCore");

        // 2. 创建sparkContext
        JavaSparkContext sc = new JavaSparkContext(conf);

        sc.setCheckpointDir("ck");

        // 3. 编写代码
        JavaRDD<String> lineRDD = sc.textFile("input/2.txt");

        JavaPairRDD<String, Long> tupleRDD = lineRDD.mapToPair(new PairFunction<String, String, Long>() {
            @Override
            public Tuple2<String, Long> call(String s) throws Exception {
                return new Tuple2<String, Long>(s, System.currentTimeMillis());
            }
        });

        // 查看血缘关系
        System.out.println(tupleRDD.toDebugString());

        // 增加检查点避免计算两次
        tupleRDD.cache();

        // 进行检查点
        tupleRDD.checkpoint();

        tupleRDD. collect().forEach(System.out::println);

        System.out.println(tupleRDD.toDebugString());
        // 第二次计算
        tupleRDD. collect().forEach(System.out::println);
        // 第三次计算
        tupleRDD. collect().forEach(System.out::println);

        // 4. 关闭sc
        sc.stop();
    }
}
