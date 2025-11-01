package com.jie.sparksql.loadstorage;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

/**
 * 相当于嵌套了Mysql数据库
 * @Author ZhuPo
 * @date 2025/11/1 18:42
 */
public class LoadDataFromMysql {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("sparkSql");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        Dataset<Row> json = spark.read().json("input/user.json");

        Properties properties = new Properties();
        properties.setProperty("user", "lizhenjie");
        properties.setProperty("password","123456");

        json.write()
                .mode(SaveMode.Append)
                .jdbc("jdbc:mysql://121.43.102.232:3306/tc_study?useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true",
                        "tc_spark_user",properties
                        );


        Dataset<Row> dataFromMysql = spark.read()
                .jdbc("jdbc:mysql://121.43.102.232:3306/tc_study?useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true",
                        "tc_spark_user", properties
                );

        dataFromMysql.show();

        spark.close();

    }
}
