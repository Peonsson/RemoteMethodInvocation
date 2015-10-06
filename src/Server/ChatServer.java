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
      for (ConnectedClient client : clients) {
         client.getCallbackObject().sendMessage(msg);
      }
   }

   @Override
   public void getHelp(Notifiable client) throws RemoteException {
      client.sendMessage("Commands:\n/help - this help text\n/nick - change your nickname\n/who - list all online clients\n/quit - quit the chat");
   }

   @Override
   synchronized public void setNickname(Notifiable client, String newNickname) throws RemoteException {

   }

   @Override
   synchronized public void getOnlineClients(Notifiable c) throws RemoteException {
      StringBuilder sb = new StringBuilder();

      for (ConnectedClient client : clients) {
         sb.append(client.getNickname() + "\n");
      }

      c.sendMessage(sb.toString());
   }

   @Override
   synchronized public void register(Notifiable c) throws RemoteException {
      clients.add(new ConnectedClient("anonymous", c));
   }

   @Override
   synchronized public void deRegister(Notifiable c) throws RemoteException {
      int index = clients.indexOf(c);
      clients.remove(index);
   }
}
