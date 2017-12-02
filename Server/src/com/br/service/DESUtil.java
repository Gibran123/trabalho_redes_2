package com.br.service;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static javax.crypto.Cipher.getInstance;

public class DESUtil {

    public static Map<String, String> clientsPublicKeys = new HashMap<>();

    public static byte[] encrypt(String message, String key) {
        Key privateKey = getPrivateKey(key.getBytes());
        Cipher cipher = null;
        byte[] encryptedMessage = null;
        try {
            cipher = getInstance("desede" + "/ECB/PKCS5Padding");
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
        Key privateKey;
        privateKey = getPrivateKey(key.getBytes());
        Cipher cipher = null;
        byte[] dencryptedMessage = null;
        try {
            cipher = getInstance("desede" + "/ECB/PKCS5Padding");
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
            AtomicReference<DESedeKeySpec> sedeKeySpec = new AtomicReference<>(new DESedeKeySpec(key));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            deskey = keyFactory.generateSecret(sedeKeySpec.get());
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return deskey;
    }

}
