package at.RDG.network.communication;

import general.Enums.Directions;
import general.Enums.MessageType;

import java.net.InetAddress;
import java.util.Map;

/**
 * The NetworkMessage is an envelop for all messages sent through the network.</br>
 * All fields are public.
 * 
 * @author Clemens
 */
public class NetworkMessage {
	
	public final MessageType type;
	
	//for network
	public final InetAddress addr;
	public final int port;
	
	//for chat and network
	public final String message;
	
	//for item/monster remove
	/**
	 * Position 0 = X Position</br>
	 * Position 1 = Y Position
	 */
	public final int itempos[];
	
	//for player movement
	/**
	 * Position 0 = X Position</br>
	 * Position 1 = Y Position
	 */
	public final int playerpos[];
	public final Directions playerdir;
	
	//for fight
	public final Map<String, Float> fightvalues;
	
	/**
	 * The NetworkMessage predefined for network related messages.</br>
	 * All other fields are null.
	 * 
	 * @see NetworkMessage
	 * @param addr Address of an socket.
	 * @param port Port of an socket.
	 * @param msg A Message that could be sent additionally.
	 */
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
	
	/**
	 * The NetworkMessage predefined for chat related messages.</br>
	 * All other fields are null/0.
	 * 
	 * @see NetworkMessage
	 * @param msg The Chatmessage sent.
	 */
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
	
	/**
	 * The NetworkMessage predefined for item pickup related messages.</br>
	 * The values are accessible through the 2 position long array {@link NetworkMessage#itempos}</br>
	 * All other fields are null/0.
	 * 
	 * @see NetworkMessage
	 * @param itemposX The x position of the item on the map.
	 * @param itemposY The y position of the item on the map.
	 */
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
	
	/**
	 * The NetworkMessage predefined for player position related messages.</br>
	 * The values of the player position are accessible through the 2 position long array {@link NetworkMessage#playerpos}</br>
	 * All other fields are null/0.
	 * 
	 * @see NetworkMessage
	 * @param playerposX The x position of the player on the map.
	 * @param playerposY The y position of the player on the map.
	 * @param playerdir The players facing direction.
	 */
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
	
	/**
	 * The NetworkMessage predefined for fight related messages.</br>
	 * All other fields are null/0.
	 * 
	 * @see NetworkMessage
	 * @param fightvalues
	 */
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
	
	/**
	 * A fully customizable NetworkMessage.
	 * 
	 * @see NetworkMessage
	 * @param type The {@link MessageType} of the NetworkMessage.
	 * @param addr A InetAdress.
	 * @param port A port in int representation.
	 * @param msg A String message.
	 * @param itempos A int array.
	 * @param playerpos A int array.
	 * @param playerdir The {@link Directions} a player is facing.
	 * @param fightvalues A Map for fightvalues.
	 */
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
