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

/***
 * The NetworkManager can only be instanced once and is responsible for every
 * network communication.
 * 
 * @author Clemens
 *
 */
public class NetworkManager {

	private static NetworkManager INSTANCE = null;

	private Queue<NetworkMessage> writeQueue;
	private Queue<NetworkMessage> readQueue;
	private ServerSocket serverSocket;
	private Socket socket = null;
	private LobbySearcher searcher = null;
	private LobbyServer lserver = null;
	private NetworkWriter writer = null;
	private NetworkReader reader = null;
	private Thread acceptor;
	private Boolean lobbyHost = false;

	/**
	 * @see NetworkManager
	 * @throws IOException
	 *             The Exception is thrown if there is no free port on the
	 *             system and the ServerSocket was not able to bind.
	 */
	private NetworkManager() throws IOException {
		this.writeQueue = new LinkedList<NetworkMessage>();
		this.readQueue = new LinkedList<NetworkMessage>();
		this.serverSocket = new ServerSocket();
	}

	/**
	 * Every NetworkMessage put as param is sent through the network.</br> If
	 * there is no connection the operation is ignored and nothing gets sent.
	 * 
	 * @param msg
	 *            NetworkMessage to send.
	 */
	public void sendMessage(NetworkMessage msg) {
		if (this.writer == null || !this.writer.isAlive())
			return;
		// writes into the queue and notifies the writer thread that something
		// is in the queue.
		this.writeQueue.offer(msg);
		this.writer.notify();
	}

	/**
	 * Returns next received NetworkMessage in the queue.
	 * 
	 * @return next NetworkMessage in the queue. Returns null if there is no
	 *         message.</br>A message cannot be gotten 2 times.
	 */
	public NetworkMessage getNextMessage() {
		return this.readQueue.poll();
	}

	/**
	 * Starts a Lobby with the name passed to it.
	 * 
	 * @param lobbyName
	 *            The name for the lobby.
	 * @throws ArgumentOutOfRangeException
	 *             Is thrown if the lobbyName is greater then 256 characters.
	 * @throws UnableToStartConnectionException
	 *             Is thrown if the lobby was not able to start.
	 * @throws IllegalThreadStateException
	 *             Is thrown if the lobby is started more then once.
	 */
	public void startLobby(String lobbyName)
			throws ArgumentOutOfRangeException,
			UnableToStartConnectionException, IllegalThreadStateException {
		// starts thread to accept incomming connection
		if (this.acceptor == null) {
			this.acceptor = new Thread() {
				@Override
				public void interrupt() {
					try {
						serverSocket.close();
					} catch (IOException e) {
					} finally {
						super.interrupt();
					}
				}

				@Override
				public void run() {
					try {
						serverSocket.bind(null);
						socket = serverSocket.accept();
					} catch (IOException e) {
						Logger.getLogger(LobbyServer.class.getName())
								.log(Level.SEVERE,
										"Unable bind socket. Accept connection or thread was interrupted.",
										e);
					}
				}
			};
		}
		this.acceptor.start();
		//starts lobby thread
		if (this.lserver == null) {
			try {
				this.lserver = new LobbyServer(lobbyName,
						this.serverSocket.getLocalPort());
			} catch (NoSuchAlgorithmException e) {
				Logger.getLogger(LobbyServer.class.getName()).log(Level.SEVERE,
						"Unable to create UID for server.", e);
				throw new UnableToStartConnectionException(
						"The LobbyServer is unable to start.");
			}
		}
		this.lserver.start();
	}

	/**
	 * Stops the started lobby.</br> Has no effect if the lobby is not started.
	 */
	public void stopLobby() {
		this.lserver.interrupt();
		this.acceptor.interrupt();
	}

	/**
	 * Starts a search for all open lobby in the local network.
	 * 
	 * @param lobbyList
	 *            A list where all found lobbies are written to.
	 * @throws IllegalThreadStateException
	 *             Is thrown if the search is started more then once.
	 */
	public void searchLobby(List<Serverinfo> lobbyList)
			throws IllegalThreadStateException {
		if (this.searcher == null)
			this.searcher = new LobbySearcher(lobbyList);
		this.searcher.start();
	}

	/**
	 * Stops the search for lobbies.</br> Has no effect if the lobby is not
	 * started.
	 */
	public void stopSearchLobby() {
		this.searcher.interrupt();
	}

	/**
	 * Tries to connect to a lobby.
	 * 
	 * @param lobbyInfo
	 *            The {@link Serverinfo} object of the lobby where to connect
	 *            to.
	 * @throws UnableToStartConnectionException
	 *             Is thrown if it was not possible to connect to the lobby.
	 */
	public void connect(Serverinfo lobbyInfo)
			throws UnableToStartConnectionException {
		try {
			// connect to lobby and start the writer and reader for the socket.
			this.socket = new Socket(lobbyInfo.getAddress(),
					lobbyInfo.getPort());
			this.writer = new NetworkWriter(this.socket, this.writeQueue);
			this.writer.start();
			this.reader = new NetworkReader(this.socket, this.readQueue);
			this.reader.start();
		} catch (IOException e) {
			Logger.getLogger(LobbyServer.class.getName()).log(Level.SEVERE,
					"Unable to connect to server.", e);
			if (this.writer.isAlive())
				this.writer.interrupt();
			if (this.reader.isAlive())
				this.reader.interrupt();
			throw new UnableToStartConnectionException(
					"The connection is unable to start.");
		}
	}

	/**
	 * @return true if connected to another player and false if not.
	 */
	public boolean isConnected() {
		return this.socket != null;
	}

	/**
	 * Returns a instance of the NetworkManager to work with. Once it is created
	 * it is always the same.
	 * 
	 * @return instance of NetworkManager
	 * @throws IOException
	 *             The Exception is thrown if there is no free port on the
	 *             system and the ServerSocket was not able to bind.</br> The
	 *             Exception can only be thrown if the NetworkManager is
	 *             created.
	 */
	public static NetworkManager getInstance() throws IOException {
		if (INSTANCE == null) {
			INSTANCE = new NetworkManager();
		}
		return INSTANCE;
	}
	
	/**
	 * @return true if this Computer hosted the lobby
	 */
	public Boolean isLobbyHost() {
		return this.lobbyHost;
	}
	
	/**Sets Boolean to tell if this computer is the lobbyHost.
	 * @param lobbyHost
	 */
	public void setLobbyHost(Boolean lobbyHost) {
		this.lobbyHost = lobbyHost;
	}
}
