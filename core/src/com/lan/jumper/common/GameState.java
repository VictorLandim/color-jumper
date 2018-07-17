package com.lan.jumper.common;

public enum GameState {
    READY,
    PLAYING,
    FALLING,
    GAME_OVER;

    public boolean isReady() { return this == READY; }
    public boolean isPlaying() { return this == PLAYING; }
    public boolean isFalling() { return this == FALLING; }
    public boolean isGameOver() { return this == GAME_OVER; }
}
