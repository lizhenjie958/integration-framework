package com.jie.flink.watermark;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 *
 * 有序流中内置水位线设置
 *
 * @author ZhuPo
 * @date 2025/11/10 15:38
 */
public class WatermarkMonoDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS = environment.socketTextStream("localhost", 9999)
                .map(new WaterSensorMapFunction());

        // 1、定义WaterMark策略
        WatermarkStrategy<WaterSensor> watermarkStrategy =
                // 指定watermark生成：升序的watermark，没有等待时间
                WatermarkStrategy.<WaterSensor>forMonotonousTimestamps()
                // 指定时间戳分配器，从数据中提取
                .withTimestampAssigner((element, recordTimestamp) -> {
                    System.err.println("数据=" + element + ",recordTs=" + recordTimestamp);
                    // 返回时间戳，要毫秒
                    return element.getTs() * 1000L;
                });

        // 2、指定Watermark策略
        SingleOutputStreamOperator<WaterSensor> sendSorDSWithWatermark = sensorDS.assignTimestampsAndWatermarks(watermarkStrategy);
        SingleOutputStreamOperator<String> process = sendSorDSWithWatermark.keyBy(WaterSensor::getId)
                // 使用事件语义的窗口
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .process(new ProcessWindowFunction<WaterSensor, String, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<WaterSensor> elements, Collector<String> out) throws Exception {
                        long start = context.window().getStart();
                        long end = context.window().getEnd();
                        String windowStart = DateFormatUtils.format(start, "yyyy-MM-dd HH:mm:ss.SSS");
                        String windowEnd = DateFormatUtils.format(end, "yyyy-MM-dd HH:mm:ss.SSS");
                        long count = elements.spliterator().estimateSize();
                        out.collect("key=" + s + "窗口：" + windowStart + "~" + windowEnd + "，个数：" + count);
                    }
                });
        process.print();

        environment.execute();
    }
}
