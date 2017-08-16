package com.monoton.horizont.crowd.pattern.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.monoton.horizont.crowd.pattern.CrowndPatternCommand;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		cfg.title = "Title";

		cfg.height = 610;
		cfg.width = 987;
		new LwjglApplication(new CrowndPatternCommand(), cfg);
	}
}
