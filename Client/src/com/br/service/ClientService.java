/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.bean.ChatMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.*;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mac-gibran
 */
public class ClientService {
    
    private Socket socket;
    private ObjectOutputStream output;
    private static final short PORT = 8080;
    private PublicKey publicRSAKey;
    private PrivateKey privateRSAKey;
    private String tripleDESKey;

    public ClientService() {
        this.tripleDESKey = Arrays.toString(getNextNumber());
        setKeys();
    }
    
    public Socket connect() {
        try {
            this.socket = new Socket("localhost", PORT);
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return socket;
    }
    
    public void send(ChatMessage message) {
        
        try {
            output.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public PublicKey getPublicRSAKey() {
        return publicRSAKey;
    }

    public PrivateKey getPrivateRSAKey() {
        return privateRSAKey;
    }

    public String getTripleDESKey() {
        return tripleDESKey;
    }

    public void setKeys() {

        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();
            this.publicRSAKey = keyPair.getPublic();
            this.privateRSAKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getNextNumber(){
        byte[] r = new byte[8]; //Means 64 bit
        Random random = new Random();
        random.nextBytes(r);
        return r;
    }
}
