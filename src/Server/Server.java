package Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Assignment 2A
 * Created by Peonsson & roppe546 on 2015-09-26.
 */
public class Server {

   public static void main(String[] args) {
      try {
         ChatServer chatServer = new ChatServer();
         LocateRegistry.createRegistry(1099);
         Naming.rebind("chatserver", chatServer);
         System.out.println("Server running...");
      }
      catch (RemoteException e) {
         e.printStackTrace();
      } catch (MalformedURLException e) {
         e.printStackTrace();
      }
   }
}