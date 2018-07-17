package com.lan.jumper.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.lan.jumper.assets.AssetDescriptors;
import com.lan.jumper.assets.RegionNames;
import com.lan.jumper.config.GameConfig;
import com.lan.jumper.game.ColorJumperGame;

public class MenuScreen extends MenuScreenBase {

    public MenuScreen(ColorJumperGame game) {
        super(game);
    }

    @Override
    protected void onKeyDown() {
        show();
    }

    @Override
    protected Actor createUi() {
        Skin skin = assetManager.get(AssetDescriptors.SKIN);
        TextureAtlas atlas = assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS);

        Image logoImage = setupLogo();

        Button playButton = new Button(skin, "play");
        Button highScoresButton = new Button(skin, "highscores");
        Button settingsButton = new Button(skin, "options");

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                play();
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settings();
            }
        });
        highScoresButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                highscores();
            }
        });

        Table buttonTable = new Table();
        float buttonSize = GameConfig.HUD_WIDTH * 0.2f;
        float padding = 5f;

        buttonTable.defaults().pad(5f).center();

        buttonTable.add(playButton)
                .width(buttonSize * 2f + padding * 2f)
                .height(buttonSize)
                .colspan(2)
                .padBottom(15f)
                .row();

        buttonTable.add(highScoresButton)
                .width(buttonSize)
                .height(buttonSize)
                .colspan(1);

        buttonTable.add(settingsButton)
                .width(buttonSize)
                .height(buttonSize)
                .colspan(1);

        buttonTable.setVisible(false);

        Table root = new Table();
        root.setBackground(
                new TextureRegionDrawable(atlas.findRegion(RegionNames.MAIN_BACKGROUND))
        );

        root.add(logoImage).width(GameConfig.HUD_WIDTH * 0.85f).center().row();
        root.add(buttonTable).grow();

        root.center();
        root.setFillParent(true);
        root.pack();

        float x = buttonTable.getX();
        float y = buttonTable.getY();
        buttonTable.setY(-GameConfig.HUD_HEIGHT);
        buttonTable.setVisible(true);
        buttonTable.addAction(Actions.moveTo(x, y, 0.35f, Interpolation.sineIn));

        logoImage.setScale(4f);
        logoImage.addAction(Actions.scaleTo(1f, 1f, 0.35f));

        return root;
    }

    private Image setupLogo() {
        TextureAtlas atlas = assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS);
        Image logoImage = new Image(atlas.findRegion(RegionNames.GAME_LOGO));

        float aspectRatio = logoImage.getHeight() / logoImage.getWidth();
        float width = GameConfig.HUD_WIDTH * 0.75f;
        float height = width * aspectRatio;

        logoImage.setWidth(width);
        logoImage.setHeight(height);
        logoImage.setScaling(Scaling.fit);
        logoImage.setOrigin(Align.center);

        logoImage.addAction(Actions.repeat(
                RepeatAction.FOREVER,
                Actions.sequence(
                        Actions.rotateTo(2f, 1.5f),
                        Actions.rotateTo(-2f, 1.5f)
                )));
        return logoImage;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    private void play() {
        game.setScreen(new GameplayScreen(game));
    }

    private void settings() {
        quit();
    }

    private void highscores() {
        quit();
    }

    private void quit() {
        Gdx.app.exit();
    }
}
