import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.math.BigInteger;
import java.security.*;
import java.util.ArrayList;

import com.cn.uitls.serialize;

import com.cn.client.client;
import com.cn.client.transtion.txInput.txinput;
import com.cn.console.console;
import com.cn.crypto.hash;
import com.cn.crypto.keypair.hexUtil;
import com.cn.crypto.keypair.keypair;
import com.cn.crypto.keypair.address;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.cn.config.hardware;

import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import com.cn.client.transtion.transtion;
import com.cn.client.transtion.utxo;

import static com.cn.crypto.keypair.keypair.*;

//2CAHSf4iSVEPrTbmmGacSvW1qPYjbg3kvAjtyVdZdiATdMQXkm8M5saaypM7oX
//2C9xop5KpWJ7RLoQd6wGjUMPSiXeZtQmSkxgbX14bQ1cE1k9HQM2x5VaCSRamg
public class Main {
    private static Boolean runflag = true;
    public static void main(String[] args){
        client  Client = new client();
        try {
            String err =  Client.init();
            if(err!=null){
                console.Println(err);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//       Client.Chain.getJedis().del(Client.USERKEY);
//        generategensis
//       Client.queryAllkeypair();
//        Client.ParseArgs("newaddress",null);
//        String str =  Client.ParseArgs("sendtransaction","{\"from\":\"2CBbKQkSiAmeBJ7SW3Md7WMxpLZa3Zb5LrvZKhZnsfueF2ZevABJC8pZhEKTCU\",\"to\":\"2CCdDf6aHr5abfkPKSqbP95hWBSryKPEzMFS8Niq52ySRfmzvDcsU2hNTt6E5n\",\"value\":\"9.9\"}");
//        String str =  Client.ParseArgs("getallblocks",null);
//        String str =  Client.ParseArgs("getbalance","2CBbKQkSiAmeBJ7SW3Md7WMxpLZa3Zb5LrvZKhZnsfueF2ZevABJC8pZhEKTCU");
//        console.Println(str);
    }
}


