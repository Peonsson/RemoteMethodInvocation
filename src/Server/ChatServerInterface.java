package Server;

import Client.Notifiable;
import com.sun.tools.corba.se.idl.constExpr.Not;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by robin on 2015-10-06.
 */
public interface ChatServerInterface extends Remote {
   public void broadcast(Notifiable c, String msg) throws RemoteException;
   public String getHelp() throws RemoteException;
   public String setNickname(Notifiable c, String newNickname) throws RemoteException;
   public String getOnlineClients() throws RemoteException;
   public void register(Notifiable c) throws RemoteException;
   public void deRegister(Notifiable c) throws RemoteException;
}
