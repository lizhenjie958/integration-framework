package com.jie.flink.sink;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author ZhuPo
 * @date 2025/11/5 20:44
 */
public class SinkMySQL {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        SingleOutputStreamOperator<WaterSensor> sensorDS = env
                .socketTextStream("127.0.0.1", 9999)
                .map(new WaterSensorMapFunction());


        /**
         * TODO 写入mysql
         * 1、只能用老的sink写法： addsink
         * 2、JDBCSink的4个参数:
         *    第一个参数： 执行的sql，一般就是 insert into
         *    第二个参数： 预编译sql， 对占位符填充值
         *    第三个参数： 执行选项 ---》 攒批、重试
         *    第四个参数： 连接选项 ---》 url、用户名、密码
         */
        SinkFunction<WaterSensor> sink = JdbcSink.sink("insert into ws values(?,?,?)",
                new JdbcStatementBuilder<WaterSensor>() {
                    @Override
                    public void accept(PreparedStatement preparedStatement, WaterSensor waterSensor) throws SQLException {
                        //每收到一条WaterSensor，如何去填充占位符
                        preparedStatement.setString(1, waterSensor.getId());
                        preparedStatement.setInt(2, waterSensor.getTs());
                        preparedStatement.setInt(3, waterSensor.getVc());

                    }
                }, JdbcExecutionOptions.builder()
                        .withMaxRetries(3)  // 重试次数
                        .withBatchSize(100) // 攒批大小，条数
                        .withBatchIntervalMs(3000) // 攒批时间间隔,毫秒
                        .build()// 重试次数
                , new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("jdbc:mysql://121.43.102.232:3306/big_data?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8")
                        .withUsername("lizhenjie")
                        .withPassword("123456")
                        .withConnectionCheckTimeoutSeconds(60) // 重试的超时时间
                        .build()
        );

        sensorDS.addSink(sink);

        env.execute();
    }
}
