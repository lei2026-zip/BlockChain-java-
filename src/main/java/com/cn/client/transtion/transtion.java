package com.cn.client.transtion;

import com.cn.client.transtion.txInput.txinput;
import com.cn.client.transtion.txOutput.txoutput;
import com.cn.console.console;
import com.cn.crypto.hash;
import com.cn.uitls.serialize;

import java.lang.reflect.Array;
import java.security.PrivateKey;
import java.util.ArrayList;

import com.cn.crypto.keypair.keypair;

//必须实现cloneable接口 才可以调用clone方法
public class transtion implements Cloneable{
    private String TxHash;
    public txinput[] inputs;
    public txoutput[] outputs;
    private long nonce;

    /**
     * 使用私钥对某个交易进行交易的签名
     */
    public void Sign(PrivateKey prik, utxo[] utxos) throws CloneNotSupportedException {

        if(this.IsCoinbaseTransaction()){
            return;
        }

        ////交易的交易输入的个数与utxo的个数需要一致
        //if len(tx.Inputs) != len(utxos) {
        //	return errors.New("签名遇到错误，请重试")
        //}
        //拷贝交易，复制交易对象
        transtion txCopy = (transtion) this.clone();

        //tx: 包含多个交易输入TxInput
        for(int i = 0; i < txCopy.inputs.length;i++){
            txinput input = txCopy.inputs[i]; //当前遍历到的第几个交易输入

            utxo utxo = utxos[i]; //当前遍历到的第几个utxo
            //scirptPub := utxo.PubHash //获得当前遍历到的utxo的锁定脚本中的公钥哈希
            input.setPubk(utxo.getPubkhash());

            String txHash = txCopy.CalculateTxHash();

            input.setPubk(null);

            //签名过程 big.Int -> []byte


            this.inputs[i].setSig(keypair.signECDSA(prik,txHash)); //赋值的是原tx对象
        }
    }

    /**
     * 对交易进行签名验证
     */
    public Boolean VertifySign( utxo[] utxos) throws CloneNotSupportedException {

        if(this.IsCoinbaseTransaction()){
            return true;
        }

        //消费构建的input与所引用的utxo的个数不匹配，直接返回false
        if(this.inputs.length != utxos.length){
            console.Println("消费构建的input与所引用的utxo的个数不匹配");
            return false;
        }

        transtion txCopy =  (transtion) this.clone();

        Boolean result = false;
        //对交易的每一个交易输入进行签名验证
        for(int i=0;i<txCopy.inputs.length;i++){

            //签名验证：公钥、签名、原文->hash
            String pubk = txCopy.inputs[i].getPubk();     //公钥
            String signBytes = txCopy.inputs[i].getSig(); //签名

            //对交易副本中的每一个input进行还原，还原签名之前的状态
            //① 签名置空
            txCopy.inputs[i].setSig(null);
            //② pubk设置为所引用的utxo的pubkhash字段
            txCopy.inputs[i].setPubk(utxos[i].getPubkhash());
            //hash
            String txCopyHash = txCopy.CalculateTxHash();

            try {
                if(pubk==null){
                    console.Println("pubk is null !");
                    return false;
                }
                result = keypair.verifyECDSA(keypair.getPublicKey(pubk),signBytes, txCopyHash);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(!result){ //签名验证失败
                return false;
            }
        }
        return true;
    }

    /**
     * 判断某个交易是否是Coinbase交易
     */
   public boolean IsCoinbaseTransaction() {
        return this.inputs.length == 0 && this.outputs.length == 1;
    }
    /**
     * 计算交易的哈希，并将哈希值返回
     */
    public String CalculateTxHash(){
        String txhash = hash.sum(serialize.Serialize(this), hash.HashType.SHA256);
        return txhash;
    }

    //创建铸币交易
    public static transtion NewCoinBaseTx(String address,double money){
        transtion txs = new transtion();
        txs.inputs = new txinput[0];
        txs.outputs = new txoutput[1];
        txs.outputs[0] = txoutput.Lock2Address(money, address);
        txs.setTxHash(txs.CalculateTxHash());
        return txs;
    }

    //创建用户交易
    public static transtion NewTransaction(utxo[] utxos,String from,String to,String pubk,double value){
        //交易输入的容器切片
        ArrayList<txinput> txInputs = new ArrayList<txinput>();
        double inputAmount = 0;
        for( int i =0;i<utxos.length;i++){
             inputAmount += utxos[i].getValue();
             txinput input = new txinput();
             input.setPubk(pubk);
             input.setTxid(utxos[i].getTxid());
             input.setVout(utxos[i].getVout());
            //把构建好的交易输入放入到交易输入容器中
             txInputs.add(input);
        }
        //交易输出切片;
        ArrayList<txoutput> txOutputs = new ArrayList<txoutput>();

        //第一个交易输出：对应转账接收者的输出
        txoutput txOutput = txoutput.Lock2Address(value,to);
        txOutputs.add(txOutput);

        //还有可能产生找零的一个输出：交易发起者给的钱比要转账的钱多
        if( inputAmount-value > 0){ //需要找零给转账发起人
            txoutput txOutput1 =  txoutput.Lock2Address(inputAmount-value, from);
            txOutputs.add(txOutput1);
        }

        //构建交易
        transtion tx = new transtion();
        tx.setInputs((txinput[])txInputs.toArray());
        tx.setOutputs((txoutput[])txOutputs.toArray());
        tx.nonce = System.nanoTime();
        //序列化hash
        tx.setTxHash(tx.CalculateTxHash());
        return tx;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        transtion newtrs = (transtion)super.clone();
        if(newtrs.getInputs()!=null){
            txinput[] newinput = new txinput[newtrs.getInputs().length];
            for(int i=0;i<newinput.length;i++){
                newinput[i] = (txinput) newtrs.getInputs()[i].clone();
            }
            newtrs.setInputs(newinput);
        }
        if(newtrs.getOutputs()!=null){
            txoutput[] newouput = new txoutput[newtrs.getOutputs().length];
            for(int i=0;i<newouput.length;i++){
                newouput[i] = (txoutput) newtrs.getOutputs()[i].clone();
            }
            newtrs.setOutputs(newouput);
        }
        if(newtrs.getTxHash()!=null) {
            newtrs.setTxHash(new String(newtrs.getTxHash()));
        }
        return newtrs;
    }

    public String getTxHash() {
        return TxHash;
    }

    public void setTxHash(String txHash) {
        TxHash = txHash;
    }

    public txinput[] getInputs() {
        return inputs;
    }

    public void setInputs(txinput[] inputs) {
        this.inputs = inputs;
    }

    public txoutput[] getOutputs() {
        return outputs;
    }

    public void setOutputs(txoutput[] outputs) {
        this.outputs = outputs;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }
}
