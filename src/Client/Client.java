package Client;

import Server.ChatServer;
import Server.ChatServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Assignment 2A
 * Created by Peonsson & roppe546 on 2015-09-26.
 */
public class Client implements Notifiable {
    public static void main(String[] args) {
       System.out.println("Client starting!");
       try {
          ChatServerInterface chatServer = (ChatServerInterface) Naming.lookup("chatserver");
       }
       catch (NotBoundException e) {
          e.printStackTrace();
       }
       catch (MalformedURLException e) {
          e.printStackTrace();
       }
       catch (RemoteException e) {
          e.printStackTrace();
       }
    }

    @Override
    public void sendMessage(String msg) throws RemoteException {

    }
}