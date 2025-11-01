package com.jie.mapreduce.comparable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMapper extends Mapper<LongWritable, Text, FlowBean, Text> {

    private Text outV = new Text();
    private FlowBean outK = new FlowBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] split = line.split("\t");

        //3 抓取我们需要的数据:手机号,上行流量,下行流量
        String phone = split[1];
        String up = split[split.length - 3];
        String down = split[split.length - 2];

        outK.setUpFlow(Long.parseLong(up));
        outK.setDownFlow(Long.parseLong(down));
        outK.sumFlow();

        outV.set(phone);

        context.write(outK, outV);
    }

}
