package com.jie.sparksql.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Aggregator;
import scala.Function1;
import scala.Function2;

import java.io.Serializable;

import static org.apache.spark.sql.functions.udaf;

/**
 * 输入多行，返回一行
 * @Author ZhuPo
 * @date 2025/11/1 17:32
 */
public class SparkSqlOfUDAF {
    public static void main(String[] args) {
        SparkConf sc = new SparkConf().setMaster("local[*]").setAppName("sparkSql");
        SparkSession session = SparkSession.builder().config(sc).getOrCreate();
        session.read().json("input/user.json")
                .createOrReplaceTempView("user");

        session.udf().register("avgAge", udaf(new MyAvgAge(), Encoders.LONG()));

        session.sql("select avgAge(age) from user").show();

        session.close();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Buffer implements Serializable{
        private Long sum;
        private Long count;
    }

    /**
     * 聚合函数
     */
    public static class MyAvgAge extends Aggregator<Long,Buffer,Double> {

        /**
         * 初始值
         * @return
         */
        @Override
        public Buffer zero() {
            return new Buffer(0L,0L);
        }

        /**
         * 聚合
         * @param b 缓存
         * @param a  数据
         * @return
         */
        @Override
        public Buffer reduce(Buffer b, Long a) {
            b.sum += a;
            b.count += 1;
            return b;
        }

        /**
         * 合并
         *
         */
        @Override
        public Buffer merge(Buffer b1, Buffer b2) {
            b1.sum = b1.sum + b2.sum;
            b1.count = b1.count + b2.count;
            return b1;
        }

        /**
         * 结果
         * @param reduction
         * @return
         */
        @Override
        public Double finish(Buffer reduction) {
            return Double.valueOf(reduction.sum) / reduction.count;
        }

        /**
         * 缓存
         * @return
         */
        @Override
        public Encoder<Buffer> bufferEncoder() {
            return Encoders.kryo(Buffer.class);
        }

        /**
         * 输出
         */
        @Override
        public Encoder<Double> outputEncoder() {
            return Encoders.kryo(Double.class);
        }
    }
}
