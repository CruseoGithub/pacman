package uas.lntv.pacmangame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import uas.lntv.pacmangame.PacManGame;

public class DesktopLauncher {
<<<<<<< HEAD:desktop/src/com/vieth/pacman/desktop/DesktopLauncher.java
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new PacMan(), config);
    }
=======
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new PacManGame(), config);
	}
>>>>>>> dev_Denis:desktop/src/uas/lntv/pacmangame/desktop/DesktopLauncher.java
}
