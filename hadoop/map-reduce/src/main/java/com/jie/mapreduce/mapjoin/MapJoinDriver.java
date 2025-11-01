package com.jie.mapreduce.mapjoin;

import com.jie.mapreduce.consts.PathConst;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MapJoinDriver {

    public static void main(String[] args) throws IOException,
            URISyntaxException, ClassNotFoundException, InterruptedException {

        // 1 获取 job 信息
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        // 2 设置加载 jar 包路径
        job.setJarByClass(MapJoinDriver.class);
        // 3 关联 mapper
        job.setMapperClass(MapJoinMapper.class);
        // 4 设置 Map 输出 KV 类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        // 5 设置最终输出 KV 类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        // 加载缓存数据
        job.addCacheFile(new URI("file:///C:/Users/35017/Desktop/other/hadoop/input/inputtable/pd.txt"));
        // 如果是集群运行,需要设置 HDFS 路径
//        job.addCacheFile(new URI("hdfs://hadoop102:8020/cache/pd.txt"))

        // Map 端 Join 的逻辑不需要 Reduce 阶段，设置 reduceTask 数量为 0
        job.setNumReduceTasks(0);

        // 6 设置输入输出路径
        FileInputFormat.setInputPaths(job, new Path(PathConst.BASE_INPUT_PATH + "inputtable2"));
        FileOutputFormat.setOutputPath(job, new Path(PathConst.BASE_OUTPUT_PATH + "mapjoinoutputtable"));
        // 7 提交
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
