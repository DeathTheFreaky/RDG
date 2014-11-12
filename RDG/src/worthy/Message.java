package worthy;

import java.util.Calendar;

public class Message {

	enum Channels {
		PRIVATE, PUBLIC
	}

	String message;
	int minute = -1;
	int hour = -1;
	Channels channel;

	public Message(String message, Calendar time) {
		this(message, time, Channels.PRIVATE);
	}

	public Message(String message, Calendar time, Channels channel) {
		this.message = message;
		this.hour = time.get(Calendar.HOUR_OF_DAY);
		this.minute = time.get(Calendar.MINUTE);
		this.channel = channel;
	}

	public String print() {
		return "<" + hour + ":"
				+ (minute > 9 ? minute : ("0"+minute)) + "> - " + message;
	}
	
	public Channels getChannel() {
		return this.channel;
	}
}
