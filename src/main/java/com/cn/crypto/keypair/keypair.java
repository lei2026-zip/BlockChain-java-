package com.cn.crypto.keypair;

import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class keypair {
    private String PubK;
    private String PriK;

    private static final String SIGNALGORITHMS = "SHA256withECDSA";
    private static final String ALGORITHM = "EC";
    private static final String SECP256K1 = "secp256k1";

    /**
     * 生成密钥对
     * @return
     * @throws Exception
     */
    public static keypair getKeyPair() throws Exception {
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(SECP256K1);
            java.security.KeyPairGenerator kf =  KeyPairGenerator.getInstance(ALGORITHM);
            kf.initialize(ecSpec, new SecureRandom());
            KeyPair keyPai = kf.generateKeyPair();
            keypair key = new keypair();
            key.PubK =  hexUtil.encodeHexString(keyPai.getPublic().getEncoded());
            key.PriK =  hexUtil.encodeHexString(keyPai.getPrivate().getEncoded());
            return key;
    }


    //私钥签名
    public String signature(byte[] date) throws Exception {
        Signature sig = Signature.getInstance("ECDH", "BC");
        sig.initSign(getPrivateKey(this.PriK), new SecureRandom());
        sig.sign(date,0,date.length);
        return "";
    }

    /**
     * 加签
     * @param privateKey 私钥
     * @param data 数据
     * @return
     */
    public static String signECDSA(PrivateKey privateKey, String data) {
        String result = "";
        try {
            //执行签名
            Signature signature = Signature.getInstance(SIGNALGORITHMS);
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            byte[] sign = signature.sign();
            return hexUtil.encodeHexString(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 验签
     * @param publicKey 公钥
     * @param signed 签名
     * @param data 数据
     * @return
     */
    public static boolean verifyECDSA(PublicKey publicKey, String signed, String data) {
        try {
            //验证签名
            Signature signature = Signature.getInstance(SIGNALGORITHMS);
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            byte[] hex = hexUtil.decode(signed);
            boolean bool = signature.verify(hex);
            // System.out.println("验证：" + bool);
            return bool;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从string转private key
     * @param key 私钥的字符串
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {

        byte[] bytes = DatatypeConverter.parseHexBinary(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);

    }

    /**
     * 从string转publicKey
     * @param key 公钥的字符串
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {

        byte[] bytes = DatatypeConverter.parseHexBinary(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);

    }




    public String getPubK() {
        return PubK;
    }

    public void setPubK(String pubK) {
        PubK = pubK;
    }

    public String getPriK() {
        return PriK;
    }

    public void setPriK(String priK) {
        PriK = priK;
    }

    // PriK *ecdsa.PrivateKey
}
