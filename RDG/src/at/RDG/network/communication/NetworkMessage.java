package at.RDG.network.communication;

import general.Enums.Directions;
import general.Enums.MessageType;

import java.net.InetAddress;
import java.util.Map;

public class NetworkMessage {
	
	public final MessageType type;
	
	//for network
	public final InetAddress addr;
	public final int port;
	
	//for chat and network
	public final String message;
	
	//for item/monster remove
	public final int itempos[];
	
	//for player movement
	public final int playerpos[];
	public final Directions playerdir;
	
	//for fight
	public final Map<String, Float> fightvalues;
	
	public NetworkMessage(InetAddress addr, int port, String msg){
		this.type = MessageType.NETWORK;
		this.addr = addr;
		this.port = port;
		this.message = msg;
		this.itempos = null;
		this.playerpos = null;
		this.playerdir = null;
		this.fightvalues = null;
	}
	
	public NetworkMessage(String msg){
		this.type = MessageType.CHAT;
		this.addr = null;
		this.port = 0;
		this.message = msg;
		this.itempos = null;
		this.playerpos = null;
		this.playerdir = null;
		this.fightvalues = null;
	}
	
	public NetworkMessage(int itemposX, int itemposY){
		this.type = MessageType.ITEMPICKUP;
		this.addr = null;
		this.port = 0;
		this.message = null;
		this.itempos = new int[2];
		this.itempos[0] = itemposX;
		this.itempos[1] = itemposY;
		this.playerpos = null;
		this.playerdir = null;
		this.fightvalues = null;
	}
	
	public NetworkMessage(int playerposX, int playerposY, Directions playerdir){
		this.type = MessageType.PLAYERPOSITION;
		this.addr = null;
		this.port = 0;
		this.message = null;
		this.itempos = null;
		this.playerpos = new int[2];
		this.playerpos[0] = playerposX;
		this.playerpos[1] = playerposY;
		this.playerdir = playerdir;
		this.fightvalues = null;
	}
	
	public NetworkMessage(Map<String, Float> fightvalues){
		this.type = MessageType.FIGHT;
		this.addr = null;
		this.port = 0;
		this.message = null;
		this.itempos = null;
		this.playerpos = null;
		this.playerdir = null;
		this.fightvalues = fightvalues;
	}
	
	public NetworkMessage(MessageType type, InetAddress addr, int port, String msg, int[] itempos, int[] playerpos, Directions playerdir, Map<String,Float> fightvalues){
		this.type = type;
		this.addr = addr;
		this.port = port;
		this.message = msg;
		this.itempos = itempos;
		this.playerpos = playerpos;
		this.playerdir = playerdir;
		this.fightvalues = fightvalues;
	}
	
}
