package com.jie.flink.watermark;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssignerSupplier;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;
import org.apache.flink.api.common.eventtime.WatermarkOutput;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ZhuPo
 * @date 2025/11/10 16:58
 */
public class CustomPeriodicWatermarkExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        environment.socketTextStream("localhost", 9999)
                .map(new WaterSensorMapFunction())
                .assignTimestampsAndWatermarks(new CustomWatermarkStrategy ())
                .print();

        environment.execute();
    }


    public static class CustomWatermarkStrategy  implements WatermarkStrategy<WaterSensor> {

        @Override
        public WatermarkGenerator<WaterSensor> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
            return new CustomBoundedOutOfOrdernessGenerator();
        }

        @Override
        public TimestampAssigner<WaterSensor> createTimestampAssigner(TimestampAssignerSupplier.Context context) {

            return new SerializableTimestampAssigner<WaterSensor>() {

                @Override
                public long extractTimestamp(WaterSensor element, long recordTimestamp) {
                    return element.getTs(); // 告诉程序数据源里的时间戳是哪一个字段
                }
            };
        }

    }


    public static class CustomBoundedOutOfOrdernessGenerator implements WatermarkGenerator<WaterSensor> {
        private Long delayTime = 5000L; // 延迟时间
        private Long maxTs = -Long.MAX_VALUE + delayTime + 1L; // 观察到的最大时间戳

        @Override
        public void onEvent(WaterSensor waterSensor, long l, WatermarkOutput watermarkOutput) {
            // 每来一条数据就调用一次
            maxTs = Math.max(waterSensor.getTs(), maxTs); // 更新最大时间戳
        }

        @Override
        public void onPeriodicEmit(WatermarkOutput output) {
            // 发射水位线，默认200ms调用一次
            output.emitWatermark(new Watermark(maxTs - delayTime - 1L));
        }
    }
}
