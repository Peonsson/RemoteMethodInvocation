package Server;

import Client.Notifiable;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by robin on 2015-10-06.
 */
public interface ChatServerInterface extends Remote {
   public void broadcast(String msg) throws RemoteException;
   public void register(Notifiable c) throws RemoteException;
   public void deRegister() throws RemoteException;
}
