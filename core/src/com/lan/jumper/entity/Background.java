package com.lan.jumper.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
    private float x;
    private float y;
    private float width;
    private float height;

    public Background(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void update(float delta) {
    }

    public void draw(SpriteBatch batch, TextureRegion region) {
        batch.draw(region, x, y, width, height);
    }
}
