package com.wfmyzyz.user.utils;

import com.wfmyzyz.user.config.ProjectConfig;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author admin
 */
@Component
public class RsaUtils {

    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";

    /**
     * 获取经过BASE64编码后RSA的公私钥
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Map<String,String> genKeyPair(){
        Map<String,String> map = new HashMap<>();
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGenerator.initialize(1024,new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String base64PrivateKey = new String(Base64.encodeBase64(privateKey.getEncoded()));
        String base64PublicKey = new String(Base64.encodeBase64(publicKey.getEncoded()));
        map.put(PUBLIC_KEY,base64PublicKey);
        map.put(PRIVATE_KEY,base64PrivateKey);
        return map;
    }

    /**
     * 利用公钥加密 （公钥经过base64编码）
     * @param str
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String str,String publicKey) throws Exception {
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * 根据私钥解密（私钥经过base64编码）
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String str,String privateKey) throws Exception {
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,rsaPrivateKey);
        String decryptStr = new String(cipher.doFinal(inputByte));
        return decryptStr;
    }

    public static void main(String[] args) throws Exception {
//        Map<String, String> stringStringMap = genKeyPair();
//        String str = "0296ad1f8b74ea72b6cf7f619b221600";
//        System.out.println(str);
//        String encrypt = encrypt(str, stringStringMap.get(PUBLIC_KEY));
//        System.out.println(encrypt);
//        String decrypt = decrypt("btlNKWX3zxV5/S8dAIZPi2PMCouwqjZXaleUXn3tx8PtR7CIfR+WYLy8fTChrDYFqILoSNtc58APfoFlcswO65W5J89VWOGlUttQB7qhTlFO+GU4mAWH4T9kQ6gKT+DRhpEyLOGZbZkjlAhit7ecEpbdLTvqeXGODWDIF8UsDQ0=", stringStringMap.get(PRIVATE_KEY));
    }
}
