package at.RDG.network;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.RDG.network.communication.NetworkMessage;
import at.RDG.network.communication.NetworkReader;
import at.RDG.network.communication.NetworkWriter;
import at.RDG.network.discovery.LobbySearcher;
import at.RDG.network.discovery.LobbyServer;
import at.RDG.network.discovery.Serverinfo;

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
		if(!this.writer.isAlive())
			return;
		this.writeQueue.offer(msg);
		this.writer.notify();
	}
	
	public NetworkMessage getNextMessage(){
		return this.readQueue.poll();
	}
	
	public boolean startLobby(String lobbyName) throws ArgumentOutOfRangeException{
		if(this.lserver != null){
			return true;
		}
		try {
			this.lserver = new LobbyServer(lobbyName);
		} catch (NoSuchAlgorithmException e) {
			Logger.getLogger(LobbyServer.class.getName()).log(Level.SEVERE,
					"Unable to create UID for server.");
			return false;
		}
		if(!this.lserver.isAlive())
			this.lserver.start();
		return true;
	}
	
	public void stopLobby(){
		if(this.lserver.isAlive())
			this.lserver.interrupt();
	}
	
	public void searchLobby(List<Serverinfo> lobbyList){
		if(this.searcher == null)
			this.searcher = new LobbySearcher(lobbyList);
		if(!this.searcher.isAlive())
			this.searcher.start();
	}
	
	public void stopSearchLobby(){
		if(this.searcher.isAlive())
			this.searcher.interrupt();
	}

	public static NetworkManager getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new NetworkManager();
		}
		return INSTANCE;
	}
}
