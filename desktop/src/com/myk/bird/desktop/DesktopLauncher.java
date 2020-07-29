package com.myk.bird.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.myk.bird.MainGame;
import com.myk.bird.res.Res;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = (int) Res.FIX_WORLD_WIDTH;
		config.height = (int) Res.FIX_WORLD_HEIGHT;
		config.resizable = false;

		new LwjglApplication(new MainGame(), config);
	}
}
