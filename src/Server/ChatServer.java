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
   public void broadcast() throws RemoteException {
      // Callback to all clients

   }

   @Override
   public void register() throws RemoteException {

   }

   @Override
   public void deRegister() throws RemoteException {

   }
}
