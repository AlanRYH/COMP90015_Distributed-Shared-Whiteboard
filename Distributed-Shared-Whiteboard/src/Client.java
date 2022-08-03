//Student name: RAO YUNHUI   Student number:1316834
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Graphics2D;

import org.json.JSONObject;
import javax.swing.JOptionPane;

public class Client {
	private String ip;
	private int port;
	private String username = "";
	private Board_GUI gui;
	private Socket socket;
	private PrintWriter printwriter;
	private BufferedReader bufferedreader;
	//private JOptionPane pane;
	private int clientNum;
	
	public Client(String[] args, Board_GUI gui) {
		try {
			ip = args[0];
			port = Integer.valueOf(args[1]);
			username = args[2];
			this.gui = gui;
		} catch(Exception e) {
			System.out.println("Invalid Input! Input should be ip, port and username!");
			System.exit(1);
		}
	}
	
	public void connect() {
		try {
			socket = new Socket(ip, port);
			System.out.println("Connected to server!");
			Thread shutdown = new Thread(() -> sendDisconnect());
			Runtime.getRuntime().addShutdownHook(shutdown);
			printwriter = new PrintWriter(socket.getOutputStream());
			bufferedreader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			join();
			communicate();
		} catch(IOException e) {
			System.out.println("Connection failed! Please check if your input is valid!");
			System.exit(1);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	public void join() {
		JSONObject json = new JSONObject();
		json.put("command", "join");
		json.put("username", username);
		printwriter.write(json.toString()+"\n");
		printwriter.flush();
		JOptionPane.showMessageDialog(null, "Waiting for manager's permission...");
	}
	
	public void communicate() {
		try {
		while(true) {
			String input = bufferedreader.readLine();
			JSONObject json = new JSONObject(input);
			String command = json.getString("command");
			switch(command) {
			case "chat":
				String usernameStr = json.getString("username");
				String time = json.getString("time");
				String text = json.getString("text");
				String receivedClientNum = json.getString("clientNum");
				String msg = usernameStr + receivedClientNum + " " + time + ": \n" + text;
				gui.setChatTextArea(msg);
				break;
			case "clientNum":
				//pane.setVisible(false);
				clientNum = json.getInt("clientNum");
				JOptionPane.showMessageDialog(null, "You have joined the board.");
				gui.setUserInfo(username,String.valueOf(clientNum));
				break;
			case "draw":
				receiveDraw(json);
				break;
			case "disconnect":
				JOptionPane.showMessageDialog(null, "You have been disconnected by server!");
				System.exit(0);
				break;
			case "newboard":
				gui.clearBoard();
				gui.clearMemory();
				break;
			case "userlist":
				gui.setUserlist(json.getString("userlist"));
				break;
			case "newfile":
				gui.clearBoard();
				gui.clearMemory();
				break;
			case "close":
				JOptionPane.showMessageDialog(null, "Server closed application.");
				return;
			default:
				System.out.println("Unknown command!");
			}
		}
		} catch(IOException e) {
			System.out.println("Error when buffer reading!");
			System.exit(1);
		}
	}
	
	public void receiveDraw(JSONObject json) {
		String shapeStr, text;
		Color color;
		int colorR, colorG, colorB;
		int x1,x2,y1,y2;

		shapeStr = json.getString("shape");
		text = json.getString("drawtext");
		x1 = json.getInt("x1");
		x2 = json.getInt("x2");
		y1 = json.getInt("y1");
		y2 = json.getInt("y2");
		colorR = json.getInt("colorR");
		colorG = json.getInt("colorG");
		colorB = json.getInt("colorB");
		color = new Color(colorR, colorG, colorB);
		
		Shape shape = new Shape(shapeStr, text, color, x1, x2, y1, y2);
		gui.getDrawMethod().shapelist.add(shape);
		shape.draw((Graphics2D)gui.getCanvas().getGraphics());
	}
	
	public void sendDraw(Shape shape) {
		int colorR, colorG, colorB;
		int[] colorRGB;
		
		colorRGB = DrawMethod.colorToInt(shape.color);
		colorR = colorRGB[0];
		colorG = colorRGB[1];
		colorB = colorRGB[2];
		
		JSONObject json = new JSONObject();
		json.put("command", "draw");
		json.put("shape", shape.shape);
		json.put("drawtext", shape.text);
		json.put("x1", shape.x1);
		json.put("x2", shape.x2);
		json.put("y1", shape.y1);
		json.put("y2", shape.y2);
		json.put("colorR", colorR);
		json.put("colorG", colorG);
		json.put("colorB", colorB);
		json.put("clientNum", clientNum);
		printwriter.write(json.toString()+"\n");
		printwriter.flush();
	}
	
	public void sendChat(String text) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		String time = simpleDateFormat.format(new Date());
		gui.setChatInputTextField();
		//username + "_server " + time + ": \n" + chat
		JSONObject json = new JSONObject();
		json.put("command", "chat");
		json.put("username", username);
		json.put("time", time);
		json.put("text", text);
		json.put("clientNum", "_client "+String.valueOf(clientNum));
		printwriter.write(json.toString()+"\n");
		printwriter.flush();
	}
	
	public void sendDisconnect() {
		JSONObject json = new JSONObject();
		json.put("command", "disconnect");
		printwriter.write(json.toString()+"\n");
		printwriter.flush();
	}
}
