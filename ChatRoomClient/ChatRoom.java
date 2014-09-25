package ChatRoomClient;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import chatData.Packet;
import chatData.Packet.*;
//import ChatRoomClient.Communicator.MessageType;



public class ChatRoom extends JFrame {//implements ActionListener/*, ListSelectionListener*/ {
	//static
	private static String userName;
	
	//Data member
	private Communicator communicator;
//	private Hashtable roomHtable;
	
	//GUI
	private JTextField nameSetting;
	private JList roomList;
	private DefaultListModel listModel;

	public ChatRoom(){
	}
	public static void main(String[] args) {
		
		ChatRoom chatRoom = new ChatRoom();
//		chatRoom.roomHtable = new Hashtable();
		userName = JOptionPane.showInputDialog("Setup your name!");
		chatRoom.communicator = new Communicator(chatRoom, userName);
		chatRoom.communicator.start();
		chatRoom.communicator.sent(new Packet(MessageType.askLogin, Packet.DEFAULTROOM, userName));
	}

	public void createGUI(){
		//GUI com
		JButton loginButton, createARoom;
		JPanel buttonPanel, roomListPanel;
		JMenuBar menuBar;
		JMenu fileMenu, aboutMenu;
		JMenuItem exitProgram, aboutProgram;
		//Create GUI
		Dimension wndSize = this.getToolkit().getScreenSize();
		this.setTitle("!!!RoOms!!!");
		this.setBounds(wndSize.width - 345, 0, 345, wndSize.height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container window = getContentPane();
		window.setLayout(new BorderLayout());
		
		roomListPanel = new JPanel();
		roomListPanel.setBackground(Color.white);
		buttonPanel = new JPanel();
		
		//Menu Bar
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		ActionListener FileActionListener = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.exit(DO_NOTHING_ON_CLOSE);
			}
		};
		fileMenu = new JMenu("File");
		exitProgram = new JMenuItem("Exit & Have a Good Day!");
		fileMenu.add(exitProgram);
		exitProgram.addActionListener(FileActionListener);
		
		menuBar.add(fileMenu);
		
		ActionListener AboutActionListener = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				
			}
		};
		aboutMenu = new JMenu("About");
		aboutProgram = new JMenuItem("about this program");
		aboutMenu.add(aboutProgram);
		aboutProgram.addActionListener(AboutActionListener);
		
		menuBar.add(aboutMenu);
		
		//Room List
		MouseListener mouseListener = new MouseAdapter(){
			public void mouseClicked(MouseEvent event){
				JList theList = (JList) event.getSource();
				if (event.getClickCount() == 2){
					int index = roomList.getSelectedIndex();
					String newRoom = new String();
					if (index == 0) {
						newRoom = JOptionPane.showInputDialog("Name of this room!");
						createARoom(newRoom);
					}
					else if (index > 0 && index <= roomList.getMaxSelectionIndex()){
						getIntoRoom((String)roomList.getSelectedValue());
					}
				}
			}
		};
		listModel = new DefaultListModel();
		listModel.add(0, "<New RoOm>");
		
		roomList = new JList(listModel);
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		roomList.addMouseListener(mouseListener);
		roomListPanel.add(roomList);
		
		//Button
		
		ActionListener loginActionListener = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				//TODO
//				userName = JOptionPane.showInputDialog("Setup your name!");
//				communicator.pack(MessageType.askLogin, "Login");
			}
		};
		loginButton = new JButton("Login");
		loginButton.addActionListener(loginActionListener);
		buttonPanel.add(loginButton);
		
		ActionListener createAActionListener = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				String newRoom = new String();
				newRoom = JOptionPane.showInputDialog("Name of this room!");
				createARoom(newRoom);
			}
		};
		createARoom = new JButton("Create a Room!");
		createARoom.addActionListener(createAActionListener);
		buttonPanel.add(createARoom);
		
		
		window.add("Center", roomListPanel);
		window.add("South", buttonPanel);
		
	}
	public void addToRoomList(String nameOfRoom){
		listModel.addElement(nameOfRoom);
		return;
	}
	
	
	//Class member
	public void createARoom(String nameOfRoom){
		communicator.sent(new Packet(MessageType.askRoomCreate, nameOfRoom, userName));
		/*
		if (nameOfRoom.length() > 30){
			System.out.println("error room name(L)");
			return;
		}
		byte[] roomByte = new byte[32];
		
		try {
			System.arraycopy(nameOfRoom.getBytes(Packet.ENCODE), 0, roomByte, 0, nameOfRoom.length() * Packet.ENCODESIZE + 2);
			communicator.sent(new Packet(MessageType.askRoomCreate, nameOfRoom, userName, roomByte));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println(new String(roomByte, 0, 31, Packet.ENCODE));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return;
	}
	public void getIntoRoom(String nameOfRoom){
		communicator.sent(new Packet(MessageType.joint, nameOfRoom, userName));
		/*
		try {
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	public String getUserName(){
		return userName;
	}
}
