package com.cn.client.wallet;

import com.cn.client.wallet.walletUtil.walletUtil;
import com.cn.console.console;
import com.cn.crypto.keypair.keypair;
import com.cn.crypto.keypair.address;
import com.cn.uitls.serialize;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;


public class wallet extends walletUtil {
    private String USERKEY = "SERIVEADDR";

    public String NewUseraddress() throws Exception {
        keypair key = keypair.getKeyPair();
        String adderss = address.getBitAddress(key.getPubK());
        console.Println(adderss);
        this.Address.put(adderss,key);
        console.Println("ed"+adderss);
        //序列化所以暂存的密钥对
        String str = serialize.Serialize(this.Address);
        //存储到rides数据库中
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
        keys = serialize.Deserialize(date, keys.getClass());
        this.Address = keys;
    }
}
