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
   private ArrayList<Notifiable> clients = new ArrayList<>();

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
      for (Notifiable client : clients) {
         client.sendMessage(msg);
      }
   }

   @Override
   synchronized public void command(Notifiable client, int command) throws RemoteException {
      // help
      if (command == 1) {

      }
      // nick
      else if (command == 2) {

      }
      // who
      else if (command == 3) {

      }
   }

   @Override
   synchronized public void register(Notifiable c) throws RemoteException {
      clients.add(c);
   }

   @Override
   synchronized public void deRegister(Notifiable c) throws RemoteException {
      int index = clients.indexOf(c);
      clients.remove(index);
   }
}
