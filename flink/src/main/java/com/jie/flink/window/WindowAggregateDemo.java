package com.jie.flink.window;

import com.jie.flink.bean.WaterSensor;
import com.jie.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * 聚合函数
 * 首先调用createAccumulator()为任务初始化一个状态（累加器）；
 * 而后每来一个数据就调用一次add()方法，对数据进行聚合，得到的结果保存在状态中；
 * 等到了窗口需要输出时，再调用getResult()方法得到计算结果。
 * 很明显，与ReduceFunction相同，AggregateFunction也是增量式的聚合
 *
 * @Author ZhuPo
 * @date 2025/11/6 22:11
 */
public class WindowAggregateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS = env.socketTextStream("127.0.0.1", 9999)
                .map(new WaterSensorMapFunction());

        KeyedStream<WaterSensor, String> sensorKS = sensorDS.keyBy(WaterSensor::getId);

        WindowedStream<WaterSensor, String, TimeWindow> sensorWS = sensorKS.window(TumblingProcessingTimeWindows.of(Time.seconds(10)));

        /**
         * 窗口函数，增量聚合，aggregate
         *
         * 第一个类型：输入数据类型
         * 第二个类型：累加器类型
         * 第三个类型：输出数据类型
         *
         * 与reduce的区别，第一条数据都会开窗口
         *
         * -- aggregate
         * 第一条数据来之后，累加器会初始化，然后调用add方法，将数据聚合到累加器中
         * 当窗口触发之后，会调用getResult方法，将累加器中的数据，转换成结果数据
         * 累加器中的数据，转换成结果数据，作为窗口的输出数据
         *
         * -- reduce
         * 第一条数据来之后，不会执行reduce计算
         */
        SingleOutputStreamOperator<String> aggregate = sensorWS.aggregate(new AggregateFunction<WaterSensor, Integer, String>() {
            @Override
            public Integer createAccumulator() {
                System.out.println("创建累加器");
                return 0;
            }

            @Override
            public Integer add(WaterSensor waterSensor, Integer value) {
                System.out.println("调用add方法,value=" + value);
                return waterSensor.getVc() + value;
            }

            @Override
            public String getResult(Integer integer) {
                System.out.println("调用getResult方法");
                return integer.toString();
            }

            @Override
            public Integer merge(Integer integer, Integer acc1) {
                System.out.println("调用merge方法");
                // 只有会话窗口才会调用此方法
                return integer + acc1;
            }
        });

        aggregate.print();

        env.execute();
    }
}
