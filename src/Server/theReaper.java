package Server;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Johan Pettersson on 2015-10-06 22:14.
 * At Kungliga Tekniska Högskolan (KTH) in Sweden.
 * Contact: johanp7@kth.se
 */
public class theReaper extends Thread {

    private List <ConnectedClient> clients;
    private final int SLEEP_TIMER = 1000;

    public theReaper(List<ConnectedClient> clients) {
        this.clients = clients;
    }

    /**
     * Sends periodic isAlive? messages to each client.
     * If they fail to respond; remove them.
     */
    @Override
    public void run() {
        while(true) {

            synchronized (clients) {
                for (int i = 0; i < clients.size(); i++) { //for all clients;
                    try {

                        if (clients.get(i).getCallbackObject().isAlive()) //check alive;
                            continue;

                    } catch (RemoteException e) {

                        String disconnectedClientName = clients.get(i).getNickname(); //if someone is alive get their nickname;
                        clients.remove(i);//remove the dead user
                        i--;
                        for (ConnectedClient j : clients) {//for all clients
                            try {

                                j.getCallbackObject().
                                        sendMessage("[Server]: " + disconnectedClientName +
                                                " has left the chat."); //send a dead message

                            } catch (RemoteException e1) {
                            }
                        }
                    }
                }
            }

            try {
                Thread.sleep(SLEEP_TIMER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
