package com.lan.jumper.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lan.jumper.assets.AssetDescriptors;
import com.lan.jumper.assets.AssetPaths;
import com.lan.jumper.config.GameConfig;
import com.lan.jumper.game.ColorJumperGame;
import com.lan.jumper.util.GdxUtils;

public class LoadingScreen extends ScreenAdapter {

    private float displayTime = 1f;

    private ColorJumperGame game;
    private AssetManager assetManager;

    private Viewport viewport;
    private SpriteBatch batch;
    private Stage stage;
    private Image logo;

    public LoadingScreen(ColorJumperGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        this.batch = game.getBatch();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        stage = new Stage(viewport, batch);

        logo = new Image(new Texture(Gdx.files.internal(AssetPaths.LOGO)));

        float aspectRatio = logo.getHeight() / logo.getWidth();
        float width = GameConfig.HUD_WIDTH * 0.4f;
        float height = width * aspectRatio;

        logo.setWidth(width);
        logo.setHeight(height);
        logo.setScaling(Scaling.fit);
        logo.setPosition((stage.getWidth() - logo.getWidth()) / 2f, (stage.getHeight() - logo.getHeight()) / 2f);

        logo.getColor().a = 0;

        logo.addAction(Actions.fadeIn(1f));

        stage.addActor(logo);

        ParticleEffectLoader.ParticleEffectParameter particleEffectParameter = new ParticleEffectLoader.ParticleEffectParameter();
        particleEffectParameter.atlasFile = AssetPaths.GAMEPLAY;
        assetManager.load(AssetPaths.PARTICLE, ParticleEffect.class, particleEffectParameter);
        assetManager.load(AssetDescriptors.GAMEPLAY_ATLAS);
        assetManager.load(AssetDescriptors.SKIN);
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen(GameConfig.BACKGROUND_COLOR);
        update(delta);

        stage.act();
        stage.draw();
    }

    private void update(float delta) {
        if (assetManager.update()) {
            displayTime -= delta;

            if (displayTime <= 0) {
                logo.addAction(Actions.sequence(
                        Actions.fadeOut(1f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                game.setScreen(new MenuScreen(game));
                            }
                        })
                ));
            }
        }
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
