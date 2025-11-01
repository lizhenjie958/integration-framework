package com.jie.mapreduce.compress;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
/**
 * KEYIN, map阶段输入的key的类型：LongWritable
 * VALUEIN,map阶段输入value类型：Text
 * KEYOUT,map阶段输出的Key类型：Text
 * VALUEOUT,map阶段输出的value类型：IntWritable
 */
public class WordCountMapper extends Mapper</**行号*/LongWritable, /**内容*/Text, /**分割后内容*/Text, /**内容计数*/IntWritable> {

    Text k = new Text();
    IntWritable v = new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] split = line.split(" ");
        for(String word:split){
            k.set(word);
            context.write(k,v);
        }
    }
}
