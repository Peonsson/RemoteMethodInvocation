package Server;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Client.Notifiable;

/**
 * Created by Peonsson and roppe546 on 2015-10-06.
 */
public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {

   //private ArrayList<ConnectedClient> clients;
   private List<ConnectedClient> clients;
   private int numOfConns = 1;

   /**
    * Sole constructor.
    *
    * @throws RemoteException®
    */
   public ChatServer() throws RemoteException {
      super();
      clients  = Collections.synchronizedList(new ArrayList());
      new theReaper(clients).start();
   }

   /**
    * Send a message to all registered clients.
    * If some clients have disconnected during broadcast; remove them.
    * @param c
    * @param msg
    * @throws RemoteException
    */
   @Override
   public synchronized void broadcast(Notifiable c, String msg) throws RemoteException {
      synchronized (clients) {
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

               clients.get(i).getCallbackObject().sendMessage("[" + sendingClientNickname + "]" + ": " + msg);
            }
            // Remove client if not connectable
            catch (RemoteException e) {
               String disconnectedClientName = clients.get(i).getNickname();
               clients.remove(i);
               i--;
               for (ConnectedClient j : clients) {
                  j.getCallbackObject().sendMessage("[Server]: " + disconnectedClientName + " has left the chat.");
               }
            }
         }
      }
   }

   @Override
   public String getHelp() throws RemoteException {
      return "Commands:\n/help - this help text\n/nick - change your nickname\n/who - list all online clients\n/quit - quit the chat";
   }

   /**
    * Sets a new nickname for given user.
    * @param c
    * @param newNickname
    * @return
    * @throws RemoteException
    */
   @Override
   public synchronized String setNickname(Notifiable c, String newNickname) throws RemoteException {
      synchronized (clients) {
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
   }

   /**
    * Build string with online clients, a new line seperating each client.
    * @return a String with all the online clients on seperate lines.
    * @throws RemoteException
    */
   @Override
   synchronized public String getOnlineClients() throws RemoteException {
      synchronized (clients) {
         StringBuilder sb = new StringBuilder();
         boolean first = true;

         for (ConnectedClient client : clients) {
            if (first) {
               first = false;
               sb.append(client.getNickname());
            } else {
               sb.append("\n" + client.getNickname());
            }
         }
         return sb.toString();
      }
   }

   /**
    * Register new clients to the chat server.
    * @param c
    * @throws RemoteException
    */
   @Override
   public synchronized void register(Notifiable c) throws RemoteException {
      synchronized (clients) {
         try {
            clients.add(new ConnectedClient("anonymous " + numOfConns++, getClientHost(), c));
            c.sendMessage("[Server]: Welcome to Peonsson and roppe546's SimpleChatRMI!");
         } catch (ServerNotActiveException e) {
            System.out.println("Error registering new client.");
            e.printStackTrace();
         }
      }
   }

   /**
    * Remove clients from the server.
    * @param c
    * @throws RemoteException
    */
   @Override
   public synchronized void deRegister(Notifiable c) throws RemoteException {
      synchronized (clients) {
         int index = -1;

         for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getCallbackObject().equals(c)) {
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
      }
   }
}
