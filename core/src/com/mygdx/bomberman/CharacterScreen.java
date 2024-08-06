package com.mygdx.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class CharacterScreen extends ScreenAdapter
{
    private final Stage stage;
    public static CharacterScreen INSTANCE;
    private final Assets assets;
    private final ImageButton[] playerFields = new ImageButton[4];
    private final ImageButton[] leftArrowButtons = new ImageButton[4];
    private final ImageButton[] rightArrowButtons = new ImageButton[4];

    public CharacterScreen(Assets assets)
    {
        this.assets = assets;
        INSTANCE = this;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        initUI();
    }

    private void initUI()
    {
        assets.createBackground(stage, "CharacterScreen");
        assets.createTextButton(stage, "SPIEL STARTEN", 350, 30, 210, 55, () ->
        {
            Bomberman.INSTANCE.setScreen(new GameScreen(assets));
            LobbyScreen.menuMusic.stop();
        });

        assets.createBackButton(stage, () -> Bomberman.INSTANCE.setScreen(LobbyScreen.INSTANCE));
        createPlayerFieldsAndArrows();
    }

    private void createPlayerFieldsAndArrows()
    {
        for (int i = 0; i < 4; i++)
        {
            int playerIndex = i;
            ImageButton playerField = assets.createPlayerField(stage, i, 67 + i * 123, 222, false); //Die Boxen mit skins und disable Effekt
            ImageButton leftArrowButton = assets.createArrowButton(stage, "arrowLeft", playerField.getX() - 13, (float) (playerField.getY()*1.35), () -> changeSkin(false, playerIndex), false);
            ImageButton rightArrowButton = assets.createArrowButton(stage, "arrowRight", playerField.getX() + playerField.getWidth() + 2, (float) (playerField.getY()*1.35), () -> changeSkin(true, playerIndex), false);

            playerFields[i] = playerField;
            leftArrowButtons[i] = leftArrowButton;
            rightArrowButtons[i] = rightArrowButton;
        }
    }

    private void changeSkin(boolean increase, int playerIndex)
    {
        int skinValue = GlobalSettings.skins[playerIndex];
        int limit = 5;

        while (GlobalSettings.isSkinAlreadySelected(skinValue))
        {
            if (increase)
                skinValue = (skinValue + 1) % (limit + 1);
            else
                skinValue = (skinValue - 1 + (limit + 1)) % (limit + 1);
        }

        GlobalSettings.skins[playerIndex] = skinValue;
        playerFields[playerIndex].remove();
        playerFields[playerIndex] = assets.createPlayerField(stage, skinValue, 67 + playerIndex * 123, 222, false);
    }

    public void updateUI(int playerNumber)
    {
        for (int i = 0; i < playerFields.length; i++)
        {
            boolean shouldBeDisabled = i >= playerNumber;
            leftArrowButtons[i].setDisabled(shouldBeDisabled);
            rightArrowButtons[i].setDisabled(shouldBeDisabled);
            playerFields[i].remove();
            playerFields[i] = assets.createPlayerField(stage, i, 67 + i * 123, 222, shouldBeDisabled);
        }
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
        updateUI(GlobalSettings.noOfPlayers);
        GlobalSettings.updateSelectedSkins();
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
