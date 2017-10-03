package com.monoton.horizont.crowd.pattern.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.monoton.horizont.crowd.pattern.CrowndPatternCommand;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		cfg.title = "Light Dance";

		cfg.height = 610;
		cfg.width = 987;

//		cfg.fullscreen = true;
/*
		cfg.height = 1000;
		cfg.width = 1500;*/
		new LwjglApplication(new CrowndPatternCommand(), cfg);
	}
}
