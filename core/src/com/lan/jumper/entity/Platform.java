package com.lan.jumper.entity;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Platform extends EntityBase {

    public Platform() {
        super();
    }

    public void render(SpriteBatch batch, NinePatch ninePatch) {
        ninePatch.draw(batch, getX(), getY(), getWidth(), getHeight());
    }
}
