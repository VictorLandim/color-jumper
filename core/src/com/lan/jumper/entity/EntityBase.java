package com.lan.jumper.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.lan.jumper.common.EntityColor;

public abstract class EntityBase {

    // == attributes ==
    protected float x;
    protected float y;

    protected float width = 1;
    protected float height = 1;

    protected Rectangle bounds;

    protected EntityColor color;

    // == constructors ==
    public EntityBase() {
        bounds = new Rectangle(x, y, width, height);
    }

    // == public methods ==
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        updateBounds();
    }

    public void setRandomColor() {
        this.color = EntityColor.getRandomColor();
    }

    public void setColor(EntityColor color) {
        this.color = color;
    }

    public EntityColor getColor() {
        return color;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
        updateBounds();
    }

    public void setY(float y) {
        this.y = y;
        updateBounds();
    }

    public void render(SpriteBatch batch, TextureRegion texture) {
        batch.draw(texture,
                getX(),
                getY(),
                getWidth(), getHeight());
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void updateBounds() {
        bounds.setPosition(x, y);
        bounds.setSize(width, height);
    }

}
