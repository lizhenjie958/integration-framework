package com.jie.flink.aggregation;

import com.jie.flink.bean.WaterSensor;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * reduce同简单聚合算子一样，也要针对每一个key保存状态。
 * 因为状态不会清空，所以我们需要将reduce算子作用在一个有限key的流上。
 * @author ZhuPo
 * @date 2025/11/5 14:24
 */
public class UseReduceFunction {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.socketTextStream("127.0.0.1",9999)
                .map(line ->{
                    String[] split = line.split(",");
                    return new WaterSensor(split[0],Long.parseLong(split[1]),Integer.parseInt(split[2]));
                })
                .keyBy(WaterSensor::getId)
                .reduce((value1, value2) -> {
                    int maxVc = Math.max(value1.getVc(), value2.getVc());
                    // 实现max(vc)的效果  取最大值，其他字段以当前组的第一个为主
                    // value1.setVc(maxVc);
                    if(value1.getVc() > value2.getVc()){
                        value1.setVc(maxVc);
                        return value1;
                    }else {
                        value2.setVc(maxVc);
                        return value2;
                    }
                })
                .print();

        environment.execute();

    }
}
