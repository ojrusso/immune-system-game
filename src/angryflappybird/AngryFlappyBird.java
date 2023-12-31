package angryflappybird;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.image.Image;

//The Application layer
/**
 * The AngryFlappyBird class provides an interface for playing a modified version of
 * the game flappy bird. The user clicks to make the bird fly through a series of pipes.
 * Keeps track of score and lives, which change depending on various collisions. Game is over 
 * when the user loses all lives. 
 */
public class AngryFlappyBird extends Application {
    
    private Defines DEF = new Defines(); 
    
    // Variables to manage game state
    private int score = 0;
    private int lives = 3;
  
    private Text scoreValue; //display score
    private Text scoreText; //display "Score: "
    private Text livesText; //display "Lives: "

    private Text livesValue; //display lives left 

    private Text gameOverText; //display "game over!"
    private Text readyText; //display "get ready!"

    // time related attributes
    private long clickTime, startTime, elapsedTime, backgroundStartTime, snoozeStartTime;   

    private AnimationTimer timer;
    
    // game components
    private Sprite blob;
    private ArrayList<Sprite> floors;
    private ArrayList<Sprite> topPipes;
    private ArrayList<Sprite> bottomPipes;

    //define lists for eggs and pigs
    private ArrayList<Sprite> whiteEggs;
    private ArrayList<Sprite> yellowEggs;
    private ArrayList<Sprite> pigs;
  
   
    // game flags
    private boolean CLICKED, GAME_START, GAME_OVER, DAYTIME, AUTOPILOT,COLLIDED;
    
    private boolean EASY, MED, HARD;
       

    // scene graphs
    private Group gameScene;     // the left half of the scene
    private VBox gameControl;    // the right half of the GUI (control)
    private GraphicsContext gc;     
    
    // the mandatory main method 
    /**
     * Mandatory main method to launch the program.
     * @param args Mandatory arguments to launch the program.
     */
    public static void main(String[] args) {
        launch(args);
    }
       
    // the start method sets the Stage layer
    /**
     * Sets the stage layer of the GUI. Initializes scene graphs and UIs, 
     * adds scene graphs and shows the stage. Throws an exception.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        // initialize scene graphs and UIs
        resetGameControl();    // resets the gameControl
        resetGameScene(true);  // resets the gameScene
        
        HBox root = new HBox();
        HBox.setMargin(gameScene, new Insets(0,0,0,15));
        root.getChildren().add(gameScene);
        root.getChildren().add(gameControl);
        
        // add scene graphs to scene
        Scene scene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);
        
        // finalize and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle(DEF.STAGE_TITLE);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    // the getContent method sets the Scene layer
    /**
     * Resets the game control, including the start button.
     */
    private void resetGameControl() {
        
        DEF.startButton.setOnMouseClicked(this::mouseClickHandler);
        
        gameControl = new VBox();
       gameControl.getChildren().addAll(DEF.startButton);
      
       // Create ImageViews for icons using the pathImage method
       ImageView whiteEggIcon = new ImageView(new Image(DEF.pathImage("whiteegg")));
       ImageView goldenEggIcon = new ImageView(new Image(DEF.pathImage("goldegg")));
       ImageView pigIcon = new ImageView(new Image(DEF.pathImage("pig")));
     
        // Set the desired size for the icons (adjust these values as needed)
       double iconWidth = 48; // for example, half of the original width
       double iconHeight = 48; // for example, half of the original height
       whiteEggIcon.setFitWidth(iconWidth);
       whiteEggIcon.setFitHeight(iconHeight);
       goldenEggIcon.setFitWidth(iconWidth);
       goldenEggIcon.setFitHeight(iconHeight);
       pigIcon.setFitWidth(iconWidth);
       pigIcon.setFitHeight(iconHeight);
        
       // Create Text descriptions
       Text whiteEggDescription = new Text("Purple Potion: 5 points added");
       Text goldenEggDescription = new Text("Red Potion: Snooze for 6 seconds!");
       Text pigDescription = new Text("Enemy: Avoid!");
       // Add icons and descriptions to the VBox
       gameControl.getChildren().addAll(
               //DEF.startButton,
               //difficultySelector, // Add the difficulty selector
               whiteEggIcon, whiteEggDescription,
               goldenEggIcon, goldenEggDescription,
               pigIcon, pigDescription
       );
       
       gameControl.getChildren().addAll(DEF.lvl_easy,DEF.lvl_med,DEF.lvl_hard);
     }

    /**
     * Handles mouse click events. If the game is over, the game scene is reset.
     * Otherwise the time of mouse click is recorded. 
     * @param e Event to be handled
     */
    private void mouseClickHandler(MouseEvent e) {
        if (GAME_OVER) {
            resetGameScene(false);
        }
        else if (GAME_START){
            clickTime = System.nanoTime();   
        }
        GAME_START = true;
        CLICKED = true;
    }
    
    /**
     * Resets the game scene and attributes. Initializes the floor, blob/bird,
     * pipes, pigs, and timer. 
     * @param firstEntry True if this is the first time starting the game.
     */
    private void resetGameScene(boolean firstEntry) {
        
        // reset variables
        CLICKED = false;
        GAME_OVER = false;
        GAME_START = false;
        floors = new ArrayList<>();
        topPipes = new ArrayList<>();
        bottomPipes = new ArrayList<>();
        whiteEggs = new ArrayList<>();
        pigs = new ArrayList<>();
        yellowEggs = new ArrayList<>();
        COLLIDED = false;

       
        if(firstEntry) {
            // create two canvases
            Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
            gc = canvas.getGraphicsContext2D();

            // create a background
            ImageView background = DEF.IMVIEW.get("background");
            DAYTIME = true;
            
            // create the game scene
            gameScene = new Group();
            gameScene.getChildren().addAll(background, canvas);
            
            //****
            scoreText = new Text("Score: ");
            scoreText.setStyle("-fx-font-size: 20;");
            scoreText.setLayoutX(DEF.SCENE_WIDTH - 150);
            scoreText.setLayoutY(30);
            
            scoreValue = new Text(""+ score);
            scoreValue.setStyle("-fx-font-size: 20;");
            scoreValue.setLayoutX(DEF.SCENE_WIDTH - 90);
            scoreValue.setLayoutY(30);
            

            livesText = new Text("Lives: ");
            livesText.setStyle("-fx-font-size: 20;");
            livesText.setLayoutX(DEF.SCENE_WIDTH - 150);
            livesText.setLayoutY(60);
            
            livesValue = new Text("" + lives);
            livesValue.setStyle("-fx-font-size: 20;");
            livesValue.setLayoutX(DEF.SCENE_WIDTH - 90);
            livesValue.setLayoutY(60);

            gameScene.getChildren().addAll(scoreText, livesText, scoreValue, livesValue);
          
            readyText = new Text("get ready!");
            readyText.setFont(Font.font("Courier",FontWeight.EXTRA_BOLD,50));
            readyText.setStroke(Color.BLACK);
            readyText.setFill(Color.WHITE);
            readyText.setLayoutX(75);
            readyText.setLayoutY(DEF.SCENE_HEIGHT/2);
            gameScene.getChildren().add(readyText);
            

        }else {
            //remove old score text
            gameScene.getChildren().remove(scoreValue);
            scoreValue = new Text(""+ score);
            scoreValue.setStyle("-fx-font-size: 20;");
            scoreValue.setLayoutX(DEF.SCENE_WIDTH - 90);
            scoreValue.setLayoutY(30);
            gameScene.getChildren().add(scoreValue);

            gameScene.getChildren().remove(gameOverText);
        }
        
        // initialize floor
        for(int i=0; i<DEF.FLOOR_COUNT; i++) {
            
            int posX = i * DEF.FLOOR_WIDTH;
            int posY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
            
            Sprite floor = new Sprite(posX, posY, DEF.IMAGE.get("floor"));
            floor.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            floor.render(gc);
            
            floors.add(floor);
        }
        
        // initialize blob
        DEF.resizeImage("blob0", 40, 40);
        DEF.resizeImage("blob1", 40, 40);
        DEF.resizeImage("blob2", 40, 40);
        DEF.resizeImage("blob3", 40, 40);
        blob = new Sprite(DEF.BLOB_POS_X, DEF.BLOB_POS_Y,DEF.IMAGE.get("blob0"));
        blob.render(gc);
        
        
        
        

        //initialize bottom pipe
        for(int i=0; i<DEF.PIPE_COUNT; i++) {
            int posX = i * DEF.FLOOR_WIDTH;
            double posY = Math.floor(Math.random()*(DEF.BOT_PIPE_MAX_Y-DEF.BOT_PIPE_MIN_Y+1)+DEF.BOT_PIPE_MIN_Y);
                    
            Sprite pipe = new Sprite(posX, posY, DEF.IMAGE.get("pipe"));
            pipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            pipe.render(gc);
                    
            bottomPipes.add(pipe);
            
            int eggPosX = -DEF.FLOOR_WIDTH;
            Sprite whiteEgg = new Sprite(eggPosX,posY-DEF.EGG_SHIFT_Y,DEF.IMAGE.get("whiteegg"),"white");
            whiteEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            whiteEgg.render(gc);
            
            Sprite yellowEgg = new Sprite(eggPosX,posY-DEF.EGG_SHIFT_Y,DEF.IMAGE.get("goldegg"),"golden");
            yellowEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            yellowEgg.render(gc);
            
            whiteEggs.add(whiteEgg);
            yellowEggs.add(yellowEgg);
        }
        
        //initialize top pipe
        for(int i=0; i<DEF.PIPE_COUNT; i++) {
            int posX = i * DEF.FLOOR_WIDTH;
            double posY = Math.floor(Math.random()*(DEF.TOP_PIPE_MAX_Y-DEF.TOP_PIPE_MIN_Y+1)+DEF.TOP_PIPE_MIN_Y);
                    //Math.floor(Math.random()*(DEF.PIPE_MAX_Y-DEF.PIPE_MIN_Y+1)+DEF.PIPE_MIN_Y);
                    
            Sprite pipe = new Sprite(posX, posY, DEF.IMAGE.get("toppipe"));
            pipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            pipe.render(gc);
            
            topPipes.add(pipe);
        }
        
        // initialize pigs
        for (int i = 0; i < DEF.PIPE_COUNT; i++) {
            int posX = -DEF.FLOOR_WIDTH;
            double posY = 0;
            
            DEF.resizeImage("pig", 40, 40);
            Sprite pig = new Sprite(posX, posY, DEF.IMAGE.get("pig"));

            pig.setVelocity(DEF.SCENE_SHIFT_INCR, -DEF.SCENE_SHIFT_INCR - 0.05);

            pig.render(gc);    
            
            pigs.add(pig);
        }
        
        // initialize timer
        startTime = System.nanoTime();
        timer = new MyTimer();
        timer.start();
        backgroundStartTime = System.nanoTime();
    } // end of reset game scene
    
    //timer stuff
    /**
     * The timer class keeps track of the internal time of the program.
     * Handles the positioning of objects that depend on time, namely the 
     * floor, pipes, pigs, blob/bird, background, and autopilot mode.
     * Maintains lives and score based off of various collisions.
     */
    class MyTimer extends AnimationTimer {
        
        int counter = 0;
        //**
        
        /**
         * Called continuously while program is running. If the game has started,
         * updates game components as necessary. 
         * @param now Internal start time of the program.
         */
         @Override
         public void handle(long now) {          
             // time keeping
             elapsedTime = now - startTime;
             startTime = now;
             
             // clear current scene
             gc.clearRect(0, 0, DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);

             if (GAME_START) {
                  
                 // step1: update floor
                 
                 levelSelect();

                 moveFloor();
                 spawnBottomPipe();
                 spawnTopPipe();

                 dropPigs();
                 
                 // step2: update blob
                 moveBlob();
                 
                 
                 
                 // remove ready text once blob starts moving? maybe move back to top
                 gameScene.getChildren().remove(readyText);
                 
                 // Check for collisions if not in autopilot mode 
                 if (!AUTOPILOT) {
                     checkCollision();    
                 }

                 // Increment score when blob passes through pipes
                 for (int i = 0; i < DEF.PIPE_COUNT; i++) {
                     double pipeRightX = topPipes.get(i).getPositionX() + DEF.FLOOR_WIDTH;
                     double blobLeftX = blob.getPositionX();
                 }
                 
                 // step3: update background
                 changeBackground();
    
                 grantSnoozeTime();
             }
         }
         
         // step1: update floor
         /**
          * Increments the floor's position and renders on the GUI. When one floor has moved
          * completely out of frame, it is moved to the opposite side of the other floor so as
          * to make the floor's movement seem continuous. 
          */
         private void moveFloor() {
            
            for(int i=0; i<DEF.FLOOR_COUNT; i++) {
                if (floors.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
                    double nextX = floors.get((i+1)%DEF.FLOOR_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
                    double nextY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
                    floors.get(i).setPositionXY(nextX, nextY);
                }
                floors.get(i).render(gc);
                floors.get(i).update(DEF.SCENE_SHIFT_TIME);
            }
         }
         
         private void levelSelect() {
             if (DEF.lvl_easy.isSelected()) {
                 System.out.println("easy");
                 EASY = true;
                 MED=false;
                 HARD=false;
             } 
             if (DEF.lvl_med.isSelected()) {
                 System.out.println("medium");
                 EASY=false;
                 MED = true;
                 HARD=false;
             } else if (DEF.lvl_hard.isSelected()) {
                 System.out.println("hard");
                 EASY=false;
                 MED=false;
                 HARD = true;
             } 
         }
                        
         //update lives display

         /**
          * Updates the lives text on the GUI to the current lives value.
          */
         private void updateLivesText() {
             //remove old lives text
             gameScene.getChildren().remove(livesValue);
             
             //update lives with new value
             livesValue.setText("" + lives);
             livesValue.setStyle("-fx-font-size: 20;");
             livesValue.setLayoutX(DEF.SCENE_WIDTH - 90);
             livesValue.setLayoutY(60);
             
             gameScene.getChildren().add(livesValue);
         }
         
         /**
          * Spawns the bottom pipes. Ramdomly spawns yellow and white eggs on top of the pipes.
          */
         private void spawnBottomPipe() {  
             for(int i=0; i<DEF.PIPE_COUNT; i++) {
                 if (bottomPipes.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
                     double nextX = floors.get((i+1)%DEF.PIPE_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
                     double nextY = Math.floor(Math.random()*(DEF.BOT_PIPE_MAX_Y-DEF.BOT_PIPE_MIN_Y+1)+DEF.BOT_PIPE_MIN_Y);
                     bottomPipes.get(i).setPositionXY(nextX, nextY);


                     int random = (int)Math.floor(Math.random()*4);
                     
                     if (MED) {
                         random = (int)Math.floor(Math.random()*6);
                     } if (HARD) {
                         random = (int)Math.floor(Math.random()*7);
                     }


                     boolean spawnWhite = random == 0;
                     boolean spawnYellow = random == 1;
                     if (spawnWhite) {
                         whiteEggs.get(i).setPositionXY(nextX, nextY-DEF.EGG_SHIFT_Y);
                     } else if (spawnYellow) {
                         yellowEggs.get(i).setPositionXY(nextX, nextY-DEF.EGG_SHIFT_Y);
                     }
                 }
                 bottomPipes.get(i).render(gc);
                 bottomPipes.get(i).update(DEF.SCENE_SHIFT_TIME);
                 whiteEggs.get(i).render(gc);
                 whiteEggs.get(i).update(DEF.SCENE_SHIFT_TIME); 

                 yellowEggs.get(i).render(gc);
                 yellowEggs.get(i).update(DEF.SCENE_SHIFT_TIME);
             }
           }
         
         /**
          * Spawns top pipes.
          */
         private void spawnTopPipe() { 
             for(int i=0; i<DEF.PIPE_COUNT; i++) {
                 if (topPipes.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
                     double nextX = floors.get((i+1)%DEF.PIPE_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
                     double nextY = Math.floor(Math.random()*(DEF.TOP_PIPE_MAX_Y-DEF.TOP_PIPE_MIN_Y+1)+DEF.TOP_PIPE_MIN_Y);
                     topPipes.get(i).setPositionXY(nextX, nextY);
                 }
                 topPipes.get(i).render(gc);
                 topPipes.get(i).update(DEF.SCENE_SHIFT_TIME);
                 
                 if(topPipes.get(i).getPositionX()==(60.0)){ //updates score by one if passed a pipe
                     handlePipePass(1);
                 }
             }
         }
         
        /**
         * Drops pigs randomly from pipes. Pipes have a 1/4 chance of spawning a pig.
         * The first two pipes will not spawn pigs. 
         */
         private void dropPigs() {
             for (int i = 0; i < DEF.PIPE_COUNT; i++) {

                 
                 int random = (int)Math.floor(Math.random() * 4); // generate random int
                 
                 if (MED) {
                     random = (int)Math.floor(Math.random() * 2);
                 } if (HARD) {
                     random = (int)Math.floor(Math.random() * 1);
                 }

                 boolean pigsFly = random == 0;
                 
                 if (pigsFly && topPipes.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
                     double nextX = floors.get((i + 1) % DEF.PIPE_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
                     pigs.get(i).setPositionXY(nextX, 0);
                 }
    
                 pigs.get(i).update(DEF.SCENE_SHIFT_TIME);
                 
                 //pigs won't render if autopilot is on
                 if (!AUTOPILOT) {
                     pigs.get(i).render(gc);
                 }
             }  
         }
    
         // step2: update blob
         /**
          * Moves the blob. If the blob hasn't been clicked in a set time, it will drop.
          * If it has been clicked, it will raise. 
          */
         private void moveBlob() {
             
            long diffTime = System.nanoTime() - clickTime;
            
            if (!AUTOPILOT && !COLLIDED) {
             // blob flies upward with animation
                if (CLICKED && diffTime <= DEF.BLOB_DROP_TIME) {
                    int imageIndex = Math.floorDiv(counter++, DEF.BLOB_IMG_PERIOD);
                    imageIndex = Math.floorMod(imageIndex, DEF.BLOB_IMG_LEN);
                    blob.setImage(DEF.IMAGE.get("blob"+String.valueOf(imageIndex)));
                    blob.setVelocity(0, DEF.BLOB_FLY_VEL);
                }
                // blob drops after a period of time without button click
                else {
                    blob.setVelocity(0, DEF.BLOB_DROP_VEL); 
                    CLICKED = false;
                }
            }
            
            // render blob on GUI
            blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
            blob.render(gc);
         }
         
         /**
          * Updates the background periodically.
          * If it has been 5 seconds since the background was last changed, the background will
          * change to either night or day. 
          */
         private void changeBackground() {
             long displayTime = System.nanoTime() - backgroundStartTime;
             
             // if background has been displayed over 5 seconds
             if (displayTime >= 5000000000L) {
                 ImageView background = DEF.IMVIEW.get("background");
                 ImageView nightBackground = DEF.IMVIEW.get("nightBackground");
                 
                 if (DAYTIME) {
                     // Remove the day background and add the night background
                     gameScene.getChildren().remove(0);
                     gameScene.getChildren().add(0, nightBackground);
                     DAYTIME = false;
                 } else {
                     // Remove the night background and add the day background
                     gameScene.getChildren().remove(0);
                     gameScene.getChildren().add(0, background);
                     DAYTIME = true;
                 } 
                 backgroundStartTime = System.nanoTime();
             }
         }
         
         /**
          * Checks for collisions between the blob and the floor, pipes, pigs, and eggs,
          * between pigs and eggs, and maintains game over state.
          */
         public void checkCollision() {
             
           // check collision 
           for (Sprite floor: floors) {
               GAME_OVER = GAME_OVER || blob.intersectsSprite(floor);
               if (!COLLIDED && blob.intersectsSprite(floor)) {
                   score = 0;
                   updateScoreText();
                   break;
               }
               }
            
            for (Sprite pipe:bottomPipes) {
                if (!COLLIDED && blob.intersectsSprite(pipe)) {
                    lives--;
                    bounceBack();
                    break;
                }
            }
            
            for (Sprite toppipe:topPipes) {
                if (!COLLIDED && blob.intersectsSprite(toppipe)) {
                    lives--;
                    bounceBack();
                    break;
                }   
            }
                      
            // check collision with pigs
           for (Sprite pig : pigs) {
               if (!COLLIDED && blob.intersectsSprite(pig)) {
                   score = 0;
                   updateScoreText();
                   bounceBack();
                   //break;
               }
           }
            
            //check collision with eggs
            for(Sprite egg: whiteEggs) {
                if(!COLLIDED && blob.intersectsSprite(egg)) {
                    handleEggCollision(egg);
                }
            }
       
            for(Sprite egg: yellowEggs) {
                if(!COLLIDED && blob.intersectsSprite(egg)) {
                    handleEggCollision(egg);
                }
            }
        
            // end the game when blob hit stuff
            // all that should happen with gameOver is the text should be displayed and score set to 0
            // and floor stops moving
            if (GAME_OVER) {
                System.out.println("Game over!");
                updateLivesText(); 
                showHitEffect(); 
                for (Sprite floor: floors) {
                    floor.setVelocity(0, 0);
                }
                timer.stop();
                
                gameOverText = new Text("game over!");
                gameOverText.setFont(Font.font("Courier",FontWeight.EXTRA_BOLD,50));
                gameOverText.setStroke(Color.BLACK);
                gameOverText.setFill(Color.WHITE);
                gameOverText.setLayoutX(75);
                gameOverText.setLayoutY(DEF.SCENE_HEIGHT/2);
                gameScene.getChildren().add(gameOverText);
            }
            // Check if lives have reached 0, reset lives to 3 and restart the game
            // this could maybe go along/be changed with the gameOver state

            if (lives <= 0) {
                lives = 3;
                updateLivesText();
            }
            
            // check collisions between pig and yellow eggs
            for (Sprite pig: pigs) {
                for (Sprite yellowEgg: yellowEggs) {
                    // check that egg is on screen and intersects
                    if (yellowEgg.getPositionX() > -DEF.SCENE_WIDTH && yellowEgg.intersectsSprite(pig)) {
                        pigEatsEgg(yellowEgg);
                    }
                }
            }
            
            // check collisions between pig and white eggs
            for (Sprite pig: pigs) {
                for (Sprite whiteEgg: whiteEggs) {
                    // check that egg is on screen and intersects
                    if (whiteEgg.getPositionX() > -DEF.SCENE_WIDTH && whiteEgg.intersectsSprite(pig)) {
                        pigEatsEgg(whiteEgg);
                    }
                }
            }
         }
         
         /**
          * Removes the egg sprite from the screen and decreases score by 1.
          * @param egg The egg to be removed.
          */
         private void pigEatsEgg(Sprite egg) {
             egg.setPositionXY(2 * -DEF.FLOOR_WIDTH, 0);
             score--;
             updateScoreText();
         }

         /**
          * Updates the score. Called when the blob passes through pipes.
          * @param value Amount to add to score.
          */
         private void handlePipePass(int value) { //pass in how much you want to increase the score
            score = score + value;
            updateScoreText();
         }
         
         /**
          * Makes the blob move down and backwards at a rapid velocity.
          */
         private void bounceBack() {
             COLLIDED = true;
             blob.setVelocity(-200, 700); // based on blob drop val
         }
       
         /**
          * Updates the score text.
          */
         private void updateScoreText() {
             //remove old score text
             gameScene.getChildren().remove(scoreValue);
             
             // update score text with new score
             scoreValue.setText("" + score);
             scoreValue.setStyle("-fx-font-size: 20;");
             scoreValue.setLayoutX(DEF.SCENE_WIDTH - 90);
             scoreValue.setLayoutY(30);

             gameScene.getChildren().add(scoreValue);

         }
        
         //method for handling bird collisions with eggs
         /**
          * Handles collisions between the blob and eggs. If the egg is white, 5
          * points are added to the score. If the egg is yellow, autopilot mode is activated. 
          * @param egg The egg that was collided with. 
          */

         private void handleEggCollision(Sprite egg) {
                
             //remove egg from the scene/mark as collected
             egg.setPositionXY(-DEF.FLOOR_WIDTH, 0);

             
             String eggType = egg.getType();
             
             //handle different types of eggs
             switch(eggType) {
                 case "white":
                     //add 5 points for 1 white egg
                     handlePipePass(5);
                     break;
                     
                 case "golden":
                     //grant 6 second snooze time for 1 golden egg
                     AUTOPILOT = true;
                     
                     blob.setPositionXY(DEF.BLOB_POS_X, DEF.BLOB_POS_Y - 75); // raise blob
                     blob.setVelocity(0, 20); // based on blob drop velocity for y val
                     snoozeStartTime = System.nanoTime();
                     
                     break;

             }  
         }


        /**
         * Keeps autopilot mode on for 6 seconds. AUTOPILOT is true if it has been on for < 6 seconds.
         */
        private void grantSnoozeTime() {
            long snoozeTime = System.nanoTime() - snoozeStartTime;
            AUTOPILOT = snoozeTime < 6000000000L;
        }
   
        /**
         * Displays the hit effect. Called when there is a collision between the
         * blob and floor. 
         */
        private void showHitEffect() {
            ParallelTransition parallelTransition = new ParallelTransition();
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(DEF.TRANSITION_TIME), gameScene);
            fadeTransition.setToValue(0);
            fadeTransition.setCycleCount(DEF.TRANSITION_CYCLE);
            fadeTransition.setAutoReverse(true);
            parallelTransition.getChildren().add(fadeTransition);
            parallelTransition.play();
         }
         
    } // End of MyTimer class

} // End of AngryFlappyBird Class
