package com.jie.flink.state.operator;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.BroadcastConnectedStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

/**
 * 算子并行子任务都保持同一份“全局”状态，用来做统一的配置和规则设定
 *
 * 水位超过指定的阈值发送告警，阈值可以动态修改
 *
 * @author ZhuPo
 * @date 2025/11/11 16:52
 */
public class OperatorBroadcastStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(2);

        // 数据流
        SingleOutputStreamOperator<WaterSensor> sensorDS = environment.socketTextStream("localhost", 9999)
                .map(new WaterSensorMapFunction());

        // 1、配置流（用来广播配置）
        DataStreamSource<String> configDS = environment.socketTextStream("localhost", 8888);

        MapStateDescriptor<String, Integer> broadcastMapState = new MapStateDescriptor<>("broadcast-state", Types.STRING, Types.INT);
        BroadcastStream<String> configBS = configDS.broadcast(broadcastMapState);

        // 2、把数据流和广播后的配置流connect
        BroadcastConnectedStream<WaterSensor, String> sensorBCS = sensorDS.connect(configBS);

        // 3、处理数据
        sensorBCS.process(new BroadcastProcessFunction<WaterSensor, String, String>() {

            /**
             * 数据流的处理方法： 数据流 只能 读取 广播状态，不能修改
             * @param waterSensor
             * @param readOnlyContext
             * @param collector
             * @throws Exception
             */
            @Override
            public void processElement(WaterSensor waterSensor, ReadOnlyContext readOnlyContext, Collector<String> collector) throws Exception {
                // 5、通过 context 获取广播状态
                ReadOnlyBroadcastState<String, Integer> broadcastState = readOnlyContext.getBroadcastState(broadcastMapState);
                Integer threshold = broadcastState.get("threshold");
                // 判断广播状态里是否有数据
                threshold  = threshold == null ? 0 : threshold;
                if (waterSensor.getVc() > threshold) {
                    collector.collect("id=" + waterSensor.getId() + "的传感器数据超过阈值：" + threshold);
                }
            }

            /**
             * 广播后的配置流的处理方法:  只有广播流才能修改 广播状态
             * @param s
             * @param context
             * @param collector
             * @throws Exception
             */
            @Override
            public void processBroadcastElement(String s, Context context, Collector<String> collector) throws Exception {
                // 4、获取广播状态,写入数据
                context.getBroadcastState(broadcastMapState)
                        .put("threshold", Integer.parseInt(s));
            }
        }).print();

        environment.execute();
    }

}
