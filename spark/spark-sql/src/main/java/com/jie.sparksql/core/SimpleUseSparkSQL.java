package com.jie.sparksql.core;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * 一行进入，一行出
 * @Author ZhuPo
 * @date 2025/11/1 16:34
 */
public class SimpleUseSparkSQL {
    public static void main(String[] args) {
        SparkConf sc = new SparkConf().setMaster("local[*]").setAppName("sparkSql");
        SparkSession session = SparkSession.builder().config(sc).getOrCreate();
        Dataset<Row> ds = session.read().json("input/user.json");

        // 创建视图 => 转换为表格 填写表名
        // 临时视图的生命周期和当前的sparkSession绑定
        // orReplace表示覆盖之前相同名称的视图
        ds.createOrReplaceTempView("user");
        Dataset<Row> result = session.sql("select * from user where age > 18");

        result.show();

        session.close();
    }
}
