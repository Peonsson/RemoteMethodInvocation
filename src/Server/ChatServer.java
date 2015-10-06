package Server;

import Client.Notifiable;
import com.sun.tools.corba.se.idl.constExpr.Not;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by robin on 2015-10-06.
 */
public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {

   private ArrayList<ConnectedClient> clients;
   private int numOfConns = 1;

   /**
    * Sole constructor.
    *
    * @throws RemoteException®
    */
   public ChatServer() throws RemoteException {
      super();
      clients  = new ArrayList<ConnectedClient>();
      new theReaper(clients).start();
   }

   @Override
   public synchronized void broadcast(Notifiable c, String msg) throws RemoteException {
      String sendingClientNickname = null;

      // Find nickname of sending client
      for (int i = 0; i < clients.size(); i++) {
         if (clients.get(i).getCallbackObject().equals(c)) {
            sendingClientNickname = clients.get(i).getNickname();
            break;
         }
      }

      for (int i = 0; i < clients.size(); i++) {
         try {
            if (clients.get(i).getCallbackObject().equals(c)) {
               // Skip the client itself
               continue;
            }

            clients.get(i).getCallbackObject().sendMessage("["+sendingClientNickname+"]" + ": " + msg);
         }
         // Remove client if not connectable
         catch(RemoteException e) {
            String disconnectedClientName = clients.get(i).getNickname();
            clients.remove(i);
            i--;
            for(ConnectedClient j: clients) {
               j.getCallbackObject().sendMessage("[Server]: " + disconnectedClientName + " has left the chat.");
            }
         }
      }
   }

   @Override
   public String getHelp() throws RemoteException {
      return "Commands:\n/help - this help text\n/nick - change your nickname\n/who - list all online clients\n/quit - quit the chat";
   }

   @Override
   public synchronized String setNickname(Notifiable c, String newNickname) throws RemoteException {
      // Check if nickname is already in use
      for (int i = 0; i < clients.size(); i++) {
         if (clients.get(i).getNickname().equals(newNickname)) {
            return "Nickname already in use. Please choose another.";
         }
      }

      // Get index of calling client from client list
      int index = -1;
      for (int i = 0; i < clients.size(); i++) {
         if (clients.get(i).getCallbackObject().equals(c)) {
            index = i;
            break;
         }
      }

      // Notify other clients of nickname change
      broadcast(c, "I'm now known as " + newNickname + ".");

      // Set new nickname to client
      clients.get(index).setNickname(newNickname);

      return "[Server]: Your new nickname is " + newNickname + ".";
   }

   @Override
   synchronized public String getOnlineClients() throws RemoteException {
      // Build string with online clients.
      StringBuilder sb = new StringBuilder();
      for (ConnectedClient client : clients) {
         sb.append(client.getNickname() + "\n");
      }

      return sb.toString();
   }

   @Override
   public synchronized void register(Notifiable c) throws RemoteException {
      try {
         clients.add(new ConnectedClient("anonymous " + numOfConns++, getClientHost(), c));
         c.sendMessage("[Server]: Welcome to Peonsson and roppe546's SimpleChatRMI!");
      }
      catch (ServerNotActiveException e) {
         System.out.println("Error registering new client.");
         e.printStackTrace();
      }
   }

   @Override
   public synchronized void deRegister(Notifiable c) throws RemoteException {
      int index = -1;
      String disconnectingClientName = null;

      for (int i = 0; i < clients.size(); i++) {
         if (clients.get(i).getCallbackObject().equals(c)) {
            disconnectingClientName = clients.get(i).getNickname();
            index = i;
            break;
         }
         System.out.println("Could not find calling client in deRegister.");
      }

      if (index == -1) {
         System.out.println("No such client.");
         return;
      }

      clients.remove(index);

      // Notify others that client left
      for (ConnectedClient client : clients) {
         client.getCallbackObject().sendMessage("[Server]: " + disconnectingClientName + " has left the chat.");
      }
   }
}
