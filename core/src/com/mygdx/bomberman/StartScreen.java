package com.mygdx.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.audio.Music;

public class StartScreen extends ScreenAdapter
{
    private final Stage stage;
    private final Assets assets;
    private Animation<TextureRegion> scarfAnimation;
    TextureRegion currentScarfFrame;
    private float stateTime = 0;
    private final Batch batch;
    private Image cloud;
    static StartScreen INSTANCE;
    Music startMusic;

    public StartScreen(Assets assets)
    {
        INSTANCE = this;
        this.assets = assets;
        batch = new SpriteBatch();
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        startMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/startMusic.mp3"));
        initUI();
    }

    private void initUI()
    {
        assets.createBackground(stage, "StartScreen");
        cloud = assets.createImage(stage, "MenuUI.atlas", "clouds", 100, 280, 570, 150);
        assets.createImage(stage,"Winner.atlas", "0", 200,120,200, 320);
        scarfAnimation = assets.createAnimation(4, 0.38f, "FireworkScarf.atlas", "scarf");
        assets.createTextButton(stage, "START", 55, 390, 110, 65, () -> Bomberman.INSTANCE.setScreen(LobbyScreen.INSTANCE));
        assets.createTextButton(stage, "OPTIONEN", 55, 325, 110, 65, () -> Bomberman.INSTANCE.setScreen(SettingsScreen.INSTANCE));
        assets.createTextButton(stage, "BEENDEN", 55, 260, 110, 65, Gdx.app::exit);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
        startMusic.setLooping(true);
        startMusic.play();
    }

    @Override
    public void hide()
    {
        if(startMusic.isPlaying())
            startMusic.stop();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        stateTime += delta;

        float newX = cloud.getX() - 50 * delta; //Bewege die Wolken in jedem Frame

        if (newX + cloud.getWidth() < 0) //Wenn die Wolke den linken Rand des Bildschirms verlässt, setze sie wieder rechts
            newX = 600;

        cloud.setPosition(newX, cloud.getY());

        //Zeichne die Animation
        batch.begin();
        stage.draw();
        currentScarfFrame = scarfAnimation.getKeyFrame(stateTime);
        batch.draw(currentScarfFrame, 142, 192, 125, 125);
        batch.end();
        stage.act(delta);
    }

    @Override
    public void dispose()
    {
        if (scarfAnimation != null)
        {
            for (TextureRegion textureRegion : scarfAnimation.getKeyFrames())
                textureRegion.getTexture().dispose();
        }
        if (batch != null)
            batch.dispose();
        if (cloud != null)
        {
            cloud.clear();
            cloud.remove();
        }

        stage.dispose();
        startMusic.dispose();
    }
}