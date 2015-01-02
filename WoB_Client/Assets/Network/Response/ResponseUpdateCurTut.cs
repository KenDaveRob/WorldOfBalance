using UnityEngine;

using System;

public class ResponseUpdateCurTutEventArgs : ExtendedEventArgs {
  public int currentTutorial { get; set; }
  
  public ResponseUpdateCurTutEventArgs() {
    event_id = Constants.SMSG_TUTORIAL_UPDATE_CUR_TUT;
  }
}

public class ResponseUpdateCurTut : NetworkResponse {
  
  private int currentTutorial;

  public ResponseUpdateCurTut() {
  }
  
  public override void parse() {
    
    currentTutorial = DataReader.ReadInt(dataStream);
  }
  
  public override ExtendedEventArgs process() {
    ResponseUpdateCurTutEventArgs args = null;
    
    args = new ResponseUpdateCurTutEventArgs();
    args.currentTutorial = currentTutorial;
    
    return args;
  }
}