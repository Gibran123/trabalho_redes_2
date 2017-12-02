package com.br.service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    public static void setPublicKey(PublicKey publicKey) {
        RSAUtil.publicKey = publicKey;
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }

    public static void setPrivateKey(PrivateKey privateKey) {
        RSAUtil.privateKey = privateKey;
    }

    public static PrivateKey getPrivateKey() {
        return privateKey;
    }

    public static Map<String, PublicKey> clientsRSAPublicKeys = new HashMap<>();

    public static void generateKeys() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keys = generator.generateKeyPair();
            publicKey = keys.getPublic();
            privateKey = keys.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String decryptData(byte[] data) {
        byte[] decryptedData = null;
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedData = cipher.doFinal(data);
        } catch (NoSuchAlgorithmException |
                 NoSuchPaddingException |
                 IllegalBlockSizeException |
                 BadPaddingException |
                 InvalidKeyException e) {
            e.printStackTrace();
        }
        if (decryptedData == null) throw new AssertionError();
        return new String(decryptedData);
    }
}
