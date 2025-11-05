package com.jie.flink.split;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.SideOutputDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * 只需要调用上下文ctx的.output()方法，就可以输出任意类型的数据了。而侧输出流的标记和提取
 * @author ZhuPo
 * @date 2025/11/5 16:27
 */
public class SplitStreamByOutputTag {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<WaterSensor> sensorStream = env.socketTextStream("127.0.0.1", 9999)
                .map(new WaterSensorMapFunction());

        // 使用TAG标签分流
        OutputTag<WaterSensor> s1 = new OutputTag<>("s1", Types.POJO(WaterSensor.class));
        OutputTag<WaterSensor> s2 = new OutputTag<>("s2", Types.POJO(WaterSensor.class));

        SingleOutputStreamOperator<WaterSensor> ds1 = sensorStream.process(new ProcessFunction<WaterSensor, WaterSensor>() {
            @Override
            public void processElement(WaterSensor waterSensor, ProcessFunction<WaterSensor, WaterSensor>.Context context, Collector<WaterSensor> collector) throws Exception {
                if ("s1".equals(waterSensor.getId())) {
                    context.output(s1, waterSensor);
                } else if ("s2".equals(waterSensor.getId())) {
                    context.output(s2, waterSensor);
                } else {
                    // 主流
                    collector.collect(waterSensor);
                }
            }
        });

        ds1.print("主流，非s1，s2的传感器");

        SideOutputDataStream<WaterSensor> s1Stream = ds1.getSideOutput(s1);
        SideOutputDataStream<WaterSensor> s2Stream = ds1.getSideOutput(s2);

        s1Stream.printToErr("s1");
        s2Stream.printToErr("s2");

        env.execute();
    }
}
