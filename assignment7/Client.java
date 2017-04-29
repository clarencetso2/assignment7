package assignment7;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;

public class Client extends Application implements Runnable {
	private static BufferedReader reader;
	private static PrintWriter writer;
	private static int clientID = -1;
	static HashMap<Integer, ChatTab> chatTabs = new HashMap<Integer, ChatTab>();
	private static TabPane tabPane;
	private static int loginFlag = 0;
	private static int mainWindow = 0;
	public static void main(String[] args) {
		try {
			new Client().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		launch(args);
	}

	public void run() {
		try {
			
			initNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	MediaPlayer mediaPlayer;
	public void start(Stage chatStage) {
			try {
				String current = System.getProperty("user.dir");
				System.out.println(current);
				URL musicFile = getClass().getResource("cock.mp3");   
				Media sound = new Media(musicFile.toString());
				 mediaPlayer = new MediaPlayer(sound);
				 
				
				chatStage.setTitle("Chat Room");
				tabPane = new TabPane();

				ChatTab initialTab = new ChatTab();
				tabPane.getTabs().add(initialTab.getTab());
				chatTabs.put(mainWindow, initialTab);
				
				chatStage.setScene(new Scene(tabPane));
				chatStage.show();
		 		mediaPlayer.play();		

			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	
	
	private void initNetworking() throws Exception {
		
		@SuppressWarnings("resource")
		Socket clientSocket = new Socket("127.0.0.1", 4000);
		InputStreamReader inputReader = new InputStreamReader(clientSocket.getInputStream());
		reader = new BufferedReader(inputReader);
		writer = new PrintWriter(clientSocket.getOutputStream());
		System.out.println("networking established");
		mainWindow = ServerMain.getMainWindow();
		
		
		Thread readerThread = new Thread(new IncomingReader(this));
		readerThread.start();
	}
	
	private void sendHandler(String text, int chatID) {
		writer.println(clientID + " " + chatID + " " + text);
		writer.flush();
		chatTabs.get(chatID).chatInput.setText("");
		chatTabs.get(chatID).chatInput.requestFocus();
	}
	
	class IncomingReader implements Runnable {
		private Client theClient;
		
		public IncomingReader(Client theClient) {
			this.theClient = theClient;
		}
		
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					final String messageText = message;
					Platform.runLater(() -> {
						String output = ClientTranslator.processInput(messageText, theClient);
						if (output != null) {
							chatTabs.get(mainWindow).chatOutput.appendText(output);
						}
					});
					//chatTabs.get(-1).chatOutput.appendText(message + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static TabPane getTabPane() {
		return tabPane;
	}
	
	public int getId() {
		return clientID;
	}
	
	public void setId(int newId) {
		clientID = newId;
	}
	
	public int getMainWindow() {
		return mainWindow;
	}
	public void runAnotherApp(Class<? extends Application> anotherAppClass) throws Exception {
	    Application app2 = anotherAppClass.newInstance(); 
	    Stage anotherStage = new Stage();
	    app2.start(anotherStage);
	}
	
	class ChatTab {
		private TextArea chatOutput;
		private TextField chatInput;
		private int chatID;
		private Tab theTab;
		
		public ChatTab() {
			this(mainWindow);
		}
		
		public ChatTab(int chatID) {
			this.chatID = chatID;
			GridPane chatPane = new GridPane();
			chatPane.setHgap(10);
			chatPane.setVgap(10);
			chatPane.setPadding(new Insets(0, 10, 0, 10));
			
			chatOutput = new TextArea();
			chatOutput.setEditable(false);
			ScrollPane output = new ScrollPane(chatOutput);
			output.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
			output.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		
			chatPane.add(output, 0, 0);
			
			
			chatInput = new TextField();
			chatInput.setEditable(true);
			
			
			ScrollPane input = new ScrollPane(chatInput);
			input.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
			input.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
			chatPane.add(input, 0, 1);
			
			Button sendButton = new Button();
			sendButton.setText("Send");
			sendButton.setOnAction(e->sendHandler(chatInput.getText(), this.chatID));
			chatPane.add(sendButton, 0, 2);
			
			Button nightVision = new Button();
			nightVision.setText("Night");
			nightVision.setOnAction(e-> {setUserAgentStylesheet(STYLESHEET_CASPIAN);});
			chatPane.add(nightVision, 1, 2);
			
			Button dayVision = new Button();
			dayVision.setText("Day");
			dayVision.setOnAction(e-> {setUserAgentStylesheet(STYLESHEET_MODENA);});
			chatPane.add(dayVision, 2, 2);
			
			Button game = new Button();
			game.setText("I'm Bored");
			
			game.setOnAction(e-> {
				
				try {
					runAnotherApp(Snake.class);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				
			});
			chatPane.add(game, 3, 2);
			
			theTab = new Tab();
			theTab.setContent(chatPane);
		}
		
		public Tab getTab() {
			return theTab;
		}
		
		public TextArea getChatOutput() {
			return chatOutput;
		}
	}
		
}