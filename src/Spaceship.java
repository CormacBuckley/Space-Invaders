import java.awt.Image;

public class Spaceship extends Sprite2D {

	public Spaceship(Image i) {
		super(i, i);
		// TODO Auto-generated constructor stub
	}
	
	public void move(){
	
		this.x += xSpeed;
		
		
		if(this.x <= 0){
			this.x = 0;
			xSpeed = 0;
			
		}else if(this.x >= winWidth - myImage.getWidth(null))
		{
			this.x = winWidth - myImage.getWidth(null);
			xSpeed = 0;
		}
	
	
		
	}

}

