package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerMain {
	private ArrayList<PrintWriter> clientStreams;
	
	static HashMap<Integer, User> loginInfo = new HashMap<Integer,User>();
	private static int globalUserId = 0;
	private static Object lockLoginInfo = new Object(); //Locked whenever id or loginInfo is written to
	
	static HashMap<Integer,Chat> conversations=new  HashMap<Integer,Chat>();
	static Object lockConversations= new Object(); //Locked whenever id or conversations are written to
	private static int globalConversationId=0;
	private static Object lockConversationId=new Object();
	private static int mainWindow = 0;
	
	public static void main(String[] args) {
		try {
			new ServerMain().networking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void networking() throws Exception {
		clientStreams = new ArrayList<PrintWriter>();
		ServerSocket serverSocket = new ServerSocket(4000);
		System.out.println("Started on port 4000");
		while (true) {
			Socket clientSocket = serverSocket.accept();
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			clientStreams.add(writer);
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			System.out.println("Got a connection");
		}
	}
	
	class ClientHandler implements Runnable {
		private BufferedReader inputReader;
		
		public ClientHandler(Socket clientSocket) throws IOException {
			Socket client = clientSocket;
			inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}
		
		public void run() {
			String message;
			try {
				while ((message = inputReader.readLine()) != null) {
					System.out.println("read " + message);
					String outboundMessage = ServerTranslator.processInput(message);
					notifyClients(outboundMessage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void notifyClients(String message) {
		for (PrintWriter writer : clientStreams) {
			writer.println(message);
			writer.flush();
		}
	}
	
	public static String validLogin(String username, String password) {
		synchronized(lockLoginInfo) {
			for(User e: loginInfo.values()) {
				if(e.getUsername().equals(username)) {
					if(e.getPassword().equals(password)) {
						return e.getUserId() + "";
					}
					else {
						return null;
					}
				}
			}
			User newUser = new User(username,password,globalUserId);
			loginInfo.put(globalUserId, newUser);
			globalUserId++;
			return (globalUserId-1) + "";
		}
	}
	
	public static User getUser(int userId){
		synchronized(loginInfo){
			return loginInfo.get(userId);
		}
	}
	
	public static void addConversation(Chat e){
		synchronized(lockConversations){
			conversations.put(e.getConversationId(),e );
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
	
	public static int getMainWindow() {
		mainWindow = mainWindow - 1;
		return mainWindow;
	}
	
}