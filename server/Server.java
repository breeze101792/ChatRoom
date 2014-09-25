package server;
import java.net.*;
import java.util.Hashtable;
import java.io.*;

import javax.swing.*;

public class Server extends Thread{
	//final
	public final static int port = 1024;
	private static boolean active = true;
//	private static Hashtable<String, Hashtable<String, DataOutputStream>> roomsHtable;
	//data member
//	private String userName;
	private JTextArea showLog;
	
	private ServerSocket sSocket;
	private Socket clientSocket;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	
	//constructor
	public Server(JTextArea showLog){
		this.showLog = showLog;
	}
	
	//thread run
	@SuppressWarnings("deprecation")
	public void run(){
		try {
			showLog.append("Server has been started!\n");
			byte[] rawData = new byte[1000];
			sSocket = new ServerSocket(port);
			final String SystemSpace = "System";
//			roomsHtable = new Hashtable();
			
			while(active){
				clientSocket = sSocket.accept();
				showLog.append("Address: " + clientSocket.getInetAddress().toString() + '\n');
				showLog.append("Port: " + Integer.toString(clientSocket.getPort()) + '\n');
				
				dataOut = new DataOutputStream(clientSocket.getOutputStream());
				dataIn = new DataInputStream(clientSocket.getInputStream());
				
				Thread thread = new Thread(new ServerThread(clientSocket, showLog, dataIn));
				thread.start();
			}
		}
		catch (IOException ecp){
			ecp.printStackTrace();
		}
	}
}
