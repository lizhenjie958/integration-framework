package com.jie.flink.watermark;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author ZhuPo
 * @date 2025/11/10 16:32
 */
public class WatermarkOutOfOrderlessDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS = environment.socketTextStream("localhost", 9999)
                .map(new WaterSensorMapFunction());

        // 1、定义WaterMark策略
        WatermarkStrategy<WaterSensor> watermarkStrategy =
                // 1.1 指定watermark生成： 乱序的，等待3s
                WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        // 1.2 指定时间戳分配器，从数据中提取
                                .withTimestampAssigner((element, recordTimestamp) ->{
                                    System.err.println("数据=" + element + ",recordTs=" + recordTimestamp);
                                    return element.getTs() * 1000L;
                                });

        // 指定watermark策略
        SingleOutputStreamOperator<WaterSensor> sensorDSWithWaterMark = sensorDS.assignTimestampsAndWatermarks(watermarkStrategy);

        sensorDSWithWaterMark.keyBy(WaterSensor::getId)
                //  3、使用事件语义窗口
                        .window(TumblingEventTimeWindows.of(Time.seconds(10L)))
                                .process(new ProcessWindowFunction<WaterSensor, String, String, TimeWindow>() {
                                    @Override
                                    public void process(String s, Context context, Iterable<WaterSensor> elements, Collector<String> out) throws Exception {
                                        long startTs = context.window().getStart();
                                        long endTs = context.window().getEnd();
                                        String windowStart = DateFormatUtils.format(startTs, "yyyy-MM-dd HH:mm:ss.SSS");
                                        String windowEnd = DateFormatUtils.format(endTs, "yyyy-MM-dd HH:mm:ss.SSS");
                                        long count = elements.spliterator().estimateSize();
                                        out.collect("key=" + s + "的窗口[" + windowStart + "," + windowEnd + ")包含" + count + "条数据===>" + elements.toString());
                                    }
                                });

        sensorDSWithWaterMark.print();

        environment.execute();
    }
}
