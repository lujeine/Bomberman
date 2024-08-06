package com.mygdx.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class QuitScreen extends ScreenAdapter
{
    Stage stage;
    private final Assets assets;
    static QuitScreen INSTANCE;

    public QuitScreen(Assets assets)
    {
        INSTANCE = this;
        this.assets = assets;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    }

    private void initUI()
    {
        assets.createBackground(stage, "QuitScreen");
        assets.createBackButton(stage, () -> Bomberman.INSTANCE.setScreen(PauseScreen.INSTANCE));

        int yCoordinates = 280;

        for (Actor actor : GameScreen.field.getPlayerGroup().getChildren())
        {
            final Player player = (Player) actor;
            assets.createImageButton(stage,"MenuUI.atlas", ("Player"+(player.getID()+1)+"Button"), 150, yCoordinates, 260, 40,
                    () ->
                    {       Bomberman.INSTANCE.setScreen(Bomberman.INSTANCE.pauseScreen);
                            GlobalSettings.playerData[player.getID()].setInGame(false);
                            GameScreen.field.removeObject(player);
                    }
            );

            yCoordinates -= 55;
        }
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
        initUI();
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
