package com.jie.sparksql.core;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;

/**
 * @Author ZhuPo
 * @date 2025/11/1 16:53
 */
public class SparkSqlOfUDF {
    public static void main(String[] args) {
        SparkConf sc = new SparkConf().setMaster("local[*]").setAppName("sparkSql");
        SparkSession session = SparkSession.builder().config(sc).getOrCreate();
        Dataset<Row> lineDS = session.read().json("input/user.json");
        lineDS.createOrReplaceTempView("user");

        // 匿名内部类方式
        session.udf().register("addNameOne", new UDF1<String, String>() {
            @Override
            public String call(String s) throws Exception {
                return s + "大侠";
            }
        }, DataTypes.StringType);
        // lambda方式
        session.udf().register("addNameTwo", s -> s + "大侠", DataTypes.StringType);


        Dataset<Row> result = session.sql("select addNameTwo(name) newName from user");
        result.show();

        session.close();
    }
}
