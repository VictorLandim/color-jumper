package com.lan.jumper.common;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.lan.jumper.config.GameConfig;

public class CameraController {

    private Vector2 target;
    private OrthographicCamera camera;
    private boolean isFollowingDescent = false;
    private boolean isAfterReset = false;
    private float delay = 0;

    public CameraController(OrthographicCamera camera) {
        this.camera = camera;
        target = new Vector2();
    }

    public void setTarget(float x, float y) {
        target.set(x, y);
    }

    public void update(float delta) {

        if (isFollowingDescent) {
            delay += delta;
            if(delay >= GameConfig.GAME_OVER_CAMERA_DELAY) {
                camera.position.y = camera.position.y + (target.y - camera.position.y) * GameConfig.GAME_OVER_CAMERA_LERP * delta;
            }
        } else if(isAfterReset) {
          if(camera.position.y > target.y) {
              camera.position.y = camera.position.y + (target.y - camera.position.y) * GameConfig.RESET_CAMERA_LERP * delta;
          } else {
              isAfterReset = false;
          }
        } else {
            //if player is going up
            //or if game was just reset
            if (target.y > camera.position.y) {
                camera.position.y = camera.position.y + (target.y - camera.position.y) * GameConfig.CAMERA_LERP * delta;
            }
            camera.position.y += GameConfig.CAMERA_MOVE_SPEED * delta;
        }

        camera.update();
    }

    public void setFollowDescent(boolean flag) {
        if (flag) {
            //follow player falling down
            if (!isFollowingDescent) {
                isFollowingDescent = true;
                delay = 0;
            }
        } else {
            isFollowingDescent = false;
        }
    }

    public void reset() {
        isAfterReset = true;
    }
}