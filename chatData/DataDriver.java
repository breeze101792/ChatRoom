package chatData;
import java.io.UnsupportedEncodingException;

import chatData.Packet.*;

public class DataDriver {

	
	public static void main(String[] args){
		
		
		byte[] x=new byte[100];
		try {
			System.arraycopy((new String("x 2").getBytes("UTF-16")), 0, x, 0, 8);
			Packet packet = new Packet(MessageType.askRoomCreate, "Room", "Shaowu", x);
			Packet accept = new Packet(packet.getPacket());
			System.out.println(accept.getName().equals("Shaowu"));
			System.out.println(("shaowu".equals("shao" + "wu")));
			
			

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("\n");
		

		
	}
}
