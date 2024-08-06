package com.mygdx.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

import java.util.Objects;

public class Assets
{
    private final AssetManager assetManager;
    private TextButton.TextButtonStyle buttonStyle;
    private Sound buttonClickSound;

    public Assets(AssetManager assetManager)
    {
        this.assetManager = assetManager;
        load();
    }

    private void load()
    {
        assetManager.load("Fonts/light_pixel-7.fnt", BitmapFont.class);
        assetManager.load("GameAssets.atlas", TextureAtlas.class);
        assetManager.load("MenuUI.atlas", TextureAtlas.class);
        assetManager.load("BombAnimation.atlas", TextureAtlas.class);
        assetManager.load("Winner.atlas", TextureAtlas.class);
        assetManager.load("Screens.atlas", TextureAtlas.class);
        assetManager.load("FireworkScarf.atlas", TextureAtlas.class);

        for (int i = 0; i < 6; i++)
            assetManager.load("skin" + i +".atlas", TextureAtlas.class);

        assetManager.load("Music/button_click.mp3", Sound.class);

        for (int i = 0; i < 3; i++)
            assetManager.load("Music/gameMusic" + i + ".mp3", Sound.class);

        for (int i = 0; i < 3; i++)
            assetManager.load("Music/suddenMusic" + i + ".mp3", Sound.class);

        assetManager.load("Music/startMusic.mp3", Sound.class);
        assetManager.load("Music/menuMusic.mp3", Sound.class);
        assetManager.load("Music/scoreboardMusic.mp3", Sound.class);
        assetManager.load("Music/ceremonyMusic.mp3", Sound.class);
        assetManager.load("Music/death.mp3", Sound.class);
        assetManager.load("Music/explosion.mp3", Sound.class);
    }

    public TextureRegion getRegion(String atlasName, String regionName)
    {
        try
        {
            TextureAtlas atlas = assetManager.get(atlasName, TextureAtlas.class);
            return atlas.findRegion(regionName);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    public BitmapFont getFont(String fontName)
    {
        return assetManager.get(fontName, BitmapFont.class);
    }

    public ImageButton.ImageButtonStyle getImageButtonStyle(String atlasName, String assetName)
    {
        Drawable upDrawable = new TextureRegionDrawable(getRegion(atlasName, assetName));
        Drawable overDrawable = new TextureRegionDrawable(getRegion(atlasName, assetName + "Hover"));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = upDrawable;
        style.over = overDrawable;

        return style;
    }

    public BitmapFont createFont(String fontPath, int size)
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }

    public void createButtonStyleAndFont()
    {
        assetManager.finishLoading(); // Ensure all assets are loaded

        BitmapFont font = assetManager.get("Fonts/light_pixel-7.fnt", BitmapFont.class);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(getRegion("MenuUI.atlas","button"));  //up gibt, an welche Textur die ganze Zeit sichtbar ist
        buttonStyle.over = new TextureRegionDrawable(getRegion("MenuUI.atlas","buttonHover"));   //over gibt an, welche Textur beim hovern sichtbar ist
        buttonStyle.font = font;
        buttonClickSound = assetManager.get("Music/button_click.mp3", Sound.class);
    }

    public void createBackground(Stage stage, String textureName)
    {
        Image background;

        if (Objects.equals(textureName, GlobalSettings.chosenMap + "field_texture"))
            background = new Image(getRegion("GameAssets.atlas", textureName));
        else
            background = new Image(getRegion("Screens.atlas", textureName));

        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setScaling(Scaling.fit);
        stage.addActor(background);
    }

    public Animation<TextureRegion> createAnimation(int noOfFrames, float frameDuration, String atlasName, String regionName)
    {
        Array<TextureRegion> frames = new Array<>();

        for (int i = 1; i < noOfFrames; i++)
            frames.add(getRegion(atlasName, regionName + i));

        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }


    public ImageButton createArrowButton(Stage stage, String assetName, float x, float y, Runnable action, boolean isDisabled)
    {
        ImageButton button = new ImageButton(getImageButtonStyle("MenuUI.atlas", assetName));
        button.setSize(11, 19);
        button.setPosition(x, y);

        if (Objects.equals(assetName, "arrowLeft"))
            button.getStyle().imageDisabled = new TextureRegionDrawable(getRegion("MenuUI.atlas", "grayArrow"));
        else
            button.getStyle().imageDisabled = new TextureRegionDrawable(getRegion("MenuUI.atlas", "grayArrowRight"));

        button.setDisabled(isDisabled);
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (!button.isDisabled())
                {
                    buttonClickSound.play();
                    action.run();
                }
            }
        });
        stage.addActor(button);

        return button;
    }

    public ImageButton createImageButton(Stage stage, String atlasName, String assetName, float x, float y, float width, float height, Runnable action)
    {
        ImageButton button = new ImageButton(getImageButtonStyle(atlasName, assetName));
        button.setSize(width, height);
        button.setPosition(x, y);

        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                buttonClickSound.play();
                action.run();
            }
        });

        stage.addActor(button);

        return button;
    }

    public void createMuteButton(Stage stage, String asset1Name, String asset2Name, float x, float y, float width, float height,
                                 Runnable stopAction, Runnable playAction)
    {
        ImageButton.ImageButtonStyle style1 = getImageButtonStyle("GameAssets.atlas", asset1Name);
        ImageButton.ImageButtonStyle style2 = getImageButtonStyle("GameAssets.atlas", asset2Name);

        ImageButton button = new ImageButton(style1);
        button.setSize(width, height);
        button.setPosition(x, y);

        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                buttonClickSound.play();

                if (GameScreen.isMuted)
                {
                    GameScreen.isMuted = false;
                    playAction.run();
                }
                else
                {
                    GameScreen.isMuted = true;
                    stopAction.run();
                }

                button.setStyle(GameScreen.isMuted ? style2 : style1); // style an mute status anpassen
            }
        });

        stage.addActor(button);
    }

    public void updateSelectedMapButton()
    {
        for (int i = 0; i < LobbyScreen.mapPreviewButtons.size(); i++)
        {
            ImageButton button = LobbyScreen.mapPreviewButtons.get(i);

            if (i == GlobalSettings.chosenMap)
                button.getStyle().up = button.getStyle().over; //set hover Texture as normal
            else
                button.getStyle().up = new TextureRegionDrawable(getRegion("GameAssets.atlas", i + "Preview"));
        }
    }


    public TextButton createTextButton(Stage stage, String text, float x, float y, float width, float height, Runnable action)
    {
        TextButton button = new TextButton(text, buttonStyle);
        button.setSize(width, height);
        button.setPosition(x, y);
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                buttonClickSound.play();
                action.run();
            }
        });

        stage.addActor(button);

        return button;
    }

    public void createBackButton(Stage stage, Runnable action)
    {
        ImageButton backButton = new ImageButton(getImageButtonStyle("MenuUI.atlas", "backButton"));

        backButton.setSize(30, 35);
        backButton.setPosition(38, 485);
        backButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                buttonClickSound.play();
                action.run();
            }
        });

        stage.addActor(backButton);
    }

    public ImageButton createPlayerField(Stage stage, int index, float x, float y, boolean isDisabled)
    {
        Drawable upDrawable = new TextureRegionDrawable(getRegion("MenuUI.atlas", "CScreen" + index));
        Drawable backgroundDrawable = new TextureRegionDrawable(getRegion("MenuUI.atlas", "box"));
        Drawable disabledDrawable = new TextureRegionDrawable(getRegion("MenuUI.atlas", "gray"));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = upDrawable;
        style.up = backgroundDrawable;
        style.imageDisabled = disabledDrawable;

        ImageButton playerField = new ImageButton(style);
        playerField.setSize(92, 171);
        playerField.setPosition(x, y);
        playerField.setDisabled(isDisabled);
        stage.addActor(playerField);

        return playerField;
    }

    public Label createLabel(Stage stage, String text, float x, float y, boolean hasBackground)
    {
        BitmapFont font = getFont("Fonts/light_pixel-7.fnt");
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        Label label = new Label(text, style);
        label.setPosition(x, y);

        if (hasBackground)
        {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.BLACK);
            pixmap.fill();

            style.background = new TextureRegionDrawable(new Texture(pixmap));
            style.background.setMinWidth(label.getMinWidth()+5);
            style.background.setMinHeight(label.getMinHeight()+5);
        }

        stage.addActor(label);

        return label;
    }

    public Image createImage(Stage stage, String atlasName, String regionName, float x, float y, float width, float height)
    {
        TextureRegion imageRegion = getRegion(atlasName, regionName);
        Image image = new Image(new TextureRegionDrawable(imageRegion));

        image.setSize(width, height);
        image.setPosition(x, y);
        stage.addActor(image);

        return image;
    }

    public void dispose()
    {
        assetManager.dispose();
    }
}
