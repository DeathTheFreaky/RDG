package at.RDG.network.discovery;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * The Serverinfo class is a storage class that represents all important
 * information about a lobby.
 * 
 * @author Clemens
 */
public class Serverinfo implements Serializable {

	private static final long serialVersionUID = 2153567303370864667L;
	private InetAddress address;
	private int port;
	private String lobbyName;
	private String UID;

	/**
	 * @see Serverinfo
	 * @param address
	 *            Informations about the servers socket address
	 * @param port
	 *            that the server listens at
	 * @param lobbyName
	 *            The name of the lobby
	 * @param UID
	 *            The UID of the server
	 */
	Serverinfo(InetAddress address, int port, String lobbyName, String UID) {
		this.address = address;
		this.lobbyName = lobbyName;
		this.port = port;
		this.UID = UID;
	}

	/**
	 * @return the information about the servers socket address
	 */
	public InetAddress getAddress() {
		return this.address;
	}

	/**
	 * Sets the information about the servers socket address
	 * 
	 * @param address
	 *            about the servers socket address
	 */
	void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * @return the port the server is listening to
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * @return the lobby name of the server
	 */
	public String getLobbyName() {
		return this.lobbyName;
	}

	/**
	 * @return the UID of the server
	 */
	String getUID() {
		return this.UID;
	}
}
