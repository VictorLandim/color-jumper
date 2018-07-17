package com.lan.jumper.entity;

public enum PlayerState {
    STANDING,
    JUMPING,
    FALLING,
    DEAD,
    ROTATING;

    public boolean isStanding() { return this == STANDING; }
    public boolean isJumping() { return this == JUMPING; }
    public boolean isFalling() { return this == FALLING; }
    public boolean isDead() { return this == DEAD; }
    public boolean isRotating() { return this == ROTATING; }
}
