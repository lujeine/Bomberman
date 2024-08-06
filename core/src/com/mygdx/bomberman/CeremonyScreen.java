package com.mygdx.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.audio.Music;

public class CeremonyScreen extends ScreenAdapter
{
    Stage stage;
    private final Assets assets;
    static CeremonyScreen INSTANCE;
    private final int winnerID;
    private Animation<TextureRegion> scarfAnimation;
    private Animation<TextureRegion> fireworkAnimation;
    TextureRegion scarfFrame;
    TextureRegion fireworkFrame;
    TextureRegion firework2Frame;
    private float stateTime = 0;
    private final Batch batch;
    Music ceremonyMusic;

    public CeremonyScreen(Assets assets, int winnerID)
    {
        INSTANCE = this;
        this.assets = assets;
        batch = new SpriteBatch();
        this.winnerID = winnerID;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        ceremonyMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/ceremonyMusic.mp3"));

        initUI();

        for (int i = 0; i < 4; i++)
            GlobalSettings.playerData[i].resetPlayer(i);
    }

    private void initUI()
    {
        assets.createBackground(stage, "CeremonyScreen");
        fireworkAnimation = assets.createAnimation(13, 0.35f, "FireworkScarf.atlas", "firework");
        scarfAnimation = assets.createAnimation(4, 0.38f, "FireworkScarf.atlas", "scarf");
        assets.createImage(stage, "Winner.atlas", String.valueOf(GlobalSettings.skins[winnerID]), 200,120,200, 320);
        assets.createTextButton(stage, "Nochmal", 480, 30, 100, 55, () -> Bomberman.INSTANCE.setScreen(LobbyScreen.INSTANCE));
        assets.createTextButton(stage, "Startscreen", 20, 30, 150, 55, () -> Bomberman.INSTANCE.setScreen(StartScreen.INSTANCE));
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
        ceremonyMusic.play();
    }

    @Override
    public void hide()
    {
        if (ceremonyMusic.isPlaying())
            ceremonyMusic.stop();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        stateTime += delta;
        batch.begin();
        stage.act(delta);
        stage.draw();

        scarfFrame = scarfAnimation.getKeyFrame(stateTime);
        batch.draw(scarfFrame, 142, 192, 125, 125);

        fireworkFrame = fireworkAnimation.getKeyFrame(stateTime);
        batch.draw(fireworkFrame, -50, 25, 273, 447);

        firework2Frame = fireworkAnimation.getKeyFrame(stateTime+1);
        batch.draw(firework2Frame, 365, 0, 303, 497);

        batch.end();
    }

    @Override
    public void dispose()
    {
        if (scarfAnimation != null)
            for (TextureRegion textureRegion : scarfAnimation.getKeyFrames())
                textureRegion.getTexture().dispose();
        if (fireworkAnimation != null)
            for (TextureRegion textureRegion : fireworkAnimation.getKeyFrames())
                textureRegion.getTexture().dispose();
        if (batch != null)
            batch.dispose();

        stage.dispose();
        ceremonyMusic.dispose();
    }
}
