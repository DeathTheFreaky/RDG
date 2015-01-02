package at.RDG.network.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The NetworkWriter is a subclass of Thread and when started it writes every
 * object in the queue into the network stream. It termitates itself if the
 * connection gets lost.
 * 
 * @author Clemens
 */
public class NetworkWriter extends Thread {

	private ObjectOutputStream oos;
	private BlockingQueue<NetworkMessage> writeQueue;
	private Socket s;

	/**
	 * @see NetworkWriter
	 * @param s
	 *            The socket where the messages are written into
	 * @param writeQueue
	 *            The queue where the messages are take from to write it in the
	 *            network stream
	 * @throws IOException
	 *             The Exception is thrown if the output stream can`t be
	 *             created.
	 */
	public NetworkWriter(Socket s, BlockingQueue<NetworkMessage> writeQueue)
			throws IOException {
		this.s = s;
		this.oos = new ObjectOutputStream(s.getOutputStream());
		this.writeQueue = writeQueue;
	}

	/**
	 * The method is started if the thread is started and writes every time it
	 * is notified everything in the queue into the network stream.</br> (Don't
	 * start this directly! Use Thread.start() instead.)
	 */
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			// writes every object in the queue into the network stream
			while (!writeQueue.isEmpty()) {
				if (this.writeQueue.isEmpty())
					break;
				try {
					this.oos.writeObject(this.writeQueue.take());
					this.oos.flush();
				} catch (SocketException e) {
					Logger.getLogger(NetworkReader.class.getName())
							.log(Level.WARNING,
									"Lost connection to Enemy. Shuting down NetworkReader.");
					Thread.currentThread().interrupt();
					System.exit(1);
				} catch (IOException e) {
					Logger.getLogger(NetworkWriter.class.getName())
							.log(Level.SEVERE,
									"Unable to write the object into the network stream.",
									e);
					System.exit(1);
				} catch (InterruptedException e) {
					break;
				}
			}
			if (Thread.interrupted()) {
				break;
			}
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					Logger.getLogger(NetworkWriter.class.getName())
							.log(Level.INFO,
									"NetworkWriter got interrupted and stops operating",
									e);
					System.exit(1);
				}
			}
		}
		try {
			this.s.close();
		} catch (IOException e) {
		}
	}
}
