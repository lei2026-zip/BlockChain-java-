package com.cn.client;

import com.cn.BlockChain.blockchain;
import com.cn.client.wallet.wallet;
import com.cn.concensus.pow.powblock;
import com.cn.cmd.commid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cn.console.console;
import com.cn.uitls.serialize;

public class client extends wallet {

     public String init() throws Exception {
          //检查redis数据库的存在
          try {
              this.Chain.getJedis().ping();
          }catch (Exception e){
              return "Jedis isn't running !";
          }
         //初始化密钥对
         this.ReLoadkeypairFromRides();
         //初始化lastblock and lasthash
         this.Chain.LastHash = this.Chain.GetDateByKeyFromRides(blockchain.LASTHASH);

         if( this.Chain.LastHash!=null){
             //如果last区块存在 则初始化
             String strblock = this.Chain.GetDateByKeyFromRides(this.Chain.LastHash);
             this.setLastBlock(serialize.Deserialize(strblock,powblock.class));
         }
         return null;
     }
    public String ParseArgs(String command,String args){
        if (command==null) {
            return "command is null";
        }else {
            //2、确定用户输入的命令
            switch(command){
                case commid.GENERATEGENESIS:
                     return this.GenerateGensis(args);
                case commid.SENDTRASACTION: //发送一笔新
                    return this.SendTransaction(args);
                case commid.GETBALANCE: //获取地址的余额功能
                    try {
                        return String.valueOf(this.GetBalance(args));   //有精度问题 比如 80.2 结果会是79.1999999999  暂未解决
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "getbalance is error:"+e.toString();
                    }
                case commid.GETLASTBLOCK:
                    return this.GetLastBlock();
                case commid.GETALLBLOCKS: //获取所有的区块信息并打印输出给用户
                     return this.GetAllBlocks();
                case commid.NEWADDRESS:
                    return this.NewAddress();
                case commid.GETBLOCKCOUNT:
                    return this.GetBlockCount();
                case commid.HELP:
                    return this.Help();
                case commid.RESET:
                    try {
                        return this.del();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "delete is error:"+e.toString();
                    }
                default:
                    return this.Default();
            }
        }
    }
    public String NewAddress(){
         String addr = null;
        try {
            addr = this.NewUseraddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addr;
    }


    public String Default() {
        return "Error Unknown subcommand !"+"\ninput \"help\" for more information.";
    }

    public String GetBlockCount(){
       // fmt.Printf("当前共有%d个区块\n",C.LastBlock.Height)
        return String.valueOf(this.getLastBlock().getHeight());
    }

    public String GetAllBlocks(){
        ArrayList<powblock> blocks = new ArrayList<powblock>();
        try {
            for(;this.BlockNext();){
                blocks.add(this.getIteratorBlock());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(blocks.size()==0){
            console.Println("blocks is null...");
            return "blocks is null...";
        }else{
            console.Println("getblock is susseccced !");
        }
        return  serialize.Serialize(blocks.toArray(powblock[]::new));
    }

    public String GetLastBlock(){
        String lasthashbytes = null;
        try {
            lasthashbytes = this.Chain.GetDateByKeyFromRides(blockchain.LASTHASH);
            if(lasthashbytes == null){
                console.Println("抱歉，当前暂无最新区块");
                console.Println("请使用go run main.go generategensis生成创世区块");
                return "lasthash is null";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "getlasthash is error"+e.toString();
        }
        try {
            String lastblockbyte = this.Chain.GetDateByKeyFromRides(lasthashbytes);
            if(lastblockbyte==null){
                return "LastBlock is null";
            }
            return lastblockbyte;
        } catch (Exception e) {
            e.printStackTrace();
            return "getlastblock is error:"+e.toString();
        }
    }

    /**
     * 获取地址的余额的功能
     */

    public String getbalance(String add){
        if(this.Address == null){
            return "Address is null";
        }
        String totalbalance  = null;
        try {
            totalbalance = String.valueOf(this.GetBalance(add));
        } catch (Exception e) {
            e.printStackTrace();
            return "getbalance is error"+e.toString();
        }
        console.Printf("地址%s的余额是%f\n ",add,totalbalance);
        return totalbalance;
    }

    /**
     * 发送一笔新的交易
     */
    public String SendTransaction(String str){
        Map<String,String> result = new HashMap<String, String>();
        result = serialize.Deserialize(str,result.getClass());
        if(result.get("from")==null||result.get("to")==null||result.get("value")==null){
            return "请输入正确的格式 ,详细参考 help";
        }
        String err = null;
        try {
            err = this.SendTransaction(result.get("from"),result.get("to"),result.get("value"));
        } catch (Exception e) {
            e.printStackTrace();
            return err + "\n err:"+e.toString();
        }
        console.Println("SendTransaction is successed.");
        return "\nSendTransaction is successed.";
    }

    public String GenerateGensis(String str){
        Map<String,String> result = new HashMap<String, String>();
        result = serialize.Deserialize(str,result.getClass());
        if(result.get("address")==null){
            return "Error: please input user address !";
        }
//        ////1、先判断是否已存在创世区块
//        hashBig := new(big.Int)
//        hashBig = hashBig.SetBytes(C.Wallet_util.LastBlock.Hash)
//        if hashBig.Cmp(big.NewInt(0)) == 1 {//创世区块的hash值不为0，即有值
//        	return errors.New("Error:已有coinbase交易，暂不能重复构建")
//        }
        //2、如果创世区块不存在，才去调用creategenesis
        String coinbaseHash = null;
        try {
            coinbaseHash = this.CreateCoinbase(result.get("address"),100);
        } catch (Exception e) {
            e.printStackTrace();
            return "createcoinbase is error:"+e.toString();
        }
        console.Printf("恭喜，COINBASE交易创建成功，交易hash是：%s\n", coinbaseHash);
        return "Generate coinbase is successed!";
    }
    /**
     * 该方法用于向控制台输出项目的使用说明
     */
    public String Help(){
        String str = "\n-------------Welcome to Mimical BlockChain Transtion system Project-------------\n USAGE：";
        for(int i=0;i< commid.POST_COMMAD.length;i++){
            str = str + "\n    "+commid.POST_COMMAD[i][0]+"   "+commid.POST_COMMAD[i][1];
        }
        str = str + "\nthis is all commmid.";
        return str;
    }

    //迭代删除所有区块
    public void reset(){
        
    }

}
