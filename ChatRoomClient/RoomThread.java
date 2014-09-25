package ChatRoomClient;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


import javax.swing.*;

import chatData.Packet;
import chatData.Packet.*;

public class RoomThread extends JFrame implements Runnable{
//	static private final String serverAddress = "127.0.0.1";
	static private final int port = 1024;
	//Data member
	
	private String roomName;
	private String userName;
	private JList memberList;
	private DefaultListModel listModel;
	private JTextArea showRoomMsg, msgInput;
	
/*	private Socket clientSocket;
	private DataOutputStream dataOut;
	private DataInputStream dataIn;
	*/
	
	private Communicator communicator;
	
	public RoomThread(String roomName, String name, Communicator communicator){
		this.communicator = communicator;
		this.setName(name);
		this.userName = name;
		this.roomName = roomName;
	}
	
	public void run(){
		this.setTitle(roomName + " ~ " + userName);
		CreateGUI();
		setVisible(true);
		communicator.sent(new Packet(MessageType.askPersonList, roomName, userName));
	}
	
	//GUI Member
	public void CreateGUI(){
		SpringLayout layout = new SpringLayout();
		JScrollPane RoomMsgScrollPane, msgInputScrollPane;
		JButton upload, sent, smail;
		Dimension wndSize = new Dimension(640, 480); 
		Container window = getContentPane();
		
		setSize(wndSize);
		setResizable(false);
		window.setLayout(layout);
		
		
		
		showRoomMsg = new JTextArea();
		showRoomMsg.setEditable(false);
		RoomMsgScrollPane = new JScrollPane(showRoomMsg);
		window.add(RoomMsgScrollPane);
		layout.getConstraints(RoomMsgScrollPane).setX(Spring.constant(10, 10, 10));
		layout.getConstraints(RoomMsgScrollPane).setY(Spring.constant(10, 10, 10));
		layout.getConstraints(RoomMsgScrollPane).setWidth(Spring.constant(400, 400, 400));
		layout.getConstraints(RoomMsgScrollPane).setHeight(Spring.constant(260, 260, 260));
		
		msgInput = new JTextArea();
		msgInput.setEditable(true);
		msgInputScrollPane = new JScrollPane(msgInput);
		window.add(msgInputScrollPane);
		layout.getConstraints(msgInputScrollPane).setX(Spring.constant(10, 10, 10));
		layout.getConstraints(msgInputScrollPane).setY(Spring.constant(330, 330, 330));
		layout.getConstraints(msgInputScrollPane).setWidth(Spring.constant(300, 300, 300));
		layout.getConstraints(msgInputScrollPane).setHeight(Spring.constant(100, 100, 100));
		
		//sent button
		ActionListener sentActionListener = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				//TODO ADD SENT MSG!! 
				if (msgInput.getText() != null){
					communicator.sent(new Packet(MessageType.message, roomName, userName, msgInput.getText()));
					msgInput.setText("");
				}
			}
		};
		sent = new JButton("Sent");
		sent.addActionListener(sentActionListener);
		window.add(sent);
		layout.getConstraints(sent).setX(Spring.constant(320, 320, 320));
		layout.getConstraints(sent).setY(Spring.constant(350, 350, 350));
		layout.getConstraints(sent).setWidth(Spring.constant(80, 80, 80));
		layout.getConstraints(sent).setHeight(Spring.constant(40, 40, 40));
		
		ActionListener uploadActionListener = new ActionListener(){
			public void actionPerformed(ActionEvent event){
			}
		};
		upload = new JButton("Upload");
		upload.addActionListener(uploadActionListener);
		window.add(upload);
		layout.getConstraints(upload).setX(Spring.constant(10, 10, 10));
		layout.getConstraints(upload).setY(Spring.constant(280, 280, 280));
		layout.getConstraints(upload).setWidth(Spring.constant(100, 100, 100));
		layout.getConstraints(upload).setHeight(Spring.constant(40, 40, 40));
		
		ActionListener smailActionListener = new ActionListener(){
			public void actionPerformed(ActionEvent event){
			}
		};
		smail = new JButton("Smail");
		smail.addActionListener(smailActionListener);
		window.add(smail);
		layout.getConstraints(smail).setX(Spring.constant(130, 130, 130));
		layout.getConstraints(smail).setY(Spring.constant(280, 280, 280));
		layout.getConstraints(smail).setWidth(Spring.constant(100, 100, 100));
		layout.getConstraints(smail).setHeight(Spring.constant(40, 40, 40));
		
		MouseListener mouseListener = new MouseAdapter(){
			public void mouseClicked(MouseEvent event){
				JList theList = (JList) event.getSource();
				if (event.getClickCount() == 2){
					int index = memberList.getSelectedIndex();
					showRoomMsg.append("Under develope!!\n");
				}
			}
		};
		listModel = new DefaultListModel();
//		listModel.add(0, "Shaowu Tseng");
		memberList = new JList(listModel);
		memberList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		memberList.addMouseListener(mouseListener);
		window.add(memberList);
		layout.getConstraints(memberList).setX(Spring.constant(420, 420, 420));
		layout.getConstraints(memberList).setY(Spring.constant(10, 10, 10));
		layout.getConstraints(memberList).setWidth(Spring.constant(200, 200, 200));
		layout.getConstraints(memberList).setHeight(Spring.constant(420, 420, 420));
	}
	public void addRoomMsg(String name,String msg){
		showRoomMsg.append(name + " :" + msg + "\n");
		return;
	}
	public void addPeopleToList(String name){
		listModel.addElement(name);
		return;
	}
	
}