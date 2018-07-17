package com.lan.jumper.screen;

import com.badlogic.gdx.*;
import com.lan.jumper.game.ColorJumperGame;

public class GameplayScreen extends ScreenAdapter implements InputProcessor  {

    private ColorJumperGame game;

    private GameController controller;
    private GameRenderer renderer;

    public GameplayScreen(ColorJumperGame game) {
        this.game = game;
    }

    public void show() {
        controller = new GameController(game);
        renderer = new GameRenderer(controller);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(renderer.getHud().getStage());
        multiplexer.addProcessor(controller.getInputController());
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void render(float delta) {
        controller.update(delta);
        renderer.render(delta);
    }

    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    public void hide() {
        dispose();
    }

    public void dispose() {
        renderer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.SPACE) {
            System.out.println("dsadasdsa");
            controller.init();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
