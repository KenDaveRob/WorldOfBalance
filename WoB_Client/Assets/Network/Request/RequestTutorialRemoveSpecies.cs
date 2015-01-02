using UnityEngine;

using System;

public class RequestTutorialRemoveSpecies : NetworkRequest {

  public RequestTutorialRemoveSpecies() {
    packet = new GamePacket(request_id = Constants.CMSG_TUTORIAL_REMOVE_SPECIES);
  }
  
  public void Send() {
  }
}