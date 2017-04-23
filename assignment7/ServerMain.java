package assignment7;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerMain {
static HashMap<Integer, User> loginInfo=new HashMap<Integer,User>();
private static int globalUserId=0;
private static Object lockLoginInfo= new Object();//Locked whenever id or loginInfo is written to

static HashMap<Integer,Chat> conversations=new  HashMap<Integer,Chat>();
static Object lockConversations= new Object();//Locked whenever id or conversations are written to
private static int globalConversationId=0;
private static Object lockConversationId=new Object();
static int conversationId=0;



public static void main(String[] args){
	int portNumber = 1025;
	
		// listen for a new client
	   try {
	            ServerSocket serverSock = new ServerSocket(portNumber);
	            System.out.println("Running on port " + portNumber );
	            while(true){
	            	Socket clientSocket = serverSock.accept();
	            	Thread t = new Thread(new Client(clientSocket));
	            	t.start();
	            	System.out.println("got a connection");
	            	try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            }
	   }
	   catch (IOException e) {
           System.err.println("Could not listen on port " + portNumber);
           System.exit(-1);
       }

	          
}

/**
 * @param username username to check
 * @param password password to check 
 * @return true if new user is created or username password exists; false if user exists but password does not match
 * returns the userId of user
 */
public static String validLogin(String username, String password){
	synchronized(lockLoginInfo){
		for(User e: loginInfo.values()){
			if(e.getUsername().equals(username)){
				if(e.getPassword().equals(password)){
					return e.getUserId()+"";
				}else{
					return null;
				}
			}
		}
		User newUser=new User(username,password,globalUserId);
		
		loginInfo.put(globalUserId, newUser);
		globalUserId++;
		return (globalUserId-1)+"";
	}
	
	
}

public static User getUserEntryFromUsername(String username){
	synchronized(lockLoginInfo){
		for(User e: loginInfo.values()){
			if(e.getUsername().equals(username)){
				return e;
			}
		
		}
	}
	return null;
}
public static void addConversation(Chat e){
	synchronized(lockConversations){
		conversations.put(e.getConversationId(),e );
	}
}


public static int numUsers(){
	synchronized(lockLoginInfo){
		return loginInfo.size();
	}
	
}

public static User getUser(int userId){
	synchronized(loginInfo){
		return loginInfo.get(userId);
	}
}


public static String usersString(){
	String usersString;
	synchronized(lockLoginInfo){
		usersString=" "+loginInfo.size();
		for(User e:loginInfo.values()){
			usersString=usersString+" "+e.getUserId()+ " " + e.getUsername().length()+ " "+ e.getUsername();
		}
	}
	return usersString;
}

public static Chat getConversation(int conversationId){
	synchronized(ServerMain.lockConversations){
		return ServerMain.conversations.get(conversationId);
	}
}

public static int getNewConversationId(){
	int returner;
	synchronized(lockConversationId){
	returner=globalConversationId;
	
	globalConversationId++;
	}
	return returner;
}

public static HashMap<Integer,Chat> conversationsCopy(){
	HashMap<Integer,Chat> ret=new HashMap<Integer,Chat>();
	synchronized(lockConversations){
		for(Chat e:ret.values()){
			ret.put(e.getConversationId(), e);
		}
	}
	return ret;
}


}
