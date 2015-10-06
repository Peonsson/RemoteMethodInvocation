package Server;

import Client.Notifiable;

import java.rmi.RemoteException;
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
    * @throws RemoteException
    */
   public ChatServer() throws RemoteException {
      super();
   }

   @Override
   public synchronized void broadcast(String msg) throws RemoteException {
      for (int i = 0; i < clients.size(); i++) {
         clients.get(i).sendMessage(msg);
      }
   }

   @Override
   public void register(Notifiable c) throws RemoteException {
      System.out.println("Hello darkness my old friend");
      clients.add(c);
      System.out.println("Hello darkness my old friend");
   }

   @Override
   public void deRegister() throws RemoteException {

   }
}
