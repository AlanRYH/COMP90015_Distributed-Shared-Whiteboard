//Student name: RAO YUNHUI   Student number:1316834
import java.net.Socket;
import java.io.*;
import java.awt.Graphics2D;
import java.awt.Color;
import org.json.JSONObject;
import javax.swing.JOptionPane;


public class ServerHandler implements Runnable{
	private Socket socket;
	private int clientNum;
	private String username;
	private Board_GUI gui;
	private Server server;
	private PrintWriter printwriter;
	private BufferedReader bufferedreader;
	
	public ServerHandler(Socket socket, int clientNum, String username, Board_GUI gui, Server server) {
		this.socket = socket;
		this.clientNum = clientNum;
		this.username = username;
		this.gui = gui;
		this.server = server;
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			printwriter = new PrintWriter(this.socket.getOutputStream());
			if(bufferedreader == null) System.out.println("reader null!");
			if(printwriter == null) System.out.println("writer null!");
		} catch(IOException e) {
			System.out.println("Encountering IO error!");
		}
	}
	
	public void run() {
		communicate();
	}
	
	public void communicate() {
		try {
		while(true) {
			String input = bufferedreader.readLine();
			if(input!=null) {
				JSONObject  json = new JSONObject(input);
				String command = json.getString("command");
				switch(command) {
				case "chat":
					String username = json.getString("username");
					String time = json.getString("time");
					String text = json.getString("text");
					String receivedClientNum = json.getString("clientNum");
					String msg = username + receivedClientNum + " " + time + ": \n" + text;
					gui.setChatTextArea(msg);
					server.passChat(json);
					break;
				case "clientNum":
					clientNum = json.getInt("clientNum");
					break;
				case "draw":
					receiveDraw(json);
					break;
				case "join":
					this.username = json.getString("username");
					int decision = JOptionPane.showConfirmDialog(null, "Do you allow " + this.username + " to join?", "Join request",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(decision==JOptionPane.YES_OPTION) {
							JOptionPane.showMessageDialog(null, this.get_Name()+" joined.");
							//send information to clients
							JSONObject send = new JSONObject();
							send.put("command", "clientNum");
							send.put("clientNum", clientNum);
							printwriter.write(send.toString() + "\n");
							printwriter.flush();
							//send the current status of canvas to new comers
							for(Shape s:gui.getDrawMethod().shapelist) {
								sendDraw(s);
							}
							server.updateClients();
							server.sendUserList();
					}
					else {
						sendDisconnect();
					}
					break;
				case "disconnect":
					server.removeThread(clientNum);
					return;
				case "newboard":
					gui.clearBoard();
					gui.clearMemory();
					server.sendNewBoard();
				default:
					System.out.println("Unknown command!");
				}
					
					
			}
		}
		} catch(IOException e) {
			System.out.println("Error when buffer reading!");
			System.exit(1);
		}
	}
	
	public String get_info() {
		return username + "_client_" + this.clientNum;
	}
	
	public String get_Name() {
		return username + "_client_" + clientNum;
	}
	
	public int getClientNum() {
		return clientNum;
	}
	
	public void receiveDraw(JSONObject json) {
		String shapeStr, text;
		Color color;
		int colorR, colorG, colorB;
		int x1,x2,y1,y2;
		int clientNum;
		try {
			shapeStr = json.getString("shape");
			text = json.getString("drawtext");
			x1 = json.getInt("x1");
			x2 = json.getInt("x2");
			y1 = json.getInt("y1");
			y2 = json.getInt("y2");
			colorR = json.getInt("colorR");
			colorG = json.getInt("colorG");
			colorB = json.getInt("colorB");
			clientNum = json.getInt("clientNum");
			color = new Color(colorR, colorG, colorB);
			
			Shape shape = new Shape(shapeStr, text, color, x1, x2, y1, y2);
			gui.getDrawMethod().shapelist.add(shape);
			shape.draw((Graphics2D)gui.getCanvas().getGraphics());
			server.sendDrawToAll(shape, clientNum);
			
		} catch(Exception e) {
			System.out.println("Error when receiving draw!");
		}
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
		printwriter.write(json.toString()+"\n");
		printwriter.flush();
	}
	
	public void sendJSON(JSONObject json) {
		printwriter.write(json.toString() + "\n");
		printwriter.flush();
	}
	
	public void sendClose() {
		JSONObject json = new JSONObject();
		json.put("command", "close");
		printwriter.write(json.toString() + "\n");
		printwriter.flush();
	}
	
	public void sendDisconnect() {
		JSONObject json = new JSONObject();
		json.put("command", "disconnect");
		printwriter.write(json.toString() + "\n");
		printwriter.flush();
	}
	
	public void sendNewFile() {
		JSONObject json = new JSONObject();
		json.put("command", "newfile");
		printwriter.write(json.toString() + "\n");
		printwriter.flush();
	}
}
