package com.cn.client.wallet.walletUtil;

import com.cn.BlockChain.blockchain;
import com.cn.concensus.pow.powblock;
import com.cn.client.transtion.transtion;
import com.cn.concensus.pow.powblock;
import com.cn.BlockChain.blockchain;
import com.cn.crypto.keypair.*;
import com.cn.client.transtion.utxo;
import com.cn.client.transtion.txInput.txinput;
import com.cn.client.transtion.txOutput.txoutput;
import com.cn.crypto.keypair.*;
import com.cn.console.console;

import com.cn.uitls.serialize;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.*;


public class walletUtil {
      public Map<String,keypair> Address = new  HashMap<String, keypair>();
      public blockchain Chain = new blockchain();
      //用于的检索迭代
      private powblock IteratorBlock = null;
      private powblock LastBlock = null;

      public String CreateCoinbase(String addr,double value) throws Exception {
            //isValid := BitcoinAddress.CheckAddress(addr)
            //if !isValid{
            //	return nil,errors.New("Address is 不合法的")
            //}
            transtion coinbase = transtion.NewCoinBaseTx(addr,value);

            String baseStr = serialize.Serialize(coinbase);
            console.Println("base::"+baseStr);
            powblock block;
            if(this.LastBlock==null){
                  block = new powblock(0,"00000000000000000000000000000000",null);
            }else{
                  block = new powblock(this.LastBlock.getHeight()+1,this.LastBlock.getHash(),null);
            }
            block.setHash(block.sumBlockHash());   //nonce 暂时为0
            block.setDate(baseStr);
            this.Chain.AddNewBlockToDB(block);
            this.LastBlock = block;
            return coinbase.getTxHash();
      }

      public String SendTransaction(String fromaddr,String toaddr,String value) throws Exception {
            String fromSlice = fromaddr;
            String toSlice = toaddr;
            double valueSlice = (double) serialize.Deserialize(value,double.class);
            //判断参数的长度，筛选参数不匹配的情况
            if(fromaddr==null||toSlice==null||valueSlice==0){
                  return "Please enter the correct parameters \n Forexmpl: go run main.go sendtransaction {\"from\":\"\",\"to\":\"\",\"value\":\"9.9\"";
            }
            if(this.LastBlock==null||this.getLastBlock()==null){
                  return "blockchain is null";
            }

            //地址有效性的判断
            //交易发起人的地址是否合法，合法为true，不合法为false
            boolean isFromValid = address.CheckBitAddress(fromSlice);
            //交易接收者的地址是否合法，合法为true，不合法为false
            boolean isToValid = address.CheckBitAddress(toSlice);
            //from: 合法   合法
            //to:   不合法  不合法
            if(!isFromValid || !isToValid){
                  return "交易的参数地址不合法，请检查后重试";
            }

            //遍历参数的切片，创建交易
            transtion txs = new transtion();
//            for(int index = 0; index < lenFrom; index++){
            ArrayList<utxo> utxos = this.GetUTXOsWithBalance(fromSlice,null);  //第二个参数同步其他还未上链的交易,防止重复花费同一个output
            if(utxos==null){
                  return "utxos is null";
            }
            double frombalance = SumbalancebyUtxo(utxos.toArray(utxo[]::new));
            if(frombalance < valueSlice){
                 return "抱歉，" + fromSlice + "余额不足，请充值！";
            }
            console.Printf("\n转账发起人%s\n当前余额：%f\n接收者:%s\n转账数额：%f\n剩余: %f\r\n", fromSlice,frombalance, toSlice, valueSlice,frombalance-valueSlice);
            double inputAmount = 0; //总的花费的钱数


            ArrayList<utxo> uts = new ArrayList<utxo>();
            for(int i=0;i<utxos.size();i++){
                  inputAmount += utxos.get(i).getValue();
                  uts.add(utxos.get(i));
                  if(inputAmount >= valueSlice){
                        //够花了
                        break;
                  }

            }

            transtion tx = transtion.NewTransaction((utxo[]) uts.toArray(utxo[]::new), fromSlice,toSlice,this.Address.get(fromSlice).getPubK(), valueSlice);

            //私钥签名
            tx.Sign(keypair.getPrivateKey(this.Address.get(fromSlice).getPriK()));
            //接收方公钥验签
            boolean vertif = tx.VertifySign(keypair.getPublicKey(this.Address.get(fromSlice).getPubK()));
            if(!vertif){
                  console.Println("Vertif is false !");
                  return "Vertif is false !";
            }
//            }
            String txsstr = serialize.Serialize(tx);
            powblock block;
            if(this.getLastBlock()==null){
                  block = new powblock(0,txsstr);
                  block.setHash(block.sumBlockHash());
            }else{
                  block = new powblock(this.LastBlock.getHeight()+1,this.LastBlock.getHash(),txsstr);
                  block.setHash(block.sumBlockHash());   //nonce 暂时为0
            }
            blockchain.runtimeflag flag = this.Chain.AddNewBlockToDB(block);
            switch (flag) {
                  case Success: break;
                  case ErrorBLock: return "ErrorBlock";
                  case lasthash_null: return "Lasthash is null";
                  default: return "default";
            }
            this.LastBlock = block;
            return null;
      }


       /**
       * 该方法用于实现chainIterator迭代器接口的方法，用于判断是否还有区块
       */
        public boolean BlockNext() throws Exception {
              //是否还有前一个区块 并返回暂存该区块到IteratorBlock
              String lasthash = this.Chain.GetDateByKeyFromRides(blockchain.LASTHASH);
              if(lasthash==null){
                   return false;
              }
              String currentBlockstr;
              //判断IteratorBlock是否为空的
              if(this.IteratorBlock == null){
                    currentBlockstr = this.Chain.GetDateByKeyFromRides(lasthash);
                    if(currentBlockstr==null){
                          console.Println("currectblockstr is null");
                          return false;
                    }
              }else{
                    String hash = this.IteratorBlock.getPerHash();
                    console.Println(hash);
                    if(hash==null||hash.compareTo("00000000000000000000000000000000")<=1){
                          this.IteratorBlock.setHash(null);
                          return false;
                    }
                    currentBlockstr =this.Chain.GetDateByKeyFromRides(this.IteratorBlock.getPerHash());
              };
              this.IteratorBlock = serialize.Deserialize(currentBlockstr,powblock.class);
              return true;
        }

       /**
       * 定义该方法，用于实现寻找与from有关的所有可花费的交易输出，即寻找UTXO
       */
        public utxo[] SearchUTXOs(String from) throws Exception {
              //定义容器，存放from的所有的花费
              ArrayList<txinput> spends = new ArrayList<txinput>();

              //定义容器，存放from的所有的收入
              ArrayList<utxo> inComes = new ArrayList<utxo>();
              this.IteratorBlock = null;
              //使用迭代器进行区块的遍历
              while(this.BlockNext()){ //遍历区块
                    console.Println("blockheight:"+this.IteratorBlock.getHeight());
                    transtion Txs;
                    Txs =  serialize.Deserialize(this.IteratorBlock.getDate(),transtion.class);
                    if(Txs==null){
                        break;
                    }
//                    for(int i=0;i<Txs.length;i++){ //遍历区块的交易
                          //a、遍历交易输入
                    for(int j=0;j<Txs.inputs.length;j++) {
//                          console.Println("compare:");
//                          console.Println(Txs.inputs[j].getPubk());
//                          console.Println("from:"+from);
//                          console.Println(this.Address.get(from).getPriK());
                          if(Txs.inputs[j]==null){
                                Txs.inputs = null;
                                break;
                          }
                          if (Txs.inputs[j].getPubk().compareTo(this.Address.get(from).getPubK()) != 0) {
                                continue;
                          }

                          //该交易输入是from的，即from花钱了
                          spends.add(Txs.inputs[j]);
//                          }
                    }
//                    console.Println("spent:"+spends.size());
                    //b、遍历交易输出
                    for(int k=0;k<Txs.outputs.length;k++){
                          if (!Txs.outputs[k].VertifyOutputWithAddress(from)){
                              continue;
                          }
                          //该交易输出是from的，即from有收入
                          utxo input = new utxo();
                          input.setTxid(Txs.getTxHash());
                          input.setVout(k);
                          input.setValue(Txs.outputs[k].getValue());
                          input.setPubkhash(Txs.outputs[k].getPubkhash());
                          inComes.add(input);
                    }

            }

              ArrayList<utxo> utxos = new ArrayList<utxo>();
              //遍历spends和inComes,将已花费的记录剔除掉，剩下可花费的UTXO
              boolean isInComeSpend;
              for(int i=0;i<inComes.size();i++){
                    //判断每一笔收入是否在之前的交易中已经被花过了
                    isInComeSpend = false;
                    console.Println("OUTX txid:"+inComes.get(i).getTxid());
                    console.Println("OUTX value:"+inComes.get(i).getValue());
                    console.Println("OUTX Vout:"+inComes.get(i).getVout());
                    for(int j=0;j<spends.size();j++){ //
                          console.Println("spent txid:"+spends.get(j).getTxid());
                          console.Println("spent Vout:"+spends.get(j).getVout());
                          if((inComes.get(i).getTxid().equals(spends.get(j).getTxid()))&&(inComes.get(i).getVout() == spends.get(j).getVout())){
                                //spends =  append(spends[:index],spends[index+1:]...)//防止一个输入标记多个输出
                                isInComeSpend = true;
                                break;
                          }
                    }
                    //追加
                    if(!isInComeSpend){ //isInComeSpend如果如果为false，表示未被花,可加到utxos中
                          utxos.add(inComes.get(i));
                    }
              }
              return  utxos.toArray(utxo[]::new);
        }
      /**
       * 获取某个地址的余额
       */
      public double GetBalance(String addr) throws Exception {
        utxo[] utxos = this.GetUTXOsWithBalance(addr,null).toArray(utxo[]::new);
        return SumbalancebyUtxo(utxos);
      }

      /**
       * 通过获取的所有未花费的utxo计算并返回余额
       * @param utxos
       * @return
       */
       public static double SumbalancebyUtxo(utxo[] utxos) {
             double balance = 0;
             for(int i=0;i<utxos.length;i++) {
                   balance +=utxos[i].getValue();
             }
             console.Printf("%f\r\n",balance);
             return balance;
       }
       /**
       *
       * @param addr
       * @param txs
       * @return 获取未话费的交易输出utxo并返回
       */
        public ArrayList<utxo> GetUTXOsWithBalance(String addr,transtion[] txs) throws Exception {
              keypair key = (keypair) this.Address.get(addr);
              if(key == null){
                   console.Println("Keypair is null !");
                   return null;
              }
              utxo[] dbutxos = this.SearchUTXOs(addr);
              //2、遍历内存中的txs切片, 如果当前已构建还未存储的交易已经花了前，要剔除掉
              ArrayList<txinput> memSpends = new ArrayList<txinput>();
              ArrayList<utxo> memInComes = new ArrayList<utxo>();
              if(txs!=null) {
                    for (int i = 0; i < txs.length; i++) {
                          //花的钱
                          for (int j = 0; j < txs[i].inputs.length; j++) {
                                if (txs[i].inputs[j].checkPubk(key.getPubK())) {
                                      memSpends.add(txs[i].inputs[j]);
                                }
                          }
                          //收入的钱
                          for (int k = 0; k < txs[i].outputs.length; k++) {
                                if (txs[i].outputs[k].VertifyOutputWithAddress(addr)) {
                                      utxo in = new utxo();
                                      in.setPubkhash(txs[i].outputs[k].getPubkhash());
                                      in.setValue(txs[i].outputs[k].getValue());
                                      in.setVout(k);
                                      in.setTxid(txs[i].getTxHash());
                                      memInComes.add(in);
                                }

                          }

                    }
              }
              //3、合并1和2, 将内存中已经花掉的utxo从dbUtxo删除掉，将内存中产生的收入加入到可花费收入中
              ArrayList<utxo> utxos = new ArrayList<utxo>();
              Boolean isSpend ;
              for(int i=0;i<dbutxos.length;i++){
                    isSpend = false;
                    for(int j=0;j<memSpends.size();j++){
                          //判断某一个utxo是否已经被消费了
                          if(dbutxos[i].getTxid() == memSpends.get(j).getTxid() && dbutxos[i].getVout() == memSpends.get(j).getVout()){
                                console.Println("utxo is spent");
                                isSpend = true;
                          }
                    }
                    if(!isSpend){
                         utxos.add(dbutxos[i]);
                     }
              }
              //把内存中的产生的收入放入到可花的utxo中
              console.Println(utxos.size());
              utxos.addAll(memInComes);
              return utxos;
        }
        public String del() throws Exception {
              this.IteratorBlock = null;
              //使用迭代器进行区块的遍历
              while(this.BlockNext()) { //遍历区块
                    console.Println("deleted block height:"+this.IteratorBlock.getHeight());
                    this.Chain.DelKeyFromRides( this.IteratorBlock.getHash());
              }
              this.LastBlock = null;
              this.IteratorBlock = null;
              this.Chain.LastHash = null;
              this.Chain.DelKeyFromRides(blockchain.LASTHASH);
              return "delete is successed.";
        }

      public powblock getIteratorBlock() {
            return IteratorBlock;
      }

      public void setIteratorBlock(powblock iteratorBlock) {
            IteratorBlock = iteratorBlock;
      }

      public powblock getLastBlock() {
            return LastBlock;
      }

      public void setLastBlock(powblock lastBlock) {
            LastBlock = lastBlock;
      }
}

