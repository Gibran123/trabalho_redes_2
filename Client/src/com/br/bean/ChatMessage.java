/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.bean;

import java.io.Serializable;
import java.security.Key;
import java.security.PublicKey;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mac-gibran
 */
public class ChatMessage implements Serializable{
    
    private String name;
    private String message;
    private String nameReserved;
    private Set<String> setOnlines = new HashSet<>();
    private Action action;
    private PublicKey publicServerKey;
    private PublicKey publicClientKey;
    private byte[] encryptedTripleDESkey;
    private byte[] encryptedMessage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNameReserved() {
        return nameReserved;
    }

    public void setNameReserved(String nameReserved) {
        this.nameReserved = nameReserved;
    }

    public Set<String> getSetOnlines() {
        return setOnlines;
    }

    public void setSetOnlines(Set<String> setOnlines) {
        this.setOnlines = setOnlines;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public PublicKey getPublicServerKey() {
        return publicServerKey;
    }

    public void setPublicServerKey(PublicKey publicServerKey) {
        this.publicServerKey = publicServerKey;
    }

    public PublicKey getPublicClientKey() {
        return publicClientKey;
    }

    public void setPublicClientKey(PublicKey publicClientKey) {
        this.publicClientKey = publicClientKey;
    }

    public byte[] getEncryptedTripleDESkey() {
        return encryptedTripleDESkey;
    }

    public void setEncryptedTripleDESkey(byte[] encryptedTripleDESkey) {
        this.encryptedTripleDESkey = encryptedTripleDESkey;
    }

    public byte[] getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(byte[] encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    public enum Action {
        CONNECT,
        DISCONNECT,
        SEND_ONE,
        SEND_ALL,
        USERS_ONLINE,
        USER_HAS_LEFT,
        SEND_PRIVATE,
        PUBLIC_SERVER_KEY,
        PUBLIC_CLIENT_KEY,
        TRIPLE_DSE_KEY
    }
    
    
}
