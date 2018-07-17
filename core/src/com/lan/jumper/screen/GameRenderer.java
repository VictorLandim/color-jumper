package com.lan.jumper.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lan.jumper.assets.AnimationNames;
import com.lan.jumper.assets.AssetDescriptors;
import com.lan.jumper.assets.AssetPaths;
import com.lan.jumper.assets.RegionNames;
import com.lan.jumper.common.CameraController;
import com.lan.jumper.common.ColorValues;
import com.lan.jumper.common.EntityColor;
import com.lan.jumper.common.GameState;
import com.lan.jumper.config.GameConfig;
import com.lan.jumper.debug.DebugCameraController;
import com.lan.jumper.entity.*;
import com.lan.jumper.ui.GameHud;
import com.lan.jumper.util.GdxUtils;
import com.lan.jumper.util.ViewportUtils;

import javax.swing.plaf.synth.Region;

public class GameRenderer implements Disposable {

    private GameController controller;

    private DebugCameraController debugCameraController;
    private CameraController cameraController;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Viewport hudViewport;

    private GameHud gameHud;

    private ShapeRenderer renderer;
    private SpriteBatch batch;

    private Background background;

    private AssetManager assetManager;

    private TextureRegion backgroundRegion;

    private NinePatch bluePlatform;
    private NinePatch redPlatform;
    private NinePatch yellowPlatform;

    private TextureRegion bluePlayer;
    private TextureRegion redPlayer;
    private TextureRegion yellowPlayer;

    private TextureRegion blueSpring;
    private TextureRegion redSpring;
    private TextureRegion yellowSpring;

    private TextureRegion blueJetpack;
    private TextureRegion redJetpack;
    private TextureRegion yellowJetpack;

    private Animation<TextureRegion> blueToRed;
    private Animation<TextureRegion> redToYellow;
    private Animation<TextureRegion> yellowToBlue;

    private Animation<TextureRegion> currentAnimation;

    private float startTime;

    private ParticleEffect explosion;

    public GameRenderer(GameController controller) {
        this.controller = controller;

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT, camera);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        gameHud = new GameHud(controller.getGame(), controller, hudViewport);

        background = new Background(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2,
               GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);

        debugCameraController = new DebugCameraController();
//        debugCameraController.setTargetCamera(camera);
//        debugCameraController.setStartPosition(GameConfig.VIEWPORT_WIDTH / 2f, GameConfig.VIEWPORT_HEIGHT / 2f);

        cameraController = new CameraController(camera);

        renderer = controller.getGame().getRenderer();
        batch = controller.getGame().getBatch();
        assetManager = controller.getGame().getAssetManager();

        startTime = 0;

        initTextures();
        initAnimations();
        initParticles();
    }

    public void render(float delta) {
        GdxUtils.clearScreen();
        handleCamera(delta);
        renderGameplay(delta);
        renderUi(delta);
    }

    private void renderGameplay(float delta) {
        viewport.apply();

        Matrix4 combined = viewport.getCamera().combined;
        renderer.setProjectionMatrix(combined);
        batch.setProjectionMatrix(combined);


        batch.begin();
        renderBackground();
        renderPlatforms();
        renderObjects();
        renderPlayer(delta);
        batch.end();

//        renderDebugLines();
    }

    private void renderUi(float delta) {
        hudViewport.apply();
        gameHud.render(delta);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    public void dispose() {
        batch.dispose();
        renderer.dispose();
    }

    private void renderDebugLines() {
        ViewportUtils.drawGrid(viewport, renderer);
    }

    private void renderBackground() {
        background.setY(camera.position.y - GameConfig.VIEWPORT_HEIGHT / 2f);
        background.draw(batch, backgroundRegion);
    }

    private void renderDebugHeight() {
        renderer.setColor(Color.FIREBRICK);
        renderer.line(0f, controller.getHeight(), GameConfig.VIEWPORT_WIDTH, controller.getHeight());
    }

    private void renderPlayer(float delta) {

        if(controller.getState() == GameState.GAME_OVER) {

            Player player = controller.getPlayer();

            //TODO: debug later?
            float x = player.getX() + (player.getWidth() / 2f);
            float y = player.getY() + (player.getHeight() / 2f);

            explosion.setPosition(x, y);

            if(player.getColor().isBlue()) {
                explosion.getEmitters().first().getTint().setColors(ColorValues.BLUE_2);
            } else if(player.getColor().isRed()) {
                explosion.getEmitters().first().getTint().setColors(ColorValues.RED_2);
            } else {
                explosion.getEmitters().first().getTint().setColors(ColorValues.YELLOW_2);
            }

            renderParticles(delta);
            return;
        }

        startTime += delta;

        Player player = controller.getPlayer();
        TextureRegion region;

        if(player.isChangingColor()) {
            startTime = 0;
            if (player.getPreviousColor().isBlue()) {
                currentAnimation = blueToRed;
            } else if (player.getPreviousColor().isRed()) {
                currentAnimation = redToYellow;
            } else {
                currentAnimation = yellowToBlue;
            }
            region = currentAnimation.getKeyFrame(startTime);
        } else {
            if(!currentAnimation.isAnimationFinished(startTime)) {
                region = currentAnimation.getKeyFrame(startTime);
            } else {
                if (player.getColor().isBlue()) {
                    region = bluePlayer;
                } else if (player.getColor().isRed()) {
                    region = redPlayer;
                } else {
                    region = yellowPlayer;
                }
            }
        }

        player.render(batch, region);
    }

    private void renderPlatforms() {
        Array<Platform> platforms = controller.getPlatforms();

        NinePatch patch;

        for (Platform platform : platforms) {
            if (platform.getColor().isBlue()) {
                patch = bluePlatform;
            } else if (platform.getColor().isRed()) {
                patch = redPlatform;
            } else {
                patch = yellowPlatform;
            }

            //ninepatch is a 32 pixels wide square
            //we have to scale it to match world units
            patch.scale(
                    GameConfig.PLATFORM_HEIGHT / patch.getTotalWidth(),
                    GameConfig.PLATFORM_HEIGHT / patch.getTotalHeight())
            ;
            platform.render(batch, patch);
        }
        renderer.end();
    }

    private void renderParticles(float delta) {
        explosion.update(delta);
        explosion.draw(batch, delta);
        if (explosion.isComplete()) {
            resetParticles();
            cameraController.reset();
            controller.init();
        }
    }

    private void renderObjects() {
        //springs
        Array<Spring> springs = controller.getSprings();

        for (Spring spring : springs) {
            TextureRegion region;

            if (spring.getColor().isBlue()) {
                region = blueSpring;
            } else if (spring.getColor().isRed()) {
                region = redSpring;
            } else {
                region = yellowSpring;
            }

            spring.render(batch, region);
        }

        //jetpacks
        Array<Jetpack> jetpacks = controller.getJetpacks();

        for (Jetpack jetpack : jetpacks) {
            TextureRegion region;

            if (jetpack.getColor().isBlue()) {
                region = blueJetpack;
            } else if (jetpack.getColor().isRed()) {
                region = redJetpack;
            } else {
                region = yellowJetpack;
            }

            jetpack.render(batch, region);
        }
    }

    public GameHud getHud() {
        return gameHud;
    }

    private void handleCamera(float delta) {
//        debugCameraController.handleDebugInput(delta);

        //center of the player
        float targetX = controller.getPlayer().getX() + controller.getPlayer().getWidth() / 2f;
        float targetY = controller.getPlayer().getY() + controller.getPlayer().getHeight() / 2f;

        cameraController.setTarget(targetX, targetY);

        boolean isGameOver = controller.getState() == GameState.FALLING || controller.getState() == GameState.GAME_OVER;
        cameraController.setFollowDescent(isGameOver);

        cameraController.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            System.out.println("Camera position: " + camera.position.x + ", " + camera.position.y);
            System.out.println("Player position: " + targetX + ", " + targetY);
        }
    }

    private void initTextures() {
        TextureAtlas atlas = assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS);

        backgroundRegion = atlas.findRegion(RegionNames.MAIN_BACKGROUND);

        TextureRegion temp = atlas.findRegion(RegionNames.PLATFORM_BLUE);
        bluePlatform = new NinePatch(
                temp,
                GameConfig.PLATFORM_EDGE,
                GameConfig.PLATFORM_EDGE,
                GameConfig.PLATFORM_EDGE,
                GameConfig.PLATFORM_EDGE
        );

        temp = atlas.findRegion(RegionNames.PLATFORM_RED);
        redPlatform = new NinePatch(
                temp,
                GameConfig.PLATFORM_EDGE,
                GameConfig.PLATFORM_EDGE,
                GameConfig.PLATFORM_EDGE,
                GameConfig.PLATFORM_EDGE
        );

        temp = atlas.findRegion(RegionNames.PLATFORM_YELLOW);
        yellowPlatform = new NinePatch(
                temp,
                GameConfig.PLATFORM_EDGE,
                GameConfig.PLATFORM_EDGE,
                GameConfig.PLATFORM_EDGE,
                GameConfig.PLATFORM_EDGE
        );

        bluePlayer = atlas.findRegion(RegionNames.PLAYER_BLUE);
        redPlayer = atlas.findRegion(RegionNames.PLAYER_RED);
        yellowPlayer = atlas.findRegion(RegionNames.PLAYER_YELLOW);

        blueSpring = atlas.findRegion(RegionNames.SPRING_BLUE);
        redSpring = atlas.findRegion(RegionNames.SPRING_RED);
        yellowSpring = atlas.findRegion(RegionNames.SPRING_YELLOW);

        blueJetpack = atlas.findRegion(RegionNames.JETPACK_BLUE);
        redJetpack = atlas.findRegion(RegionNames.JETPACK_RED);
        yellowJetpack = atlas.findRegion(RegionNames.JETPACK_YELLOW);
    }

    private void initAnimations() {
        TextureAtlas atlas = assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 6; i++) {
            frames.add(
                    atlas.findRegion(
                            AnimationNames.BLUE_TO_RED[i]
                    )
            );
        }
        blueToRed = new Animation<TextureRegion>(GameConfig.PLAYER_ROTATE_DURATION, frames, Animation.PlayMode.NORMAL);

        frames.clear();

        for (int i = 0; i < 6; i++) {
            frames.add(
                    atlas.findRegion(
                            AnimationNames.RED_TO_YELLOW[i]
                    )
            );
        }
        redToYellow = new Animation<TextureRegion>(GameConfig.PLAYER_ROTATE_DURATION, frames, Animation.PlayMode.NORMAL);

        frames.clear();

        for (int i = 0; i < 6; i++) {
            frames.add(
                    atlas.findRegion(
                            AnimationNames.YELLOW_TO_BLUE[i]
                    )
            );
        }
        yellowToBlue = new Animation<TextureRegion>(GameConfig.PLAYER_ROTATE_DURATION, frames, Animation.PlayMode.NORMAL);

        currentAnimation = blueToRed;
    }

    private void initParticles () {
        explosion = assetManager.get(AssetDescriptors.PARTICLES);
        resetParticles();
        explosion.start();
    }

    private void resetParticles () {
        explosion.reset();
        explosion.scaleEffect(GameConfig.PARTICLE_SIZE / 16f);

        float x = controller.getPlayer().getX() + (controller.getPlayer().getWidth() / 2f);
        float y = controller.getPlayer().getY() + (controller.getPlayer().getHeight() / 2f);

        explosion.setPosition(x, y);
    }
}
