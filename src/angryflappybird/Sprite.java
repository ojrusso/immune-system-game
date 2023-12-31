package angryflappybird;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * The Sprite class handles functionality for a Sprite object, including positioning
 * based on velocity and visual rendering. 
 */
public class Sprite {  
    
    //create a 'type' attribute
    private String type;
   
	
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
    // private String IMAGE_DIR = "../resources/images/"; (Deleted - already defined in Defines.java)

    /**
     * Constructs a Sprite. X and Y positions and velocities are set to 0.
     */
    public Sprite() {
        this.positionX = 0;
        this.positionY = 0;
        this.velocityX = 0;
        this.velocityY = 0;
    }
    
    /**
     * Constructs a sprite with an X and Y position, image, and type 
     * @param pX X position
     * @param pY Y position
     * @param image The image to be associated with the Sprite
     * @param type The type to be associated with the Sprite
     */
    public Sprite(double pX, double pY, Image image, String type) {
        setPositionXY(pX, pY);
        setImage(image);
        setVelocity(0,0);
        this.type = type;
    }
    
    /**
     * Gets the type of the Sprite
     * @return Type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Constructs a Sprite with an X and Y position and image.
     * @param pX X position
     * @param pY Y position
     * @param image The image to be associated with the Sprite
     */
    public Sprite(double pX, double pY, Image image) {
    	setPositionXY(pX, pY);
        setImage(image);
        this.velocityX = 0;
        this.velocityY = 0;
    }

    /**
     * Sets the Sprite's image.
     * @param image Desired image for the Sprite
     */
    public void setImage(Image image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    /**
     * Sets the Sprite's X and Y positions.
     * @param positionX X position
     * @param positionY Y position
     */
    public void setPositionXY(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * Gets the current X position.
     * @return X position
     */
    public double getPositionX() {
        return positionX;
    }

    /**
     * Gets the current Y position.
     * @return Y position
     */
    public double getPositionY() {
        return positionY;
    }

    /**
     * Sets the velocity.
     * @param velocityX X velocity
     * @param velocityY Y velocity
     */
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Adds to the current velocity.
     * @param x X velocity to be added
     * @param y Y velocity to be added
     */
    public void addVelocity(double x, double y) {
        this.velocityX += x;
        this.velocityY += y;
    }

    /**
     * Gets the current X velocity.
     * @return X velocity
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Gets the current Y velocity.
     * @return Y velocity
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Gets the width of the Sprite.
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Renders the Sprite on the GUI.
     * @param gc The necessary graphics context
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);
    }

    /**
     * Gets the current boundary of the Sprite.
     * @return X and Y boundaries, plus width and height
     */
    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    /**
     * Determines if another Sprite is intersecting this Sprite. 
     * @param s The other sprite
     * @return True if the two sprites intersect, false otherwise
     */
    public boolean intersectsSprite(Sprite s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    /**
     * Updates the X and Y positions based on time and velocity.
     * @param time Desired time of positionality, typically the current time
     */
    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
}