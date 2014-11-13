package worthy;

import java.util.Calendar;

import enums.Enums.Channels;

/**A Message used and displayed by the Chat.<br>
 * Messages can be sent on two channels: private is shown to this player only, public is shown to both players.
 * 
 * @see Chat
 */
public class Message {
	
	/* content, time and channel of message */
	String message;
	int minute = -1;
	int hour = -1;
	Channels channel;

	/**Constructs a Message.<br>
	 * Channel will be set to PRIVATE by default.
	 * 
	 * @param message
	 * @param time
	 * @see Message
	 */
	public Message(String message, Calendar time) {
		this(message, time, Channels.PRIVATE);
	}

	/**Constructs a Message.
	 *
	 * @param message
	 * @param time
	 * @param channel
	 */
	public Message(String message, Calendar time, Channels channel) {
		this.message = message;
		this.hour = time.get(Calendar.HOUR_OF_DAY);
		this.minute = time.get(Calendar.MINUTE);
		this.channel = channel;
	}

	/**
	 * @return time and message content
	 */
	public String print() {
		return "<" + hour + ":"
				+ (minute > 9 ? minute : ("0"+minute)) + "> - " + message;
	}
	
	/**
	 * @return channel type of this message
	 */
	public Channels getChannel() {
		return this.channel;
	}
}
