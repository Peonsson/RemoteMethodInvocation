package Server;

import Client.Notifiable;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Johan Pettersson on 2015-10-06 22:14.
 * At Kungliga Tekniska Högskolan (KTH) in Sweden.
 * Contact: johanp7@kth.se
 */
public class theReaper extends Thread {

    ArrayList<ConnectedClient> clients;

    public theReaper(ArrayList<ConnectedClient> clients) {
        this.clients = clients;
    }

    @Override
    public void run() {
        while(true) {

            for(int i = 0; i< clients.size();i++) { //for all clients;
                try {

                    if(clients.get(i).getCallbackObject().checkAlive()) //check alive;
                        continue;

                } catch (RemoteException e) {

                    String disconnectedClientName = clients.get(i).getNickname(); //if someone is alive get their nickname;
                    clients.remove(i);//remove the dead user
                    i--;

                    for(ConnectedClient j: clients) {//for all clients
                        try {

                            j.getCallbackObject().
                                    sendMessage("[Server]: " + disconnectedClientName +
                                            " has left the chat."); //send a dead message

                        } catch (RemoteException e1) {
                        }
                    }
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
