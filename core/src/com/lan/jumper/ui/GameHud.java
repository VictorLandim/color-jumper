package com.lan.jumper.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lan.jumper.assets.AssetDescriptors;
import com.lan.jumper.config.GameConfig;
import com.lan.jumper.game.ColorJumperGame;
import com.lan.jumper.screen.GameController;
import com.lan.jumper.screen.MenuScreen;

public class GameHud {
    private static final Logger log = new Logger(GameHud.class.getSimpleName(), Logger.DEBUG);
    private static final boolean DEBUG = false;
    private Stage stage;
    private Skin skin;

    private final ColorJumperGame game;
    private SpriteBatch batch;
    private AssetManager assetManager;

    private GameController controller;

    private Label scoreLabel;
    private Button backButton;

    public GameHud(ColorJumperGame game, GameController controller, Viewport viewport) {
        this.game = game;
        this.batch = game.getBatch();
        this.controller = controller;
        this.assetManager = game.getAssetManager();

        this.stage = new Stage(viewport, batch);
        skin = assetManager.get(AssetDescriptors.SKIN);

        scoreLabel = createRetryButton();
        backButton = createBackButton();

        stage.addActor(createUi());
        stage.setDebugAll(DEBUG);
    }

    private Label createRetryButton() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");
        labelStyle.fontColor = Color.WHITE;

        Label label = new Label("", skin);
        label.setAlignment(Align.left);
        label.setStyle(labelStyle);
        label.setFontScale(1.1f);

        return label;
    }

    private Button createBackButton() {
        Button button = new Button(skin, "back");
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.goToMenu();
            }
        });
        button.setWidth(GameConfig.HUD_WIDTH * 0.15f);
        button.setHeight(GameConfig.HUD_WIDTH * 0.15f);
        return button;
    }

    private Actor createUi() {

        Table root = new Table();
        root.top();
        root.defaults().pad(10f);

        root.add(backButton).left().expandX();
        root.add(scoreLabel).right().expandX();

        root.setFillParent(true);
        root.pack();

        return root;
    }

    public void render(float delta) {
        scoreLabel.setText(controller.getScore());
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }
}
