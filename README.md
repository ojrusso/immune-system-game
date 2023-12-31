# AngryFlappyBird
AngryFlappyBird is an implementation of a modified version of the game flappy bird. 

** How to Run the Program **
- Make sure a JavaFX library is added to the project folder.
- In the run configurations, make sure JavaFX is added to VM arguments using
  --module-path="<your javaFX library path" --add-   modules="javafx.base,javafx.controls,javafx.media"
- Make sure the project is set to angryflappybird-team-ako and the main class is set to angryflappybird.AngryFlappyBird
- The only checked box should be "Use the -XX:+ShowCodeDetailsInExceptionMessages argument when launching"

** Program Features **
- The "bird" (germ) will fly when the user clicks the "go" button. Try to avoid obstacles (walls, floors and macrophages)! 
- Walls of white blood cells will appear in pairs every fixed amount of time. When the germ collides with a wall, one life is taken and the germ will fall backwards.
- Potions will appear randomly on upward facing pipes. If a macrophage collects a potion before the germ, points will be lost. If the germ collects a purple potion, it will be awarded bonus points. If it collects a red potion, 6 seconds of autopilot mode will be triggered. During autopilot mode, the germ will not collide with any obstacles and macrophages will not appear. 
- If the germ collides with a macrophage, the score is reset to 0 and the germ immediately falls backwards.
- The floor scrolls through continuously. If the germ collides with the floor, it stops moving and the score is set to 0.
- The background changes from night to day periodically.
- User can select a difficulty level before starting, which will affect how often macrophages and potions spawn.
- Score and lives count are recorded and displayed. 