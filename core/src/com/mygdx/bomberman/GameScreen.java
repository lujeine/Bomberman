package com.mygdx.bomberman;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.audio.Music;

public class GameScreen extends ScreenAdapter
{
    Stage stage;
    SpriteBatch batch;
    Music gameMusic;
    Music suddenDeathMusic;
    public static GameScreen INSTANCE;
    private Label timerLabel;
    private Label countdownLabel;
    public static Field field;
    private float startCountdownTime = 3;
    private float gameTimer = 180;
    private boolean gameStarted = false;
    static Assets assets;
    boolean suddenDeathTriggered = false;
    public static boolean isMuted = false;
    private final SuddenDeath suddenDeath;

    public GameScreen(Assets assets)
    {
        INSTANCE = this;
        GameScreen.assets = assets;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        field = new Field(assets);
        field.placeBlocks();
        batch = new SpriteBatch();
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/gameMusic"+ GlobalSettings.chosenMap + ".mp3"));
        suddenDeathMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/suddenMusic"+ GlobalSettings.chosenMap + ".mp3"));
        suddenDeath = new SuddenDeath(field, assets);
        initUI();
    }

    private void initUI()
    {
        assets.createBackground(stage, GlobalSettings.chosenMap + "field_texture");
        assets.createImage(stage,"GameAssets.atlas","GameHud",0, 520, 600, 50);
        assets.createImageButton(stage,"GameAssets.atlas","PauseButton", 540, 530, 30, 30,
                () -> Bomberman.INSTANCE.setScreen(PauseScreen.INSTANCE));

        timerLabel = assets.createLabel(stage, String.format("%02d:%02d", (int) gameTimer / 60, (int) gameTimer % 60),(float) Gdx.graphics.getWidth() / 2 - 24, Gdx.graphics.getHeight() - 38, true);

        BitmapFont largeFont = assets.createFont("Fonts/light_pixel-7.ttf", 48);

        countdownLabel = assets.createLabel(stage, String.format("%.0f", startCountdownTime),(float) Gdx.graphics.getWidth() / 2 - 30, (float) Gdx.graphics.getHeight() / 2, false);
        countdownLabel.setAlignment(Align.center);
        countdownLabel.setStyle(new Label.LabelStyle(largeFont, Color.WHITE));

        for (Actor actor : field.getPlayerGroup().getChildren())
        {
            Player player = (Player) actor;

            if (player.getID() < 2)
            {
                assets.createImage(stage, "skin" + GlobalSettings.skins[player.getID()] + ".atlas", "head", 85 + 80 * player.getID(), Gdx.graphics.getHeight() - 40, 35, 30);
                assets.createLabel(stage, String.valueOf(player.getCurrentPoints()), 85 + 80 * player.getID() + 45, Gdx.graphics.getHeight() - 38, false);
            }
            else
            {
                assets.createImage(stage, "skin" + GlobalSettings.skins[player.getID()] + ".atlas", "head", 240 + 80 * player.getID(), Gdx.graphics.getHeight() - 40, 35, 30);
                assets.createLabel(stage, String.valueOf(player.getCurrentPoints()), 240 + 80 * player.getID() - 20, Gdx.graphics.getHeight() - 38, false);
            }
        }


        assets.createMuteButton(stage, "MuteButton", "UnmuteButton", 25, 530, 30, 30, () ->
        {
            if (gameMusic.isPlaying())
                gameMusic.pause();
            if (suddenDeathMusic.isPlaying())
                suddenDeathMusic.pause();
        }, () ->
        {
            if (!suddenDeathTriggered)
                gameMusic.play();
            else
                suddenDeathMusic.play();
        });
    }

    public void handleStartCountdown(float delta)
    {
        startCountdownTime -= delta;
        countdownLabel.setText(startCountdownTime > 1 ? String.format("%.0f", startCountdownTime) : "START");
        countdownLabel.toFront();

        if (startCountdownTime <= 0)
        {
            gameStarted = true;
            countdownLabel.setVisible(false);

            for (Actor actor : field.getPlayerGroup().getChildren())
                ((Player) actor).setCanMove(true);
        }
    }

    public static void handleRoundOver(Player lastPlayer)
    {
        lastPlayer.increaseCurrentPoints();
        GlobalSettings.playerData[lastPlayer.getID()].setPoints(lastPlayer.getCurrentPoints());

        if (lastPlayer.getCurrentPoints() < GlobalSettings.pointsToWin && field.howManyPlayersInGame() > 1)
            Bomberman.INSTANCE.setScreen(new ScoreboardScreen(assets));
        else
            Bomberman.INSTANCE.setScreen(new CeremonyScreen(assets, lastPlayer.getID()));

        GameScreen.INSTANCE.dispose();

        lastPlayer.dispose();
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);

        stage.addActor(field);

        gameMusic.setLooping(true);

        if (!isMuted && !suddenDeathTriggered)
            gameMusic.play();
        if (suddenDeathTriggered)
        {
            suddenDeath.resume();
            suddenDeathMusic.setLooping(true);

            if (!isMuted)
                suddenDeathMusic.play();
        }
    }

    @Override
    public void hide()
    {
        if (gameMusic.isPlaying())
            gameMusic.pause();
        if (suddenDeathMusic.isPlaying())
            suddenDeathMusic.pause();
        if (suddenDeathTriggered)
            suddenDeath.pause();
    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear( 0, 0, 0, 1);

        if (!gameStarted)
            handleStartCountdown(delta);
        else
        {
            gameTimer -= delta;

            if (gameTimer <= 0)
            {
                if (!suddenDeathTriggered)
                {
                    suddenDeath.startTimer();
                    suddenDeathTriggered = true;
                }

                gameTimer = 0;

                if (!isMuted)
                {
                    gameMusic.stop(); //Stoppe die aktuelle Musik
                    suddenDeathMusic.play(); //Spiele die Sudden Death-Musik ab
                }
            }

            timerLabel.setText(String.format("%02d:%02d", (int) gameTimer / 60, (int) gameTimer % 60));
        }

        batch.begin();
        stage.act();
        stage.draw();
        batch.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        stage.dispose();
        gameMusic.dispose();
        suddenDeathMusic.dispose();
    }
}

