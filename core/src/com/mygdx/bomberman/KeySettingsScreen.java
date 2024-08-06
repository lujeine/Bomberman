package com.mygdx.bomberman;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import java.util.function.Consumer;

public class KeySettingsScreen extends ScreenAdapter
{
    Stage stage;
    private TextButton moveLeftButton;
    private TextButton moveRightButton;
    private TextButton moveUpButton;
    private TextButton moveDownButton;
    private TextButton placeBombButton;
    private final int playerNumber;
    private final Assets assets;

    public KeySettingsScreen(int playerNumber, Assets assets)
    {
        this.assets = assets;
        this.stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.playerNumber = playerNumber;
        initUI();
    }

    private void initUI()
    {
        assets.createBackground(stage, "KeySettingsScreen");

        assets.createImage(stage,"MenuUI.atlas", String.valueOf(playerNumber), 390, 409, 27, 35);
        assets.createBackButton(stage, () -> Bomberman.INSTANCE.setScreen(SettingsScreen.INSTANCE));

        KeyProfile currentProfile = GlobalSettings.keyProfiles[playerNumber - 1];

        moveLeftButton = assets.createTextButton(stage, formatButtonText(currentProfile.getMoveLeftKey()), 74, 230, 60, 60, () -> waitForKeypressAndUpdateKey(moveLeftButton, currentProfile::setMoveLeftKey));
        moveRightButton = assets.createTextButton(stage, formatButtonText(currentProfile.getMoveRightKey()), 190, 230, 60, 60, () -> waitForKeypressAndUpdateKey(moveRightButton, currentProfile::setMoveRightKey));
        moveUpButton = assets.createTextButton(stage, formatButtonText(currentProfile.getMoveUpKey()), 132, 288, 60, 60, () -> waitForKeypressAndUpdateKey(moveUpButton, currentProfile::setMoveUpKey));
        moveDownButton = assets.createTextButton(stage, formatButtonText(currentProfile.getMoveDownKey()), 132, 172, 60, 60, () -> waitForKeypressAndUpdateKey(moveDownButton, currentProfile::setMoveDownKey));
        placeBombButton = assets.createTextButton(stage, formatButtonText(currentProfile.getPlaceBombKey()), 395, 230, 60, 60, () -> waitForKeypressAndUpdateKey(placeBombButton, currentProfile::setPlaceBombKey));
    }

    String formatButtonText(int keycode)
    {
        String text = Input.Keys.toString(keycode);
        return text.replace("Numpad ", "Num");
    }

    private void waitForKeypressAndUpdateKey(final TextButton button, Consumer<Integer> updateFunction) //Gewünschte Tastenbelegungen der Spieler werden entgegengenommen. Belegte Tasten werden für andere Spieler gesperrt.
    {
        Gdx.input.setInputProcessor(new InputAdapter()
        {
            @Override
            public boolean keyDown(int keycode)
            {
                boolean keyIsAvailable = true;

                for (int playerID = 0; playerID < 3; playerID++)
                {
                    if ((keycode == GlobalSettings.keyProfiles[playerID].getMoveUpKey()) ||
                            (keycode == GlobalSettings.keyProfiles[playerID].getMoveDownKey()) ||
                            (keycode == GlobalSettings.keyProfiles[playerID].getMoveRightKey()) ||
                            (keycode == GlobalSettings.keyProfiles[playerID].getMoveLeftKey()) ||
                            (keycode == GlobalSettings.keyProfiles[playerID].getPlaceBombKey()))
                    {

                        keyIsAvailable = false;
                        showMessage();
                        break;
                    }
                }

                if (keyIsAvailable)
                {
                    updateFunction.accept(keycode);
                    button.setText(Input.Keys.toString(keycode));
                }

                Gdx.app.postRunnable(() -> Gdx.input.setInputProcessor(stage)); //damit andere Eingaben wieder funktionieren
                return true;
            }
        });
    }

    private void showMessage()
    {
        BitmapFont font = assets.getFont("Fonts/light_pixel-7.fnt");
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        Label messageLabel = new Label("Taste schon belegt!", style);
        messageLabel.setPosition(195, 380);
        stage.addActor(messageLabel);
        messageLabel.addAction(Actions.sequence(
                Actions.fadeIn(0.5f),
                Actions.delay(0.5f),
                Actions.fadeOut(0.5f)
        ));
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
