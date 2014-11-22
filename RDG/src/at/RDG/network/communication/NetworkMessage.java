package at.RDG.network.communication;

import enums.MessageType;

public class NetworkMessage {
	
	MessageType type;
	String message;
	
	public NetworkMessage(String msg){
		this.type = MessageType.CHAT;
		this.message = msg;
	}
	
}
