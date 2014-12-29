package at.RDG.network.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The NetworkReader is a subclass of Thread which reads every incoming object
 * of the network stream and writes it into a queue. It termitates it self if
 * the connection gets lost.
 * 
 * @author Clemens
 */
public class NetworkReader extends Thread {
	private ObjectInputStream ois;
	private BlockingQueue<NetworkMessage> readQueue;
	private Socket s;

	/**
	 * @see NetworkReader
	 * @param s
	 *            The socket to read from.
	 * @param readQueue
	 *            The queue to write to.
	 * @throws IOException
	 *             The Exception is thrown if it is impossible to bind the
	 *             InputStream.
	 */
	public NetworkReader(Socket s, BlockingQueue<NetworkMessage> readQueue)
			throws IOException {
		this.s = s;
		this.ois = new ObjectInputStream(s.getInputStream());
		this.readQueue = readQueue;
	}

	/**
	 * @see Thread.interrupt
	 */
	@Override
	public void interrupt() {
		try {
			s.close();
		} catch (IOException e) {
		} finally {
			super.interrupt();
			Logger.getLogger(NetworkReader.class.getName()).log(Level.INFO,
					"Thread is interrupted and socket is closed.");
		}
	}

	/**
	 * The method is started if the thread is started and reads everything from
	 * the network stream and writes it into the queue.</br> (Don't start this
	 * directly! Use Thread.start() instead.)
	 */
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			// reads the next object from the network stream and writes it into
			// the queue.
			try {
				NetworkMessage msg = (NetworkMessage) this.ois.readObject();
				System.out.println("got msg");
				this.readQueue.put(msg);
			} catch (SocketException e) {
				Logger.getLogger(NetworkReader.class.getName())
						.log(Level.WARNING,
								"Lost connection to Enemy. Shuting down NetworkReader.");
				Thread.currentThread().interrupt();
			} catch (IOException e) {
				Logger.getLogger(NetworkReader.class.getName())
						.log(Level.SEVERE,
								"Unable to read the object from the network stream or add it to the queue.",
								e);
			} catch (ClassNotFoundException e) {
				Logger.getLogger(NetworkReader.class.getName())
						.log(Level.SEVERE,
								"Unable to read the object from the network stream.",
								e);
			} catch (InterruptedException e) {
			}
		}
	}
}
