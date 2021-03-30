# Pac-Man Game
<div>
  <img style="vertical-align:middle" src="https://github.com/CruseoGithub/pacman/blob/main/android/assets/maps/LNTVLogo.png" width="150" height="100" />
</div>

 Created by LNTV productions.
 This project was created as a team effort over a period of 9 weeks.
 Very mucho danke to everyone who was involved.

 # About the Game

 This game is inspired by the original arcade pac-man game.
 The protagonist is Pac-Man a yellow ball with a mouth. Pac-Man finds himself in a maze that he can not escape from.
 Only eating all the dots which are scattered around the map will get pac-man to the next level. 
 But beware! There are enemy ghosts on the map too and they are deadly! At first, they might not seem too smart but they will get more and more intelligent.
 Keep in mind, they eventually will try to cut off every escape route.
 Don't be too freighted though, there are some special items to collect which will give pac-man some pretty cool buffs:
 - Hunter item: pac-man gets into rage mode! He becomes very fast and can kill ghosts for a period of time.
 - Life item: Pac-Man gets an extra Life (1up)
 - Slow item: Ghosts are slower for a short time
 - Time item: The clock is always ticking! ...but now Pac-Man has 10 more seconds.
 
 The game will iterate through a bunch of maps and increase the difficulty each time. 
 This means, over time there will be more ghosts that get smarter. 
 Also, the overall speed of every actor will increase. 
 
 The first map features a classic pac-man design and layout. All other maps keep the same layout but have some very unique graphics. 
 We wanted to have our own take on the game, so we did put a lot of effort into designing these maps. Each map has its own look and feel.
 The custom SFX and music give the game a personal touch too. ("ALARM!!!!")
 
 
 # How to play
 
 In the beginning, you can doubletap the icons to skip the SplashScreen and directly start loading all the assets.
 After loading you will see PacMan in the MenuScreen, where you need to move PacMan onto the panel you would like to activate (Play or Settings).
 If it's your first time here, you will be asked for your name.
 The game is basically controlled via moving pacman into one of 4 directions (Up, Down, Left, Right).
 If you're playing on the desktop version, you can use the arrow keys of your keyboard (all the time).
 The default setting draws 4 arrows on the screen, which will detect a touch input anywhere on the screen in the direction of the arrow
 (so more than half of the screen is direction UP).
 In the SettingsScreen PacMan will stop at every crossroads which will make it easier to reach the desired settings (Same in the PauseScreen).
 There you can change the controller to a joystick-controller, which can also be used as a swipe controller.
 The joystick will keep the position you first touched in mind and then calculate in which direction you moved from that point on.
 In the SettingsScreen you can also turn on or off the music and sound effects (but you shouldn't because they are awesome) and you can change the player's name.
 
 In the game itself, PacMan will only stop, if you run towards a wall and didn't choose another direction before.
 If you change the direction but there is a wall blocking the way, PacMan will keep his current direction until it is possible for him to turn to the new direction.
 This allows you to choose a new direction before PacMan reaches a crossroad that opens up to the desired direction.
 
 There are 5 kinds of items on the map:
 1. Dots: You need to eat all of them to reach the next level. 1 Dot = 1 ScorePoint
 2. CherryCoins: PacMan will turn red and mad. He speeds up and is able to eat ghosts for a short period of time. 1 ghost = 50 ScorePoints
 3. RunningMan: What would you expect? Exactly! This item slows down the ghosts for a short period of time.
 4. ClockCoin: This item gives a little time bonus of 10 seconds.
 5. Heart: Restores one life of PacMan. If you already have got 3 lives, you will receive 75 ScorePoints instead.
 
 You can pause the game by touching the HUD or on the desktop version by hitting SPACE.
 
 The higher your level is, the more difficult the game gets. Here you can see the changes:
 1. Level 1: No. of ghosts: 1, Difficulty: red-easy, Speed: 2
 2. Level 3: No. of ghosts: 2, Difficulty: orange-easy
 3. Level 5: No. of ghosts: 3, Difficulty: red-medium, pink-easy
 4. Level 7: Difficulty: orange-medium Speed: 4
 5. Level 9: Difficulty: pink-medium
 6. Level 13: Difficulty: red-hard
 7. Level 20: Difficulty: orange-hard
 8. Level 25: Speed: 8
 9. Level 31: Difficulty: pink-hard
 
 Ghost difficulties:
 
 Easy: Moves randomly until the distance to PacMan is lower than 16 tiles. Then it will just compare the horizontal to the vertical distance and will try to decrease the bigger one. Doesn't look out for walls while searching for PacMan.
 
 Medium: Moves randomly until the distance to PacMan is lower than 16 tiles. Then it will use the A*-Algorithm to find the shortest path to PacMan and go that way.
 The A*-Algorithm will consider other ghosts as obstacles and will choose another path. This avoids stacking ghosts, which would make you lose more than one life by contact.
 It also makes the ghosts more dangerous, because they can cut off the way of PacMan.
 
 Hard: Uses the A*-Algorithm permanently. Very dangerous. Very vicious... RUN!
 
 If the time is almost up (30 seconds remaining), you will hear an alert sound and the time display will blink red and white.
 If the time elapses or you lose all of your lives, the game will end and lead you back into the MenuScreen, unless you achieved a new highscore, this will lead you to the ScoreScreen.

 # Installation Guide:

<p>Where to find the Java Documentation:&nbsp;<a href="https://github.com/CruseoGithub/pacman/tree/main/Javadocs">pacman\javadocs</a></p>
<p>Where to find the UML class-diagrams:&nbsp;<a href="https://github.com/CruseoGithub/pacman/tree/main/diagrams">pacman\diagrams</a></p>
<p>Where to find the APK file:&nbsp;<a href="https://github.com/CruseoGithub/pacman/tree/main/android/release">pacman\android\release</a></p>
<h3>How to run on Smartphones:</h3>
<p>Download the APK-file to your smartphone. Installing it might prompt a warning: In this case, you have to agree to "insecure sources" in your android settings first. If it doesn't work instantly, you may need a APK-Installer app, you can get from the play store. After installing the APK-file run the game from your home screen.</p>
<h3>How to run on PC:</h3>
<p>Download and install Android Studio.&nbsp;Click on open project and select the pacman project.&nbsp;You can run the game with a Desktop Launcher or via a virtual android environment.</p>
<p><strong>Desktop Launcher:</strong></p>
<p>If you don't have a Desktop Launcher, you can add one like this: Click on Run -&gt; "Edit Configurations..." and add a new Application Launcher.</p>
<p>For Main class select "uas.lntv.pacmangame.desktop.DesktopLauncher"</p>
<p>For use of classpath module select "Pacman.desktop"</p>
<p><strong>Virtual&nbsp;environment:</strong></p>
<p>Create a new virtual device: Click on Tools -&gt; AVD Manager -&gt; "Create Virtual Device..."&nbsp;(We went with Pixel 4, API 29).</p>
<p>After that you have to add an Android Launcher: Click on Run -&gt; "Edit Configurations..." and add a new Android app.</p>
<p>For the Module select "Pacman.android"</p>
 
 # Credits:
 - Libgdx
 - bfxr.net
 - beepbox.co
 - soundjay.com
 - fesliyanstudios.com
 - audiotrimmer.com
 - clipconverter.cc
 - mapeditor.org
 - GIMP2
 - pngfind.com
 - youtube.com/c/BrentAurelisCodeSchool/featured
 
