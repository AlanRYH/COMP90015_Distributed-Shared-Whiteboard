//Student name: RAO YUNHUI   Student number:1316834
import java.awt.EventQueue;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.JFileChooser;

import java.awt.BasicStroke;
import java.awt.Canvas;
import javax.swing.JFormattedTextField;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Stroke;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.Component;
import javax.swing.JTextArea;
import java.awt.Graphics2D;


public class Board_GUI {
	private Boolean isServer;
	private  Server server;
	private Client client;
	private JPanel userList;//panel for user list showing
	private JPanel whiteBoard;//the canvas
	private DrawMethod dm;
	private final String default_location = "canvas.txt";
	private JTextArea chatBox_text;//text area for users' chat history(this should be used)
	private JTextArea userlist_text;//text area for users' list(use this)
	private JPanel chatBox;//panel for users' chat history
	private JTextField typeBox;//for users' input area
	private userManage newPage;//user manager page
	
	private JFrame frmWhiteBoard;//the whole GUI
	//button for change colors
	private JButton colorButton_1;
	private JButton colorButton_2;
	private JButton colorButton_3;
	private JButton colorButton_4;
	private JButton colorButton_5;
	private JButton colorButton_6;
	private JButton colorButton_7;
	private JButton colorButton_8;
	private JButton colorButton_9;
	private JButton colorButton_10;
	private JButton colorButton_11;
	private JButton colorButton_12;
	private JButton colorButton_13;
	private JButton colorButton_14;
	private JButton colorButton_15;
	private JButton colorButton_16;
	
	private JPanel toolBar;
	
	private JLabel userIdLabel;
	private JLabel usernameLabel;
	private JLabel UserListLabel;
	private JLabel chatBoxLabel;
	private JButton sendButton;
	private JLabel typeLabel;
	
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem closeMenuItem;
	private JMenuItem userManageMenuItem;

	

	/**
	 * Create the application.
	 */
	public Board_GUI() {
		initialize();
		this.frmWhiteBoard.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWhiteBoard = new JFrame();
		frmWhiteBoard.setTitle("White Board");
		frmWhiteBoard.setBounds(new Rectangle(0, 0, 750, 1000));
		frmWhiteBoard.getContentPane().setSize(new Dimension(750, 1000));
		frmWhiteBoard.getContentPane().setBounds(new Rectangle(0, 0, 1000, 750));
		frmWhiteBoard.setBounds(100, 100, 1300, 665);
		frmWhiteBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//make the window shown on the center of the screen
		frmWhiteBoard.setLocationRelativeTo(null);
		frmWhiteBoard.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);
		menuBar.setMinimumSize(new Dimension(100, 40));
		menuBar.setMaximumSize(new Dimension(100, 40));
		menuBar.setBorderPainted(false);
		menuBar.setBounds(new Rectangle(0, 0, 200, 50));
		menuBar.setAlignmentY(Component.CENTER_ALIGNMENT);
		menuBar.setBounds(0, 0, 1283, 40);
		menuBar.setMargin(new Insets(5, 20, 5, 20));
		frmWhiteBoard.getContentPane().add(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setBorderPainted(true);
		fileMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
		fileMenu.setBounds(new Rectangle(0, 0, 100, 40));
		fileMenu.setMargin(new Insets(2, 20, 2, 20));
		fileMenu.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		fileMenu.setForeground(Color.BLACK);
		fileMenu.setHorizontalAlignment(SwingConstants.CENTER);
		fileMenu.setBackground(new Color(255, 255, 255));
		menuBar.add(fileMenu);
		
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		fileMenu.add(newMenuItem);
		
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!isServer()) {
					JOptionPane.showMessageDialog(null, "You are not manager!\nYou cannot open a new canvas!");
				}
				else {
					clearMemory();
					clearBoard();
					server.sendNewBoard();
				}
			}
		});
		
		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		fileMenu.add(openMenuItem);
		
		openMenuItem.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				if(!isServer()) {
					JOptionPane.showMessageDialog(null, "You are not manager!\nYou cannot open a canvas!");
				}
				else {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(frmWhiteBoard);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       try {
						File file = chooser.getSelectedFile();
						FileInputStream fileIn = new FileInputStream(file);
						ObjectInputStream in = new ObjectInputStream(fileIn);
						dm.shapelist = (ArrayList<Shape>) in.readObject();
						clearBoard();
						server.sendNewBoard();
						if(dm.shapelist == null) {
							dm.shapelist = new ArrayList<Shape>();
						}
						for(Shape shape:dm.shapelist) {
							shape.draw((Graphics2D)whiteBoard.getGraphics());
							server.sendDrawToAll(shape, -1);
						}
						
						in.close();
						fileIn.close();
					} catch(FileNotFoundException exc) {
						exc.printStackTrace();
					} catch(IOException exc) {
						System.out.println("Encountering error when opening selected file!");
						exc.printStackTrace();
					} catch(ClassNotFoundException exc) {
						System.out.println("Object class not found for the file object!");
						exc.printStackTrace();
					} catch(Exception exc) {
						exc.printStackTrace();
					}
			    }
			}
			}
		});
		
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		fileMenu.add(saveMenuItem);
		
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!isServer) {
					JOptionPane.showMessageDialog(null, "You are not manager!\nYou cannot save file!");
				}
				else {
					try {
						FileOutputStream fileOut = new FileOutputStream(default_location);
						ObjectOutputStream out = new ObjectOutputStream(fileOut);
						out.writeObject(dm.shapelist);
						out.close();
						fileOut.close();
					} catch(IOException exc) {
						System.out.println("Encountering error when writing file!");
						exc.printStackTrace();
					} catch(Exception exc) {
						exc.printStackTrace();
					}
				}
			}
		});
		
		JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
		saveAsMenuItem.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		fileMenu.add(saveAsMenuItem);
		
		saveAsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!isServer()) {
					JOptionPane.showMessageDialog(null, "You are not manager!\nYou cannot open a canvas!");
				}
				else {
					JFileChooser chooser = new JFileChooser();
					int returnVal = chooser.showSaveDialog(frmWhiteBoard);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();
						if (file == null) {
					        return;
					      }
					    if (!file.getName().toLowerCase().endsWith(".txt")) {
					    	file = new File(file.getParentFile(), file.getName() + ".txt");
					    }
					    
					    try {
							FileOutputStream fileOut = new FileOutputStream(file);
							ObjectOutputStream out = new ObjectOutputStream(fileOut);
							out.writeObject(dm.shapelist);
							out.close();
							fileOut.close();
						} catch(IOException exc) {
							System.out.println("Encountering error when writing file!");
							exc.printStackTrace();
						} catch(Exception exc) {
							exc.printStackTrace();
						}
					    
				        //String name = chooser.getSelectedFile().getName();
				        //String dir = chooser.getCurrentDirectory().toString();
				        
				      }
				}
			}
		});
		
		JMenuItem closeMenuItem = new JMenuItem("Close");
		closeMenuItem.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		fileMenu.add(closeMenuItem);
		
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		JMenu manageMenu = new JMenu("Manage");
		manageMenu.setBorderPainted(true);
		manageMenu.setMargin(new Insets(2, 20, 2, 20));
		manageMenu.setBounds(new Rectangle(0, 0, 100, 40));
		manageMenu.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		manageMenu.setForeground(Color.BLACK);
		menuBar.add(manageMenu);
		
		JMenuItem userManageMenuItem = new JMenuItem("User management");
		userManageMenuItem.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		manageMenu.add(userManageMenuItem);
		
		newPage = new userManage();
		userManageMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!isServer) {
					JOptionPane.showMessageDialog(null, "You are not manager!\nYou manage users!");
				}
				else {
					newPage.setServer(server);
					newPage.frmUserManagement.setVisible(true);
				}
			}
		});
		
		whiteBoard = new JPanel();
		whiteBoard.setBounds(110, 50, 700, 575);
		whiteBoard.setBackground(Color.WHITE);
		frmWhiteBoard.getContentPane().add(whiteBoard);
		whiteBoard.setLayout(null);
		
		JPanel colorBar = new JPanel();
		colorBar.setBounds(0, 50, 105, 405);
		colorBar.setBackground(new Color(255, 255, 255));
		frmWhiteBoard.getContentPane().add(colorBar);
		colorBar.setLayout(null);
		
		
		JButton colorButton_1 = new JButton();
		colorButton_1 = new JButton();
		colorButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(0,0,128);
			}
		});
		colorButton_1.setBounds(5, 5, 45, 45);
		colorButton_1.setBackground(new Color(0, 0, 128));
		colorButton_1.setBorderPainted(false);
		colorBar.add(colorButton_1);
		
		JButton colorButton_2 = new JButton();
		colorButton_2 = new JButton();
		colorButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(0,0,255);
			}
		});
		colorButton_2.setBounds(55, 5, 45, 45);
		colorButton_2.setBackground(new Color(0, 0, 255));
		colorButton_2.setBorderPainted(false);
		colorBar.add(colorButton_2);
		
		JButton colorButton_3 = new JButton();
		colorButton_3 = new JButton();
		colorButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(0,128,0);
			}
		});
		colorButton_3.setBounds(5, 55, 45, 45);
		colorButton_3.setBackground(new Color(0, 128, 0));
		colorButton_3.setBorderPainted(false);
		colorBar.add(colorButton_3);
		
		JButton colorButton_4 = new JButton();
		colorButton_4 = new JButton();
		colorButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(0,255,0);
			}
		});
		colorButton_4.setBounds(55, 55, 45, 45);
		colorButton_4.setBackground(new Color(0, 255, 0));
		colorButton_4.setBorderPainted(false);
		colorBar.add(colorButton_4);
		
		JButton colorButton_5 = new JButton();
		colorButton_5 = new JButton();
		colorButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(128,0,128);
			}
		});
		colorButton_5.setBounds(5, 105, 45, 45);
		colorButton_5.setBackground(new Color(128, 0, 128));
		colorButton_5.setBorderPainted(false);
		colorBar.add(colorButton_5);
		
		JButton colorButton_6 = new JButton();
		colorButton_6 = new JButton();
		colorButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(255,0,255);
			}
		});
		colorButton_6.setBounds(55, 105, 45, 45);
		colorButton_6.setBackground(new Color(255, 0, 255));
		colorButton_6.setBorderPainted(false);
		colorBar.add(colorButton_6);
		
		JButton colorButton_7 = new JButton();
		colorButton_7 = new JButton();
		colorButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(255,0,0);
			}
		});
		colorButton_7.setBounds(5, 155, 45, 45);
		colorButton_7.setBackground(new Color(255, 0, 0));
		colorButton_7.setBorderPainted(false);
		colorBar.add(colorButton_7);
		
		JButton colorButton_8 = new JButton();
		colorButton_8 = new JButton();
		colorButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(255,192,203);
			}
		});
		colorButton_8.setBounds(55, 155, 45, 45);
		colorButton_8.setBackground(new Color(255, 192, 203));
		colorButton_8.setBorderPainted(false);
		colorBar.add(colorButton_8);
		
		JButton colorButton_9 = new JButton();
		colorButton_9 = new JButton();
		colorButton_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(255,165,0);
			}
		});
		colorButton_9.setBounds(5, 205, 45, 45);
		colorButton_9.setBackground(new Color(255, 165, 0));
		colorButton_9.setBorderPainted(false);
		colorBar.add(colorButton_9);
		
		JButton colorButton_10 = new JButton();
		colorButton_10 = new JButton();
		colorButton_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(255,255,0);
			}
		});
		colorButton_10.setBounds(55, 205, 45, 45);
		colorButton_10.setBackground(new Color(255, 255, 0));
		colorButton_10.setBorderPainted(false);
		colorBar.add(colorButton_10);
		
		JButton colorButton_11 = new JButton();
		colorButton_11 = new JButton();
		colorButton_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(169,169,169);
			}
		});
		colorButton_11.setBounds(5, 255, 45, 45);
		colorButton_11.setBackground(new Color(169, 169, 169));
		colorButton_11.setBorderPainted(false);
		colorBar.add(colorButton_11);
		
		JButton colorButton_12 = new JButton();
		colorButton_12 = new JButton();
		colorButton_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(220,220,220);
			}
		});
		colorButton_12.setBounds(55, 255, 45, 45);
		colorButton_12.setBackground(new Color(220, 220, 220));
		colorButton_12.setBorderPainted(false);
		colorBar.add(colorButton_12);
		
		JButton colorButton_13 = new JButton();
		colorButton_13 = new JButton();
		colorButton_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(255,218,185);
			}
		});
		colorButton_13.setBounds(5, 305, 45, 45);
		colorButton_13.setBackground(new Color(255, 218, 185));
		colorButton_13.setBorderPainted(false);
		colorBar.add(colorButton_13);
		
		JButton colorButton_14 = new JButton();
		colorButton_14 = new JButton();
		colorButton_14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(128,0,0);
			}
		});
		colorButton_14.setBounds(55, 305, 45, 45);
		colorButton_14.setBackground(new Color(128, 0, 0));
		colorButton_14.setBorderPainted(false);
		colorBar.add(colorButton_14);
		
		JButton colorButton_15 = new JButton();
		colorButton_15 = new JButton();
		colorButton_15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(218,112,214);
			}
		});
		colorButton_15.setBounds(5, 355, 45, 45);
		colorButton_15.setBackground(new Color(218, 112, 214));
		colorButton_15.setBorderPainted(false);
		colorBar.add(colorButton_15);
		
		JButton colorButton_16 = new JButton();
		colorButton_16 = new JButton();
		colorButton_16.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.color = new Color(0,0,0);
			}
		});
		colorButton_16.setBounds(55, 355, 45, 45);
		colorButton_16.setBackground(new Color(0, 0, 0));
		colorButton_16.setBorderPainted(false);
		colorBar.add(colorButton_16);
		
		toolBar = new JPanel();
		toolBar.setBounds(0, 465, 105, 155);
		toolBar.setBackground(new Color(255, 255, 255));
		frmWhiteBoard.getContentPane().add(toolBar);
		toolBar.setLayout(null);
		
		JButton line = new JButton();
		line.setIcon(new ImageIcon(Board_GUI.class.getResource("/picture/sline.png")));
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.shape = "line";
			}
		});
		line.setBounds(new Rectangle(5, 5, 45, 45));
		line.setBackground(Color.WHITE);
		line.setOpaque(false);  
		line.setBorder(null); 
		toolBar.add(line);
		
		JButton oval = new JButton();
		oval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.shape="oval";
			}
		});
		oval.setBounds(new Rectangle(55, 5, 45, 45));
		oval.setBackground(Color.WHITE);
		oval.setIcon(new ImageIcon(Board_GUI.class.getResource("/picture/circle.png")));
		oval.setOpaque(false);  
		oval.setBorder(null); 
		toolBar.add(oval);
		
		JButton triangle = new JButton();
		triangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.shape="triangle";
			}
		});
		triangle.setBounds(new Rectangle(5, 55, 45, 45));
		triangle.setBackground(Color.WHITE);
		triangle.setIcon(new ImageIcon(Board_GUI.class.getResource("/picture/yield.png")));
		triangle.setOpaque(false);  
		triangle.setBorder(null); 
		toolBar.add(triangle);
		
		JButton rectangle = new JButton();
		rectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.shape="rectangle";
			}
		});
		rectangle.setBounds(new Rectangle(55, 55, 45, 45));
		rectangle.setBackground(Color.WHITE);
		rectangle.setIcon(new ImageIcon(Board_GUI.class.getResource("/picture/rectangle.png")));
		rectangle.setOpaque(false);  
		rectangle.setBorder(null); 
		toolBar.add(rectangle);
		
		JButton text = new JButton();
		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//dm.shape="text";
				//text function is not implemented
			}
		});
		text.setBounds(new Rectangle(5, 105, 45, 45));
		text.setBackground(Color.WHITE);
		text.setIcon(new ImageIcon(Board_GUI.class.getResource("/picture/text.png")));
		text.setOpaque(false);  
		text.setBorder(null); 
		toolBar.add(text);
		
		chatBox = new JPanel();
		chatBox.setBackground(new Color(255, 255, 255));
		chatBox.setBounds(820, 83, 218, 542);
		frmWhiteBoard.getContentPane().add(chatBox);
		
		chatBox_text = new JTextArea();
		chatBox_text.setFont(new Font("Monospaced", Font.PLAIN, 13));
		chatBox_text.setColumns(30);
		chatBox_text.setEditable(false);
		chatBox.add(chatBox_text);
		
		userList = new JPanel();
		userList.setBackground(Color.WHITE);
		userList.setBounds(1048, 176, 225, 237);
		frmWhiteBoard.getContentPane().add(userList);
		
		userlist_text = new JTextArea();
		userlist_text.setColumns(22);
		userlist_text.setFont(new Font("Monospaced", Font.PLAIN, 19));
		userlist_text.setEditable(false);
		userList.add(userlist_text);
		
		userIdLabel = new JLabel("User ID:");
		userIdLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		userIdLabel.setBounds(1048, 97, 225, 30);
		frmWhiteBoard.getContentPane().add(userIdLabel);
		
		usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		usernameLabel.setBounds(1048, 50, 225, 30);
		frmWhiteBoard.getContentPane().add(usernameLabel);
		
		UserListLabel = new JLabel("User list");
		UserListLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		UserListLabel.setBounds(1048, 147, 200, 30);
		frmWhiteBoard.getContentPane().add(UserListLabel);
		
		chatBoxLabel = new JLabel("Chating Window:");
		chatBoxLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		chatBoxLabel.setBounds(820, 50, 200, 30);
		frmWhiteBoard.getContentPane().add(chatBoxLabel);
		
		typeBox = new JTextField();
		typeBox.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		typeBox.setBounds(1048, 458, 225, 109);
		frmWhiteBoard.getContentPane().add(typeBox);
		typeBox.setColumns(10);
		
		sendButton = new JButton("send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = typeBox.getText();
				setChatInputTextField();
				if(isServer) {
					server.sendChat(msg);
				}
				else {
					client.sendChat(msg);
				}
			}
		});
		sendButton.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		sendButton.setBounds(1048, 577, 225, 43);
		frmWhiteBoard.getContentPane().add(sendButton);
		
		typeLabel = new JLabel("Type mesage here");
		typeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		typeLabel.setBounds(1048, 423, 200, 30);
		frmWhiteBoard.getContentPane().add(typeLabel);
		
		dm = new DrawMethod(whiteBoard, this);
		whiteBoard.addMouseListener(dm);
        whiteBoard.addMouseMotionListener(dm);
	}
	
	public Boolean isServer() {
		return isServer;
	}
	
	public Server getServer() {
		return server;
	}
	
	public Client getClient() {
		return client;
	}
	
	//clear the canvas
	public void clearBoard() {
		Graphics2D g2d = (Graphics2D) whiteBoard.getGraphics();
		Stroke stroke = g2d.getStroke();
		Color color = g2d.getColor();
		g2d.setStroke(new BasicStroke(800));
		g2d.setColor(Color.WHITE);
		g2d.drawLine(0,0,whiteBoard.getWidth(),whiteBoard.getHeight());
		g2d.setStroke(stroke);
		g2d.setColor(color);
	}
	
	//clear the shapes in memory
	public void clearMemory() {
		dm.shapelist.clear();
	}
	
	public DrawMethod getDrawMethod() {
		return dm;
	}
	
	public JPanel getCanvas() {
		return whiteBoard;
	}
	
	public void setUserlist(String s) {
		userlist_text.setText(s);
		if(newPage!=null) {
			newPage.userlist.setText(s);
		}
	}
	
	public void setChatInputTextField() {
		typeBox.setText("");
	}
	
	//add msg to current chat
	public void setChatTextArea(String msg) {
		chatBox_text.append(msg+"\n");
	}
	
	public void setUserInfo(String username, String clientNum) {
		//TO DO
		usernameLabel = new JLabel("Username: "+username);
		usernameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		usernameLabel.setBounds(1048, 50, 225, 30);
		frmWhiteBoard.getContentPane().add(usernameLabel);
		
		userIdLabel = new JLabel("User ID: "+clientNum);
		userIdLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		userIdLabel.setBounds(1048, 97, 225, 30);
		frmWhiteBoard.getContentPane().add(userIdLabel);
		
		frmWhiteBoard.setVisible(true);
	}
	
	public void setIsServer(Boolean b) {
		isServer = b;
	}
	
	public void setServer(Server s) {
		server = s;
	}
	
	public void setClient(Client c) {
		client = c;
	}
}



