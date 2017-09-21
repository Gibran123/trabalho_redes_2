/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br;

import com.br.service.ServidorService;

/**
 *
 * @author mac-gibran
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Servidor on!");
        ServidorService servidorService = new ServidorService();
    }
    
}
