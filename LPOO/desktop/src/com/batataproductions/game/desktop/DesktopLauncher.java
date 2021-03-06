package com.batataproductions.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.batataproductions.game.DeathmatchMania;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		DeathmatchMania game = new DeathmatchMania();
		config.width = game.WIDTH;
		config.height = game.HEIGHT;
		config.addIcon("icon-16.png", Files.FileType.Internal);
		config.addIcon("icon-32.png", Files.FileType.Internal);
		config.addIcon("icon-128.png", Files.FileType.Internal);
		new LwjglApplication(game, config);
	}
}
