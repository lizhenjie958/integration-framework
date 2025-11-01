package com.jie.sparksql.core;

import com.jie.sparksql.bean.User;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.sql.*;
import scala.Tuple2;

public class SimpleUseSparkFunction {
    public static void main(String[] args) {

        //1. 创建配置对象
        SparkConf sc = new SparkConf().setMaster("local[*]").setAppName("sparkSql");

        //2、获取SparkSession
        SparkSession session = SparkSession.builder().config(sc).getOrCreate();

        //3、编写代码按行读取
        Dataset<Row> lineDS = session.read().json("input/user.json");
        //3.1转化为类对象
        Dataset<User> userDataset = lineDS.as(Encoders.bean(User.class));

        userDataset.show();

        // 使用方法操作，函数式方法
        Dataset<User> usesrDataset = lineDS.map(new MapFunction<Row, User>() {
            @Override
            public User call(Row row) throws Exception {
                return new User(row.getLong(0), row.getString(1));
            }
            // 使用kryo在底层会有部分算子无法使用
        }, Encoders.bean(User.class));
        // 常规方法
        Dataset<User> sortAgeDataSet = userDataset.sort("age");
        sortAgeDataSet.show();

        // 分组
        RelationalGroupedDataset groupByDS = usesrDataset.groupBy("name");
        groupByDS.count().show();


        // 推荐使用函数式的方法  使用更灵活
        KeyValueGroupedDataset<String, User> groupedDataset = usesrDataset.groupByKey(new MapFunction<User, String>() {
            @Override
            public String call(User user) throws Exception {
                return user.getName();
            }
        }, Encoders.STRING());

        // 聚合算子都是从groupByKey开始
        // 推荐使用reduceGroup
        Dataset<Tuple2<String, User>> result = groupedDataset.reduceGroups(new ReduceFunction<User>() {
            @Override
            public User call(User u1, User u2) throws Exception {
                return new User(Math.max(u1.getAge(), u2.getAge()), u1.getName());
            }
        });
        result.show();
    }
}
