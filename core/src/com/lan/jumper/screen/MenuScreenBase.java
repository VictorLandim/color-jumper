package com.lan.jumper.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lan.jumper.config.GameConfig;
import com.lan.jumper.game.ColorJumperGame;
import com.lan.jumper.util.GdxUtils;

public abstract class MenuScreenBase extends ScreenAdapter {

    protected final ColorJumperGame game;
    protected final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    public MenuScreenBase(ColorJumperGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.SPACE) onKeyDown();
                return false;
            }
        });
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        stage.addActor(createUi());
    }

    protected abstract Actor createUi();
    protected abstract void onKeyDown();

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen(GameConfig.BACKGROUND_COLOR);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
