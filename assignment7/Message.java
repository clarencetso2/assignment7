package assignment7;

public class Message {
	
	private String timestamp;
	private int senderUserId;
	private String msg;
	public Message(int senderUserId, String timestamp, String msg){
		this.senderUserId=senderUserId;
		this.timestamp=timestamp;
		this.msg=msg;
	}
	
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	
	/**
	 * @return the sender of message
	 */
	public int getSender() {
		return senderUserId;
	}

}
