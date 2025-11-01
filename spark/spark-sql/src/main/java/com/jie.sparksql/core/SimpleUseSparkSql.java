package com.jie.sparksql.core;

import com.jie.sparksql.bean.User;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;

public class SimpleUseSparkSql {
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
    }
}
