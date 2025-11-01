package com.jie.mapreduce.partitioner;

import com.jie.mapreduce.consts.PathConst;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * （1）如果ReduceTask的数量> getPartition的结果数，则会多产生几个空的输出文件part-r-000xx；
 * （2）如果1<ReduceTask的数量<getPartition的结果数，则有一部分分区数据无处安放，会Exception；
 * （3）如果ReduceTask的数量=1，则不管MapTask端输出多少个分区文件，最终结果都交给这一个
 * ReduceTask，最终也就只会产生一个结果文件part-r-00000；
 * （4）分区号必须从零开始，逐一累加。
 */
public class FlowDriver {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf);
        job.setJarByClass(FlowDriver.class);
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //8 指定自定义分区器
        job.setPartitionerClass(ProvincePartitioner.class);

        //9 同时指定相应数量的 ReduceTask
        job.setNumReduceTasks(5);

        //6 设置程序的输入输出路径
        FileInputFormat.setInputPaths(job, new Path(PathConst.BASE_INPUT_PATH + "inputflow"));
        FileOutputFormat.setOutputPath(job, new Path(PathConst.BASE_OUTPUT_PATH + "outputflow4partition"));

        //7 提交 Job
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
