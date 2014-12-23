package views.chat;

import java.util.Calendar;

import views.Chat;
import general.Enums.Channels;

/**
 * A Message used and displayed by the Chat.<br>
 * Messages can be sent on two channels: private is shown to this player only,
 * public is shown to both players.
 * 
 * @see Chat
 */
public class Message {

	/* content, time and channel of message */
	String message;
	int minute = -1;
	int hour = -1;
	Channels channel;

	/**
	 * Constructs a Message.<br>
	 * Channel will be set to PRIVATE by default.
	 * 
	 * @param message
	 * @param time
	 * @see Message
	 */
	public Message(String message, Calendar time) {
		this(message, time, Channels.PRIVATE);
	}
	
	/**Constructs a Message.<br>
	 * Time will not be set - use only when using Chat.newMessage() which adds time later on!
	 * @param message
	 * @param channel
	 */
	public Message(String message, Channels channel) {
		this.message = message;
		this.channel = channel;
	}

	/**
	 * Constructs a Message.
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
		return "<" + (hour > 9 ? Integer.toString(hour) : "0" + hour) + ":"
				+ (minute > 9 ? minute : ("0" + minute)) + "> - " + message;
	}

	/**
	 * @return channel type of this message
	 */
	public Channels getChannel() {
		return this.channel;
	}
	
	/**Used by Chat.newMessage() to set time 
	 * @param time
	 */
	public void setTime(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}
}
