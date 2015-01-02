using UnityEngine;

using System;

public class RequestUpdateCurTut : NetworkRequest {
  public RequestUpdateCurTut() {
    packet = new GamePacket(request_id = Constants.CMSG_TUTORIAL_UPDATE_CUR_TUT);
  }
  
  public void Send(int id, int tutorialIndex) {
    packet.addInt32(id);
    packet.addInt32(tutorialIndex);
  }
}
