package at.RDG.network;

import java.util.LinkedList;
import java.util.Queue;

import at.RDG.network.communication.NetworkMessage;
import at.RDG.network.communication.NetworkReader;
import at.RDG.network.communication.NetworkWriter;
import at.RDG.network.discovery.LobbySearcher;
import at.RDG.network.discovery.LobbyServer;

public class NetworkManager {
	
	private static NetworkManager INSTANCE = null;
	
	private Queue<NetworkMessage> writeQueue;
	private Queue<NetworkMessage> readQueue;
	private LobbySearcher searcher = null;
	private LobbyServer lserver = null;
	private NetworkWriter writer = null;
	private NetworkReader reader = null;
	
	private NetworkManager(){
		this.writeQueue = new LinkedList<NetworkMessage>();
		this.readQueue = new LinkedList<NetworkMessage>();
	}
	
	public void sendMessage(NetworkMessage msg){
		this.writeQueue.offer(msg);
		this.writer.notify();
	}
	
	public NetworkMessage getNextMessage(){
		return this.readQueue.poll();
	}

	public static NetworkManager getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new NetworkManager();
		}
		return INSTANCE;
	}
}
