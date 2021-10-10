package moon_lander;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


public class obstacles {
    /*
     random
     */
    private Random randomNumber;

    /*
    position
     */
    public int x;
    public int y;

    public boolean crashed;
    /*
    speed of x
     */
    private int speedx;
    /*
	speed of y
     */
    public int speedy;

    /*
    Image
     */
    private BufferedImage obstacleImg;
    
    private BufferedImage obstacleExplosionImg;
    
    private BufferedImage obstacleFireImg;

    /**
     obstacle explosion.
     */
    //private BufferedImage obstacleExplosionImg;

    /**
    
     */
    //private BufferedImage holeImg;

    /**
     * Width of obstacle
     */
    public int obstacleImgWidth;
    /**
     * Height of obstacle
     */
    public int obstacleImgHeight;

    /**
     * Random direction (left or right).
     */
    private boolean obstacleFlyDirection;

    /**
     * Starting position of obstacle
     */
    private final int STARTING_POSITION = -110;

    /**
     * Max random speed.
     */
    private final int MAX_RANDOM_SPEED = 5;

    /**
     * The smallest X speed.
     */
    private final int SMALLEST_X_SPEED = 3;
    /**
     * The smallest Y speed.
     */
    private final int SMALLEST_Y_SPEED = 4;
    
    /**
     * Accelerating speed of the obstacle
     */
    private int speedAccelerating;
    /**
     * Stopping/Falling speed of the obstacle. Falling speed because, the gravity pulls the obstacle down to the moon.
     */
    private int speedStopping;

    obstacles() {
        Initialize();
        LoadContent();
    }

    private void Initialize() {
        randomNumber = new Random();
        resetObstacles();
        
        speedAccelerating = 1;
        speedStopping = 1;

    }

    private void LoadContent() {
         try{
            java.net.URL obstacleImgUrl = this.getClass().getResource("/resources/images/rocket.png");
            obstacleImg = ImageIO.read(obstacleImgUrl);
            obstacleImgWidth = obstacleImg.getWidth();
            obstacleImgHeight = obstacleImg.getHeight();
            
            URL obstacleExplosionImgUrl = this.getClass().getResource("/resources/images/rocket_crashed.png");
           obstacleExplosionImg = ImageIO.read(obstacleExplosionImgUrl);
            
           URL obstacleFireImgUrl = this.getClass().getResource("/resources/images/rocket_fire.png");
           obstacleFireImg = ImageIO.read(obstacleFireImgUrl);
           
         }
            
        	
        catch (IOException ex) {
            Logger.getLogger(obstacles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resetObstacles() {
        crashed = false;
        x = randomNumber.nextInt(Framework.frameWidth - obstacleImgWidth);
        y = STARTING_POSITION;
        obstacleFlyDirection = randomNumber.nextBoolean();
        if (obstacleFlyDirection) {
            speedx = randomNumber.nextInt(MAX_RANDOM_SPEED) + SMALLEST_X_SPEED;
            speedy = randomNumber.nextInt(MAX_RANDOM_SPEED) + SMALLEST_Y_SPEED;
        } else {
            speedx = -randomNumber.nextInt(MAX_RANDOM_SPEED) - SMALLEST_X_SPEED;
            speedy = randomNumber.nextInt(MAX_RANDOM_SPEED) + SMALLEST_Y_SPEED;
        }
    }
    
    /*public void Update1()
    {
        // Calculating speed for moving up or down.
        if(Canvas.keyboardKeyState(KeyEvent.VK_W))
            speedx -= speedAccelerating;
        else
            speedy += speedStopping;
        
        // Calculating speed for moving or stopping to the left.
        if(Canvas.keyboardKeyState(KeyEvent.VK_A))
            speedx -= speedAccelerating;
        else if(speedx < 0)
            speedx += speedStopping;
        
        // Calculating speed for moving or stopping to the right.
        if(Canvas.keyboardKeyState(KeyEvent.VK_D))
            speedx += speedAccelerating;
        else if(speedx > 0)
            speedx -= speedStopping;
        
        // Moves the obstacle
        x += speedx;
        y += speedy;
    }*/

    /**
     * Here we move the rocket.
     */
    public void Update() {
        if (crashed) {
            speedx = 0;
            speedy = 0;
            resetObstacles();
        }
        x += speedx;
        y += speedy;
    }
    public void DrawobstacleCrash(Graphics2D g2d) {
        g2d.drawImage(obstacleExplosionImg, x, y - 50, null);
    } 
    public void Draw(Graphics2D g2d) {
        g2d.drawImage(obstacleImg, x, y, null);
          
        if(Canvas.keyboardKeyState(KeyEvent.VK_W)) {
                g2d.drawImage(obstacleFireImg, x + 12, y + 66, null);
        }
    }
}