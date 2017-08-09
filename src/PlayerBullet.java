import java.awt.Image;

public class PlayerBullet extends Sprite2D{
	protected int ySpeed = 20;
	protected boolean isVisible = true;

	public PlayerBullet(Image i) {
		super(i, i);
		// TODO Auto-generated constructor stub
	}

	public boolean move(){
		this.y -= ySpeed;
		return true;
		
	}

}
