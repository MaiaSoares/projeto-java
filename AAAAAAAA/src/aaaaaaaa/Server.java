/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aaaaaaaa;

import static aaaaaaaa.Comando.MSG_PVT_SUC;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Neka
 */
public class Server extends Thread implements Callback {
    
    List<GerencConect> conexoes = new ArrayList<>();
    public void run()
    {
        try 
        {
            ServerSocket server = new ServerSocket(8000);
            while(true)
            {
                System.out.println("Server aguardando conexao");
                Socket socket = server.accept();
                System.out.println("Conexao recebida");
                GerencConect gc = new GerencConect(socket, this);
                conexoes.add(gc);
                
            }
        } catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void recebeObjeto(Object obj, Socket socket) {
        System.out.println("Objeto recebido");
        Requisicao req = (Requisicao)obj;
        if(req.comando == Comando.RECEBE_MSG_PUB)
        {
                String usuario = (String)req.pacote[0];
                String mensagem = (String)req.pacote[1];
                System.out.println("User: "+usuario+" mensagem: "+mensagem);
                Requisicao req2 = new Requisicao();
                req2.comando = Comando.SUC;
                 Object[] pacote = new Object[1];
                 pacote[0] = "Sucess";
                 req2.pacote = pacote;
                 try{
                     procuraConec(socket).escreverObjeto(req2);
                 }
                 catch(Exception e){
                     e.printStackTrace();
                 }
        }
        else if(req.comando == Comando.MSG_PVT_REQ) {            
                String usuario = (String)req.pacote[0];
                String mensagem = (String)req.pacote[1];
                String destinatario = (String)req.pacote[2];
                System.out.println("User: "+usuario+" mensagem: "+mensagem);
                Requisicao req2 = new Requisicao();
                req2.comando = MSG_PVT_SUC;
                Object[] obj2 = new Object[2];
                obj2[0] = usuario;
                obj2[1] = mensagem;
                req2.pacote = obj2;
                try{
                    procuraConec(destinatario).escreverObjeto(req2);
                } catch (Exception e){
                    e.printStackTrace();
                }
                
                
        }
        else if(req.comando == Comando.LOGIN){
            String usuario = (String)req.pacote[0];
            GerencConect gc = procuraConec(socket);
            gc.setName(usuario);
            System.out.println("Logado " + gc.getName());
        }
    }
    
    
    private GerencConect procuraConec(Socket socket){
        for(GerencConect con : this.conexoes)
            if(con.getSocket() == socket)
                return con;
        //VOLTAR AQUI
        return null;
    }
    
    private GerencConect procuraConec(String name){
        for(GerencConect con : this.conexoes)
            if(con.getName().equals(name))
                return con;
        //VOLTAR AQUI
        return null;
    }
    
    
}
