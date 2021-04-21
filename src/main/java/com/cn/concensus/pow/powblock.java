package com.cn.concensus.pow;

import com.alibaba.fastjson.JSON;
import com.cn.BlockChain.Block;
import com.cn.config.hardware;

import java.security.Timestamp;
import java.sql.Time;
import java.util.Arrays;
import java.util.Timer;

import com.cn.uitls.serialize;
import com.cn.BlockChain.blockchain;

import com.cn.crypto.hash;

public class powblock implements Block {
    private long Height = 0;  //区块高度
    private long Version = hardware.BLOCKVERSION; //版本号
    private String Hash = null;   //区块hash
    private String PerHash="00000000000000000000000000000000"; //上一个区块的hash
    private long Timestamp = 0; //时间戳
    private long Nonce = 0;  //随机数
    private String Date = null; //区块数

    public powblock(){

    }

    public powblock(long hei,String date){
            this.Height = Height;
            this.Date=date;
            this.Timestamp = System.currentTimeMillis();
            this.Version = hardware.BLOCKVERSION;
    }
    //重载
    public powblock(long hei,String per,String date){
            this.Height = Height;
            this.Date=date;
            this.PerHash = per;
            this.Timestamp = System.currentTimeMillis();
            this.Version = hardware.BLOCKVERSION;
    }

    public String sumBlockHash(){
        return hash.sum(serialize.Serialize(this), hash.HashType.SHA256);
    }

    @Override
    public String toString() {
        return "block{" +
                "Height=" + Height +
                ", Version=" + Version +
                ", Hash=" + Hash +
                ", PerHash=" + PerHash +
                ", Timestamp=" + Timestamp +
                ", Nonce=" + Nonce +
                ", Date=" + Date +
                '}';
    }
    //=======================================
    public void setHeight(long height) {
        Height = height;
    }

    public void setVersion(long version) {
        Version = version;
    }

    public void setHash(String hash) {
        Hash = hash;
    }

    public void setPerHash(String perHash) {
        PerHash = perHash;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }

    public void setNonce(long nonce) {
        Nonce = nonce;
    }
    public void setDate(String date) {
        this.Date = date;
    }
    public long getHeight() {
        return Height;
    }

    public long getVersion() {
        return Version;
    }

    public String getHash() {
        return Hash;
    }

    public String getPerHash() {
        return PerHash;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public long getNonce() {
        return Nonce;
    }

    public String getDate() {
        return Date;
    }
}
