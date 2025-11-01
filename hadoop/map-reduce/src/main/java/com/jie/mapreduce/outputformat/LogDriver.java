package com.jie.mapreduce.outputformat;

import com.jie.mapreduce.consts.PathConst;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class LogDriver {
    public static void main(String[] args) throws IOException,
            ClassNotFoundException, InterruptedException {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(LogDriver.class);
        job.setMapperClass(LogMapper.class);
        job.setReducerClass(LogReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        //设置自定义的 outputformat
        job.setOutputFormatClass(LogOutputFormat.class);

        // （1）ReduceTask=0，表示没有Reduce阶段，输出文件个数和Map个数一致。
        //（2）ReduceTask默认值就是1，所以输出文件个数为一个。
        //（3）如果数据分布不均匀，就有可能在Reduce阶段产生数据倾斜
        //（4）ReduceTask数量并不是任意设置，还要考虑业务逻辑需求，有些情况下，需要计算全
        //局汇总结果，就只能有1个ReduceTask。
        //（5）具体多少个ReduceTask，需要根据集群性能而定。
        //（6）如果分区数不是1，但是ReduceTask为1，是否执行分区过程。答案是：不执行分区过
        //程。因为在MapTask的源码中，执行分区的前提是先判断ReduceNum个数是否大于1。不大于1
        //肯定不执行
//        job.setNumReduceTasks(4);

        FileInputFormat.setInputPaths(job, new Path(PathConst.BASE_INPUT_PATH + "inputoutputformat"));
        //虽 然 我 们 自 定 义 了 outputformat， 但 是 因 为 我 们 的 outputformat 继 承fileoutputformat
        //而 fileoutputformat 要输出一个_SUCCESS 文件，所以在这还得指定一个输出目录
        FileOutputFormat.setOutputPath(job, new Path(PathConst.BASE_OUTPUT_PATH + "outputoutputformat"));

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}