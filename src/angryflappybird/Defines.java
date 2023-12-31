package angryflappybird;

import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * The Defines class maintains variables pertaining to various game components as
 * well as images. Provides functionality for resizing an image and getting its file path.
 */
public class Defines {
    
	// dimension of the GUI application
    final int APP_HEIGHT = 600;
    final int APP_WIDTH = 600;
    final int SCENE_HEIGHT = 570;
    final int SCENE_WIDTH = 400;

    // coefficients related to the blob
    final int BLOB_WIDTH = 70;
    final int BLOB_HEIGHT = 70;
    final int BLOB_POS_X = 70;
    final int BLOB_POS_Y = 200;
    final int BLOB_DROP_TIME = 300000000;  	// the elapsed time threshold before the blob starts dropping
    final int BLOB_DROP_VEL = 150;    		// the blob drop velocity
    final int BLOB_FLY_VEL = -100;
    final int BLOB_IMG_LEN = 4;
    final int BLOB_IMG_PERIOD = 5;
    
    // coefficients related to the floors
    final int FLOOR_WIDTH = 400;
    final int FLOOR_HEIGHT = 100;
    final int FLOOR_COUNT = 2;
    
    final int EGG_WIDTH = 95;
    final int EGG_HEIGHT = 120;
    final double EGG_SHIFT_Y = 56;
    
    //coefficients related to the pipes
    final int PIPE_WIDTH = 95;
    final int PIPE_HEIGHT = 290;
    final int PIPE_COUNT = 2;
    final int BOT_PIPE_MIN_Y = 350;
    final int BOT_PIPE_MAX_Y = 420;
    final int TOP_PIPE_MIN_Y = -175;
    final int TOP_PIPE_MAX_Y = -105;
    
    // coefficients related to time
    final int SCENE_SHIFT_TIME = 5;
    final double SCENE_SHIFT_INCR = -0.4;
    final double NANOSEC_TO_SEC = 1.0 / 1000000000.0;
    final double TRANSITION_TIME = 0.1;
    final int TRANSITION_CYCLE = 2;
    
    
    // coefficients related to media display
    final String STAGE_TITLE = "Angry Flappy Bird";
	private final String IMAGE_DIR = "../resources/images/";
    final String[] IMAGE_FILES = {"background","blob0", "blob1", "blob2", "blob3", "floor","pipe","toppipe", "nightBackground", "pig","whiteegg", "goldegg"}; // added nightBackground and pig

    final HashMap<String, ImageView> IMVIEW = new HashMap<String, ImageView>();
    final HashMap<String, Image> IMAGE = new HashMap<String, Image>();
    
    //nodes on the scene graph
    Button startButton;
    ToggleGroup levels;
    RadioButton lvl_easy;
    RadioButton lvl_med;
    RadioButton lvl_hard;
    
    // constructor
    /**
     * Creates a new Defines Object. Initializes the images and sets them to their
     * respective sizes. Initializes the start button.
     */
	Defines() {
		
		// initialize images
		for(int i=0; i<IMAGE_FILES.length; i++) {
			Image img;
			if (i == 5) { //floor
				img = new Image(pathImage(IMAGE_FILES[i]), FLOOR_WIDTH, FLOOR_HEIGHT, false, false);
			}
			else if (i == 1 || i == 2 || i == 3 || i == 4 || i == 9){ //blobs and pig
				img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, BLOB_HEIGHT, false, false);
			}
			else if (i == 6) { //pipe
			    img = new Image(pathImage(IMAGE_FILES[i]),PIPE_WIDTH,PIPE_HEIGHT,false,false);
			}
			else if (i == 7) { //toppipe
			    img = new Image(pathImage(IMAGE_FILES[i]),PIPE_WIDTH,PIPE_HEIGHT,false,false);
			} else if (i == 10) {
			    img = new Image(pathImage(IMAGE_FILES[i]),EGG_WIDTH,EGG_HEIGHT,false,false);
			} else if (i == 11) {
			    img = new Image(pathImage(IMAGE_FILES[i]),EGG_WIDTH,EGG_HEIGHT,false,false);
			}
			else { // i == 0 || i == 8 (backgrounds)
				img = new Image(pathImage(IMAGE_FILES[i]), SCENE_WIDTH, SCENE_HEIGHT, false, false);
			}
    		IMAGE.put(IMAGE_FILES[i],img);
    	}
		
		// initialize image views
		for(int i=0; i<IMAGE_FILES.length; i++) {
    		ImageView imgView = new ImageView(IMAGE.get(IMAGE_FILES[i]));
    		IMVIEW.put(IMAGE_FILES[i],imgView);
    	}
		
		// initialize scene nodes
		startButton = new Button("Go!");
		levels = new ToggleGroup();
		lvl_easy = new RadioButton("Easy");
		lvl_med = new RadioButton("Medium");
		lvl_hard = new RadioButton("Hard");
		lvl_easy.setToggleGroup(levels);
		lvl_med.setToggleGroup(levels);
		lvl_hard.setToggleGroup(levels);
	}
	
	/**
	 * Gets the full file path of an image. 
	 * @param filepath The name of an image
	 * @return The full path of that image. 
	 */
	public String pathImage(String filepath) {
    	String fullpath = getClass().getResource(IMAGE_DIR+filepath+".png").toExternalForm();
    	return fullpath;
    }
	
	/**
	 * Resizes an image. 
	 * @param filepath The corresponding file path for the image
	 * @param width The desired width
	 * @param height The desired height
	 * @return the newly resized image
	 */
	public Image resizeImage(String filepath, int width, int height) {
    	IMAGE.put(filepath, new Image(pathImage(filepath), width, height, false, false));
    	return IMAGE.get(filepath);
    }
}