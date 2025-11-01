package com.jie.mapreduce.compress;

import com.jie.mapreduce.consts.PathConst;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountCompress4ReduceDriver {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);

        job.setJarByClass(WordCountCompress4ReduceDriver.class);

        job.setMapperClass(WordCountMapper.class);

        job.setReducerClass(WordCountReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, new Path(PathConst.BASE_INPUT_PATH + "inputword"));
        FileOutputFormat.setOutputPath(job, new Path(PathConst.BASE_OUTPUT_PATH + "outputwordcompress4reduce"));

        // 设置 reduce 端输出压缩开启
        FileOutputFormat.setCompressOutput(job, true);

        // 设置压缩的方式
        FileOutputFormat.setOutputCompressorClass(job, BZip2Codec.class);
// FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
// FileOutputFormat.setOutputCompressorClass(job,DefaultCodec.class);

        boolean result = job.waitForCompletion(true);

        System.exit(result?0:1);
    }
}
