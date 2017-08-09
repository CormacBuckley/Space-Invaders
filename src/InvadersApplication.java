import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.*;

public class InvadersApplication extends JFrame implements Runnable, KeyListener{

	//member data
	
	private static String workingDirectory;
	private static boolean isGraphicsInitialised = false;
	private static final Dimension WindowSize = new Dimension(800,600);
	private static final int NUMALIENS = 30;
	private Alien[] AliensArray = new Alien[NUMALIENS];
	private Spaceship PlayerShip;
	private PlayerBullet Bullet;
	private BufferStrategy strategy;
	private Graphics offscreenGraphics;
	private ArrayList<PlayerBullet> bullets = new ArrayList<PlayerBullet>();
	private int score = 0;
	private int highscore;
	private boolean GameState = true;
	private int waveNo = 1;
	
	//load image from disk, make sure your path is correct
	ImageIcon icon = new ImageIcon(workingDirectory + "\\alien_ship_1.png");
	ImageIcon icon2 = new ImageIcon(workingDirectory + "\\alien_ship_2.png");
	Image alienImage = icon.getImage();
	Image alienImage2 = icon2.getImage();
	
	//constructor
	public InvadersApplication(){	
	//Display the window, centered on screen
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int x = screensize.width/2 - WindowSize.width/2;
		int y = screensize.height/2 - WindowSize.height/2;
		setBounds(x, y, WindowSize.width, WindowSize.height);
		setVisible(true);
		this.setTitle("Invaders From Space!");
		
		// create and initialize the player's spaceship
		icon = new ImageIcon(workingDirectory + "\\player_ship.png");
		Image shipImage = icon.getImage();
		PlayerShip = new Spaceship(shipImage);
		PlayerShip.setPosition(300, 530);		
		Sprite2D.setWinWidth(WindowSize.width);
		
		 
			 	Scanner scan;
				try {
					scan = new Scanner(new File("HighScore.txt"));
					 highscore = scan.nextInt();
			               scan.close();       
				} catch (FileNotFoundException e) {
					
				}   
		
		//Start a New Game
		startNewGame();
		
		// create and start our animation thread
		Thread t = new Thread(this);
		t.start();

		//send keyboard events arriving into this JFrame back to it's own event handlers
		addKeyListener(this);
		
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		offscreenGraphics = strategy.getDrawGraphics();
		isGraphicsInitialised = true; //it's now safe to paint the images
	}
	// thread's entry point
	public void run() {
		while (true){
			if(GameState){
				this.repaint();
			}
			// 1: sleep for 1/50 second
			try {
				Thread.sleep(20);
			} catch (InterruptedException e){ }
			
			// 2: Animate game objects
			boolean reversalNeeded = false;
			for (int i=0;i<NUMALIENS;i++){
				if(AliensArray[i].isAlive)
				if(AliensArray[i].move())
					reversalNeeded = true;
			}
				if(reversalNeeded){
					for(int i = 0; i < NUMALIENS; i++)  
						if(AliensArray[i].isAlive)
						AliensArray[i].reverseDirection();
					}
					PlayerShip.move();
					
					Iterator iterator = bullets.iterator();
					
					while(iterator.hasNext()){
						PlayerBullet b = (PlayerBullet) iterator.next();
						b.move();
						
						for(int i = 0; i < NUMALIENS; i++){
							Alien a = AliensArray[i];
							if(a.isAlive)
							if((a.x < b.x && a.x + 50>b.x)
									&& (a.y < b.y && a.y + 35 > b.y+6))// || (b.y<a.y && b.y+6 > a.y))
						{
								a.isAlive = false;
								a.numAlive--;
								iterator.remove();
								score++;
								if(highscore < score){
									highscore++;
									 try {
								         FileWriter writer = new FileWriter("HighScore.txt");
								         writer.write(new Integer(highscore).toString());   // write new line
								         writer.close();
									 	 } catch (IOException e) {
								         e.printStackTrace();
								    }
								}
								checkEnd();
						}
							if((a.x < PlayerShip.x && a.x + 50>PlayerShip.x)
									&& (a.y < PlayerShip.y && a.y + 35 > PlayerShip.y+6))
							{
								startNewGame();
							}
							else if(a.y > 600){
								a.numAlive = 0;
								startNewGame();
							}
						}
					}
				this.repaint(); // 3: force application repaint
		}
	}
	private void startNewGame() {
		this.GameState = true;
		waveNo = 0;
		score = 0;
		checkEnd();
	}
	private void checkEnd()
	{
		if(Alien.numAlive == 0)
		{
			startNewLevel();	
		}
	}
	public void keyPressed(KeyEvent e) {
		changeGameState();
	
		if (e.getKeyCode()==KeyEvent.VK_LEFT)
			PlayerShip.setXSpeed(-4);
		else if (e.getKeyCode()==KeyEvent.VK_RIGHT)
			PlayerShip.setXSpeed(4);
		else if(e.getKeyCode()==KeyEvent.VK_SPACE)
			shootBullet();	
	}
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT)
			PlayerShip.setXSpeed(0);
	}
	public void keyTyped(KeyEvent e) { }
	
	public void shootBullet(){
	
		ImageIcon icon = new ImageIcon(workingDirectory + "\\bullet.png");
		Image BulletImage = icon.getImage();
		PlayerBullet b = new PlayerBullet(BulletImage);
		b.setPosition(PlayerShip.x + 54/2, PlayerShip.y);
		bullets.add(b);
		b.move();
	}
	public void changeGameState()
	{
	  if(GameState == true){
		this.GameState = false;
		} 
	}
	public void startNewLevel()
	{
		waveNo++;
		// create and initialize some aliens, passing them each the image we have loaded
	for (int i=0;i<NUMALIENS;i++){
			AliensArray[i] = new Alien(alienImage,alienImage2);
			
			int xx = (i%5)*80 + 70;
			int yy = (i/5)*40 + 50;
			
			AliensArray[i].setPosition(xx,yy);
			AliensArray[i].setXSpeed(waveNo);
		
	}	
	}
	// application's paint method
	public void paint(Graphics g){	
		if(!GameState){
		if (!isGraphicsInitialised) 
			return;
			// clear the canvas with a big black rectangle
			g= offscreenGraphics;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WindowSize.width, WindowSize.height);
			// redraw all game objects
			for (int i = 0; i<NUMALIENS;i++)
				if(AliensArray[i].isAlive)
				{
				AliensArray[i].paint(g);
				}
				PlayerShip.paint(g);
				Iterator iterator = bullets.iterator();
			while(iterator.hasNext())
			{
				PlayerBullet b = (PlayerBullet) iterator.next();
				if(b.isVisible)
				b.paint(g);
			}
				Font f = new Font( "Times", Font.PLAIN, 24 );
				g.setFont(f);
				Color c = Color.WHITE;
				g.setColor(c);
				g.drawString("SCORE: " + score, 240, 60);
				g.drawString("HighScore " +  highscore, 440, 60);
				g.drawString("Wave: " + waveNo, 20, 60);
				strategy.show();
		}
		if(GameState){
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, WindowSize.width, WindowSize.height);
			Font f = new Font( "Times", Font.PLAIN, 24 );
			g.setFont(f);
			Color c = Color.BLACK;
			g.setColor(c);
			g.drawString("NEW GAME: ", 320, 250);
			g.drawString("Press Any Key To Start ", 265, 300);
			g.drawString("[Arrows to move - Space to shoot] ", 205, 350);
		}
	}
	// application entry point
		public static void main(String[] args){
			workingDirectory = System.getProperty("user.dir");
			InvadersApplication w = new InvadersApplication();
		}
	}
