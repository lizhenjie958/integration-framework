package com.jie.flink.process;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author ZhuPo
 * @date 2025/11/11 10:57
 */
public class KeyedProcessTimerDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS = environment
                .socketTextStream("localhost", 9999)
                .map(new WaterSensorMapFunction())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner((element, recordTimestamp) -> element.getTs() * 1000L)
                );

        KeyedStream<WaterSensor, String> sensorKS = sensorDS.keyBy(r -> r.getId());

        SingleOutputStreamOperator<String> process = sensorKS.process(new KeyedProcessFunction<String, WaterSensor, String>() {

            /**
             * 2.时间进展到定时器注册的时间，调用该方法
             *  timestamp 当前时间进展，就是定时器被触发时的时间
             * @param timestamp
             * @param ctx
             * @param out
             * @throws Exception
             */
            @Override
            public void onTimer(long timestamp, KeyedProcessFunction<String, WaterSensor, String>.OnTimerContext ctx, Collector<String> out) throws Exception {
                super.onTimer(timestamp, ctx, out);
                String currentKey = ctx.getCurrentKey();

                System.out.println("key=" + currentKey + "现在时间是" + timestamp + "定时器触发");
            }

            @Override
            public void processElement(WaterSensor waterSensor, Context context, Collector<String> collector) throws Exception {
                // 获取当前key
                String currentKey = context.getCurrentKey();

                // 定时器注册
                TimerService timerService = context.timerService();

                // 事件时间的案例
                Long timestamp = context.timestamp();
                timerService.registerEventTimeTimer(5000L);
                System.out.printf("当前key=" + currentKey + ",当前时间=" + timestamp + ",注册了一个5s的定时器");

                // 处理时间的案例
                long currentTS = timerService.currentProcessingTime();
                timerService.registerProcessingTimeTimer(currentTS + 5000L);
                System.out.println("当前key=" + currentKey + ",当前时间=" + currentTS + ",注册了一个5s的定时器");

                // 获取process的当前的Watermark
                long currentWatermark = timerService.currentWatermark();
                System.out.println("当前key=" + currentKey + ",当前Watermark=" + currentWatermark);

                // 注册定时器： 处理时间、事件时间
                timerService.registerProcessingTimeTimer(currentTS + 5000L);
                timerService.registerEventTimeTimer(5000L);
                timerService.deleteEventTimeTimer(5000L);
                timerService.deleteProcessingTimeTimer(5000L);

                // 获取当前时间进展：处理时间-系统时间，事件时间-Watermark
                long currentTime = timerService.currentProcessingTime();
                long watermark = timerService.currentWatermark();


            }
        });

        process.print();

        environment.execute();
    }
}
