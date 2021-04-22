package com.cn.client.wallet;

import com.cn.client.wallet.walletUtil.walletUtil;
import com.cn.console.console;
import com.cn.crypto.keypair.keypair;
import com.cn.crypto.keypair.address;
import com.cn.uitls.serialize;

import java.security.Key;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;


public class wallet extends walletUtil {
    public final String USERKEY = "SERIVEADDR";

    public String NewUseraddress() throws Exception {
        keypair key = keypair.getKeyPair();
        String adderss = address.getBitAddress(key.getPubK());
        this.Address.put(adderss,key);
        console.Println("ed"+adderss);
        //序列化所以暂存的密钥对
        String str = serialize.Serialize(this.Address);
        //存储到rides数据库中
        console.Println(str);
        this.Chain.SetDateByKeyFromRides(USERKEY,str);
        console.Println("NewAddress is Successed .");
        console.Println("Address:"+adderss);
        return adderss;
    }

    public void ReLoadkeypairFromRides() throws Exception {
        Map<String,keypair> keys = new HashMap<String, keypair>();
        String date = this.Chain.GetDateByKeyFromRides(USERKEY);
        if(date==null){
            return;
        }
        //=============================
        //此处反序列化会出现数据不匹配的情况,目前进行序列化再json解析来解决
        keys = serialize.Deserialize(date, keys.getClass());
        Map<String,keypair> keys1 = new HashMap<String, keypair>();
        for(String key : keys.keySet()){

            Object value = keys.get(key);

            String str = serialize.Serialize(value);
            keys1.put(key,serialize.Deserialize(str,keypair.class));
        }
        this.Address = keys1;
    }

    public  void queryAllkeypair(){
        for(String key : this.Address.keySet()){
            console.Println(key);
            console.Println(serialize.Serialize(this.Address.get(key)));
        }
    }
}
