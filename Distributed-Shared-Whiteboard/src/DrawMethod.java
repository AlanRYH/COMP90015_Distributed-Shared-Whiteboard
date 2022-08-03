//Student name: RAO YUNHUI   Student number:1316834
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.awt.event.MouseAdapter;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.util.ArrayList;
import java.awt.Color;


@SuppressWarnings("serial")
public class DrawMethod extends MouseAdapter implements ActionListener, Serializable{
	public Board_GUI gui;
	public Graphics2D g2d;
	public String shape = "line";
	public JPanel canvas;
	public ArrayList<Shape> shapelist = new ArrayList<Shape>();
	public ArrayList<JTextArea> texts = new ArrayList<JTextArea>();
	public int x1, x2, y1, y2;
	public Color color = Color.black;
	
	
	public DrawMethod(JPanel canvas, Board_GUI gui) {
		this.canvas = canvas;
		this.gui = gui;
		g2d = (Graphics2D) canvas.getGraphics();
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		return;
	}
	
	public void mousePressed(MouseEvent e) {
		g2d = (Graphics2D) canvas.getGraphics();
		g2d.setColor(color);
		repaint();
		x1 = e.getX();
		y1 = e.getY();
	}
	
	public void mouseReleased(MouseEvent e) {
		g2d = (Graphics2D) canvas.getGraphics();
		g2d.setColor(color);
		x2 = e.getX();
		y2 = e.getY();
		Shape shapeClass = new Shape(shape, "", color, x1, x2, y1, y2);
		shapelist.add(shapeClass);
		sendDraw(shapeClass);
		switch(shape) {
		case "line":
			g2d.drawLine(x1, y1, x2, y2);
			break;
		case "oval":
			g2d.drawOval(x1, y1, Math.abs(x2-x1), Math.abs(y2-y1));
			break;
		case "triangle":
			g2d.drawPolygon(new int[] {x1, x2, 2*x1-x2}, new int[] {y1, y2, y2}, 3);
			break;
		case "rectangle":
			g2d.drawPolygon(new int[] {x1,x2,x2,x1}, new int[] {y1, y1, y2, y2}, 4);
			break;
		case "text":
			//not able to implement
		}
		repaint();
	}
	
	//return RGB value of a color
	public static int[] colorToInt(Color color){
		return new int[] {color.getRed(), color.getGreen(), color.getBlue()};
	}
	
	public Color intToColor(int[] a) {
		return new Color(a[0], a[1], a[2]);
	}
	
	public void clearMemory() {
		shapelist.clear();
	}
	
	public void clearBoard() {
		gui.clearBoard();
	}
	
	public void sendDraw(Shape shape) {
		if(gui.isServer()) {
			gui.getServer().sendDrawToAll(shape, -1);
		}
		else {
			gui.getClient().sendDraw(shape);
		}
	}
	
	public void repaint() {
		for(Shape s:shapelist) {
			s.draw(g2d);
		}
	}
}
