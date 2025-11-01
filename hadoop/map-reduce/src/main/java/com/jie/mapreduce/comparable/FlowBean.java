package com.jie.mapreduce.comparable;

import lombok.Data;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
public class FlowBean implements WritableComparable<FlowBean> {
    private Long upFlow;
    private Long downFlow;
    private Long sumFlow;

    //4 实现序列化和反序列化方法,注意顺序一定要保持一致
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(upFlow);
        dataOutput.writeLong(downFlow);
        dataOutput.writeLong(sumFlow);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.upFlow = dataInput.readLong();
        this.downFlow = dataInput.readLong();
        this.sumFlow = dataInput.readLong();
    }

    public void sumFlow(){
        this.sumFlow=this.upFlow+this.downFlow;
    }

    @Override
    public String toString() {
        return  upFlow +
                "\t" + downFlow +
                "\t" + sumFlow;
    }

    @Override
    public int compareTo(FlowBean o) {
        //按照总流量比较,倒序排列
        if(this.sumFlow > o.sumFlow){
            return -1;
        }else if(this.sumFlow < o.sumFlow){
            return 1;
        }else {
            return 0;
        }
    }
}
