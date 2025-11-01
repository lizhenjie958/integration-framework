package com.jie.mapreduce.etl;

import com.jie.mapreduce.consts.PathConst;
import com.jie.mapreduce.outputformat.LogDriver;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WebLogDriver {
    public static void main(String[] args) throws Exception {

        // 1 获取 job 信息
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2 加载 jar 包
        job.setJarByClass(LogDriver.class);

        // 3 关联 map
        job.setMapperClass(WebLogMapper.class);

        // 4 设置最终输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        // 设置 reducetask 个数为 0, 则无需进行reduce阶段
        job.setNumReduceTasks(0);

        // 5 设置输入和输出路径
        FileInputFormat.setInputPaths(job, new Path(PathConst.BASE_INPUT_PATH + "inputlog"));
        FileOutputFormat.setOutputPath(job, new Path(PathConst.BASE_OUTPUT_PATH + "ouputlog"));

        // 6 提交
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
