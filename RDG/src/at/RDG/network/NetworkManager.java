package at.RDG.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
	private ServerSocket serverSocket;
	private Socket acceptSocket = null;
	private Socket connectSocket = null;
	private LobbySearcher searcher = null;
	private LobbyServer lserver = null;
	private NetworkWriter writer = null;
	private NetworkReader reader = null;

	private NetworkManager() throws IOException {
		this.writeQueue = new LinkedList<NetworkMessage>();
		this.readQueue = new LinkedList<NetworkMessage>();
		this.serverSocket = new ServerSocket(0);
	}

	public void sendMessage(NetworkMessage msg) {
		if (!this.writer.isAlive())
			return;
		this.writeQueue.offer(msg);
		this.writer.notify();
	}

	public NetworkMessage getNextMessage() {
		return this.readQueue.poll();
	}

	public void startLobby(String lobbyName)
			throws ArgumentOutOfRangeException, UnableToStartConnectionExecption, IllegalThreadStateException {
		if (this.serverSocket == null) {
			try {
				this.lserver = new LobbyServer(lobbyName,
						this.serverSocket.getLocalPort());
			} catch (NoSuchAlgorithmException e) {
				Logger.getLogger(LobbyServer.class.getName()).log(Level.SEVERE,
						"Unable to create UID for server.", e);
				throw new UnableToStartConnectionExecption(
						"The LobbyServer is unable to start.");
			}
		}
		this.lserver.start();
	}

	public void stopLobby() {
		this.lserver.interrupt();
	}

	public void searchLobby(List<Serverinfo> lobbyList) throws IllegalThreadStateException {
		if (this.searcher == null)
			this.searcher = new LobbySearcher(lobbyList);
		this.searcher.start();
	}

	public void stopSearchLobby() {
		this.searcher.interrupt();
	}

	public void connect(Serverinfo lobbyInfo) throws UnableToStartConnectionExecption {
		try {
			this.connectSocket = new Socket(lobbyInfo.getAddress(), lobbyInfo.getPort());
			this.writer = new NetworkWriter(this.connectSocket, this.writeQueue);
			this.writer.start();
			this.sendMessage(new NetworkMessage(this.serverSocket.getInetAddress(), this.serverSocket.getLocalPort(), null));
			this.acceptSocket = this.serverSocket.accept();
			this.reader = new NetworkReader(this.acceptSocket, this.readQueue);
			this.reader.start();
		} catch (IOException e) {
			Logger.getLogger(LobbyServer.class.getName()).log(Level.SEVERE,
					"Unable to connect to server.", e);
			throw new UnableToStartConnectionExecption(
					"The connection is unable to start.");
		}
	}

	public static NetworkManager getInstance() throws IOException {
		if (INSTANCE == null) {
			INSTANCE = new NetworkManager();
		}
		return INSTANCE;
	}
}
