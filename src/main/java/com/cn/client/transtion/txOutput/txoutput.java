package com.cn.client.transtion.txOutput;

import com.cn.client.transtion.transtion;

public class txoutput implements Cloneable {
    private double value;
    private String pubkhash;


//地址add1
//私钥 -> 公钥 -> hash 160 +version 2次hash check  pinjie base58 -> add1
    /**
     * 构建一个新的交易输出，锁一定数量的钱到某个交易输出上，并将该交易输出返回
     */
     public static txoutput Lock2Address(double value ,String add){
        String pubHash = add.substring(add.length()-4);
        txoutput output = new txoutput();
        output.setValue(value);
        output.setPubkhash(pubHash);
        return output;
    }
    /**
     *  该方法用于验证某个交易输出是否是属于某个地址的收入
     */
    public Boolean VertifyOutputWithAddress(String addr){
        String pubHash = addr.substring(addr.length()-4);
        return this.pubkhash.compareTo(pubHash) == 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        txoutput newtrs = (txoutput) super.clone();
        if(newtrs.getPubkhash()!=null) {
            newtrs.setPubkhash(new String(newtrs.getPubkhash()));
        }
        return newtrs;
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getPubkhash() {
        return pubkhash;
    }

    public void setPubkhash(String pubkhash) {
        this.pubkhash = pubkhash;
    }
}
