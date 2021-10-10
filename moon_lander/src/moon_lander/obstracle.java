package moon_lander;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;



public class obstracle {
	
	/**
     * Image of obstacle1
     */
    private BufferedImage obstacle1Img;
	
	/**
     * Width of obstacle
     */
    public int obstacle1ImgWidth;
    
    /**
     * Height of obstacle
     */
    public int obstacle1ImgHeight;
    
    /**
     * X coordinate of the obstacle1
     */
    public int x;
    
    /**
     * Y coordinate of the obstacle1
     */
    public int y;
    
    public obstracle()
    {
        Initialize();
        LoadContent();
    }
    
    private void Initialize()
    {   
        // X coordinate of the obstacle1 area is at 46% frame width.
        x = (int)(Framework.frameWidth * 0.46);
        // Y coordinate of the obstacle1 area is at 86% frame height.
        y = (int)(Framework.frameHeight * 0.66);
    }
    
    private void LoadContent()
    {
        try
        {
            URL obstacle1ImgUrl = this.getClass().getResource("/resources/images/landing_area.png");
            obstacle1Img = ImageIO.read(obstacle1ImgUrl);
            obstacle1ImgWidth = obstacle1Img.getWidth();
            obstacle1ImgHeight = obstacle1Img.getHeight();
        }
        catch (IOException ex) {
            Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(obstacle1Img, x, y,null);
    }
    
}
