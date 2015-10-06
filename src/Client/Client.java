package Client;

import Server.ChatServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Assignment 2A
 * Created by Peonsson & roppe546 on 2015-09-26.
 */
public class Client extends UnicastRemoteObject implements Notifiable {

    private ChatServerInterface chatServerInterface;

    public Client(ChatServerInterface s) throws RemoteException {
        super();
        this.chatServerInterface = s;
    }

    public static void main(String[] args) {
       System.out.println("Client starting!");
        int i = 0;

       try {
           String url = "rmi://localhost/chatserver";
           ChatServerInterface chatServer = (ChatServerInterface) Naming.lookup(url);
           Client c = new Client(chatServer);
           chatServer.register(c);

           String msg;
           Scanner scan = new Scanner(System.in);
           while(true) {
               System.out.print("Send: ");
                msg = scan.nextLine();
               if(msg.equals("/help")) {

                   chatServer.command(c, 1);

               } else if(msg.equals("/nick")) {

                   chatServer.command(c, 2);

               } else if(msg.equals("/quit")) {

                   chatServer.deRegister(c);
                   System.out.println("Halting execution..");
                   System.exit(0);

               } else if(msg.equals("/who")) {

                   chatServer.command(c, 3);

               } else if(msg.charAt(0) == '/') {

                   System.out.println("Invalid command. Use /help to see available commands.");

               } else {
                   chatServer.broadcast(msg);
               }
           }

       } catch (RemoteException e) {
           e.printStackTrace();
       } catch (MalformedURLException e) {
           e.printStackTrace();
       } catch (NotBoundException e) {
           e.printStackTrace();
       }
    }

    @Override
    public void sendMessage(String msg) throws RemoteException {

        Calendar c = Calendar.getInstance();
        String time = "";
        System.out.println(time + "Received: " + msg);
    }
}