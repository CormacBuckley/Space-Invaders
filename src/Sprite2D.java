import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.*;


public class Sprite2D {
	// member data
	protected int x;
	protected int y;
	protected int xSpeed = 0;
	protected Image myImage;
	protected Image myImage2;
	private int frames = 0;
	
	protected static int winWidth;
	
	public Sprite2D(Image i, Image j){
		this.myImage = i;
		this.myImage2 = j;
		
		
	}
	public void setPosition(int xx, int yy){
		this.x = xx;
		this.y = yy;
		
	}
	public void setXSpeed(int dx){
		xSpeed = dx;
	}
	public void paint(Graphics g){
		frames++;
		if(frames%100<50){
			g.drawImage(myImage, (int)x, (int)y, null);
		}else
			g.drawImage(myImage2, (int)x, (int)y, null);
		
	}
	public static void setWinWidth(int w) {
		winWidth = w;
		
	}
}

