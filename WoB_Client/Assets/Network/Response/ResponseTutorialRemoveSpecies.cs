using UnityEngine;

using System;

public class ResponseTutorialRemoveSpeciesEventArgs : ExtendedEventArgs {
  public short status { get; set; }

  public ResponseTutorialRemoveSpeciesEventArgs() {
    event_id = Constants.SMSG_TUTORIAL_REMOVE_SPECIES;
  }
}

public class ResponseTutorialRemoveSpecies : NetworkResponse {
  
  public ResponseTutorialRemoveSpecies() {
  }
  
  public override void parse() {
  }
  
  public override ExtendedEventArgs process() {
   ResponseTutorialRemoveSpeciesEventArgs args = null;
    
    args = new ResponseTutorialRemoveSpeciesEventArgs();

    return args;
  }
}