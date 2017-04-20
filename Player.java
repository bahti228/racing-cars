package racingcars;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    
    //Instance variables
    private Map map;
    private Point center;
    private BufferedImage playerImg;
    private int rotation;
    
    //Colors of the moveable road
    private final Color road2;
    private final Color road1;
    
    //Sets up the player
    public Player(int playerType, int carType, int mapType){
        
        //Sets up the correct map with given map Type
        map = new Map(mapType);
        
        //Sets up the correct center using playerType and mapType
        center = map.getCenter(playerType,mapType);
        
        //Default rotation
        rotation = 0;
        
        //Loads a correct car color
        switch (carType){
            case 1: playerImg = loadImage("res/OptionsGui/black.png"); break;
            case 2: playerImg = loadImage("res/OptionsGui/blue.png"); break;
            case 3: playerImg = loadImage("res/OptionsGui/green.png"); break;
            case 4: playerImg = loadImage("res/OptionsGui/orange.png"); break;
            case 5: playerImg = loadImage("res/OptionsGui/yellow.png"); break;
        }
        
        //Sets up the correct pixels for the track
        switch (mapType){
            case 1:
                road1 = new Color(166,201,203);
                road2 = new Color(189,218,219);
                break;
                
            case 2:
                road1 = new Color(236,220,184);
                road2 = new Color(252,239,210);
                break;
                
            default:
                road1 = new Color(166,201,203);
                road2 = new Color(166,201,203);
                break;
        }
        
    }
    
    //Returns the player's location in 0 < x < 800 && 0 < y < 800
    public Point getPoint(){
        //Center of the screen
        int x = 400;
        int y = 400;
        
        //Sets up the x and y according to relative location on the image
        if (center.getX() < 400) 
            x = (int) center.getX();
        
        if (center.getX() >= 400 && center.getX() <= map.getFullMap().getWidth() - 400)
            x = 400;
        
        if (center.getX() >= map.getFullMap().getWidth() - 400)
            x = 400 + (int) center.getX() - (map.getFullMap().getWidth() - 400);
        
        if (center.getY() < 400)
            y = (int) center.getY();
        
        if (center.getY() > 400 && center.getY() <= map.getFullMap().getHeight() - 400)
            y = 400;
        
        if (center.getY() > map.getFullMap().getHeight() - 400)
            y = 400 + (int) center.getY() - (map.getFullMap().getHeight() - 400);
        
        //Returns a new point in where to be drawen on the screen
        return new Point(x,y);
        
    }
    
    //Updates the players actual center depending on keys being hold
    public void updatePosition(boolean[] keys){
        
        //Temporary point of the old point
        Point tempCenter = new Point((int) center.getX(), (int) center.getY());
        
        //If the Left key is being hold
        if (keys[2]) addRotation(-5);
        
        //If the Right key is being hold
        if (keys[3]) addRotation(5);

        //Makes sure the rotation doesnt go above 360
        while (rotation > 360) rotation -= 360;
        
        //Makes sure the rotation doesnt go below 360
        while (rotation < -360) rotation += 360;
        
        //Temporary int
        int temp;
        
        //Updates the players value based on rotation and keys being hold
        if (rotation >= 0 && rotation <= 89){
            temp = (rotation + 5) / 10;
            
            if (keys[0]){addX(temp);addY(-9+temp);}
            if (keys[1]){addX(-temp);addY(9-temp);}
        }
        
        else if (rotation >= 90 && rotation <= 179){
            temp = (rotation-90 + 5) /10;
            
            if (keys[0]) {addX(9-temp); addY(temp);}
            if (keys[1]) {addX(-9+temp); addY(-temp);}
        }
        
        else if (rotation >= 180 && rotation <= 269){
            temp = (rotation-180 + 5) /10;
            
            if (keys[0]) {addX(-temp); addY(9-temp);}
            if (keys[1]) {addX(temp); addY(-9+temp);}
        }
        
        else if (rotation >= 270 && rotation <= 360){
            temp = (rotation-270 + 5) /10;
            
            if (keys[0]) {addX(-9+temp); addY(-temp);}
            if (keys[1]) {addX(9-temp); addY(temp);}
        }
        
        else if (rotation >= -89 && rotation <= 0){
            temp = (rotation - 5) / 10;
            
            if (keys[0]){addX(temp);addY(-(9+temp));}
            if (keys[1]){addX(-temp);addY(9+temp);}
        }
        
        else if (rotation >= -179 && rotation <= -90){
            temp = (rotation+90 + 5) /10;
            
            if (keys[0]) {addX(-9-temp); addY(-temp);}
            if (keys[1]) {addX(9+temp); addY(temp);}
        }
        
        else if (rotation >= -269 && rotation <= -180){
            temp = (rotation+180 + 5) /10;
            
            if (keys[0]) {addX(-temp); addY(9+temp);}
            if (keys[1]) {addX(temp); addY(-9-temp);}
        }
        
        else if (rotation >= -360 && rotation <= -270){
            temp = (rotation+270 + 5) /10;
            
            if (keys[0]) {addX(9+temp); addY(temp);}
            if (keys[1]) {addX(-9-temp); addY(-temp);}
        }
        
        //RGB color of the players current location
        Color current = new Color(map.getFullMap().getRGB((int) center.getX(), (int) center.getY()));
        
        //Makes sure the player doesnt go past the barrier
        if (!(current.equals(road1) || current.equals(road2))) center = tempCenter;
    }
    
    //Returns a point that player would be drawn at
    public Point getPoint(Player player){
       
        //Checks if the player is in the vision of the this player
        if (player.getCenter().getX() >= map.getVision().getX() && player.getCenter().getX() <= map.getVision().getX() + 800){
            if (player.getCenter().getY() >= map.getVision().getY() && player.getCenter().getY() <= map.getVision().getY() + 800){
                
                int x = (int) player.getCenter().getX() - (int) map.getVision().getX();
                int y = (int) player.getCenter().getY() - (int) map.getVision().getY();
                return new Point(x,y);
                
            }
        }
        
        //Returns nothing if its not in the vision
        return null;
    }
    
    //Returns the players image
    public BufferedImage getImg(){
        return playerImg;
    }
    
    //Returns the image around the players center
    public BufferedImage getMap(){
        return map.getMap(center);
    }
    
    //Returns the actual center
    public Point getCenter(){
        return center;
    }
    
    //Adds the rotation
    public void addRotation(int x){
        rotation+=x;
    }
    
    //Adds to the x position
    public void addX(int x){
        center.setLocation(center.getX()+x, center.getY());
    }
    
    //Adds to the y position
    public void addY(int y){
        center.setLocation(center.getX(), center.getY()+y);
    }
    
    //Returns the rotation
    public int getRotation(){
        return rotation;
    }
    
    //Loads the image
    private BufferedImage loadImage(String loc){
        BufferedImage img = null;
        try {
            img = ImageIO.read(getClass().getResource(loc));
        }catch (IOException e){
        }
        return img;
    }
    
}
