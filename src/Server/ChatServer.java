package Server;

import Client.Notifiable;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by robin on 2015-10-06.
 */
public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
   private ArrayList<ConnectedClient> clients = new ArrayList<>();
   private int numOfConns = 1;

   /**
    * Sole constructor.
    *
    * @throws RemoteException®
    */
   public ChatServer() throws RemoteException {
      super();
   }

   @Override
   synchronized public void broadcast(String msg) throws RemoteException {
      String sendingClientNickname = null;

      for (int i = 0; i < clients.size(); i++) {
         try {
            if (clients.get(i).getIpAddress().equals(getClientHost())) {
               sendingClientNickname = clients.get(i).getNickname();
               break;
            }
         }
         catch (ServerNotActiveException e) {
            System.out.println("Could not find calling client in broadcast.");
            e.printStackTrace();
         }
      }

      for (int i = 0; i < clients.size(); i++) {
         try{
            if (!clients.get(i).getNickname().equals(sendingClientNickname)) {
               clients.get(i).getCallbackObject().sendMessage("["+sendingClientNickname+"]" + ": " + msg);
            }
         }
         catch(RemoteException e) {
            clients.remove(i);
            i--;
         }
      }
   }

   @Override
   public String getHelp() throws RemoteException {
      Notifiable callingClient = null;

      for (int i = 0; i < clients.size(); i++) {
         try {
            if (clients.get(i).getIpAddress().equals(getClientHost())) {
               callingClient = clients.get(i).getCallbackObject();
               break;
            }
         }
         catch (ServerNotActiveException e) {
            System.err.println("Could not find calling client in getHelp.");
            e.printStackTrace();
         }
      }

      return "Commands:\n/help - this help text\n/nick - change your nickname\n/who - list all online clients\n/quit - quit the chat";
   }

   @Override
   synchronized public void setNickname(String newNickname) throws RemoteException {
      // Get index of calling client from client list
      int index = -1;
      for (int i = 0; i < clients.size(); i++) {
         try {
            if (clients.get(i).getIpAddress().equals(getClientHost())) {
               index = i;
               break;
            }
         }
         catch (ServerNotActiveException e) {
            System.err.println("Could not find calling client in setNickname.");
            e.printStackTrace();
         }
      }

      // Set new nickname to client
      clients.get(index).setNickname(newNickname);
      clients.get(index).getCallbackObject().sendMessage("[Server]: " + "Your new nickname is " + clients.get(index).getNickname() + ".");
   }

   @Override
   synchronized public void getOnlineClients() throws RemoteException {
      Notifiable callingClient = null;

      for (int i = 0; i < clients.size(); i++) {
         try {
            if (clients.get(i).getIpAddress().equals(getClientHost())) {
               callingClient = clients.get(i).getCallbackObject();
               break;
            }
         }
         catch (ServerNotActiveException e) {
            System.out.println("Could not find calling client in getOnlineClients.");
            e.printStackTrace();
         }
      }

      // Build string with online clients.
      StringBuilder sb = new StringBuilder();
      for (ConnectedClient client : clients) {
         sb.append(client.getNickname() + "\n");
      }

      callingClient.sendMessage(sb.toString());
   }

   @Override
   synchronized public void register(Notifiable c) throws RemoteException {
      try {
         clients.add(new ConnectedClient("anonymous " + numOfConns++, getClientHost(), c));
      }
      catch (ServerNotActiveException e) {
         e.printStackTrace();
      }
   }

   @Override
   synchronized public void deRegister() throws RemoteException {
      int index = -1;

      for (int i = 0; i < clients.size(); i++) {
         try {
            if (clients.get(i).getIpAddress().equals(getClientHost())) {
               index = i;
               break;
            }
         }
         catch (ServerNotActiveException e) {
            System.out.println("Could not find calling client in deRegister.");
            e.printStackTrace();
         }
      }

      if (index == -1) {
         System.out.println("No such client.");
         return;
      }

      clients.remove(index);
   }
}
