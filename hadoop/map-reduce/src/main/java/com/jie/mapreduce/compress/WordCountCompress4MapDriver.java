package com.jie.mapreduce.compress;

import com.jie.mapreduce.consts.PathConst;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountCompress4MapDriver {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        // 开启 map 端输出压缩
        conf.setBoolean("mapreduce.map.output.compress", true);

        // 设置 map 端输出压缩方式
        conf.setClass("mapreduce.map.output.compress.codec",
                BZip2Codec.class, CompressionCodec.class);

        Job job = Job.getInstance(conf);

        job.setJarByClass(WordCountCompress4MapDriver.class);

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, new Path(PathConst.BASE_INPUT_PATH + "inputword"));
        FileOutputFormat.setOutputPath(job, new Path(PathConst.BASE_OUTPUT_PATH + "outputwordcompress4map"));

        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : 1);
    }
}
