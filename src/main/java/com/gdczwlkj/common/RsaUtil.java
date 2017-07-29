package com.gdczwlkj.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by franco.cheng on 2017/4/12.
 */
public class RsaUtil {
    private static Cipher cipher;

    static{
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成密钥对
     * @param filePath 生成密钥的路径
     * @return
     */
    public static Map<String,String> generateKeyPair(String filePath){
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 密钥位数
            keyPairGen.initialize(2048);
            // 密钥对
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 公钥
            PublicKey publicKey = keyPair.getPublic();
            // 私钥
            PrivateKey privateKey = keyPair.getPrivate();
            //得到公钥字符串
            String publicKeyString = getKeyString(publicKey);
            //得到私钥字符串
            String privateKeyString = getKeyString(privateKey);
            //将密钥对写入到文件
            FileWriter pubfw = new FileWriter(filePath + "/publicKey.keystore");
            FileWriter prifw = new FileWriter(filePath + "/privateKey.keystore");
            BufferedWriter pubbw = new BufferedWriter(pubfw);
            BufferedWriter pribw = new BufferedWriter(prifw);
            pubbw.write(publicKeyString);
            pribw.write(privateKeyString);
            pubbw.flush();
            pubbw.close();
            pubfw.close();
            pribw.flush();
            pribw.close();
            prifw.close();
            //将生成的密钥对返回
            Map<String,String> map = new HashMap<String,String>();
            map.put("publicKey", publicKeyString);
            map.put("privateKey", privateKeyString);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到公钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 得到私钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 得到密钥字符串（经过base64编码）
     *
     * @return
     */
    public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        String s = (new BASE64Encoder()).encode(keyBytes);
        return s;
    }

    /**
     * 使用公钥对明文进行加密，返回BASE64编码的字符串
     * @param publicKey
     * @param plainText
     * @return
     */
    public static String encrypt(PublicKey publicKey, String plainText){
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return (new BASE64Encoder()).encode(enBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用keystore对明文进行加密
     * @param publicKeystore 公钥文件路径
     * @param plainText      明文
     * @return
     */
    public static String fileEncrypt(String publicKeystore, String plainText){
        try {
            FileReader fr = new FileReader(publicKeystore);
            BufferedReader br = new BufferedReader(fr);
            String publicKeyString="";
            String str;
            while((str=br.readLine())!=null){
                publicKeyString+=str;
            }
            br.close();
            fr.close();
            cipher.init(Cipher.ENCRYPT_MODE,getPublicKey(publicKeyString));
            byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return (new BASE64Encoder()).encode(enBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用公钥对明文进行加密
     * @param publicKey      公钥
     * @param plainText      明文
     * @return
     */
    public static String encrypt(String publicKey, String plainText){
        try {
            cipher.init(Cipher.ENCRYPT_MODE,getPublicKey(publicKey));
            byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return (new BASE64Encoder()).encode(enBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用私钥对明文密文进行解密
     * @param privateKey
     * @param enStr
     * @return
     */
    public static String decrypt(PrivateKey privateKey, String enStr){
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
            return new String(deBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用私钥对密文进行解密
     * @param privateKey       私钥
     * @param enStr            密文
     * @return
     */
    public static String decrypt(String privateKey, String enStr){
        try {
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
            return new String(deBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用keystore对密文进行解密
     * @param privateKeystore  私钥路径
     * @param enStr            密文
     * @return
     */
    public static String fileDecrypt(String privateKeystore, String enStr){
        try {
            FileReader fr = new FileReader(privateKeystore);
            BufferedReader br = new BufferedReader(fr);
            String privateKeyString="";
            String str;
            while((str=br.readLine())!=null){
                privateKeyString+=str;
            }
            br.close();
            fr.close();
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyString));
            byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
            return new String(deBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        //generateKeyPair("D:/RSA");

       String publicKey;
        String privateKey;

        publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqf1xyVpPwz5TzMH0DTc6w9u4x0NhmRW2dvclfOlvsx/dsqoH9+RYz0rsmKcQQBaAR0AOslKl47uaP+Anyaci33cWzwKU9DdyeL93/0MDErue38hTkhmn9rW59s5UYGhfBsN+/dgi8VaLCBuaYueQUdGnPnh9CuQ5dPn/DY30BdDPZ8hkaWQT1gg2ZC580+wSbfuNwnFxuu2IpuyPrvzzkhyIjhpcVvtwWC0+Ktz4fJoJg+ScwWyrBA+RaHdahyXnJPRnm05XtPRtgKJqAm7fz6qo84DPT4zi1EhJAmA6Nd80CXOyqokVTRcdAfeppwk7/nv/gpdyFnRA9W6UHDoQbQIDAQAB";
        privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCp/XHJWk/DPlPMwfQNNzrD27jHQ2GZFbZ29yV86W+zH92yqgf35FjPSuyYpxBAFoBHQA6yUqXju5o/4CfJpyLfdxbPApT0N3J4v3f/QwMSu57fyFOSGaf2tbn2zlRgaF8Gw3792CLxVosIG5pi55BR0ac+eH0K5Dl0+f8NjfQF0M9nyGRpZBPWCDZkLnzT7BJt+43CcXG67Yim7I+u/POSHIiOGlxW+3BYLT4q3Ph8mgmD5JzBbKsED5Fod1qHJeck9GebTle09G2AomoCbt/PqqjzgM9PjOLUSEkCYDo13zQJc7KqiRVNFx0B96mnCTv+e/+Cl3IWdED1bpQcOhBtAgMBAAECggEANWQNIRjkhIZG+8DPTndSVcHlP3DCbrqLHMJzW9BV7QTNNRiCeDGYU0NNHIUcbYSjtb1A4HFk+SorQHS/Cm8cXOOinlJJRotDwPkiT2JUzMVxGQhqdzDnuG1Lg2E6zyz4677rW/9Ouk5cTKeezgG9KnWW+eAOJiWYhk0e9rrnMgvnztanY4vncRVleZimSbFjnC+wXANa3qHCg1atZmZMs8t+K6BLJJZi/36zD4Jq8Qu9I42S3LJ4cBR3mcUjz6AgTGf8+xi0XkN1GqQbOuCfWOGW0nGqdUGoerFDXk1aukd3lCTgLrPO4Olo6GVetcmDZb+36O/Sm0bLKrlRF3dbYQKBgQDoGIubT/nuPMtFe3Jkz72QlpMGjFXQXGePXpGWqgxZkSRuFImYiSopoH6Vdf5kcnNKGcchedoW3avXy+QKixbo/VKcUuDLPDJiKcYBClHuxGU0dn6iPdWcb/iV65CPITIrMGyYqc9BP91059xNRnhfPcqE/7+X3aJdv3ShR7PmIwKBgQC7f2l7EWoCnShdEou9pV3/7nM3kcu//SjuksM76ocZO0VJcoU9IHqFn+HEfOKgiG02a0Z+PHZbmu+317dCl9GBh/B6KAV73nGcU6l+xWwcmqGwu2K5eD5qwLhkZOfVK+4uNn9WQI1I5jg9uDm3xgw4mHVpsDdSxWN3vNXiTebwLwKBgQDBhb3aSaw2xkAjdlQi1MjWJ4b+HcNr0bCT7aFtQ+q6P/hlRQCZzDz4qpcBnnn4XSneLnJIama7TCf97kb9t43pxVe9eqlvLIlCKlyr/2Pjt6Q/Q1JBi1dycjaPIvTNMzu2oVDbVjO2zz6jPLUHRewWVdpoXSruwOGiU4I2/Cd3iwKBgBRjGD8Pd61tFTR5jcRDNONdNB34tmTbzBWU07GyIJp3vWLtFtu8qlXZOp04zxORgf6Gz5VuUZhIUoR06jJNe7RP6a/+pgn/9+59klJ8ePLrRWWIGoX8Mx08c1BHQIYHTtCrkFD6l+IzVi4v1+bJnA6TVUD2ri1L1TLxz3fZ7KB1AoGAWLbgEnivpOVoXBZIsURkysaVxHvMNDSF2rKTvEWVcoGA9mSpHITjLIOjp7og3dIShFAGWrs9hWaedzGadj87MVOPGP7tDwfdA/OSe7+ZYq6QiaaQ9ImRBkeHziOBvSxgTmpB9JAi0UKMCvmp7W8XEXQb9zMQEdWAarzJlzAbTKQ=";


        System.err.println("公钥加密——私钥解密");
        String source = "{" +
                "\"flow_id\": \"8C24E34A-4708-4861-B4C4-3E9D720B8F9C\",\n" +
                "    \"new_pay_password\": \"E10ADC3949BA59ABBE56E057F20F883E\"" +
                "}";
        System.out.println("\r加密前文字：\r\n" + source);
        String aData = RsaUtil.encrypt(publicKey, source);
        System.out.println("加密后文字：\r\n" + aData);
        String dData = RsaUtil.decrypt(privateKey, aData);
        System.out.println("解密后文字: \r\n" + dData);

    }
}
