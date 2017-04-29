package assignment7;

import java.util.Scanner;

public class ServerTranslator {
	/**
	 * Processes a string input by the user
	 * @param input is the string to be processed
	 * @return The processed string
	 */
	public static String processInput(String input) {
		String[] inputWords = input.split(" ");
		if(inputWords[2].equals("01")){//Login request
			return requestToLogin(input); //02
		}
		else if(inputWords[0].equals("-1")) {
			return "Please login first";
		}
		else if(inputWords[2].equals("03")){//Initiate conversation request
			return requestToStartConversation(input); //04
		}
		else if(inputWords[2].equals("15")){//Client sent message to server
			return messageReceived(input);
		}
		
		return null;
	}
	
	/**
	 * Parses a string with a login request
	 * @param input is the login input string
	 * @return A string that indicates if the login was successful
	 */
	private static String requestToLogin(String input){
		String[] words = input.split(" ");
		String username = words[3];
		String password = words[4];
		
		if (!words[0].equals("-1")) {
			return "You are already logged in! Your User ID is: " + words[0];
		}
		/*
		Scanner st = new Scanner(input);
		st.nextInt();
		int usernameLen = st.nextInt();
		int passwordLen = st.nextInt();
		String restOfMsg = st.nextLine().substring(1);
		String username = restOfMsg.substring(0,usernameLen);
		String password = restOfMsg.substring(usernameLen+2);
		*/
		
		String response = ServerMain.validLogin(username,password);
		
		if(response == null){
			return "02 0 " + "0 " + words[1];
			//return "A user with that username already exists or an incorrect password was entered. Please try again.";
		}

		return "02 1 " + response + " " + words[1];
		//return "Login successful. Your User ID is: " + response;
	}
	
	/**
	 * Parses a string with a conversation request
	 * @param input is the request conversation input string
	 * @return The conversation ID
	 */
	private static String requestToStartConversation(String input){
		String[] words = input.split(" ");
		Integer initiatorUserId = Integer.parseInt(words[0]);
		int numTotalClients = words.length;
		
		/*
		Scanner st = new Scanner(input);
		st.nextInt();
		int initiatorUserId = st.nextInt();
		int chatNumberSource = st.nextInt();
		int numClientsToConnectTo = st.nextInt();
		*/
		
		Chat newConvo = new Chat();
		newConvo.addParticipant(ServerMain.getUser(initiatorUserId));
		String returnString = "04 " + initiatorUserId;
		
		for(int i = 3; i < numTotalClients; i++){
			newConvo.addParticipant(ServerMain.getUser(Integer.parseInt(words[i])));
			returnString = returnString + " " + words[i];
		}

		newConvo.setUserIdActive(initiatorUserId);
		newConvo.setUserIdRequestSent(initiatorUserId);
		ServerMain.addConversation(newConvo);
		returnString = returnString + " " + newConvo.getConversationId();
		return returnString;
	}
	
	public static String messageReceived(String input){
		
		String[] inputWords = input.split(" ");
		String resultString = "16 " + inputWords[1] + " ";
		
		for(int i = 3; i < inputWords.length; i++) {
			resultString = resultString + inputWords[i] + " ";
		}
		
		return resultString;
		/*
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
		*/
	}
}