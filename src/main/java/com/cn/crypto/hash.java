package com.cn.crypto;

import java.security.MessageDigest;

import com.cn.crypto.keypair.hexUtil;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;


public class hash {
//    private Cipher cip = Cipher.getInstance("");
    //以下常用hash算法.
     public enum HashType{
        MD2,
        MD5,
        SHA1,
        SHA256,
        SHA384,
        SHA512
    }
    public static String Ripemd160(String base){
          RIPEMD160Digest rip160 = new RIPEMD160Digest();
          rip160.update(base.getBytes(),0,base.length());
          byte[] ripemdbyte =  new byte[rip160.getDigestSize()];
          rip160.doFinal(ripemdbyte,0);
          return hexUtil.encodeHexString(ripemdbyte);
    }
    //使用指定的hash算法对字符串进行hash计算
    public static String sum(String base,HashType type) {
        String str = "";
        switch (type){
            case MD5: str = "MD5";break;
            case MD2: str = "MD2";break;
            case SHA1: str = "SHA-1";break;
            case SHA256: str = "SHA-256";break;
            case SHA384: str = "SHA-384";break;
            case SHA512: str = "SHA-512";break;
        }
        try{
            MessageDigest digest = MessageDigest.getInstance(str);

            byte[] hash = digest.digest(base.getBytes("UTF-8"));

            return hexUtil.encodeHexString(hash);

        } catch(Exception ex){
            throw new RuntimeException(ex);

        }
    }


}
