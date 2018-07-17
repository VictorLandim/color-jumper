package com.lan.jumper.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.lan.jumper.game.ColorJumperGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = (int) (360 * 1.2);
        config.height = (int) (360 * 16 / 9 * 1.2);
        config.initialBackgroundColor = new Color(0x263238ff);
        config.title = "Super BoxBoy Color Jumper";
        new LwjglApplication(new ColorJumperGame(), config);
    }
}
