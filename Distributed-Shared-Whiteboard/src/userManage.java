//Student name: RAO YUNHUI   Student number:1316834
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class userManage {

	public JFrame frmUserManagement;
	public JTextArea userlist;
	private Server server;

	/**
	 * Launch the application.
	 */
	public static void userManagePage() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					userManage window = new userManage();
					window.frmUserManagement.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public userManage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmUserManagement = new JFrame();
		frmUserManagement.setTitle("User Management");
		frmUserManagement.setBounds(100, 100, 491, 550);
		frmUserManagement.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		//make the window shown on the center of the screen
		frmUserManagement.setLocationRelativeTo(null);
		frmUserManagement.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 47, 256, 452);
		panel.setBackground(Color.WHITE);
		frmUserManagement.getContentPane().add(panel);
		
		userlist = new JTextArea();
		userlist.setEditable(false);
		userlist.setColumns(36);
		userlist.setFont(new Font("Monospaced", Font.PLAIN, 13));
		panel.add(userlist);
		
		JLabel lblNewLabel = new JLabel("Current users:");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		lblNewLabel.setBounds(10, 14, 200, 30);
		frmUserManagement.getContentPane().add(lblNewLabel);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		textArea.setBounds(287, 399, 170, 50);
		frmUserManagement.getContentPane().add(textArea);
		
		JLabel lblNewLabel_1 = new JLabel("User ID:");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		lblNewLabel_1.setBounds(287, 359, 112, 30);
		frmUserManagement.getContentPane().add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Stop Sevice");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String s = textArea.getText();
				int num;
				try {
					num = Integer.valueOf(s);
					server.disconnectWithClient(num);
				}catch(Exception exc) {
					JOptionPane.showMessageDialog(null, "You need to input a number to kick!");
				}
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		btnNewButton.setBounds(287, 469, 170, 30);
		frmUserManagement.getContentPane().add(btnNewButton);
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
}
