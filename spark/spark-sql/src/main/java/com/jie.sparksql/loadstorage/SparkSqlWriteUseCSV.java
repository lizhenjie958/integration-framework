package com.jie.sparksql.loadstorage;

import com.jie.sparksql.bean.User;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;

/**
 * @Author ZhuPo
 * @date 2025/11/1 17:55
 */
public class SparkSqlWriteUseCSV {
    public static void main(String[] args) {
        SparkConf sc = new SparkConf().setMaster("local[*]").setAppName("sparkSql");
        SparkSession session = SparkSession.builder().config(sc).getOrCreate();
        Dataset<Row> userDS = session.read()
                .option("header", "true")//默认为false 不读取列名
                .option("sep",",") // 默认为, 列的分割
                // 不需要写压缩格式  自适应
                .csv("input/user.csv");
        userDS.show();

        // 转换为user的ds
        // 直接转换类型会报错  csv读取的数据都是string
//        Dataset<User> userDS1 = userDS.as(Encoders.bean(User.class));
        userDS.printSchema();

        Dataset<User> userDataset = userDS.map(new MapFunction<Row, User>() {
            @Override
            public User call(Row row) throws Exception {
                return new User(Long.valueOf(row.getString(0)), row.getString(1));
            }
        }, Encoders.bean(User.class));

        userDataset.show();

        // 写出为csv文件
        DataFrameWriter<User> writer = userDataset.write();

        writer.option("seq",";")
                .option("header","true")
//                .option("compression","gzip")// 压缩格式
                // 写出模式
                // append 追加
                // Ignore 忽略本次写出
                // Overwrite 覆盖写
                // ErrorIfExists 如果存在报错
                .mode(SaveMode.Append)
                .csv("output");

        //4. 关闭sparkSession

        session.close();
    }
}
