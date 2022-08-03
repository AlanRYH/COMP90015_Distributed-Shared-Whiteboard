//Student name: RAO YUNHUI   Student number:1316834
import javax.swing.JOptionPane;

public class CreateWhiteBoard {
	public static void main(String[] args) {
		if(args.length != 3) {
			JOptionPane.showMessageDialog(null, "The number of arguments should be 3, including ip, port and username!", "Error",JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		Board_GUI gui = new Board_GUI();
		Server server = new Server(args, gui);
		gui.setIsServer(true);
		gui.setServer(server);
		server.connect();
	}
}
