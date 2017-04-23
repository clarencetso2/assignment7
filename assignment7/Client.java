package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;


public class Client implements Runnable {
	private Socket socket=null;
	private Object writeOutLock= new Object();
	private PrintWriter out ;
		public void Client(Socket socket){
			this.socket=socket;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
		
		
		
		
		

}
