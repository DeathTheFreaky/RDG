package at.RDG.network.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Queue;

public class NetworkReader extends Thread {
	private ObjectInputStream ois;
	private Queue<NetworkMessage> readQueue;
	private Socket s;

	public NetworkReader(Socket s, Queue<NetworkMessage> readQueue)
			throws IOException {
		this.s = s;
		this.ois = new ObjectInputStream(s.getInputStream());
		this.readQueue = readQueue;
	}

	/**
	 * @see Thread.interrupt
	 */
	@Override
	public void interrupt(){
		try {
			s.close();
		} catch (IOException e) {
		} finally{
			super.interrupt();
		}
	}
	
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				this.readQueue.offer((NetworkMessage)this.ois.readObject());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
