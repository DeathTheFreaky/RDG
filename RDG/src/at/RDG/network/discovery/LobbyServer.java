package at.RDG.network.discovery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.RDG.network.ArgumentOutOfRangeException;

/**
 * The LobbyServer is listening for incoming discovery requests and sends back
 * informations about him self.
 * 
 * @author Clemens
 */
public class LobbyServer extends Thread {

	private final String lobbyName; // the name of the lobby
	private final String UID; // the UID of the server
	private final int port;
	private MulticastSocket socket = null;

	/**
	 * @see LobbyServer
	 * @param lobbyName
	 *            The name the lobby should show up with. Should not be greater
	 *            then 256 characters.
	 * @throws ArgumentOutOfRangeException
	 *             is thrown if the passed lobby name is greater then 256
	 *             characters.
	 * @throws NoSuchAlgorithmException
	 *             is thrown if the LobbyServer class is unable to launch the
	 *             MD5 algorithm to create his UID.
	 */
	public LobbyServer(String lobbyName, int port) throws ArgumentOutOfRangeException,
			NoSuchAlgorithmException {
		if (lobbyName == null) {
			throw new NullPointerException("lobbyName cannot be null.");
		} else if (lobbyName.equals("")) {
			throw new ArgumentOutOfRangeException(
					"lobbyName must at least be one character!");
		} else if (lobbyName.length() > LobbyStatics.LOBBYNAMEMAXLENGTH) {
			throw new ArgumentOutOfRangeException(
					"lobbyName cannot be more then "
							+ LobbyStatics.LOBBYNAMEMAXLENGTH
							+ " characters long!");
		}
		this.lobbyName = lobbyName;
		this.port = port;

		// creates a UID for the server so if the server responses on two
		// different ips it can be identified as on.
		MessageDigest md = null;
		byte[] digest = null;
		Date date = new Date();
		md = MessageDigest.getInstance("MD5");
		digest = md.digest((this.lobbyName + UUID.randomUUID() + date
				.toString()).getBytes(StandardCharsets.UTF_8));
		this.UID = new String(digest);
	}

	/**
	 * @see Thread.interrupt
	 */
	@Override
	public void interrupt(){
		this.socket.close();
		super.interrupt();
		Logger.getLogger(LobbyServer.class.getName()).log(Level.INFO, "The thread is interrupted and the socked is closed");
	}
	
	/**
	 * The method is started if the thread is started and responses to every
	 * discovery request it receives.</br> (Don't start this directly! Use
	 * Thread.start() instead.)
	 */
	@Override
	public void run() {
		// open MulticastSocket and bound it to one of three free ports.
		try {
			for (int i = 0; i < LobbyStatics.SERVERPORTS.length; i++) {
				this.socket = new MulticastSocket(LobbyStatics.SERVERPORTS[i]);
				System.out.println(LobbyStatics.SERVERPORTS[i]);
				System.out.println(this.socket.getLocalPort());
				if (this.socket.isBound())
					break;
			}
			this.socket.setBroadcast(true);
			this.socket.setTimeToLive(10);
		} catch (IOException e) {
			Logger.getLogger(LobbyServer.class.getName()).log(Level.SEVERE,
					"IOExeption while creating the MulticastSocket.", e);
			Thread.currentThread().interrupt();
		} finally {
			// If its not successfully bound it stops the process.
			if (!this.socket.isBound()) {
				Logger.getLogger(LobbyServer.class.getName()).log(Level.SEVERE,
						"Unable to bind the MulticastSocket.");
				Thread.currentThread().interrupt();
			}
		}

		// receive and answer Requests
		DatagramPacket packet;
		while (!Thread.interrupted()) {
			byte[] buf = new byte[1];
			packet = new DatagramPacket(buf, buf.length);
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				Logger.getLogger(LobbyServer.class.getName()).log(Level.SEVERE,
						"Unable to receive a packet. SKIPPING", e);
				continue;
			}
			// check if the request is a valid one
			if (packet.getData()[0] == 7) {
				// prepare the answer and send lobby informations back
				Serverinfo server = new Serverinfo(null, this.port,
						this.lobbyName, this.UID);
				ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
				ObjectOutputStream oos = null;
				try {
					oos = new ObjectOutputStream(bos);
					oos.writeObject(server);
				} catch (IOException e) {
					Logger.getLogger(LobbyServer.class.getName()).log(
							Level.SEVERE,
							"Unable to write to ObjectOutputStream. SKIPPING",
							e);
					continue;
				}

				DatagramPacket sendPacket = new DatagramPacket(
						bos.toByteArray(), bos.size(), packet.getAddress(),
						packet.getPort());
				try {
					this.socket.send(sendPacket);
				} catch (IOException e) {
					Logger.getLogger(LobbyServer.class.getName()).log(
							Level.SEVERE,
							"Unable to send the packet. SKIPPING", e);
					continue;
				}
			}
		}

		// close Socket when interrupted
		this.socket.close();
		this.socket = null;
	}

}
