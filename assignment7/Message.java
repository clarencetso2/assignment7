package assignment7;

public class Message {
	
	private String timestamp;
	private int senderUserId;
	private String msg;
	
	/**
	 * Constructor
	 * @param senderUserId
	 * @param timestamp
	 * @param msg
	 */
	public Message(int senderUserId, String timestamp, String msg){
		this.senderUserId=senderUserId;
		this.timestamp=timestamp;
		this.msg=msg;
	}
	
	/**
	 * @return The sent message
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @return The timestamp of the sent message
	 */
	public String getTimestamp() {
		return timestamp;
	}
	
	/**
	 * @return The user ID of the sender
	 */
	public int getSender() {
		return senderUserId;
	}
}