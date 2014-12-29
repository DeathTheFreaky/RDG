package at.RDG.network.discovery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The LobbySearcher searches for open Lobbies in the local network and writes
 * them into the provided List.
 * 
 * @author Clemens
 */
public class LobbySearcher extends Thread {

	private List<Serverinfo> lobbyList;
	private Map<String, Object> serverMap;
	private MulticastSocket socket = null;

	/**
	 * @see LobbySearcher
	 * @param lobbyList
	 *            A List where to put the information about all found Lobbies.
	 */
	public LobbySearcher(List<Serverinfo> lobbyList) {
		if (lobbyList == null)
			throw new NullPointerException("lobbyList cannot be null");
		this.lobbyList = lobbyList;
		this.serverMap = new HashMap<String, Object>();
	}
	
	/**
	 * @see Thread.interrupt
	 */
	@Override
	public void interrupt(){
		this.socket.close();
		super.interrupt();
		Logger.getLogger(LobbySearcher.class.getName()).log(Level.INFO, "The thread is interrupted and the socked is closed");
	}

	/**
	 * The method is started if the thread is started and searches for Lobbies
	 * in the local network.</br> (Don't start this directly! Use Thread.start()
	 * instead.)
	 */
	@Override
	public void run() {
		// opens a new Multicast Socket on any open port, activates Broadcast
		// Messages and sets the Time to Live of the Packet to 10.
		try {
			this.socket = new MulticastSocket();
			this.socket.setBroadcast(true);
			this.socket.setTimeToLive(10);
		} catch (SocketException e) {
			Logger.getLogger(LobbySearcher.class.getName()).log(Level.SEVERE,
					"Unable to create the MulticastSocket.", e);
			if (!this.socket.isClosed())
				this.socket.close();
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			Logger.getLogger(LobbySearcher.class.getName()).log(Level.SEVERE,
					"Unable to create the MulticastSocket.", e);
			if (!this.socket.isClosed())
				this.socket.close();
			Thread.currentThread().interrupt();
		} finally {
			// if the Socket was not bound the thread stops
			if (!this.socket.isBound()) {
				Logger.getLogger(LobbySearcher.class.getName()).log(
						Level.SEVERE, "Unable to bind the MulticastSocket.");
				this.socket.close();
				Thread.currentThread().interrupt();
			}
		}

		if (!Thread.interrupted()) {
			// sends DatagramPackets to all Broadcast Addresses in the Network
			// on all defined ports
			DatagramPacket sendPacket = null;
			for (int i = 0; i < LobbyStatics.SERVERPORTS.length; i++) {
				if(Thread.interrupted())
					break;
				// send to global broadcast
				try {
					sendPacket = new DatagramPacket(new byte[] { (byte) 7 }, 1,
							InetAddress.getByName("255.255.255.255"),
							LobbyStatics.SERVERPORTS[i]);
					this.socket.send(sendPacket);
				} catch (UnknownHostException e) {
					Logger.getLogger(LobbySearcher.class.getName())
							.log(Level.SEVERE,
									"Unable to create a global broadcast packet. SKIPPING.",
									e);
				} catch (IOException e) {
					Logger.getLogger(LobbySearcher.class.getName()).log(
							Level.SEVERE,
							"Unable to send a global broadcast. SKIPPING.", e);
				}

				// Broadcast the message over all the network interfaces
				Enumeration<NetworkInterface> interfaces = null;
				try {
					interfaces = NetworkInterface.getNetworkInterfaces();
				} catch (SocketException e) {
					Logger.getLogger(LobbySearcher.class.getName()).log(
							Level.SEVERE,
							"Unable get network interfaces. SKIPPING.", e);
				}
				while (interfaces.hasMoreElements()) {
					if(Thread.interrupted())
						break;
					NetworkInterface networkInterface = interfaces
							.nextElement();

					try {
						if (networkInterface.isLoopback()
								|| !networkInterface.isUp()) {
							continue; // Don't want to broadcast to the loopback
										// interface
						}
					} catch (SocketException e) {
						Logger.getLogger(LobbySearcher.class.getName())
								.log(Level.SEVERE,
										"Unable to check Interface properties. SKIPPING.",
										e);
						continue;
					}

					for (InterfaceAddress interfaceAddress : networkInterface
							.getInterfaceAddresses()) {
						if(Thread.interrupted())
							break;
						InetAddress broadcast = interfaceAddress.getBroadcast();
						if (broadcast == null) {
							continue;
						}

						// Send the broadcast package!
						try {
							sendPacket = new DatagramPacket(
									new byte[] { (byte) 7 }, 1, broadcast,
									LobbyStatics.SERVERPORTS[i]);
							this.socket.send(sendPacket);
						} catch (IOException e) {
							Logger.getLogger(LobbySearcher.class.getName())
									.log(Level.SEVERE,
											"Unable to send packet. SKIPPING.",
											e);
							continue;
						}

						System.out.println(getClass().getName()
								+ ">>> Request packet sent to: "
								+ broadcast.getHostAddress() + "; Interface: "
								+ networkInterface.getDisplayName());
					}
				}
			}
		}

		// Receives all packets send back by an open Lobby
		DatagramPacket packet = null;
		while (!Thread.interrupted()) {
			byte[] buf = new byte[LobbyStatics.LOBBYNAMEMAXLENGTH * 3];
			packet = new DatagramPacket(buf, buf.length);
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				Logger.getLogger(LobbySearcher.class.getName()).log(
						Level.WARNING, "Unable to receive packet. SKIPPING. Maybe the thread was interrupted.", e);
				continue;
			}

			// read the received object and check if it is an instance of
			// Serverinfo
			ByteArrayInputStream bis = new ByteArrayInputStream(
					packet.getData());
			ObjectInputStream ios = null;
			Object obj = null;
			try {
				ios = new ObjectInputStream(bis);
				obj = ios.readObject();
			} catch (IOException e) {
				Logger.getLogger(LobbySearcher.class.getName()).log(
						Level.SEVERE,
						"Unable to create ObjectInputStream. SKIPPING.", e);
				continue;
			} catch (ClassNotFoundException e) {
				Logger.getLogger(LobbySearcher.class.getName())
						.log(Level.SEVERE,
								"Unable to read Class from ObjectInputStream. SKIPPING.",
								e);
				continue;
			}

			if (!(obj instanceof Serverinfo)) {
				Logger.getLogger(LobbySearcher.class.getName()).log(
						Level.SEVERE,
						"Read Object is the wrong type. SKIPPING.");
				continue;
			}
			// prepare the received Object for foreigner use and right it into
			// the list of open Lobbies if it isn't in there already.
			Serverinfo info = (Serverinfo) obj;
			info.setAddress(packet.getAddress());

			if (!this.serverMap.containsKey(info.getUID())) {
				this.serverMap.put(info.getUID(), null);
				this.lobbyList.add(info);
			}
		}

		this.socket.close();
		this.socket = null;
	}
	
	/**
	 * @return list of all lobbies found so far
	 */
	public synchronized List<Serverinfo> getFilledLobbyList() {
		return this.lobbyList;
	}

}
