package com.br.service;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class TripleDESUtil {

    public static byte[] encrypt(String message, String key) {
        Key privateKey = getPrivateKey(key.getBytes());
        Cipher cipher = null;
        byte[] encryptedMessage = null;
        try {
            cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            encryptedMessage = cipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException |
                 NoSuchPaddingException |
                 BadPaddingException |
                 IllegalBlockSizeException |
                 InvalidKeyException e) {
            e.printStackTrace();
        }

        return encryptedMessage;
    }

    public static byte[] dencrypt(byte[] cryptedMessage, String key) {
        Key privateKey = getPrivateKey(key.getBytes());
        Cipher cipher = null;
        byte[] dencryptedMessage = null;
        try {
            cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            dencryptedMessage = cipher.doFinal(cryptedMessage);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                BadPaddingException |
                IllegalBlockSizeException |
                InvalidKeyException e) {
            e.printStackTrace();
        }

        return dencryptedMessage;
    }

    public static Key getPrivateKey(byte[] key) {
        Key deskey = null;

        try {
            DESedeKeySpec sedeKeySpec = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            deskey = keyFactory.generateSecret(sedeKeySpec);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return deskey;
    }

}
