package com.mygdx.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PauseScreen extends ScreenAdapter
{
    Stage stage;
    private final Assets assets;
    static PauseScreen INSTANCE;

    public PauseScreen(Assets assets)
    {
        INSTANCE = this;
        this.assets = assets;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        initUI();
    }

    private void initUI()
    {
        assets.createBackground(stage, "pauseScreen");
        assets.createTextButton(stage, "SPIEL FORTSETZEN", 325, 165, 225, 100, () -> Bomberman.INSTANCE.setScreen(GameScreen.INSTANCE));
        assets.createTextButton(stage, "SPIEL VERLASSEN", 50, 165, 225, 100, () -> Bomberman.INSTANCE.setScreen(QuitScreen.INSTANCE));
        assets.createTextButton(stage, "STARTSCREEN", 185, 55, 225, 100, () ->
        {
            for (int i = 0; i < 4; i++)
                GlobalSettings.playerData[i].resetPlayer(i);

            GameScreen.INSTANCE.dispose();
            Bomberman.INSTANCE.setScreen(StartScreen.INSTANCE);
        });
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
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
    }
}
