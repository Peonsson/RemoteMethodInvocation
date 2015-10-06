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

    private ChatServerInterface chatServerInterface;

    public Client(ChatServerInterface s) {
        super();
        this.chatServerInterface = s;
    }

    public static void main(String[] args) {
       System.out.println("Client starting!");
        int i = 0;

       try {
           System.out.println("test" + i++); //0

           String url = "rmi://localhost/chatserver";

           System.out.println("test" + i++); //1

           ChatServerInterface chatServer = (ChatServerInterface) Naming.lookup(url);

           System.out.println("test" + i++); //2

           Client c = new Client(chatServer);

           System.out.println("test" + i++); //3

           chatServer.register(c);

           System.out.println("test" + i++); //4

           chatServer.broadcast();

           System.out.println("test" + i++);

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
        System.out.println("received: " + msg);
    }
}