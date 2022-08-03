//Student name: RAO YUNHUI   Student number:1316834
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Server {
	@SuppressWarnings("unused")
	private String ip;//As it is distributed locally, it's always 127.0.0.1, use this for future needs
	private int port;
	private String username;
	public Board_GUI gui;
	private ArrayList<ServerHandler> threadPool = new ArrayList<ServerHandler>();
	
	
	public Server(String [] args, Board_GUI gui) {
		//user input args should be ip, pot and username
		try {
			ip = args[0];
			port = Integer.valueOf(args[1]);
			username = args[2];
		} catch(Exception e) {
			System.out.println("Invalid Input! Input should be ip, port and username!");
			System.exit(1);
		}
		
		this.gui = gui;
		gui.setUserInfo(username, "Server");
		updateClients();
	}
	
	public void connect() {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(port);
			int clientNum = 0;
			System.out.println("Server is ready for connection.");
			Thread shutdown = new Thread(() -> sendClose());
			Runtime.getRuntime().addShutdownHook(shutdown);
			while(true) {
				Socket socket = server.accept();
				ServerHandler handler = new ServerHandler(socket, clientNum, "default_name", gui, this);
				threadPool.add(handler);
				new Thread(handler).start();
				System.out.println("Successfully connected!");
				clientNum++;
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error when connecting!", "Error",JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	public void updateClients() {
		String s = username+"_server\n";
		for(ServerHandler handler:threadPool) {
			s+=handler.get_Name() + "\n";
		}
		gui.setUserlist(s);
		//maybe need to set for manager
	}
	
	public void sendUserList() {
		String s = username+"_server\n";
		
		for(ServerHandler handler:threadPool) {
			s+=handler.get_Name()+"\n";
		}
		JSONObject json = new JSONObject();
		json.put("command", "userlist");
		json.put("userlist", s);
		
		for(ServerHandler handler:threadPool) {
			handler.sendJSON(json);
		}
	}
	
	public void sendChat(String chat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		String time = simpleDateFormat.format(new Date());
		gui.setChatInputTextField();
		gui.setChatTextArea(username + "_server " + time + ": \n" + chat);
		JSONObject json = new JSONObject();
		json.put("command", "chat");
		json.put("username", username);
		json.put("time", time);
		json.put("text", chat);
		json.put("clientNum", "_server");
		
		for(ServerHandler handler:threadPool) {
			handler.sendJSON(json);
		}
	}
	
	//send draw command to all clients except the one who sent this
	public void sendDrawToAll(Shape shape, int clientNum) {
		for(ServerHandler handler:threadPool) {
			if(handler.getClientNum()!=clientNum) handler.sendDraw(shape);
		}
	}
	
	public void disconnectWithClient(int clientNumber) {
		for(ServerHandler handler:threadPool) {
			if(clientNumber==handler.getClientNum()) {
				handler.sendDisconnect();
				sendUserList();
			}
		}
	}
	
	public void removeThread(int clientNum) {
		ServerHandler remove=null;
		for(ServerHandler handler:threadPool) {
			if(handler.getClientNum()==clientNum) remove = handler;
		}
		threadPool.remove(remove);
		System.out.println("A user left.");
		sendUserList();
		updateClients();
	}
	
	
	public void sendClose() {
		for (ServerHandler handler : threadPool) {
			handler.sendClose();
        }
	}
	
	
	//new board for everyone
	public void sendNewBoard() {
		for(ServerHandler handler:threadPool) {
			handler.sendNewFile();
		}
	}
	
	public void passChat(JSONObject json) {
		for(ServerHandler handler:threadPool) {
			handler.sendJSON(json);
		}
	}
	
}
