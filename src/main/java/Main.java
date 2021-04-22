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
    public static void main(String[] args) throws Exception {
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
//      Client.queryAllkeypair();
//       Client.ParseArgs("newaddress",null);
//       Client.del();
//       console.Println(Client.Chain.LastHash);
//         String str =  Client.ParseArgs("getallblocks",null);
//      String str =  Client.ParseArgs("generategensis","{\"address\":\"2CAHSf4iSVEPrTbmmGacSvW1qPYjbg3kvAjtyVdZdiATdMQXkm8M5saaypM7oX\"}");
//         String str =  Client.ParseArgs("sendtransaction","{\"from\":\"2CAHSf4iSVEPrTbmmGacSvW1qPYjbg3kvAjtyVdZdiATdMQXkm8M5saaypM7oX\",\"to\":\"2C9xop5KpWJ7RLoQd6wGjUMPSiXeZtQmSkxgbX14bQ1cE1k9HQM2x5VaCSRamg\",\"value\":\"0.1\"}");
//       String str =  Client.ParseArgs("sendtransaction","{\"from\":\"2C9xop5KpWJ7RLoQd6wGjUMPSiXeZtQmSkxgbX14bQ1cE1k9HQM2x5VaCSRamg\",\"to\":\"2CAHSf4iSVEPrTbmmGacSvW1qPYjbg3kvAjtyVdZdiATdMQXkm8M5saaypM7oX\",\"value\":\"9\"}");
//       String str =  Client.ParseArgs("getallblocks",null);
//        console.Println(str);
         String str2 =  Client.ParseArgs("getbalance","2CAHSf4iSVEPrTbmmGacSvW1qPYjbg3kvAjtyVdZdiATdMQXkm8M5saaypM7oX");
         console.Println(str2);
    }
}


