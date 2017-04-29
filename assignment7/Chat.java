package assignment7;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat {
	
	private static final int maxAcceptableDelay=5000;
	
	private HashMap<Integer,User> participants=new HashMap<Integer,User>();
	private Object lockParticipants=new Object();
	
	private HashMap<Integer,Boolean> activeParticipants=new HashMap<Integer,Boolean>();
	private HashMap<Integer,Long> lastUpdated=new HashMap<Integer,Long>();
	private Object lockActiveParticipants=new Object(); //Lock applies to lastUpdated as well
	
	private HashMap<Integer,Boolean> requestSent=new HashMap<Integer,Boolean>();
	private Object lockRequestSent=new Object();
	
	private ArrayList<Message> messages=new ArrayList<Message>();
	private Object lockMessages=new Object();
	
	private int conversationId;
	
	/**
	 * Constructor for a new conversation
	 */
	public void Conversation(){

		conversationId = ServerMain.getNewConversationId();
		
	}
	
	/**
	 * Add a participant to the conversation
	 * @param e is the user to be added to the conversation
	 */
	public void addParticipant(User e){
		synchronized(lockParticipants){
			participants.put(e.getUserId(),e);
		}
		
		synchronized(lockActiveParticipants){
			activeParticipants.put(e.getUserId(), false);
			lastUpdated.put(e.getUserId(), null);
		}
		
		synchronized(lockRequestSent){
			requestSent.put(e.getUserId(), false);
		}
	}
	
	/**
	 * 
	 * @return The ID for the conversation
	 */
	public int getConversationId(){
		return conversationId;
	}
	
	/**
	 * Set a user as active in the conversation
	 * @param initiatorUserId is the user ID of the user to be set active
	 */
	public void setUserIdActive(int initiatorUserId){
		synchronized(lockActiveParticipants){
			lastUpdated.put(initiatorUserId,System.currentTimeMillis());
			activeParticipants.put(initiatorUserId,true);
		}
	}
	
	/**
	 * Set whether a request has been sent to a certain user
	 * @param initiatorUserId is the user ID of the user the request was sent to
	 */
	public void setUserIdRequestSent(int initiatorUserId){
		synchronized(lockRequestSent){
			requestSent.put(initiatorUserId, true);
		}
	}
	
	/**
	 * 
	 * @return A string of active and inactive participants in the conversation
	 */
	public String getActiveInactiveUsers(){
		String returnStr="";
	
		synchronized(lockActiveParticipants){
			returnStr = returnStr + " " + activeParticipants.size();
			for(Integer id:activeParticipants.keySet()){
				returnStr = returnStr + " " + id + " " + convertBooleanToInt(activeParticipants.get(id));
				/*
				if (activeParticipants.get(id)) {
					returnStr = returnStr + " " + id + " active";
				}
				else {
					returnStr = returnStr + " " + id + " inactive";
				}
				*/
			}
		}
		return returnStr;
	}
	
	/**
	 * Converts booleans to integers
	 * @param s is the boolean to be converted
	 * @return 1 if true, 0 if false
	 */
	public int convertBooleanToInt(boolean s){
		if(s){
			return 1;
		}
		return 0;
	}
	
	/**
	 * Adds a new message to the chat
	 * @param userId is the user ID of the user that sent the message
	 * @param timestamp is the time the message was sent
	 * @param msg is the message
	 */
	public void addMsgToChat(int userId, String timestamp, String msg){
		/*
		String returnStr = "";
		*/
		Message a = new Message(userId,timestamp,msg);
		
		synchronized(lockMessages){
			messages.add(a);
		}
		
		synchronized(lockActiveParticipants){
			lastUpdated.put(userId, System.currentTimeMillis());
		}
		
		/*
		for(Integer id:activeParticipants.keySet()){
			returnStr=returnStr+" "+id+" "+activeParticipants.get(id);
		}
		*/
	}
	
	/**
	 * Returns the messages in the conversation that have not yet been displayed
	 * @param messagesClientHas is the number of messages the client has received already
	 * @return The messages that the client has not yet received
	 */
	public String getNewMessages(int messagesClientHas){
		String returnStr="";
		
		synchronized(lockMessages){
			if(messagesClientHas < messages.size()){
				returnStr = returnStr + " " + (messages.size() - messagesClientHas);
				for(int i = messagesClientHas; i < messages.size(); i++){
					int sender = messages.get(i).getSender();
					String timestamp = messages.get(i).getTimestamp();
					String msg = messages.get(i).getMsg();
					returnStr = returnStr + " " + sender + " " + timestamp.length() + " " + msg.length() + " " + timestamp + " " + msg; 
					returnStr = returnStr + " " + sender + " " + timestamp.length() + " " + msg.length() + " " + msg; 
				}
			}
			else{
				returnStr = returnStr + " 0";
			}
		}
		return returnStr;
	}
}