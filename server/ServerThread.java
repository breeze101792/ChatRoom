package server;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JTextArea;
import chatData.Packet;
import chatData.Packet.*;

public class ServerThread implements Runnable{
	static{
		roomsHtable = new Hashtable<String, Hashtable<String, DataOutputStream>>();
		pHtable = new Hashtable<String, DataOutputStream>();
	}
	//data space
	private static Hashtable<String, Hashtable<String, DataOutputStream>> roomsHtable;
	private static Hashtable<String, DataOutputStream> pHtable;
	private static int roomCounter = 0;
	
	private LinkedList<String> inRooms;
	//no need to change
	private Socket clientSocket;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
//	private String userName;
	
	//show log
	private JTextArea showLog;
	//Hashtable<String, Hashtable<String, DataOutputStream>> roomsHtable, 
	public ServerThread(Socket clientSocket, JTextArea showLog, DataInputStream dataIn){
		this.clientSocket = clientSocket;
		this.showLog = showLog;
		this.dataIn = dataIn;
//		this.roomsHtable = roomsHtable;
		
	}
	
	public void run(){
		try{
			showLog.append("The " + clientSocket.getInetAddress() +  " thread has been build!\n");
			byte[] rawData = new byte[1000];
			dataOut = new DataOutputStream(clientSocket.getOutputStream());
			Packet packet;
			
			
			while(true){
				if (!clientSocket.isClosed()){
					
					
//					this.
				}
				
				//read the message
				showLog.append("new accept\n");
				dataIn.read(rawData);
				packet = new Packet(rawData);
				process(packet);
				showLog.append("end\n\n");
				
				showLog.append("Message: " + new String(rawData, 0, 100) + "\n\n\n");
				
			}
		}
		catch (IOException ecp){
			ecp.printStackTrace();
		}
	}
	public void process(Packet packet){
//, , , , , ,, personList, , , leave, 
		switch (packet.getType()){
		case askLogin:
			pHtable.put(packet.getName(), dataOut);
			packet.changeType(MessageType.loginSuccess);
			sentToPerson(packet);
			break;
		case askRoomList:
			if (roomCounter != 0) sentRoomList(packet);
			/*
			if (roomCounter != 0){
				packet = new Packet(MessageType.roomList, packet.getRoom(), packet.getName(), getAllRoom());
				sentToPerson(packet);
			}*/
			break;
		case askRoomCreate:
			System.out.println("askroomcreate");
			
			if (!roomsHtable.containsKey(packet.getRoom())){
				/*
				System.out.println(packet.getRoom());
				temp = new String(packet.getRoom());
				System.out.println(temp);*/
				
				roomCounter++;
				roomsHtable.put(new String(packet.getRoom()), new Hashtable<String, DataOutputStream>());
				packet.changeType(MessageType.roomCreateSuccess);
				sentToAllPerson(packet);
			}
			
			break;
		case joint:
			/*
			System.out.println("Joint~~");
			System.out.println(packet.getRoom());
			System.out.println(temp.equals("test"));
			System.out.println(packet.getRoom().equals(temp));
			*/
			
			if (roomsHtable.containsKey(packet.getRoom())){
				System.out.println(packet.getName());
				roomsHtable.get(packet.getRoom()).put(packet.getName(), dataOut);
				packet.changeType(MessageType.jointSuccess);
				sentToPerson(packet);
			}
			
			break;
		case askPersonList:
			sentPersonList(packet);
			break;
		case message:
			sentToRoom(packet);
			break;
		case leave:
			
			
			break;
		default:
			this.showLog.append("process error type");
		}
	}
	
	
	
	public synchronized void sentToRoom(Packet packet){
		try {
			showLog.append("Sent to room\n");
			DataOutputStream dataOut;
			for (Enumeration<DataOutputStream> person = roomsHtable.get(packet.getRoom()).elements();person.hasMoreElements();){
				dataOut = (DataOutputStream)person.nextElement();
				dataOut.write(packet.getPacket());
			}
		}
		catch (IOException ecp){
			ecp.printStackTrace();
		}
	}
	public synchronized void sentToAllPerson(Packet packet){
		try {
			showLog.append("Sent to all persion\n");
			DataOutputStream dataOut;
			for (Enumeration<DataOutputStream> person = pHtable.elements();person.hasMoreElements();){
				dataOut = (DataOutputStream)person.nextElement();
				dataOut.write(packet.getPacket());
			}
		}
		catch (IOException ecp){
			ecp.printStackTrace();
		}
	}
	public synchronized void sentToPerson(Packet packet){
		try {
			showLog.append("Sent to persion\n");
			DataOutputStream dataOut;
			dataOut = (DataOutputStream)pHtable.get(packet.getName());
			dataOut.write(packet.getPacket());

		}
		catch (IOException ecp){
			ecp.printStackTrace();
		}
	}
	
	public void sentRoomList(Packet packet){
		String tempStr;
		try {
			for (Enumeration<String> room = roomsHtable.keys();room.hasMoreElements();){
				System.out.println("get all room!!");
				
				tempStr = room.nextElement();
				dataOut.write(new Packet(MessageType.roomList, tempStr, packet.getName()).getPacket());
				
//				System.out.println(tempStr);
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}
	public void sentPersonList(Packet packet){
		String tempStr;
		
		try {
			for (Enumeration<String> person = roomsHtable.get(packet.getRoom()).keys();person.hasMoreElements();){
				System.out.println("get all person!!");
				
				tempStr = person.nextElement();
				if(tempStr.equals(packet.getName())) sentToRoom(new Packet(MessageType.personList, packet.getRoom(), tempStr));
				else dataOut.write(new Packet(MessageType.personList, packet.getRoom(), tempStr).getPacket());
				
				System.out.println(tempStr);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ;
	}
	public void showRoom(String room){
		System.out.println("\n\nSent to show rooms/*\n");
		for (Enumeration<String> name = roomsHtable.keys();name.hasMoreElements();){
			name.nextElement();
			System.out.println(name.equals("test"));
		}
		
	}
	public void leave(){
		
	}
	
}