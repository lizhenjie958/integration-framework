package com.jie.flink.watermark;

import com.jie.flink.shuffle.ShuffleExample;
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
 * 以最小的那个作为当前任务的事件时钟。
 *
 * 水位线在上下游任务之间的传递，
 * 非常巧妙地避免了分布式系统中没有统一时钟的问题，每个任务都以“处理完之前所有数据”为标准来确定自己的时钟。
 *
 * @author ZhuPo
 * @date 2025/11/10 17:19
 */
public class WatermarkIdlenessDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);

        // 自定义分区器：数据%分区数，只输入奇数，都只会去往map的一个子任务
        SingleOutputStreamOperator<Integer> socketDS = env.socketTextStream("localhost", 9999)
                .partitionCustom(new ShuffleExample.MyPartitioner(), r -> r)
                .map(r -> Integer.parseInt(r))
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<Integer>forMonotonousTimestamps()
                                .withTimestampAssigner((r, ts) -> r * 1000L)
                                .withIdleness(Duration.ofSeconds(5))  //空闲等待5s
                );

        // 分成两组： 奇数一组，偶数一组 ， 开10s的事件时间滚动窗口
        socketDS
                .keyBy(r -> r % 2)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .process(new ProcessWindowFunction<Integer, String, Integer, TimeWindow>() {
                    @Override
                    public void process(Integer integer, Context context, Iterable<Integer> elements, Collector<String> out) throws Exception {
                        long startTs = context.window().getStart();
                        long endTs = context.window().getEnd();
                        String windowStart = DateFormatUtils.format(startTs, "yyyy-MM-dd HH:mm:ss.SSS");
                        String windowEnd = DateFormatUtils.format(endTs, "yyyy-MM-dd HH:mm:ss.SSS");

                        long count = elements.spliterator().estimateSize();

                        out.collect("key=" + integer + "的窗口[" + windowStart + "," + windowEnd + ")包含" + count + "条数据===>" + elements.toString());

                    }
                })
                .print();

        env.execute();
    }
}
