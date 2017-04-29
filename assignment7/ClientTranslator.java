package assignment7;

import java.net.URL;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class ClientTranslator {
	static MediaPlayer mediaPlayer;
	
	
	public static String processInput(String input, Client theClient) {
		String[] inputWords = input.split(" ");
		if(inputWords[0].equals("02")){//Login request
			return requestToLogin(input, theClient); //02
		}
		else if(inputWords[0].equals("04")){//Initiate conversation request
			return requestToStartConversation(input, theClient); //04
		}
		else if(inputWords[0].equals("16")){//Client sent message to server
			String current = System.getProperty("user.dir");
			System.out.println(current);
			
			URL musicFile = ClientTranslator.class.getResource("received.mp3");   
			Media sound = new Media(musicFile.toString());
			 mediaPlayer = new MediaPlayer(sound);
     		mediaPlayer.play();

			 
			return messageReceived(input, theClient);
		}
		
		return null;
	}
	
	private static String requestToLogin(String input, Client theClient) {
		String[] inputWords = input.split(" ");
		/*
		if(theClient.getMainWindow() == Integer.parseInt((inputWords[3]))) {
			theClient.setId(Integer.parseInt(inputWords[2]));
			return "login successful" + "\n";
		}
		*/
		if(theClient.getId() == -1) {
			theClient.setId(Integer.parseInt(inputWords[2]));
			return "login successful" + "\n";
		}
		return null;
	}
	
	private static String requestToStartConversation(String input, Client theClient) {
		String[] inputWords = input.split(" ");
		for (int i = 1; i < inputWords.length - 1; i++) {
			if (theClient.getId() == Integer.parseInt(inputWords[i])) {
				createTab(Integer.parseInt(inputWords[inputWords.length-1]), theClient);
				return "new window created" + "\n";
			}
		}
		return null;
	}
	
	private static void createTab(int conversationId, Client theClient) {
		Client.ChatTab newTab = theClient.new ChatTab(conversationId);
		theClient.chatTabs.put(conversationId, newTab);
		theClient.getTabPane().getTabs().add(newTab.getTab());
	}
	
	private static String messageReceived(String input, Client theClient) {
		
		 
		String[] inputWords = input.split(" ");
		if (theClient.chatTabs.containsKey(Integer.parseInt(inputWords[1]))) {
			for (int i = 2; i < inputWords.length; i++) {
				theClient.chatTabs.get(Integer.parseInt(inputWords[1])).getChatOutput().appendText(inputWords[i] + " ");
			}
			return "message received" + "\n";
		}
		return null;
	}
}



