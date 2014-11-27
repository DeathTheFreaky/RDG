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
	public LobbyServer(String lobbyName) throws ArgumentOutOfRangeException,
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

		// creates a UID for the server so if the server responses on two
		// different ips it can be identified as on.
		MessageDigest md = null;
		byte[] thedigest = null;
		Date date = new Date();
		md = MessageDigest.getInstance("MD5");
		thedigest = md.digest((this.lobbyName + UUID.randomUUID() + date
				.toString()).getBytes(StandardCharsets.UTF_8));
		this.UID = new String(thedigest);
	}

	/**
	 * The method is started if the thread is started and responses to every
	 * discovery request it receives.</br> (Don't start this directly! Use
	 * Thread.start() instead.)
	 */
	@Override
	public void run() {
		// open MulticastSocket and bound it to one of three free ports.
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket(LobbyStatics.SERVERPORTS[0]);
			System.out.println(LobbyStatics.SERVERPORTS[0]);
			System.out.println(socket.getPort());
			if (!socket.isBound()) {
				socket.close();
				socket = new MulticastSocket(LobbyStatics.SERVERPORTS[1]);
				if (!socket.isBound()) {
					socket.close();
					socket = new MulticastSocket(LobbyStatics.SERVERPORTS[2]);
				}
			}
			socket.setBroadcast(true);
			socket.setTimeToLive(10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// If its not successfully bound it stops the process.
			if (!socket.isBound()) {
				Thread.currentThread().interrupt();
				// TODO error msg and logging
			}
		}
		System.out.println(socket.getPort());

		// receive and answer Requests
		DatagramPacket packet;
		while (!Thread.interrupted()) {
			byte[] buf = new byte[1];
			packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// check if the request is a valid one
			if (packet.getData()[0] == 7) {
				System.out.println("recved");

				// prepare the answer and send lobby informations back
				Serverinfo server = new Serverinfo(null, socket.getPort(),
						this.lobbyName, this.UID);
				ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
				ObjectOutputStream oos = null;
				try {
					oos = new ObjectOutputStream(bos);
					oos.writeObject(server);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				DatagramPacket sendPacket = new DatagramPacket(
						bos.toByteArray(), bos.size(), packet.getAddress(),
						packet.getPort());
				System.out.println(bos.size());
				try {
					socket.send(sendPacket);
					System.out.println("sended");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// close Socket when interrupted
		socket.close();
	}

}
