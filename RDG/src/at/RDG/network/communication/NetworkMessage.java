package at.RDG.network.communication;

import elements.Element;
import general.Enums.Directions;
import general.Enums.MessageType;
import general.Enums.ViewingDirections;

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
	
	//for item/monster change
	/**
	 * Position 0 = X Position</br>
	 * Position 1 = Y Position
	 */
	public final int itempos[];
	public final Element item;
	
	//for player movement
	/**
	 * Position 0 = X Position</br>
	 * Position 1 = Y Position
	 */
	public final int playerpos[];
	public final ViewingDirections playerdir;
	
	//for fight
	public final Map<String, Float> fightvalues;
	
	//for map transfer
	public final Element[][] overlay;
	
	//for avoiding two players fighting the same creature
	public final int enemyPosX;
	public final int enemyPosY;
	
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
		this.item = null;
		this.playerpos = null;
		this.playerdir = null;
		this.fightvalues = null;
		this.overlay = null;
		this.enemyPosX = 0;
		this.enemyPosY = 0;
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
		this.item = null;
		this.playerpos = null;
		this.playerdir = null;
		this.fightvalues = null;
		this.overlay = null;
		this.enemyPosX = 0;
		this.enemyPosY = 0;
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
	public NetworkMessage(int itemposX, int itemposY, Element item){
		this.type = MessageType.ITEM;
		this.addr = null;
		this.port = 0;
		this.message = null;
		this.itempos = new int[2];
		this.itempos[0] = itemposX;
		this.itempos[1] = itemposY;
		this.item = item;
		this.playerpos = null;
		this.playerdir = null;
		this.fightvalues = null;
		this.overlay = null;
		this.enemyPosX = 0;
		this.enemyPosY = 0;
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
	public NetworkMessage(int playerposX, int playerposY, ViewingDirections playerdir){
		this.type = MessageType.PLAYERPOSITION;
		this.addr = null;
		this.port = 0;
		this.message = null;
		this.itempos = null;
		this.item = null;
		this.playerpos = new int[2];
		this.playerpos[0] = playerposX;
		this.playerpos[1] = playerposY;
		this.playerdir = playerdir;
		this.fightvalues = null;
		this.overlay = null;
		this.enemyPosX = 0;
		this.enemyPosY = 0;
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
		this.item = null;
		this.playerpos = null;
		this.playerdir = null;
		this.fightvalues = fightvalues;
		this.overlay = null;
		this.enemyPosX = 0;
		this.enemyPosY = 0;
	}
	
	/**The NetworkMessage predefined for initial map transfer.</br>
	 * All other fields are null/0.
	 * @param overlay
	 */
	public NetworkMessage(Element[][] overlay) {
		this.type = MessageType.MAP;
		this.addr = null;
		this.port = 0;
		this.message = null;
		this.itempos = null;
		this.item = null;
		this.playerpos = null;
		this.playerdir = null;
		this.fightvalues = null;
		this.overlay = overlay;
		this.enemyPosX = 0;
		this.enemyPosY = 0;
	}
	
	/**The NetworkMessage predefined for telling other pc against whom the player currently fights.</br>
	 * All other fields are null/0.
	 * @param overlay
	 */
	public NetworkMessage(int enemyX, int enemyY) {
		this.type = MessageType.FIGHTPOSITION;
		this.addr = null;
		this.port = 0;
		this.message = null;
		this.itempos = null;
		this.item = null;
		this.playerpos = null;
		this.playerdir = null;
		this.fightvalues = null;
		this.overlay = null;
		this.enemyPosX = 0;
		this.enemyPosY = 0;
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
	 * @param itempos A {@link Element} to add to the map.
	 * @param playerpos A int array.
	 * @param playerdir The {@link Directions} a player is facing.
	 * @param fightvalues A Map for fightvalues.
	 * @param overlay A two dimensional {@link Element} array.
	 */
	public NetworkMessage(MessageType type, InetAddress addr, int port, String msg, int[] itempos, Element item, int[] playerpos, ViewingDirections playerdir, Map<String,Float> fightvalues, Element[][] overlay, int enemyPosX, int enemyPosY){
		this.type = type;
		this.addr = addr;
		this.port = port;
		this.message = msg;
		this.itempos = itempos;
		this.item = item;
		this.playerpos = playerpos;
		this.playerdir = playerdir;
		this.fightvalues = fightvalues;
		this.overlay = overlay;
		this.enemyPosX = enemyPosX;
		this.enemyPosY = enemyPosY;
	}
	
}
