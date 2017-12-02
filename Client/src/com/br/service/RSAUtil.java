package com.br.service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class RSAUtil {

    private static PublicKey publicKeyServer;

    public static PublicKey getPublicKeyServer() {
        return publicKeyServer;
    }

    public static void setPublicKeyServer(PublicKey publicKeyServer) {
        RSAUtil.publicKeyServer = publicKeyServer;
    }

    public static byte[] encryptData(String data) {
        byte[] dataToEncrypt = data.getBytes();
        byte[] encryptedData = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKeyServer);
            encryptedData = cipher.doFinal(dataToEncrypt);
        } catch (NoSuchAlgorithmException |
                 NoSuchPaddingException |
                 InvalidKeyException |
                 BadPaddingException |
                IllegalBlockSizeException e) {
            e.printStackTrace();
        }


        return encryptedData;
    }
}
