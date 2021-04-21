package com.cn.crypto.keypair;

import com.cn.base58.base58;
import com.cn.config.hardware;
import com.cn.console.console;
import com.cn.crypto.hash;

public class address {
     private final static byte version = hardware.ADDRVersion;
     public static String getBitAddress(String pubk){
         //sha256 计算
         String pub256Hash = hash.sum(pubk, hash.HashType.SHA256);

         //ripemd160 hash计算 （github下载ripemd160库)
         String rpmd160 = hash.Ripemd160(pub256Hash);

         //第二步、添加版本号
         String versionPubRmpd160 = version + rpmd160;

         //第三步、双hash
         String hash1 =  hash.sum(versionPubRmpd160, hash.HashType.SHA256);

         String hash2 =  hash.sum(hash1, hash.HashType.SHA256);

         String check = hash2.substring(0,4);

         //第四步、拼接
         String addBytes = versionPubRmpd160+check;
         //第五步、对地址的拼接结果进行base58编码
         String addr = base58.encode(addBytes.getBytes());
         return addr;
     }

     public static boolean CheckBitAddress(String address){
         //第一步，base58解码
         String addBytes = new String(base58.decode(address));
         if(addBytes.length()<=4){
             return false;
         }
//         console.Println(addBytes);
         //第二步，截取后4位，得到校验位
         String check = addBytes.substring(addBytes.length()-4);
         //第三步，双hash计算
         //1、获取到base58解码后的数据除去后4个字节的数据
         String versionRipemd160 = addBytes.substring(0,addBytes.length()-4);
         //2、sha256
         String hash1 = hash.sum(versionRipemd160, hash.HashType.SHA256);
         String hash2 = hash.sum(hash1, hash.HashType.SHA256);
         //3、获取前4个字节
         String deCheck = hash2.substring(0,4);
         //第四步，将校验位与再次双hash以后得到的校验位进行比较
         return check.equals(deCheck);
     }
}
