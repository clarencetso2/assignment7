package assignment7;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat {

	private static final int maxAcceptableDelay=4000;
	
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
	
	
	
	
	
	public void Conversation(){
		conversationId=ServerMain.getNewConversationId();
		synchronized(ServerMain.lockConversations){
			conversationId=MainServer.conversationId;
			MainServer.conversationId++;
		}
		
	}
	
	
	public void addParticipant(User e){
		synchronized(lockParticipants){
		participants.put(e.getUserId(),e);
		}
		synchronized(lockActiveParticipants){
		activeParticipants.put(e.getUserId(),false);
		lastUpdated.put(e.getUserId(), null);
		}
		synchronized(lockRequestSent){
			requestSent.put(e.getUserId(), false);
		}
	}
	public int getConversationId(){
		return conversationId;
	}
	
	//Phase 1 after making connection 
	public void setUserIdActive(int initiatorUserId){
		synchronized(lockActiveParticipants){
			lastUpdated.put(initiatorUserId,System.currentTimeMillis());
			activeParticipants.put(initiatorUserId,true);
		}

	}
	
	public void setUserIdRequestSent(int initiatorUserId){
		synchronized(lockRequestSent){
			requestSent.put(initiatorUserId, true);
		}
	}
	
	public String getNewMessages(int messagesClientHas){
		String returnStr="";
		synchronized(lockMessages){
			if(messagesClientHas<messages.size()){
				returnStr=returnStr+" "+(messages.size()-messagesClientHas);
				for(int i=messagesClientHas;i<messages.size();i++){
					int sender=messages.get(i).getSender();
					String timestamp=messages.get(i).getTimestamp();
					String msg=messages.get(i).getMsg();
					returnStr=returnStr+" "+sender+" "+timestamp.length()+" "+msg.length()+" "+timestamp+" "+msg; 
					returnStr=returnStr+" "+sender+" "+timestamp.length()+" "+msg.length()+" "+msg; 
				}
			}
			else{
				returnStr=returnStr+" 0";
			}
		}
		return returnStr;
	}
	public String getActiveInactiveUsers(){
		String returnStr="";
	
		synchronized(lockActiveParticipants){
			returnStr=returnStr+" "+activeParticipants.size();
			for(Integer id:activeParticipants.keySet()){
				returnStr=returnStr+" "+id+" "+convertBooleanToInt(activeParticipants.get(id));
			}
		}
		return returnStr;
	}
	
	public void addMsgToConversation(int userId, String timestamp, String msg){
		String returnStr="";
		Message a=new Message(userId,timestamp,msg);
		synchronized(lockMessages){
			messages.add(a);
		}
		synchronized(lockActiveParticipants){
			lastUpdated.put(userId, System.currentTimeMillis());
		}
		for(Integer id:activeParticipants.keySet()){
				returnStr=returnStr+" "+id+" "+activeParticipants.get(id);
			}
		}
		
	
	public int convertBooleanToInt(boolean s){
		if(s){
			return 1;
		}
		return 0;
	}
	public void setFlagForActiveConversation(){
		
	}
	
	
}
