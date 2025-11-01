package com.jie.sparksql.core;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;

/**
 * @Author ZhuPo
 * @date 2025/11/1 16:41
 */
public class SparkSqlOfDSL {
    public static void main(String[] args) {
        //1. 创建配置对象
        SparkConf sc = new SparkConf().setMaster("local[*]").setAppName("sparkSql");
        //2. 获取Session
        SparkSession session = SparkSession.builder().config(sc).getOrCreate();
        Dataset<Row> lineDS = session.read().json("input/user.json");
        Dataset<Row> result = lineDS
                .select(
                        col("name").as("username"),
                        col("age").plus(1).as("next_age")
                )
                .filter(col("age").gt(18));
        result.show();
        session.close();
    }
}
