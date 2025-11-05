package com.jie.flink.udf;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ZhuPo
 * @date 2025/11/5 15:44
 */
public class RichFunctionExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度
        environment.setParallelism(2);
        
        environment.fromElements(1,2,3,4)
                .map(new RichMapFunction<Integer, Integer>() {
                    @Override
                    public Integer map(Integer integer) throws Exception {
                        return integer + 1;
                    }

                    // 是Rich Function的初始化方法，也就是会开启一个算子的生命周期
                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        System.out.println("索引是：" + getRuntimeContext().getIndexOfThisSubtask() + " 的任务的生命周期开始");
                    }

                    // 是生命周期中的最后一个调用的方法，类似于结束方法。一般用来做一些清理工作。
                    @Override
                    public void close() throws Exception {
                        super.close();
                        System.out.println("索引是：" + getRuntimeContext().getIndexOfThisSubtask() + " 的任务的生命周期结束");
                    }
                })
                .print();

        environment.execute();
    }
}
