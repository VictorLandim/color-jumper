package com.lan.jumper.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.lan.jumper.common.GameState;
import com.lan.jumper.common.Timer;
import com.lan.jumper.config.GameConfig;
import com.lan.jumper.entity.*;
import com.lan.jumper.game.ColorJumperGame;
import com.lan.jumper.input.InputController;

public class GameController {
    private ColorJumperGame game;

    private GameState state;
    private GameState previousState;
    private InputController inputController;

    private Player player;
    private Array<Platform> platforms;
    private Array<Spring> springs;
    private Array<Jetpack> jetpacks;

    public float maxHeight = 0;
    public float heightCounter = 1;
    private boolean shouldCreateNextPlatforms = false;

    private float heightSoFar;

    private Timer fallingTimer;

    public boolean shouldGoToMenu;

    public GameController(ColorJumperGame game) {
        this.game = game;
        inputController = new InputController();
        init();
    }

    public void init() {
        player = new Player();
        player.setSize(GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
        player.setPosition((GameConfig.VIEWPORT_WIDTH - player.getWidth()) / 2f, GameConfig.VIEWPORT_HEIGHT / 2f);
        player.setRandomColor();
        player.setPreviousColor(player.getColor());
        player.setFalling();

        heightSoFar = player.getY();

        platforms = new Array<Platform>();
        springs = new Array<Spring>();
        jetpacks = new Array<Jetpack>();

        state = GameState.PLAYING;

        fallingTimer = new Timer();

        createLevel();
    }

    public void update(float delta) {
        previousState = state;
        handleInput(delta);
        updatePlayer(delta);
        checkCollisions();
        handlePlatforms();
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Platform> getPlatforms() {
        return platforms;
    }

    private void handleInput(float delta) {
        player.setLastPosition(player.getX(), player.getY());
        player.setPreviousColor(player.getColor());

        if (state.isFalling() || state.isGameOver()) {
            return;
        }

        //mobile
        if (GameConfig.IS_MOBILE) {
            float accelerometerInput = -Gdx.input.getAccelerometerX() * GameConfig.ACCELEROMETER_SENSITIVITY;

            player.move(delta, accelerometerInput);

            if (inputController.isTouchReleased(0)) {
                player.nextColor();
            }
        } else {
            if (inputController.isKeyDown(Input.Keys.LEFT)) {
                player.moveLeft(delta);
            } else if (inputController.isKeyDown(Input.Keys.RIGHT)) {
                player.moveRight(delta);
            } else if (inputController.isKeyPressed(Input.Keys.B)) {
                player.nextColor();
            }

            //debug
            if (inputController.isKeyPressed(Input.Keys.SPACE)) {
                System.out.println("jetpack counter: " + player.getJetpackCounter());
            }
            if (inputController.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                init();
            }

            if (inputController.isKeyPressed(Input.Keys.UP)) {
                if (player.getState().isStanding()) {
                    player.setFalling();
                    return;
                }
                player.jump();
            }
        }

        inputController.update();
    }

    private void updatePlayer(float delta) {
        switch (state) {
            case READY:
            case PLAYING:
                float velocity = player.getVelocity() + GameConfig.GRAVITY * delta;

                player.setVelocity(velocity);
                player.updateJetpack(delta);

                float x = player.getX();
                float y = player.getY() + player.getVelocity() * delta;

                player.setPosition(x, y);

                ensureInBounds();

                heightSoFar += GameConfig.CAMERA_MOVE_SPEED * delta;

                heightSoFar = Math.max(player.getY(), heightSoFar);

                if (player.getY() + player.getHeight() < heightSoFar - GameConfig.VIEWPORT_HEIGHT / 2) {
                    state = GameState.FALLING;
                }

                if (player.getVelocity() > 0) {
                    player.setJumping();
                } else if (player.getVelocity() < 0) {
                    player.setFalling();
                } else {
                    player.setStanding();
                }
                break;
            case FALLING:
                velocity = player.getVelocity();

                if (velocity < 0) {
                    velocity += GameConfig.GRAVITY * delta;
                    velocity += 35f * delta;

                    player.setVelocity(velocity);

                    x = player.getX();
                    y = player.getY() + player.getVelocity() * delta;

                    float targetX = (GameConfig.VIEWPORT_WIDTH / 2f) - (player.getWidth() / 2f);
                    x = x + (targetX - x) * 2f * delta;
                    player.setPosition(x, y);

                } else {
                    state = GameState.GAME_OVER;
                }
                break;
            case GAME_OVER:
                //do nothing
                break;
            default:
                break;
        }
    }

    private void ensureInBounds() {
        if (player.getX() <= -player.getWidth()) {
            player.setPosition(GameConfig.VIEWPORT_WIDTH, player.getY());
        } else if (player.getX() >= GameConfig.VIEWPORT_WIDTH) {
            player.setPosition(0, player.getY());
        }
    }

    private void createLevel() {

        // algorithm steps
        // 1 - create first screen platforms always
        // 2 - if player is below the next screen by a certain amount (trigger), create next screen platforms
        // 3 - remove passed platforms (or not)

        int platformsPerScreen = GameConfig.PLATFORMS_PER_SCREEN;
        int screenCount = 50;

        float platformYStart = 0;

        for (int i = 0; i < platformsPerScreen * screenCount; i++) {

            float platformWidth = MathUtils.random(2f, 4.5f);
            float platformHeight = GameConfig.PLATFORM_HEIGHT;

            float platformX = MathUtils.random(0f, GameConfig.VIEWPORT_WIDTH - platformWidth);
            float platformY = platformYStart + MathUtils.random(1f);

            Platform platform = new Platform();
            platform.setSize(platformWidth, platformHeight);
            platform.setPosition(platformX, platformY);
            platform.setRandomColor();

            //TODO: assegurar que exista uma plataforma da mesma cor do jogador nas proximidades
//            if(MathUtils.random() > 0.7) {
//                platform.addColor(Colors.getRandomPrimaryColor());
//                if(MathUtils.random() > 0.9) {
//                    platform.addColor(Colors.getRandomPrimaryColor());
//                }
//            }

            if (MathUtils.random() > 0.8) {
                Spring spring = new Spring();

                float springWidth = GameConfig.SPRING_WIDTH;
                float springHeight = GameConfig.SPRING_HEIGHT;

                float springX = MathUtils.random(platformX, platformX + platformWidth - springWidth);
                float springY = platformY + platformHeight;

                spring.setSize(springWidth, springHeight);
                spring.setPosition(springX, springY);
                spring.setColor(platform.getColor());

                springs.add(spring);
            }

            if (MathUtils.random() > 0.95) {
                Jetpack jetpack = new Jetpack();

                float jetpackWidth = GameConfig.JETPACK_WIDTH;
                float jetpackHeight = GameConfig.JETPACK_HEIGHT;

                float jetpackX = MathUtils.random(platformX, platformX + platformWidth - jetpackWidth);
                float jetpackY = platformY + platformHeight;

                jetpack.setSize(jetpackWidth, jetpackHeight);
                jetpack.setPosition(jetpackX, jetpackY);
                jetpack.setColor(platform.getColor());

                jetpacks.add(jetpack);
            }

            //TODO check if new platform overlaps previous

            platforms.add(platform);
            platformYStart += GameConfig.VIEWPORT_HEIGHT / GameConfig.PLATFORMS_PER_SCREEN;
        }

    }

    private void handlePlatforms() {
        if (player.getY() >= maxHeight) {

            maxHeight = player.getY();

            int screenRelativeHeight = (int) (maxHeight % GameConfig.VIEWPORT_HEIGHT);

            if (screenRelativeHeight == (int) (GameConfig.VIEWPORT_HEIGHT / 2f)) {
//                createNextPlatformLayer();
            }
//            if (maxHeight / heightCounter >= GameConfig.VIEWPORT_HEIGHT) {
//                shouldCreatePlatforms = true;
//                heightCounter++;
//                maxHeight = 0;
//
//                generatePlatformsLayer();
//            }
        }
    }

    private void generatePlatformsLayer() {

        float platformsPerScreen = GameConfig.PLATFORMS_PER_SCREEN;
        float platformY = 0;

        float startY = maxHeight * heightCounter;

        for (int i = 0; i < platformsPerScreen; i++) {

            float width = MathUtils.random(1f, 4f);
            float height = 0.5f;

            float randomX = MathUtils.random(0f, GameConfig.VIEWPORT_WIDTH - width);
            float randomY = platformY + MathUtils.random(1f) + startY;

            Platform platform = new Platform();
            platform.setSize(width, height);
            platform.setPosition(randomX, randomY);

//            //check overlapping platforms
//            for(Platform p : platforms) {
//                if(p.getBounds().overlaps(platform.getBounds())) {
//                    if(i != 0) i--;
//                    continue;
//                }
//            }

            platforms.add(platform);
            platformY += GameConfig.VIEWPORT_HEIGHT / GameConfig.PLATFORMS_PER_SCREEN;
        }
    }

    private void checkCollisions() {
        if (state.isFalling() || state.isGameOver()) {
            return;
        }
        checkCollisionsPlatforms();
        checkCollisionsSprings();
        checkCollisionsJetpacks();
    }

    private void checkCollisionsPlatforms() {
        for (Platform platform : platforms) {

            Rectangle platformBounds = platform.getBounds();
            Rectangle playerBounds = player.getBounds();
            Rectangle lastPositionPlayerBounds = new Rectangle(player.getLastX(), player.getLastY(), player.getWidth(), player.getHeight());

            boolean isOverlapping = playerBounds.overlaps(platformBounds);
            boolean wasOverlapping = lastPositionPlayerBounds.overlaps(platformBounds);
            boolean isSameColor = platform.getColor() == player.getColor();
            boolean isFalling = player.getState().isFalling();
            boolean isAbove = player.getY() > platform.getY();

            if (isOverlapping && !wasOverlapping && isFalling && isAbove && isSameColor) {
                player.jump();
            }
        }
    }

    private void checkCollisionsSprings() {
        for (Spring spring : springs) {

            Rectangle springBounds = spring.getBounds();
            Rectangle playerBounds = player.getBounds();

            boolean isOverlapping = playerBounds.overlaps(springBounds);
            boolean isFalling = player.getState().isFalling();
            boolean isAbove = player.getY() > spring.getY();
            boolean isSameColor = spring.getColor() == player.getColor();

            if (isOverlapping && isFalling && isAbove && isSameColor) {
                player.jumpHigh();
            }
        }
    }

    private void checkCollisionsJetpacks() {
        for (Jetpack jetpack : jetpacks) {

            Rectangle jetpackBounds = jetpack.getBounds();
            Rectangle playerBounds = player.getBounds();

            boolean isOverlapping = playerBounds.overlaps(jetpackBounds);
            boolean isSameColor = jetpack.getColor() == player.getColor();

            if (isOverlapping && isSameColor) {
                player.startJetpack();
                jetpacks.removeValue(jetpack, true);
            }
        }
    }

    public GameState getState() {
        return state;
    }

    public GameState getPreviousState() {
        return previousState;
    }

    public Array<Spring> getSprings() {
        return springs;
    }

    public Array<Jetpack> getJetpacks() {
        return jetpacks;
    }

    public ColorJumperGame getGame() {
        return game;
    }

    public InputController getInputController() {
        return inputController;
    }

    public float getHeight() {
        return heightSoFar;
    }

    public String getScore() { return Integer.toString(MathUtils.round(heightSoFar)); }

    public void goToMenu() { shouldGoToMenu = true; }
}
