package ChatRoomClient;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import chatData.Packet;
import chatData.Packet.*;


public class Communicator extends Thread{
	static{
		roomHtable = new Hashtable<String, RoomThread>();
	}
	public static Hashtable<String, RoomThread> roomHtable;
	//final
	public static final int PORT = 1024;
	public static final String SERVERIP = "127.0.0.1";
//	public static final String SERVERIP = "10.5.6.30";
//	public static final String SERVERIP = "192.168.1.60";
	
	
	private ChatRoom chatRoom;
	private String userName;
	//data member
	private Socket clientSocket;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	

	
	public Communicator(ChatRoom chatRoom, String userName){
		try {
			clientSocket = new Socket(SERVERIP, PORT);
			dataIn = new DataInputStream(clientSocket.getInputStream());
			dataOut = new DataOutputStream(clientSocket.getOutputStream());
			
			this.chatRoom = chatRoom;
			this.userName = new String(userName);
		} catch (IOException ioerr) {
			ioerr.printStackTrace();
		}
	}
	//Thread start
	public void run(){
		try{
			System.out.println("Communicator in");
			String roomName = new String();
			byte[] rawData = new byte[1000];
			Packet packet;
			
			//TODO FLAG
			boolean flag = true;
			
			while(flag){
				System.out.println("while in");
				
				//read the message
				dataIn.read(rawData);
				packet = new Packet(rawData);
				process(packet);
				if(clientSocket.isClosed()) flag = false;
			}
		}
		catch (IOException ecp){
			ecp.printStackTrace();
		}
		finally{
			try {
				if (!clientSocket.isClosed()) clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void process(Packet packet){
//, ,askPersonList, , , , leave, message
		switch (packet.getType()){
		case loginSuccess:
			chatRoom.createGUI();
			chatRoom.setVisible(true);
			sent(new Packet(MessageType.askRoomList, Packet.DEFAULTROOM, userName));
			break;
		case roomList:
			chatRoom.addToRoomList(packet.getRoom());
/*			
			try {
				byte[] temp = packet.getByteData();
				String str;
				//TODO ?
				for(int ci = 0; ci < packet.getDataLength() / 32;ci++){
					str = new String(temp,  32 * ci, 32 * ci + 32 - 1, Packet.ENCODE);
					
					chatRoom.addToRoomList(new String(str.getBytes(Packet.ENCODE), 0, str.length() * 2,Packet.ENCODE));
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
				
			break;
		case roomCreateSuccess:
			chatRoom.addToRoomList(packet.getRoom());
			/*
			try {
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			break;
		case jointSuccess:
			RoomThread room = new RoomThread(packet.getRoom(), packet.getName(), this);
			roomHtable.put(packet.getRoom(), room);
			(new Thread(room)).start();
			break;
		case personList:
			roomHtable.get(packet.getRoom()).addPeopleToList(packet.getName());
			break;
		case message:
			roomHtable.get(packet.getRoom()).addRoomMsg(packet.getName(), packet.getStrData());
			
			break;
		default:
			System.out.println("error code!!");
		}
	}

	public void sent(Packet packet){
		try {
			System.out.println("sent\n");
			dataOut.write(packet.getPacket());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
}
