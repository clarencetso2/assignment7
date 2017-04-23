package assignment7;

import java.util.Scanner;

public class ServerTranslator {
	public static String processInput(String input){
		if(input.substring(0,2).equals("01")){//Login request
			return requestToLogin(input); //02
		}
		else if(input.substring(0,2).equals("03")){//Initiate conversation request
			return requestToStartConversation(input); //04
		}
		else if(input.substring(0,2).equals("05")){//Get all users in network request
			 return getAllUsersRequest(input); //06
		}
		else if(input.substring(0,2).equals("07")){//Conversation update request
			return updateConversationRequest(input); //08
		}
		else if(input.substring(0,2).equals("09")){//Get conversation requests command
			//To implement 10
		}
		else if(input.substring(0,2).equals("11")){//Get conversations user is part of command
			//To implement 12
		}
		else if(input.substring(0,2).equals("13")){//Get list of online and offline users for conversations
			//To implement 14
		}
		else if(input.substring(0,3).equals("15")){//Client sent message to server
			return messageReceived(input);
		}
		
		return null;
	}
	//Will be as follows: 01 <Username length> <Password length> <username> <password>
	private static String requestToLogin(String input){
		Scanner st=new Scanner(input);
		st.nextInt();
		int usernameLen=st.nextInt();
		int passwordLen=st.nextInt();
		String restOfMsg=st.nextLine().substring(1);
		String username=restOfMsg.substring(0,usernameLen);
		String password=restOfMsg.substring(usernameLen+2);
		String response=ServerMain.validLogin(username,password);
		if(response==null){
			return "02 0 ";
		}

		return "02 1 "+response;
		
	}
	//Method to start new conversation
		// 03 <userIdSource> <chatNumberGeneratedBySource> <numClientsToConnectTo> <client1Id> [other clientId seperated by whitespace]
		private static String requestToStartConversation(String input){
			Scanner st=new Scanner(input);
			st.nextInt();
			int initiatorUserId=st.nextInt();
			int chatNumberSource=st.nextInt();
			int numClientsToConnectTo=st.nextInt();
			Chat newConvo=new Chat();
			
			newConvo.addParticipant(ServerMain.getUser(initiatorUserId));
			
			for(int i=0;i<numClientsToConnectTo;i++){
				newConvo.addParticipant(ServerMain.getUser(st.nextInt()));
				
			}

			newConvo.setUserIdActive(initiatorUserId);
			
			newConvo.setUserIdRequestSent(initiatorUserId);
			
			ServerMain.addConversation(newConvo);
			
			return "06 "+initiatorUserId+" "+chatNumberSource+" "+newConvo.getConversationId();
			
		}
		private static String getAllUsersRequest(String input){//In response to 05 command
			String resultString="04";
			Scanner st=new Scanner(input);
			st.nextInt();
			int initiatorUserId=st.nextInt();
			resultString=resultString+ " "+ initiatorUserId;
			resultString=resultString+ ServerMain.usersString();
			return resultString;
			
		}
		
		public static String updateConversationRequest(String input){//In response 07 command
			String resultString="08";
			String usersString;
			String conversationString;
			Scanner st=new Scanner(input);
			st.nextInt();
			int initiatorUserId=st.nextInt();
			int clientUsersKnown=st.nextInt();
			int clientConversationsNeedUpdate=st.nextInt();
			
		
			
			
			//Parse new conversation messages into string
			conversationString=" " +clientConversationsNeedUpdate;
			for(int i=0;i<clientConversationsNeedUpdate;i++){
				int conversationId=st.nextInt();
				int numberOfMessagesInConversationKnown=st.nextInt();
				Chat convo=ServerMain.getConversation(conversationId);

				conversationString=conversationString+" "+conversationId+convo.getNewMessages(numberOfMessagesInConversationKnown);
			}
			
			//Parse new users into string if any
			if(ServerMain.numUsers()>clientUsersKnown){
				usersString=" 1"+ ServerMain.usersString();
			}else{
				usersString=" 0";
			}
			
			resultString=resultString+usersString+conversationString;
			
			return resultString;
			
		}
		
		public static String messageReceived(String input){
			String resultString="16";

			Scanner st=new Scanner(input);
			st.nextInt();
			int initiatorUserId=st.nextInt();
			int conversationId=st.nextInt();
			int msgId=st.nextInt();
			int timestampLen=st.nextInt();
			int msgLen=st.nextInt();
			String restOfString=st.nextLine();
			String timestamp=restOfString.substring(1,1+timestampLen);
			String msg=restOfString.substring(2+timestampLen);
			Chat c=ServerMain.getConversation(conversationId);
			c.addMsgToChat(initiatorUserId, timestamp, msg);

			
			return "16 "+initiatorUserId+" "+conversationId+" "+msgId;
		}
		//Not finished below skeleton code for other methods
		private static String pollingRequest(String input){
			String resultString="08";
			Scanner st=new Scanner(input);
			st.nextInt();
			int initiatorUserId=st.nextInt();
			int clientUsersKnown=st.nextInt();
			int clientConversationsKnown=st.nextInt();
			int clientConversationsNeedUpdate=st.nextInt();
			if(clientUsersKnown!=-1 && ServerMain.numUsers()!=clientUsersKnown){
			
					resultString=resultString+" 1"+ ServerMain.usersString();

			}
			else{
				resultString=resultString+" 0";
			}
			
			
			resultString=resultString+" 0";//Functionality for clientConversationKnown not implemented
			
			//Client conversations that need update
			if(clientConversationsNeedUpdate!=-1 && clientConversationsNeedUpdate!=0){
				resultString=resultString+ " " +clientConversationsNeedUpdate;
				for(int i=0;i<clientConversationsNeedUpdate;i++){
					int conversationId=st.nextInt();
					int numberOfMessagesInConversationKnown=st.nextInt();
					Chat convo=ServerMain.getConversation(conversationId);

					resultString=resultString+" "+conversationId+convo.getNewMessages(numberOfMessagesInConversationKnown);
				}
			}else{
				resultString=resultString + " 0";
			}
			
			if(st.nextInt()==1){
				
			}
			else{
				resultString=resultString+" 0";
			}
			
			
			
			return null;
		}
}
