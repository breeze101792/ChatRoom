package chatData;

import java.io.*;

public class Packet{
	public static enum MessageType{askLogin, loginSuccess, askRoomList, roomList, askRoomCreate, roomCreateSuccess,askPersonList, personList, joint, jointSuccess, leave, message};
	//constant
	public final static String DEFAULTROOM = "SystemMsg";
	public final static String ENCODE= "UTF-16";
	public final static int ENCODESIZE = 2;
	public final static int ENCODEHEADER = 2;
	public final static int headerLength = 4;	//= 1 + 1 + 32 + 32
/*	public final static int roomPos = 2;		//at position
	public final static int roomLength = 32;	//byte
	public final static int namePos = 34;		//at position
	public final static int nameLength = 32;	//byte
	*/
	
	//packet
	private byte[] packet;
	//packet component
	private MessageType type;
	private byte roomLength;
	private byte nameLength;
	private byte dataLength;
	private byte[] room;
	private byte[] name;
	private byte[] data;
	
	//for accept
	public Packet(byte[] rawData){
		packet = rawData;
		decode();
	}
	//for create a new packet
	public Packet(MessageType type, String sRoom, String sName){
		this.type = type;
		roomLength = (byte)(sRoom.getBytes().length * ENCODESIZE + ENCODEHEADER);
		nameLength = (byte)(sName.getBytes().length * ENCODESIZE + ENCODEHEADER);
		room = new byte[roomLength];
		name = new byte[nameLength];
		try {
			room = sRoom.getBytes(ENCODE);
			name = sName.getBytes(ENCODE);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dataLength = 0;
		encode();
	}
	public Packet(MessageType type, String sRoom, String sName, byte[] data){
		this.type = type;
		roomLength = (byte)(sRoom.getBytes().length * ENCODESIZE + ENCODEHEADER);
		nameLength = (byte)(sName.getBytes().length * ENCODESIZE + ENCODEHEADER);
		dataLength = (byte)data.length;
		room = new byte[roomLength];
		name = new byte[nameLength];
		this.data = data;
		try {
			room = sRoom.getBytes(ENCODE);
			name = sName.getBytes(ENCODE);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		encode();
	}
	//for message
	public Packet(MessageType type, String sRoom, String sName, String strData){
		this.type = type;
		roomLength = (byte)(sRoom.getBytes().length * ENCODESIZE + ENCODEHEADER);
		nameLength = (byte)(sName.getBytes().length * ENCODESIZE + ENCODEHEADER);
		dataLength = (byte)(strData.length() * ENCODESIZE + ENCODEHEADER);
		room = new byte[roomLength];
		name = new byte[nameLength];
		data = strData.getBytes();
		try {
			room = sRoom.getBytes(ENCODE);
			name = sName.getBytes(ENCODE);
			data = strData.getBytes(ENCODE);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		encode();
	}
/*	
	public void pack(){
		//TODOO askPersonList, personList, joint, jointSuccess, leave, message};
		encode();
		switch (type){
		//pack header 
		case askLogin:
		case askRoomList:
		
			room = DEFAULTROOM;
			break;
		//pack byte data default room
		case joint:
			appendByteData(byteData);
			break;
		case loginSuccess:
		case roomList:
		case askRoomCreate:
			room = DEFAULTROOM;
			appendByteData(byteData);
			break;
		case roomCreateSuccess:
			break;
		//pack byte with room
		case jointSuccess:
		case leave:
			appendByteData(byteData);
			break;
		case message:
			appendData();
			break;
		default:
			System.out.println("Error message type!(encode)\n");
		}
	}
	public void unPack(){
		decode();
	}
	*/
	//encode/decode
	/*
	 	packet[0] = (byte)(type.ordinal() & 0x000000ff);
		packet[1] = (byte)(dataLength & 0x000000ff);
		 */
	public void encode(){
		packet = new byte[4 + roomLength + nameLength + dataLength];
		packet[0] = (byte)(type.ordinal() & 0x000000ff);
		packet[1] = roomLength;
		packet[2] = nameLength;
		packet[3] = dataLength;
		
		System.arraycopy(room, 0, packet, headerLength, roomLength);
		System.arraycopy(name, 0, packet, headerLength + roomLength, nameLength);
		if (dataLength != 0) System.arraycopy(data, 0, packet, headerLength + roomLength + nameLength, dataLength);
		
		printThemAll("encode");
	}
	
	public void decode(){
		type = MessageType.values()[packet[0]];
		roomLength = packet[1];
		nameLength = packet[2];
		dataLength = packet[3];
		
		room = new byte[roomLength];
		name = new byte[nameLength];
		data = new byte[dataLength];
		
		
		System.arraycopy(packet, headerLength, room, 0, roomLength);
		System.arraycopy(packet, headerLength + roomLength, name, 0, nameLength);
		if (dataLength != 0) System.arraycopy(packet, headerLength + roomLength + nameLength, data, 0, dataLength);
		
		/*
		try {
			room = new String(packet, roomPos, roomLength - 1, ENCODE);
			name = new String(packet, namePos, nameLength - 1, ENCODE);
//			this.printThemAll("decode1");
//			printByte(packet);
			if(dataLength != 0){
				byteData = new byte[dataLength];
				System.arraycopy(packet, headerLength, byteData, 0, dataLength);
				data = new String(byteData, ENCODE);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		printThemAll("decode");
	}
/*	public void appendData(){
		try {
			System.arraycopy(data.getBytes(ENCODE), 0, packet, headerLength, data.length() * ENCODESIZE + 2);
			System.out.println(new String(packet, headerLength, data.length() * ENCODESIZE + 2, ENCODE));
			String test  =new String(packet, headerLength, data.length() * ENCODESIZE + 2, ENCODE);
			printByte(test.getBytes(ENCODE));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public void appendByteData(byte[] byteData){
		System.arraycopy(byteData, 0, packet, headerLength, byteData.length);
//		System.out.println(new String(packet, headerLength - 1, data.length()));
	}*/
	public void changeType(MessageType type){
		this.type = type;
		packet[0] = (byte)(type.ordinal() & 0x000000ff);
	}
	
	//get packet
	public byte[] getPacket(){
		return packet;
	}
	//get members
	public MessageType getType(){
		return type;
	}
	public int getDataLength(){
		return dataLength;
	}
	public String getRoom(){
		String temp = new String();
		try {
			temp = new String(room, 0, roomLength, ENCODE);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
	public String getName(){
		String temp = new String();
		try {
			temp = new String(name, 0, nameLength, ENCODE);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
	public String getStrData(){
		String temp = new String();
		try {
			temp = new String(data, 0, dataLength, ENCODE);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
/*	public byte[] getByteData(){
		return byteData;
	}*/
	
	// for debug
	public void printByte(byte[] data){
		System.out.println("Start");
		for(int ci = 0;ci < data.length;ci++){
			System.out.print(data[ci]);
			System.out.print(((ci + 1) % 5 == 0 ? "\n":"\t")); 
		}
		System.out.print("\nprint Byte end\n\n");
	}
	public void printThemAll(String srttitle){
		System.out.println("\nprintThemAll ~ " + srttitle);
		System.out.println(this.getType());
		System.out.println(this.roomLength);
		System.out.println(this.nameLength);
		System.out.println(dataLength);
		System.out.println(this.getRoom());
		System.out.println(this.getName());
		if (data != null) System.out.println(data);
		System.out.println("end printThemAll\n");
		
	}
	public void clear(){
		type = MessageType.askLogin;
		name = null;
		room = null;
	}
}
