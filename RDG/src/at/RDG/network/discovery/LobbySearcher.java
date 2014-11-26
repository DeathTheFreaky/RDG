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

import at.RDG.network.NetworkStatics;

public class LobbySearcher extends Thread {

	private List<Serverinfo> lobbyList;
	private Map<String, Object> serverMap;

	/**
	 * LobbySearcher searches for a open Lobbies in the local network and writes
	 * them into the provided List.
	 * 
	 * @param lobbyList
	 */
	public LobbySearcher(List<Serverinfo> lobbyList) {
		this.lobbyList = lobbyList;
		this.serverMap = new HashMap<String, Object>();
	}

	/**
	 * The method is started if the thread is started and searches for Lobbies
	 * in the local network.
	 */
	@Override
	public void run() {
		// opens a new Multicast Socket on any open port, activates Broadcast
		// Messages and sets the Time to Live of the Packet to 10.
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket();
			socket.setBroadcast(true);
			socket.setTimeToLive(10);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// if the Socket was not bound the thread stops
			if (!socket.isBound()/* || group == null */) {
				Thread.currentThread().interrupt();
				// TODO error msg and logging
			}
		}

		// sends DatagramPackets to all Broadcast Addresses in the Network on
		// all defined ports
		DatagramPacket sendPacket = null;
		for (int i = 0; i < NetworkStatics.SERVERPORTS.length; i++) {
			// send to global broadcast
			try {
				sendPacket = new DatagramPacket(new byte[] { (byte) 7 }, 1,
						InetAddress.getByName("255.255.255.255"),
						NetworkStatics.SERVERPORTS[i]);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				socket.send(sendPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Broadcast the message over all the network interfaces
			Enumeration<NetworkInterface> interfaces = null;
			try {
				interfaces = NetworkInterface.getNetworkInterfaces();
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();

				try {
					if (networkInterface.isLoopback()
							|| !networkInterface.isUp()) {
						continue; // Don't want to broadcast to the loopback
									// interface
					}
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (InterfaceAddress interfaceAddress : networkInterface
						.getInterfaceAddresses()) {
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast == null) {
						continue;
					}

					// Send the broadcast package!
					try {
						sendPacket = new DatagramPacket(
								new byte[] { (byte) 7 }, 1, broadcast,
								NetworkStatics.SERVERPORTS[i]);
						socket.send(sendPacket);
					} catch (Exception e) {
					}

					System.out.println(getClass().getName()
							+ ">>> Request packet sent to: "
							+ broadcast.getHostAddress() + "; Interface: "
							+ networkInterface.getDisplayName());
				}
			}
		}

		// Receives all packets send back by an open Lobby
		DatagramPacket packet;
		while (!Thread.interrupted()) {
			byte[] buf = new byte[NetworkStatics.LOBBYNAMEMAXLENGTH * 2];
			packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!(obj instanceof Serverinfo)) {
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

		socket.close();
	}

}
