/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.frame;

import com.br.bean.ChatMessage;
import com.br.bean.ChatMessage.Action;
import com.br.service.ClientService;
import com.br.service.RSAUtil;
import com.br.service.TripleDESUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author mac-gibran
 */
public class ClientFrame extends javax.swing.JFrame {

    private Socket socket;
    private ChatMessage chat;
    private ClientService clientService;
    private static final String FAILED_CONNECTION_MESSAGE = "Falha ao conectar!!!";
    private boolean privateChatIsOn = false;

    /**
     * Creates new form ClientFrame
     */
    public ClientFrame() {
        initComponents();
    }

    private class SocketListener implements Runnable {

        private ObjectInputStream input;

        public SocketListener(Socket socket) {
            try {
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {

            ChatMessage chat;

            try {
                while ((chat = (ChatMessage) input.readObject()) != null) {

                    Action action = chat.getAction();
                    
                    System.err.println("--------------------->" + action.toString());

                    switch (action) {
                        case CONNECT:
                            connected(chat);
                            break;
                        case SEND_ONE:
                            receive(chat);
                            break;
                        case USER_HAS_LEFT:
                            receive(chat);
                            break;
                        case USERS_ONLINE:
                            atualizatUsuariosOnline(chat.getSetOnlines());
                            break;
                        case PUBLIC_SERVER_KEY:
                            persistPublicKeyServer(chat);
                    }

                }
            } catch (IOException ex) {
                System.out.println("Thread stopped");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    private void persistPublicKeyServer(ChatMessage chat) {
        RSAUtil.setPublicKeyServer(chat.getPublicServerKey());
        System.out.println("Recebendo a chave pública do servidor-------------------->" + RSAUtil.getPublicKeyServer());
        sendPublicRSAKeyToServer();
    }

    private void sendPublicRSAKeyToServer() {
        String nameText = this.fieldName.getText();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setAction(Action.PUBLIC_CLIENT_KEY);
        //chave 3des encripitada
        byte[] encryptedKey = RSAUtil.encryptData(this.clientService.getTripleDESKey());
        chatMessage.setEncryptedTripleDESkey(encryptedKey);
        chatMessage.setName(nameText);
        chatMessage.setPublicClientKey(this.clientService.getPublicRSAKey());
        this.clientService.send(chatMessage);
        System.out.println("Enviando a chave pública pro servidor-------------------->" + this.clientService.getPublicRSAKey());
    }

    private void changeComponentsState(boolean enable) {

        this.btnConnectar.setEnabled(!enable);
        this.fieldName.setEnabled(!enable);

        this.btnEnviar.setEnabled(enable);
        this.btnSair.setEnabled(enable);
        this.btnLimpar.setEnabled(enable);
        this.txtAreaSend.setEnabled(enable);

    }

    private void connected(ChatMessage message) {
        if (message.getMessage().equals(FAILED_CONNECTION_MESSAGE)) {
            this.fieldName.setName("");
            JOptionPane.showMessageDialog(null, "Conexão não realizada!\nTente novamente com outro nome.");
            return;
        }
        this.chat = message;

        changeComponentsState(true);

        JOptionPane.showMessageDialog(null, "Você está conectado!!!");
    }

    private void disconnect() {
        changeComponentsState(false);
        this.txtAreaReceive.setText("");
        this.txtAreaSend.setText("");
        JOptionPane.showMessageDialog(null, "Você foi desconectado!!!");
    }

    private void receive(ChatMessage chat) {
        
        if (chat.getAction().equals(Action.USER_HAS_LEFT)) {


            this.txtAreaReceive.append(chat.getName().concat(" saiu da sala.").concat("\n"));
            
            return;
        }
        final byte[] dencryptMessage = TripleDESUtil.dencrypt(chat.getEncryptedMessage(), this.clientService.getTripleDESKey());
        this.txtAreaReceive.append(chat.getName().concat(" enviou: ").concat(new String(dencryptMessage).concat("\n")));
    }

    private void atualizatUsuariosOnline(Set<String> names) {

         this.listOnlines.setListData(new String[1]);
        
        if (names.size() < 2)  return;
        
        names.removeIf((name) -> name.equals(chat.getName()));
        
        String[] vetorNames = names.toArray(new String[names.size()]);
       
        this.listOnlines.setListData(vetorNames);
        this.listOnlines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listOnlines.setLayoutOrientation(JList.VERTICAL);
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        fieldName = new javax.swing.JTextField();
        btnConnectar = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listOnlines = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaReceive = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaSend = new javax.swing.JTextArea();
        btnLimpar = new javax.swing.JButton();
        btnEnviar = new javax.swing.JButton();
        ToggleButtonPrivate = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Conectar"));

        fieldName.setName("fieldName"); // NOI18N
        fieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldNameActionPerformed(evt);
            }
        });

        btnConnectar.setText("Connectar");
        btnConnectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectarActionPerformed(evt);
            }
        });

        btnSair.setText("Sair");
        btnSair.setEnabled(false);
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(fieldName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConnectar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSair)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(fieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addComponent(btnConnectar)
                .addComponent(btnSair))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Onlines"));

        listOnlines.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listOnlines.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listOnlinesValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(listOnlines);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtAreaReceive.setEditable(false);
        txtAreaReceive.setColumns(20);
        txtAreaReceive.setRows(5);
        jScrollPane1.setViewportView(txtAreaReceive);

        txtAreaSend.setColumns(20);
        txtAreaSend.setRows(5);
        txtAreaSend.setEnabled(false);
        jScrollPane2.setViewportView(txtAreaSend);

        btnLimpar.setText("Limpar");
        btnLimpar.setEnabled(false);
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        btnEnviar.setText("Enviar");
        btnEnviar.setEnabled(false);
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        ToggleButtonPrivate.setText("Privado");
        ToggleButtonPrivate.setToolTipText("Ligar/Desligar conversa privada");
        ToggleButtonPrivate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToggleButtonPrivateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(ToggleButtonPrivate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpar)
                        .addGap(3, 3, 3)
                        .addComponent(btnEnviar)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimpar)
                    .addComponent(btnEnviar)
                    .addComponent(ToggleButtonPrivate))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fieldNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNameActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        this.txtAreaSend.setText("");
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnConnectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectarActionPerformed
        String name = this.fieldName.getText();

        if (name.isEmpty()) return;
        
        this.chat = new ChatMessage();
        this.chat.setName(name);
        this.chat.setAction(Action.CONNECT);

        this.clientService = new ClientService();
        this.socket = this.clientService.connect();

        new Thread(new SocketListener(this.socket)).start();

        this.clientService.send(chat);
    }//GEN-LAST:event_btnConnectarActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        closeSocketConnection();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        String message = this.txtAreaSend.getText();
        
        if(message.trim().isEmpty()) return;
        
        String name  = this.chat.getName();
        String nameReserved = this.chat.getNameReserved();
        
        this.chat = new ChatMessage();
        this.chat.setName(name);
        this.chat.setEncryptedMessage(TripleDESUtil.encrypt(message, this.clientService.getTripleDESKey()));
        this.chat.setNameReserved(nameReserved);
        this.chat.setAction(privateChatIsOn ? Action.SEND_PRIVATE : Action.SEND_ALL);
        
        this.txtAreaReceive.append("Você disse: " + message + "\n");
        
        this.clientService.send(chat);
        
        this.txtAreaSend.setText("");
        
    }//GEN-LAST:event_btnEnviarActionPerformed

    private ChatMessage getPackage(Action action) {
        ChatMessage chat = new ChatMessage();
        chat.setAction(action);
        chat.setMessage(this.chat.getMessage());
        chat.setName(this.chat.getName());
        
        return chat;
    }
    
    private void closeSocketConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                chat.setMessage(chat.getName() + " saiu da sala.");
                this.listOnlines.setListData(new String[0]);
                this.clientService.send(getPackage(Action.USER_HAS_LEFT));
                disconnect();
                this.socket.close();
                this.socket.getInputStream().close();
                this.socket.getOutputStream().close();
            }
        } catch (IOException ex) {
            System.out.println("Socket closed");
        }
    }
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closeSocketConnection();
    }//GEN-LAST:event_formWindowClosing

    private void listOnlinesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listOnlinesValueChanged
        evt.getFirstIndex();
    }//GEN-LAST:event_listOnlinesValueChanged

    private void ToggleButtonPrivateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToggleButtonPrivateActionPerformed
        String selectedValue = this.listOnlines.getSelectedValue();
        if (privateChatIsOn) {
            privateChatIsOn = false;
            chat.setNameReserved(null);
        }else {
            if (selectedValue == null) {
                JOptionPane.showMessageDialog(null, "Você precisa selecionar alguém para iniciar uma conversa privada");
                return;
            }
            chat.setNameReserved(selectedValue);
            privateChatIsOn = true;
        }
    }//GEN-LAST:event_ToggleButtonPrivateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton ToggleButtonPrivate;
    private javax.swing.JButton btnConnectar;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnSair;
    private javax.swing.JTextField fieldName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> listOnlines;
    private javax.swing.JTextArea txtAreaReceive;
    private javax.swing.JTextArea txtAreaSend;
    // End of variables declaration//GEN-END:variables
}
