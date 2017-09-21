/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.bean.ChatMessage;
import com.br.bean.ChatMessage.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mac-gibran
 */
public class ServidorService {

    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, ObjectOutputStream> mapOnlines = new HashMap<>();
    private static final short PORT = 8080;
    private static final String SUCCESS_CONNECTION_MESSAGE = "Você foi conectado!!!";
    private static final String FAILED_CONNECTION_MESSAGE = "Falha ao conectar!!!";

    public ServidorService() {

        try {

            serverSocket = new ServerSocket(PORT);

            while (true) {

                socket = serverSocket.accept();
                new Thread(new SocketListener(socket)).start();

            }

        } catch (IOException e) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private class SocketListener implements Runnable {

        private ObjectOutputStream output;
        private ObjectInputStream input;
        private Socket socket;
        
        public SocketListener(Socket socket) {
            try {
                this.output = new ObjectOutputStream(socket.getOutputStream());
                this.input = new ObjectInputStream(socket.getInputStream());
                this.socket = socket;
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        @Override
        public void run() {

            ChatMessage chat = null;

            try {

                while ((chat = (ChatMessage) input.readObject()) != null) {

                    Action action = chat.getAction();
                    
                    System.err.println("--------------------->" + action.toString());

                    switch (action) {
                        case CONNECT:
                            boolean isConnected = connect(chat, output);
                            if (isConnected) {
                                mapOnlines.put(chat.getName(), output);
                                sendOnlineUsers(chat); 
                            }
                            break;
                        case DISCONNECT:
                            desconect(chat, output);
                            return;
                        case SEND_ONE:
                            sendOne(chat, output);
                            break;
                        case SEND_ALL:
                            sendAll(chat, Action.SEND_ONE);
                            break;
                        case USERS_ONLINE:
                            break;
                        case USER_HAS_LEFT:
                            desconect(chat, output);
                            sendAll(chat, Action.USER_HAS_LEFT);
                            break;
                        default:
                    }

                }

            } catch (IOException | ClassNotFoundException ex) {
                desconect(chat, output);
            }

        }

    }

    private synchronized boolean connect(ChatMessage message, ObjectOutputStream output) {
        if (this.mapOnlines.isEmpty()) {
            message.setMessage(SUCCESS_CONNECTION_MESSAGE);
            sendOne(message, output);
            return true;
        }

        boolean isTheNameRepeated = this.mapOnlines.containsKey(message.getName());

        if (!isTheNameRepeated) {
            message.setMessage(SUCCESS_CONNECTION_MESSAGE);
            sendOne(message, output);
            return true;
        }

        message.setMessage(FAILED_CONNECTION_MESSAGE);
        sendOne(message, output);

        return false;

    }

    private synchronized void desconect(ChatMessage message, ObjectOutputStream outputStream) {
        this.mapOnlines.remove(message.getName());
        System.err.println("DISCONNECT------------------------> " + !this.mapOnlines.containsKey(message.getName()));
        System.out.println(message.getName() + " saiu da sala.");
        
    }

    private synchronized void sendAll(ChatMessage chat, Action action) {
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (!kv.getKey().equals(chat.getName())) {
                chat.setAction(action);
                try {
                    kv.getValue().writeObject(chat);
                } catch (IOException ex) {
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sendOnlineUsers(ChatMessage chat) {
        Set<String> setNames = new HashSet<>();
        
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            setNames.add(kv.getKey());
        }

        ChatMessage message = new ChatMessage();
        message.setAction(Action.USERS_ONLINE);
        message.setSetOnlines(setNames);

        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            message.setName(kv.getKey());
            try {
                if (!kv.getKey().equals(chat.getName())) message.setName(kv.getKey());
                kv.getValue().writeObject(message);
                kv.getValue().flush();
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private synchronized void sendOne(ChatMessage message, ObjectOutputStream output) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
