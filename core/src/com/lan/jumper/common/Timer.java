package com.lan.jumper.common;

import com.badlogic.gdx.Gdx;

public class Timer {
    private float counter;

    public Timer() {
        counter = 0;
    }

    public void after(float seconds, TimerCallback callback) {
        counter += Gdx.graphics.getDeltaTime();
        if (counter >= seconds) {
            counter = 0;
            callback.run();
        }
    }

    public void reset() {
        counter = 0;
    }
}
