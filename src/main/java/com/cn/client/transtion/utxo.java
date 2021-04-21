package com.cn.client.transtion;

import com.cn.client.transtion.txOutput.*;
import com.cn.client.transtion.txInput.*;
import com.cn.crypto.hash;

import static com.cn.config.hardware.VERSION;

public class utxo extends txoutput{
    private String Txid;
    private long Vout ;

    public Boolean IsSpent(txinput input){
        boolean equalTxId = this.Txid.compareTo(input.getTxid())==0;
        boolean equalVout = this.Vout == input.getVout();
        //此处暂时省略
        //1、把原始公钥 变换计算 得到一个 对应的公钥hash
//        String pubk256 = hash.sum(input.getPubk(), hash.HashType.SHA256);
//        String ripemd160 = hash.Ripemd160(pubk256);
//        versionPubkHash = append([]byte{VERSION}, ripemd160)
        boolean equalpubk = this.VertifyOutputWithAddress(input.getPubk());
        return equalpubk&equalTxId&equalVout;
    }

    public String getTxid() {
        return Txid;
    }

    public void setTxid(String txid) {
        Txid = txid;
    }

    public long getVout() {
        return Vout;
    }

    public void setVout(long vout) {
        Vout = vout;
    }
}
