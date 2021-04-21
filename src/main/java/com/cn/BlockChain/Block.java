package com.cn.BlockChain;


//区块接口
public interface Block {
    public String sumBlockHash();
    public  void setHeight(long height);
    public  void setHash(String hash);
    public  void setPerHash(String perHash);
    public  void setDate(String date);
    public  long getHeight();
    public  String getHash();
    public  String getPerHash();
    public  String getDate();
}
