/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aaaaaaaa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Neka
 */
public class GerencConect {
    
    private Socket socket;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private Callback call;
    private String user;
    
    public GerencConect(Socket socket, Callback call) throws Exception
    {
        this.socket = socket;
        
        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());
        this.call = call;
        this.listen();
        
    }
    public void escreverObjeto(Object obj) throws Exception
    {
        os.writeObject(obj);
        os.flush();
    }
    
    private void listen(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                while (true)
                {
                    try {
                        Object guarda = is.readObject();
                        call.recebeObjeto(guarda, socket);
                        
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        
        }).start();
    }
    
    public Socket getSocket(){
        return this.socket;
    }
    
    public void setName(String name){
        this.user = name;
    }
    
    public String getName(){
        return this.user;
    }
}
