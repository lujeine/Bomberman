package com.mygdx.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.bomberman.GlobalSettings.noOfPlayers;
import static com.mygdx.bomberman.GlobalSettings.pointsToWin;

public class LobbyScreen extends ScreenAdapter
{
    private final Stage stage;
    static LobbyScreen INSTANCE;
    private final Assets assets;
    public static List<ImageButton> mapPreviewButtons = new ArrayList<>();
    private Image playerNumberImage;
    private Image pointsNumberImage;
    private final ImageButton[] increaseButtons = new ImageButton[2];
    private final ImageButton[] decreaseButtons = new ImageButton[2];
    static Music menuMusic;

    public LobbyScreen(Assets assets)
    {
        INSTANCE = this;
        this.assets = assets;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/menuMusic.mp3"));
        initUI();
    }

    private void initUI()
    {
        assets.createBackground(stage, "LobbyScreen");

        increaseButtons[0] = assets.createArrowButton(stage, "arrowRight", 102, 425, () -> changeValue(true, true), false); //0 ist für die Buttons bei playerNumber und 1 für pointsToWin
        decreaseButtons[0] = assets.createArrowButton(stage, "arrowLeft", 52, 425, () -> changeValue(true, false), noOfPlayers == 2);
        increaseButtons[1] = assets.createArrowButton(stage, "arrowRight", 102, 355, () -> changeValue(false, true), false);
        decreaseButtons[1] = assets.createArrowButton(stage, "arrowLeft", 52, 355, () -> changeValue(false, false), pointsToWin == 1);

        playerNumberImage = assets.createImage(stage,"MenuUI.atlas", String.valueOf(noOfPlayers), 73, 422, 18, 25);
        pointsNumberImage = assets.createImage(stage,"MenuUI.atlas", String.valueOf(pointsToWin), 73, 352, 18, 25);

        assets.createTextButton(stage, "WEITER", 350, 30,210,55, () -> Bomberman.INSTANCE.setScreen(CharacterScreen.INSTANCE));
        assets.createBackButton(stage, () ->
        {
            Bomberman.INSTANCE.setScreen(StartScreen.INSTANCE);

            if (menuMusic.isPlaying())
                menuMusic.stop();
        });

        for (int i = 0; i < 3; i++)
        {
            int finalI = i;

            mapPreviewButtons.add(assets.createImageButton(stage, "GameAssets.atlas", i + "Preview", 40 + 180 * i, 160, 160, 150,
                    () ->
                    {
                        GlobalSettings.chosenMap = finalI;
                        assets.updateSelectedMapButton();
                    }));
        }

        assets.updateSelectedMapButton();

    }

    private void changeValue(boolean isPlayerNumber, boolean increase)
    {
        int value = isPlayerNumber ? noOfPlayers : pointsToWin;  //prüft, ob playerNumber oder pointToWin geändert wurde
        int limit = 4;

        if (increase && value < limit)
            value++;
        else if (!increase && value > 1)
            value--;

        int index = isPlayerNumber ? 0 : 1; //0 ist playerNumber und 1 ist pointsToWin

        if (isPlayerNumber)
        {
            noOfPlayers = value;
            playerNumberImage.remove();
            playerNumberImage = assets.createImage(stage,"MenuUI.atlas", String.valueOf(noOfPlayers), 73, 422, 18, 25);
            decreaseButtons[index].setDisabled(value == 2);
        }
        else
        {
            pointsToWin = value;
            pointsNumberImage.remove();
            pointsNumberImage = assets.createImage(stage,"MenuUI.atlas", String.valueOf(pointsToWin), 73, 352, 18, 25);
            decreaseButtons[index].setDisabled(value == 1);
        }


        increaseButtons[index].setDisabled(value == limit);

        GlobalSettings.updateSelectedSkins();
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