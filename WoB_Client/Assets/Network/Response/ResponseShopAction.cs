using UnityEngine;

using System;
using System.Collections.Generic;

public class ResponseShopActionEventArgs : ExtendedEventArgs {
	public short action { get; set; }
	
	public ResponseShopActionEventArgs() {
		event_id = Constants.SMSG_SHOP_ACTION;
	}
}

public class ResponseShopAction : NetworkResponse {
	
	private short action;
	
	public ResponseShopAction() {
	}
	
	public override void parse() {
	}
	
	public override ExtendedEventArgs process() {
		ResponseShopActionEventArgs args = null;

		args = new ResponseShopActionEventArgs();

		return args;
	}
}