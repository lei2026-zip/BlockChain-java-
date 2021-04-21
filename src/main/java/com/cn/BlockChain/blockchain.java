package com.cn.BlockChain;

import com.cn.console.console;
import redis.clients.jedis.*;
import com.cn.uitls.serialize;

public class blockchain{
    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public enum runtimeflag {
        lasthash_null,
        Success,
        ErrorBLock,
    }
    //是否存储到磁盘
    private final  Boolean Saveflag = true;
    //redis server路由
    public static final String URL = "localhost";
    public static final String LASTHASH = "lasthash";
    // 存储对象
    public String LastHash = null;  //最新区块
    private Jedis jedis = null;

    public blockchain(){
        jedis = new Jedis(URL);
    }

    // 添加新的区块到存储器中
    public runtimeflag AddNewBlockToDB(Block block) throws Exception {
        try {
            String lasthash = jedis.get(LASTHASH);
            if (lasthash == null) {
                if(block.getHash()==null){
                    return runtimeflag.ErrorBLock;
                }
            }else{
                //对比区块perhash 和目前的lasthash
                if (block.getPerHash().compareTo(lasthash) != 0) {
                    return runtimeflag.ErrorBLock;
                }
            }
            lasthash = block.getHash();
            console.Printf("new lasthash %s", lasthash);
            String strblock = serialize.Serialize(block);
            jedis.set(lasthash, strblock);
            jedis.set(LASTHASH, lasthash);
            if(Saveflag){
                jedis.save();
            }
            this.LastHash = lasthash;
            return runtimeflag.Success;
        }catch (Exception e){
            throw e;
        }
    }
    //通过key获取value值
    public String GetDateByKeyFromRides(String key) throws Exception {
        try {
            return jedis.get(key);
        }catch (Exception e){
            throw e;
        }
    }

    //设置key的value值
    public String SetDateByKeyFromRides(String key,String date) throws Exception {
        try {
            String str = jedis.set(key,date);
            if(Saveflag){
                jedis.save();
            }
            return str;

        }catch (Exception e){
            throw e;
        }
    }

    public void DelKeyFromRides(String key) throws Exception {
        try {
            jedis.del(key);
        }catch (Exception e){
            throw e;
        }
    }

}
