package com.cn.client.transtion.txInput;

import com.cn.client.transtion.transtion;

public class txinput implements Cloneable{
    private String Txid;   //长度必须是32个字节
    private long Vout;
    private String Sig;
    private String Pubk;

    @Override
    public Object clone() throws CloneNotSupportedException {
        txinput newtrs = (txinput)super.clone();
        if (newtrs.getSig() != null) {
            newtrs.setSig(new String(newtrs.getSig()));
        }
        if(newtrs.getPubk()!=null) {
            newtrs.setPubk(new String(newtrs.getPubk()));
        }
        if(newtrs.getTxid()!=null){
            newtrs.setTxid(new String(newtrs.getTxid()));
        }
        return newtrs;
    }

    //判断公钥与inputd的相匹配
    public Boolean checkPubk(String pubk){
       return this.Pubk.compareTo(pubk)==0;
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

    public String getSig() {
        return Sig;
    }

    public void setSig(String sig) {
        Sig = sig;
    }

    public String getPubk() {
        return Pubk;
    }

    public void setPubk(String pubk) {
        Pubk = pubk;
    }
}
