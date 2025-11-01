package com.jie.mapreduce.comparable;

import com.jie.mapreduce.consts.PathConst;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 手机号不合并，仅排序
 */
public class FlowDriver {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf);
        job.setJarByClass(FlowDriver.class);
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);

        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //6 设置程序的输入输出路径
        FileInputFormat.setInputPaths(job, new Path(PathConst.BASE_INPUT_PATH + "inputflow"));
        FileOutputFormat.setOutputPath(job, new Path(PathConst.BASE_OUTPUT_PATH + "outputflow4comparable"));

        //7 提交 Job
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
