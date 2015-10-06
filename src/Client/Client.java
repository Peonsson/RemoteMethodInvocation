package Client;

import Server.ChatServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
                msg = scan.nextLine();
               if(msg.equals("/help")) {

                   chatServer.getHelp(c);

               } else if(msg.contains("/nick")) {

                   String[] parts = msg.split(" ");

                   if(parts.length != 2) {
                       System.out.println("/nick <username>");
                       continue;
                   }
                   chatServer.setNickname(c, parts[1]);

               } else if(msg.equals("/quit")) {

                   chatServer.deRegister(c);
                   System.out.println("Halting execution..");
                   System.exit(0);

               } else if(msg.equals("/who")) {

                   chatServer.getOnlineClients(c);

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

        Date currentTime = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
        String time = "["+ft.format(currentTime)+"]";
        String name = "[" + "Johan" + "]: ";
        System.out.println(time + name + msg);
    }
}