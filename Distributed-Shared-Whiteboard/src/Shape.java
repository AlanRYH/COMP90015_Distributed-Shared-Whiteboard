//Student name: RAO YUNHUI   Student number:1316834
import java.awt.Graphics2D;
import java.io.Serializable;
import java.awt.Color;
/*This class is to save the drawings' information
 *  and implement the draw method so draw command from other users can be 
 *  executed
 */
@SuppressWarnings("serial")
public class Shape implements Serializable{
	public String shape;
	public String text;
	public Color color;
	public int x1,x2,y1,y2;
	
	public Shape(String shape, String text, Color color, int x1, int x2, int y1, int y2) {
		this.shape = shape;
		this.text = text;
		this.color = color;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public void draw(Graphics2D g2d) {
		g2d.setColor(color);
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
			g2d.drawString(text, x1, y1);
		}
	}
}
