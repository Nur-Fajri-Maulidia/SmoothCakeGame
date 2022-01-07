package com.semutlevel.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.semutlevel.game.desktop.SemutGame;

class SemutLauncher {
	public static void main(String[] args) {
		SemutGame myProgram = new SemutGame();
		LwjglApplication launcher = new LwjglApplication(myProgram);
	}
}
