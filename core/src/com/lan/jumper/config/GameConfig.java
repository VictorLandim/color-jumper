package com.lan.jumper.config;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import static com.badlogic.gdx.Gdx.app;

public class GameConfig {
    private GameConfig() {
    }

    public static final boolean IS_MOBILE =
            Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;

    public static final float VIEWPORT_WIDTH = 9f;
    public static final float VIEWPORT_HEIGHT = 16f;

    public static final Color BACKGROUND_COLOR = new Color(0x263238ff);
//    public static final Color BACKGROUND_COLOR = new Color(0xc0c0c9ff);
    public static final Color LOADING_SCREEN_COLOR = new Color(0x333333ff);

    public static final float HUD_WIDTH = 360f;
    public static final float HUD_HEIGHT = 360f * 16f / 9f;

    public static final float PLAYER_WIDTH = 0.8f;
    public static final float PLAYER_HEIGHT = PLAYER_WIDTH;
    public static final float PLAYER_MOVESPEED = 8f;
    public static final float PLAYER_JUMP_SPEED = 14f;
    public static final float PLAYER_SPRING_JUMP_SPEED = 20f;
    public static final float PLAYER_JETPACK_SPEED = 18f;
    public static final float PLAYER_ROTATE_DURATION = 0.025f;

    public static final float GRAVITY = -25f;

    public static final float ACCELEROMETER_SENSITIVITY = 4f;

    public static final int PLATFORMS_PER_SCREEN = 10;
    public static final float PLATFORM_HEIGHT = 0.6f;
    public static final int PLATFORM_EDGE = 1;

    public static final float SPRING_WIDTH = 0.7f;
    public static final float SPRING_HEIGHT = 0.6f;

    public static final float JETPACK_WIDTH = 1.1f;
    public static final float JETPACK_HEIGHT = 1.15f;

    public static final float CAMERA_MOVE_SPEED = 1.1f;
    public static final float CAMERA_LERP = 9f;
    public static final float GAME_OVER_CAMERA_LERP = 31f;
    public static final float GAME_OVER_CAMERA_DELAY = 0.5f;
    public static final float RESET_CAMERA_LERP = 10f;
    public static final float RESET_CAMERA_SPEED = 30f;

    public static final float PARTICLE_SIZE = 0.4f;

    public static final float GAME_START_TIMER = 3f;
}
