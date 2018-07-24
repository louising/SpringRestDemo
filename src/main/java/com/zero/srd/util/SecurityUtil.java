package com.zero.srd.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class SecurityUtil {
    private static Log log = LogFactory.getLog(SecurityUtil.class);

    private static final String SEED = "appsecretappsecr";
    private static String ALGORITHM_NAME = "DESede";
    private static String TRANSFORMATION = "DESede/ECB/PKCS5Padding";
    private static String CHARSET_NAME = "UTF-8";

    public static void main(String[] args) {
        String s = "China 中国";
        String s1 = SecurityUtil.encode(s);
        String s2 = SecurityUtil.decode(s1);
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(System.getProperty("java.home")); //System.getProperties()
        System.out.println();
        System.out.println(System.getProperties());
    }

    /**
    * Encode the string
    * @param content
    * @return
    */
    public static String encode(String content) {
        return doCipher(Cipher.ENCRYPT_MODE, content);
    }

    /**
    * Decode the string
    * @param content
    * @return
    * @throws GeneralSecurityException
    */
    public static String decode(String content) {
        return doCipher(Cipher.DECRYPT_MODE, content);
    }

    /**
    * Do cipher
    * Encode: first encode(), then base64Encode()
    * Decode: first base64Decode(), then decode()
    * @param opMode
    * @param content
    * @return
    * @throws GeneralSecurityException
    * @throws IOException
    */
    private static String doCipher(int opMode, String content) {
        String result = "";

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION); //"DES/ECB/NoPadding", "DES/CBC/PKCS5Padding"
            cipher.init(opMode, getKey());

            if (opMode == Cipher.ENCRYPT_MODE) {
                byte[] encodedBytes = cipher.doFinal(content.getBytes(CHARSET_NAME));
                result = new String(new BASE64Encoder().encode(encodedBytes));
            } else if (opMode == Cipher.DECRYPT_MODE) {
                byte[] decodedBytes = cipher.doFinal(new BASE64Decoder().decodeBuffer(content));
                result = new String(decodedBytes, CHARSET_NAME);
            }
        } catch (GeneralSecurityException e) {
            log.error(e.getMessage());
            result = content;
        } catch (IOException e) {
            log.error(e.getMessage());
            result = content;
        }

        return result;
    }

    /**
    * Get key
    * @return
    * @throws GeneralSecurityException
    * @throws IOException
    */
    private static Key getKey() throws GeneralSecurityException, IOException {
        /*
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_NAME); 
        byte[] DESkey = SEED.getBytes(CHARSET_NAME); 
        DESKeySpec keySpec = new DESKeySpec(DESkey); 
        Key key = keyFactory.generateSecret(keySpec);
        */

        //Using the following code, will can not be decoded in another JVM
        KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM_NAME);
        keygen.init(new SecureRandom(SEED.getBytes())); //DES-56, AES-128, DESede-112/168
        //keygen.init(168, new SecureRandom(SEED.getBytes())); //can be [168, 112], default is 168 
        SecretKey secretKey = keygen.generateKey();
        SecretKey key = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM_NAME);

        return key;
    }
}
