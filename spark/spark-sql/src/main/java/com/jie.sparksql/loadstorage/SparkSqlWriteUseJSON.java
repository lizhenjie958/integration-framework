package com.jie.sparksql.loadstorage;

import com.jie.sparksql.bean.User;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @Author ZhuPo
 * @date 2025/11/1 17:55
 */
public class SparkSqlWriteUseJSON {
    public static void main(String[] args) {
        SparkConf sc = new SparkConf().setMaster("local[*]").setAppName("sparkSql");
        SparkSession session = SparkSession.builder().config(sc).getOrCreate();
        Dataset<Row> userDS = session.read().json("input/user.json");
        Dataset<User> userDataset = userDS.as(Encoders.bean(User.class));
        userDataset.write()
                .json("output");

        session.close();
    }
}
