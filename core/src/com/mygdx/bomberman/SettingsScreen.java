package com.mygdx.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class SettingsScreen extends ScreenAdapter
{
    private final Stage stage;
    private final Assets assets;
    static SettingsScreen INSTANCE;
    static Music menuMusic;

    public SettingsScreen(Assets assets)
    {
        INSTANCE = this;
        this.assets = assets;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/menuMusic.mp3"));
        initUI();
    }

    private void initUI()
    {
        assets.createBackground(stage, "SettingsScreen");

        for (int i=0; i < 4; i++)
        {
            int finalI = i;
            assets.createImageButton(stage,"MenuUI.atlas", ("Player" + (i + 1) + "Button"), 150, (320 - i * 55), 260, 40,
                    () -> Bomberman.INSTANCE.setScreen(Bomberman.INSTANCE.keySettingsScreens[finalI]));
        }
        assets.createBackButton(stage, () ->
        {
            Bomberman.INSTANCE.setScreen(StartScreen.INSTANCE);

            if(menuMusic.isPlaying())
                menuMusic.stop();
        });
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
        menuMusic.setLooping(true);
        menuMusic.play();
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
        menuMusic.dispose();
    }
}