package com.lan.jumper.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class InputController implements InputProcessor {

    public Array<KeyState> keyStates = new Array();
    public Array<TouchState> touchStates = new Array();

    public InputController() {

        for(int i = 0 ; i < 256 ; i++) {
            keyStates.add(new KeyState(i));
        }

        touchStates.add(new TouchState(0, 0, 0, 0));
    }

    public class InputState{
        public boolean pressed = false;
        public boolean down = false;
        public boolean released = false;
    }

    public class KeyState extends InputState {
        public int key;

        public KeyState(int key) {
            this.key = key;
        }
    }

    public class TouchState extends InputState {

        public int pointer;

        public Vector2 coordinates;

        public int button;

        private Vector2 lastPosition;
        public Vector2 displacement;

        public TouchState(int x, int y, int pointer, int button){
            this.pointer = pointer;
            this.coordinates = new Vector2(x, y);
            this.button = button;

            lastPosition = new Vector2(0, 0);
            displacement = new Vector2(lastPosition.x, lastPosition.y);
        }
    }

    @Override
    public boolean keyDown(int keycode) {

        keyStates.get(keycode).pressed = true;
        keyStates.get(keycode).down = true;

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        keyStates.get(keycode).down = false;
        keyStates.get(keycode).released = true;

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        boolean pointerFound = false;

        int coordX = coordinateX(screenX);
        int coordY = coordinateY(screenY);

        for(int i = 0 ; i < touchStates.size ; i++) {
            TouchState t = touchStates.get(i);

            if(t.pointer == pointer) {
                t.down = true;
                t.pressed = true;

                t.coordinates.x = coordX;
                t.coordinates.y = coordY;
                t.button = button;

                t.lastPosition.x = coordX;
                t.lastPosition.y = coordY;

                pointerFound = true;
            }
        }

        if(!pointerFound) {
            touchStates.add(new TouchState(coordX, coordY, pointer, button));
            TouchState t = touchStates.get(pointer);

            t.down = true;
            t.pressed = true;

            t.lastPosition.x = coordX;
            t.lastPosition.y = coordY;
        }

        return true;
    }

    private int coordinateX(int screenX) {
        return screenX;
    }
    private int coordinateY(int screenY) {
        return screenY;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        TouchState t = touchStates.get(pointer);

        t.down = false;
        t.released = true;

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        int coordX = coordinateX(screenX);
        int coordY = coordinateY(screenY);

        TouchState t = touchStates.get(pointer);

        t.coordinates.x = coordX;
        t.coordinates.y = coordY;


        t.displacement.x = coordX - t.lastPosition.x;
        t.displacement.y = coordY - t.lastPosition.y;

        t.lastPosition.x = coordX;
        t.lastPosition.y = coordY;

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void update() {

        for(int i = 0; i < keyStates.size; i++) {
            KeyState k = keyStates.get(i);

            k.released = false;
            k.pressed = false;
        }

        for (int i = 0; i < touchStates.size; i++) {
            TouchState t = touchStates.get(i);

            t.pressed = false;
            t.released = false;

            t.displacement.x = 0;
            t.displacement.y = 0;
        }
    }

    public boolean isKeyPressed(int keycode) {
        return keyStates.get(keycode).pressed;
    }

    public boolean isKeyDown(int keycode) {
        return keyStates.get(keycode).down;
    }

    public boolean isKeyReleased(int keycode) {
        return keyStates.get(keycode).released;
    }

    public boolean isTouchPressed(int pointer){
        return touchStates.get(pointer).pressed;
    }
    public boolean isTouchDown(int pointer){
        return touchStates.get(pointer).down;
    }
    public boolean isTouchReleased(int pointer){
        return touchStates.get(pointer).released;
    }

    public Vector2 touchCoordinates(int pointer){
        return touchStates.get(pointer).coordinates;
    }
    public Vector2 touchDisplacement(int pointer){
        return touchStates.get(pointer).displacement;
    }
}
