package server;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;



public class Chatserver extends JFrame {
	
	static JTextArea showLog;
	private JButton startServer, stopServer, createServer;
	private Server server;
	private final int port = 1024;
	
	public static void main(String[] args){
		Chatserver server = new Chatserver();
		server.setTitle("Server manger!");
		server.setSize(640, 480);
		server.CreateGUI();
		server.setVisible(true);
	}
	public void CreateGUI(){
		JScrollPane logScrollPane;
		JPanel ctrlPane;
		SpringLayout sLayout;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container window = getContentPane();
		window.setLayout(new FlowLayout());
		
		showLog = new JTextArea("Info\n", 28,40);
		showLog.setEditable(false);
		logScrollPane = new JScrollPane(showLog);
		window.add(logScrollPane);
		
		sLayout = new SpringLayout(); 
		ctrlPane = new JPanel();
		ctrlPane.setMaximumSize(new Dimension(200,450));
		ctrlPane.setPreferredSize(new Dimension(170,420));
		ctrlPane.setMinimumSize(new Dimension(150,450));
		ctrlPane.setBackground(Color.DARK_GRAY);
		ctrlPane.setLayout(sLayout);
		
		
		ActionListener createServerActionListener = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				server = new Server(showLog);
				server.start();
			}
		};
		createServer = new JButton("Create a Listener!");
		createServer.addActionListener(createServerActionListener);
		ctrlPane.add(createServer);
		sLayout.getConstraints(createServer).setX(Spring.constant(10, 10, 10));
		sLayout.getConstraints(createServer).setY(Spring.constant(10, 10, 10));
		sLayout.getConstraints(createServer).setWidth(Spring.constant(100, 100, 100));
		sLayout.getConstraints(createServer).setHeight(Spring.constant(40, 40, 40));
		
		window.add(ctrlPane);
	
	}
}
