package angryflappybird;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AngryFlappyBirdTest {
    
    private AngryFlappyBird game;

    @BeforeEach
    void setUp() throws Exception {
        game = new AngryFlappyBird();
    }

    @Test
    void testMouseClickHandler_GameOver() {
        game.GAME_OVER = true;
        game.mouseClickHandler(null);

        assertFalse(game.GAME_OVER);
        assertFalse(game.GAME_START);
        assertTrue(game.CLICKED);
    }
    
    @Test
    void testMouseClickHandler_GameStart() {
        game.GAME_OVER = false;
        game.GAME_START = false;
        game.mouseClickHandler(null);

        assertTrue(game.GAME_START);
        assertTrue(game.CLICKED);
    }
    
    
    @Test
    void testResetGameScene_FirstEntry() {
        game.resetGameScene(true);

        assertNotNull(game.gameScene);
        assertNotNull(game.gameControl);
        assertFalse(game.CLICKED);
        assertFalse(game.GAME_OVER);
        assertFalse(game.GAME_START);
        assertFalse(game.AUTOPILOT);
        assertFalse(game.COLLIDED);
        assertTrue(game.DAYTIME);
        assertNotNull(game.timer);
        assertNotNull(game.floors);
        assertNotNull(game.topPipes);
        assertNotNull(game.bottomPipes);
        assertNotNull(game.whiteEggs);
        assertNotNull(game.pigs);
        assertNotNull(game.yellowEggs);
        assertNotNull(game.scoreText);
        assertNotNull(game.livesText);
        assertNotNull(game.scoreValue);
        assertNotNull(game.livesValue);
        assertNotNull(game.readyText);
    }
    
    @Test
    void testResetGameScene_NotFirstEntry() {
        game.resetGameScene(false);

        assertNotNull(game.gameScene);
        assertNotNull(game.gameControl);
        assertNotNull(game.floors);
        assertNotNull(game.topPipes);
        assertNotNull(game.bottomPipes);
        assertNotNull(game.whiteEggs);
        assertNotNull(game.pigs);
        assertNotNull(game.yellowEggs);
        assertFalse(game.CLICKED);
        assertFalse(game.GAME_OVER);
        assertFalse(game.GAME_START);
        assertFalse(game.DAYTIME);
        assertFalse(game.AUTOPILOT);
        assertFalse(game.COLLIDED);
        assertNotNull(game.timer);
    }

}
