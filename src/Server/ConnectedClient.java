package Server;

import Client.Notifiable;

/**
 * Created by robin on 2015-10-06.
 */
public class ConnectedClient {
   private String nickname;
   private Notifiable callbackObject;

   public ConnectedClient(String nickname, Notifiable callbackObject) {
      this.nickname = nickname;
      this.callbackObject = callbackObject;
   }

   public String getNickname() {
      return nickname;
   }

   public void setNickname(String nickname) {
      this.nickname = nickname;
   }

   public Notifiable getCallbackObject() {
      return callbackObject;
   }
}
