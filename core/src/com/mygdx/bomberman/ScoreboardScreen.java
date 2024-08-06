package com.mygdx.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.audio.Music;

public class ScoreboardScreen extends ScreenAdapter
{
    Stage stage;
    private final Assets assets;
    static ScoreboardScreen INSTANCE;
    Music scoreboardMusic;

    public ScoreboardScreen(Assets assets)
    {
        INSTANCE = this;
        this.assets = assets;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        scoreboardMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/scoreboardMusic.mp3"));
        initUI();
    }

    private void initUI()
    {
        assets.createBackground(stage, "ScoreboardScreen");

        assets.createTextButton(stage, "Weiter", Gdx.graphics.getWidth() - 160, 30, 120, 55, () -> Bomberman.INSTANCE.setScreen(new GameScreen(assets)));

        for (int i = 0; i < GlobalSettings.noOfPlayers; i++)
        {
            assets.createImage(stage, "skin" + GlobalSettings.skins[i] + ".atlas","head", 80, (Gdx.graphics.getHeight() - 223 - 73 * GlobalSettings.playerData[i].getId()), 40, 35);

            for (int j = 0; j < GlobalSettings.playerData[i].getPoints(); j++)
                assets.createImage(stage, "GameAssets.atlas","trophy", 160 + 70 * j, (Gdx.graphics.getHeight() - 223 - 73 * GlobalSettings.playerData[i].getId()), 40, 35);
        }
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
        scoreboardMusic.setLooping(true);
        scoreboardMusic.play();
    }

    @Override
    public void hide()
    {
        if (scoreboardMusic.isPlaying())
            scoreboardMusic.stop();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
        scoreboardMusic.dispose();
    }
}
