import java.awt.Image;


public class Alien extends Sprite2D {

	
	public boolean isAlive = true;
	public static int numAlive;
	
	public Alien(Image i, Image j) {
		super(i, j);
		this.isAlive = true;
		this.numAlive++;
		// TODO Auto-generated constructor stub
	}
	
	public boolean move(){
	
	x += xSpeed;
	
	if(x<0 || x>= winWidth - myImage.getWidth(null))
		return true;
	else 
		return false;
	}
	
	public void reverseDirection(){
		xSpeed = -xSpeed;
		this.y += 55;
		
	}

}
