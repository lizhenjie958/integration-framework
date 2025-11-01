package com.jie.sparkcore.serialize;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;

public class RddOfObjectSerialize {
    public static void main(String[] args) {

        // 1.创建配置对象
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("sparkCore");

        // 2. 创建sparkContext
        JavaSparkContext sc = new JavaSparkContext(conf);

        // 3. 编写代码
        User zhangsan = new User("zhangsan", 13);
        User lisi = new User("lisi", 13);

        JavaRDD<User> userJavaRDD = sc.parallelize(Arrays.asList(zhangsan, lisi), 2);

        JavaRDD<User> mapRDD = userJavaRDD.map(new Function<User, User>() {
            @Override
            public User call(User v1) throws Exception {
                return new User(v1.getName(), v1.getAge() + 1);
            }
        });

        mapRDD.collect().forEach(System.out::println);

        // 4. 关闭sc
        sc.stop();
    }
}
