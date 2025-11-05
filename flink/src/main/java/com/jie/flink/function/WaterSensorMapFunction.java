package com.jie.flink.function;

import com.jie.flink.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author ZhuPo
 * @date 2025/11/5 16:29
 */
public class WaterSensorMapFunction implements MapFunction<String, WaterSensor> {

    @Override
    public WaterSensor map(String line) throws Exception {
        String[] split = line.split(",");
        return new WaterSensor(split[0],Integer.parseInt(split[1]),Integer.parseInt(split[2]));
    }
}
