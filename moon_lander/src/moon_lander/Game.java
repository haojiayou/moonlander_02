package moon_lander;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;



/**
 * Actual game.
 * 
 * @author www.gametutorial.net
 */

public class Game {

    /**
     * The space rocket with which player will have to land.
     */
    private PlayerRocket playerRocket;
    
    /**
     * Landing area on which rocket will have to land.
     */
    private LandingArea landingArea;
    
    
    //private obstacles obstacles;
    
    private obstracle obstacle1;
    
    private final static int NUMBER_OF_OBSTACLE = 10;
   
    private obstacles obstacle[] = new obstacles[NUMBER_OF_OBSTACLE];
    
    /**
     * Game background image.
     */
    private BufferedImage backgroundImg;
    
    /**
     * Red border of the frame. It is used when player crash the rocket.
     */
    private BufferedImage redBorderImg;


    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
    	long gameTime = 0;
        playerRocket = new PlayerRocket();
        landingArea  = new LandingArea();
        if(gameTime / Framework.secInNanosec < 5) {
        	obstacle1 = new obstracle();
    	}
        for (int i = 0; i < NUMBER_OF_OBSTACLE; i++) {
            obstacle[i] = new obstacles();
        }
    }
    
    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/resources/images/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            URL redBorderImgUrl = this.getClass().getResource("/resources/images/red_border.png");
            redBorderImg = ImageIO.read(redBorderImgUrl);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
        playerRocket.ResetPlayer();
        for (int i = 0; i < NUMBER_OF_OBSTACLE; i++)
            obstacle[i].resetObstacles();
    }
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        // Move the rocket
        playerRocket.Update();
        
        //obstacle
        for (int i = 0; i < NUMBER_OF_OBSTACLE; i++) {
            obstacle[i].Update();

            if (playerRocket.x < obstacle[i].x + obstacle[i].obstacleImgWidth &&
                    playerRocket.x + playerRocket.rocketImgWidth > obstacle[i].x &&
                    playerRocket.y < obstacle[i].y + obstacle[i].obstacleImgHeight &&
                    playerRocket.rocketImgHeight + playerRocket.y > obstacle[i].y) 
            {
                //collision = true
                playerRocket.crashed = true;
                obstacle[i].resetObstacles();
                Framework.gameState = Framework.GameState.GAMEOVER;
            }
            if (obstacle[i].y + obstacle[i].obstacleImgHeight - 10 > landingArea.y) 
            {
                obstacle[i].crashed = true;
            }
        
	    //임시로 x,y축 제한 추가
	  	if(playerRocket.y + playerRocket.rocketImgHeight < 0 || playerRocket.x + playerRocket.rocketImgWidth <(int)(Framework.frameWidth * 0) || playerRocket.x >(int)(Framework.frameWidth * 1))
	  	{
	  		playerRocket.wentout = true;

	  		Framework.gameState = Framework.GameState.GAMEOVER;
	  			
	  	}
	  	if(gameTime / Framework.secInNanosec < 5) {
	  		//고정장애물.30초작동
	  		if (playerRocket.x < obstacle1.x + obstacle1.obstacle1ImgWidth &&
	  				playerRocket.x + playerRocket.rocketImgWidth > obstacle1.x &&
	  				playerRocket.y < obstacle1.y + obstacle1.obstacle1ImgHeight &&
	  				playerRocket.rocketImgHeight + playerRocket.y > obstacle1.y) {
	  			playerRocket.crashed = true;
	  			Framework.gameState = Framework.GameState.GAMEOVER;
        }
	  	}    
        // Checks where the player rocket is. Is it still in the space or is it landed or crashed?
        // First we check bottom y coordinate of the rocket if is it near the landing area.
        if (playerRocket.y + playerRocket.rocketImgHeight - 10 == 0) {
        	 playerRocket.crashed = true;
        }
	  	else if(playerRocket.y + playerRocket.rocketImgHeight - 10 > landingArea.y)
        {

            // Here we check if the rocket is over landing area.
  	  		if((playerRocket.x > landingArea.x) && (playerRocket.x < landingArea.x + landingArea.landingAreaImgWidth - playerRocket.rocketImgWidth))
            {
                // Here we check if the rocket speed isn't too high.
                if(playerRocket.speedY <= playerRocket.topLandingSpeed)
                    playerRocket.landed = true;
                else
                    playerRocket.crashed = true;
            }
            else
                playerRocket.crashed = true;
                
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
	  	}
	  	
    }
    
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition,long gameTime)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        landingArea.Draw(g2d);
        
        playerRocket.Draw(g2d);
        
        if(gameTime / Framework.secInNanosec < 5) {
        	obstacle1.Draw(g2d);}
        
        for (int i = 0; i < NUMBER_OF_OBSTACLE; i++) {
        	obstacle[i].Draw(g2d);
            if (obstacle[i].crashed) {
                obstacle[i].DrawobstacleCrash(g2d);
            }
        }
    }
    
    
    
    
    /**
     * Draw the game over screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     * @param gameTime Game time in nanoseconds.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition,long gameTime)
    {
        Draw(g2d, mousePosition, gameTime);
        
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 70);
      
        //경계 밖으로 나갔을 때
        /*if(playerRocket.wentout)
        {
        	g2d.setColor(Color.green);
        	g2d.drawString("Your rocket rocket went out of gravity!", Framework.frameWidth/2-100, Framework.frameHeight / 3);
        }*/
        
        if(playerRocket.landed)
        {
            g2d.drawString("You have successfully landed!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3);
            g2d.drawString("You have landed in " + gameTime / Framework.secInNanosec + " seconds.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 20);
        }
        
        else if(playerRocket.crashed)
        {
            g2d.setColor(Color.red);
            g2d.drawString("You have crashed the rocket!", Framework.frameWidth / 2 - 95, Framework.frameHeight / 3);
            g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        }
        
        //경계 밖으로 나갔을 때
        else
        {
        	g2d.setColor(Color.yellow);
        	g2d.drawString("Your rocket rocket went out of gravity!", Framework.frameWidth/2-110, Framework.frameHeight / 3);
        }
    }
}
