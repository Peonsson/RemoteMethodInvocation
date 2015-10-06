package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by robin on 2015-10-06.
 */
public interface ChatServerInterface extends Remote {
   public void broadcast() throws RemoteException;
   public void register() throws RemoteException;
   public void deRegister() throws RemoteException;
}
