/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aaaaaaaa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 *
 * @author Neka
 */
public class Cliente implements Callback {
    public Cliente() throws Exception
    {
        Socket socket = new Socket("127.0.0.1", 8000);
        GerencConect gc = new GerencConect(socket, this);
        Requisicao req = new Requisicao();
        req.comando = Comando.RECEBE_MSG_PUB;
        Object[] pacote = new Object[2];
        pacote[0] = "batata";
        pacote[1] = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        req.pacote = pacote;
        gc.escreverObjeto(req);
    }

    @Override
    public void recebeObjeto(Object obj, Socket socket) {
        Requisicao req = (Requisicao)obj;
        if(req.comando == Comando.RECEBE_MSG_PUB)
        {
            System.out.println("Objeto: "+req.pacote[0]);
        }else if(req.comando == Comando.MSG_PVT_SUC)
        {
            System.out.println("Usuário: "+req.pacote[0]+" Mensagem: "+req.pacote[1]);
        }
        
    }
}
