package at.RDG.network.discovery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import at.RDG.network.ArgumentOutOfRangeException;
import at.RDG.network.NetworkStatics;

public class LobbyServer extends Thread {

	private final String lobbyName;

	public LobbyServer(String lobbyName) throws ArgumentOutOfRangeException {
		if (lobbyName.length() > NetworkStatics.LOBBYNAMEMAXLENGTH) {
			throw new ArgumentOutOfRangeException(
					"lobbyName cannot be more then "
							+ NetworkStatics.LOBBYNAMEMAXLENGTH
							+ " characters long!");
		}
		this.lobbyName = lobbyName;
	}

	@Override
	public void run() {
		// open MulticastSocket (UDP)
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket(NetworkStatics.SERVERPORTS[0]);
			if (!socket.isBound()) {
				socket.close();
				socket = new MulticastSocket(NetworkStatics.SERVERPORTS[1]);
				if (!socket.isBound()) {
					socket.close();
					socket = new MulticastSocket(NetworkStatics.SERVERPORTS[2]);
				}
			}
			socket.setBroadcast(true);
			socket.setTimeToLive(10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (!socket.isBound()) {
				Thread.currentThread().interrupt();
				// TODO error msg and logging
			}
		}

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
			if (packet.getData()[0] == 7) {
				System.out.println("recved");
				Serverinfo server = new Serverinfo(null, socket.getPort(), this.lobbyName);
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
