package com.lan.jumper.entity;

import com.lan.jumper.common.EntityColor;
import com.lan.jumper.config.GameConfig;

public class Player extends EntityBase {

    private float lastX, lastY;
    private float velocity;
    private PlayerState state;

    private EntityColor previousColor;

    private float jetpackCounter = 0;

    public Player() {
        super();
        state = PlayerState.FALLING;
        color = EntityColor.BLUE;
        previousColor = EntityColor.BLUE;
    }

    public void updateJetpack(float delta) {
        if (jetpackCounter > 0) {
            jetpackCounter -= delta;
            velocity = GameConfig.PLAYER_JETPACK_SPEED;
        } else {
            jetpackCounter = 0;
        }
    }

    public void startJetpack() {
        if (jetpackCounter == 0) {
            jetpackCounter = 2f;
        }
    }

    public void jump() {
        velocity = GameConfig.PLAYER_JUMP_SPEED;
    }

    public void jumpHigh() {
        velocity = GameConfig.PLAYER_SPRING_JUMP_SPEED;
    }

    public void moveLeft(float delta) {
        move(delta, (-1) * GameConfig.PLAYER_MOVESPEED);
    }

    public void moveRight(float delta) {
        move(delta, GameConfig.PLAYER_MOVESPEED);
    }

    public void move(float delta, float amount) {
        float x = getX() + amount * delta;
        setPosition(x, getY());
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public void setStanding() {
        state = PlayerState.STANDING;
    }

    public void setFalling() {
        state = PlayerState.FALLING;
    }

    public void setJumping() {
        state = PlayerState.JUMPING;
    }

    public void setDead() {
        state = PlayerState.DEAD;
    }

    public PlayerState getState() {
        return state;
    }

    public void setLastPosition(float x, float y) {
        lastX = x;
        lastY = y;
    }

    public float getLastX() {
        return lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public float getJetpackCounter() {
        return jetpackCounter;
    }

    public void nextColor() {
        if (color.isBlue()) setColor(EntityColor.RED);
        else if (color.isRed()) setColor(EntityColor.YELLOW);
        else if (color.isYellow()) setColor(EntityColor.BLUE);
        else throw new IllegalStateException("Invalid color!");
    }

    public boolean isChangingColor() {
        return color != previousColor;
    }

    public EntityColor getPreviousColor() {
        return previousColor;
    }

    public void setPreviousColor(EntityColor previousColor) {
        this.previousColor = previousColor;
    }
}
