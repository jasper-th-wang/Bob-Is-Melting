package dev.jasper.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import dev.jasper.game.BobIsMelting;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (final String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(BobIsMelting.V_WIDTH * 2, BobIsMelting.V_HEIGHT * 2);
		config.setTitle("Bob is Melting");
		new Lwjgl3Application(new BobIsMelting(), config);
	}
}
