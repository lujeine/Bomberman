package com.mygdx.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class Bomberman extends Game
{
	public static Bomberman INSTANCE;
	private AssetManager assetManager;
    LobbyScreen lobbyScreen;
	StartScreen startScreen;
	SettingsScreen settingsScreen;
	CharacterScreen characterScreen;
	KeySettingsScreen[] keySettingsScreens;
	PauseScreen pauseScreen;
	QuitScreen quitScreen;

	public Bomberman()
	{
		INSTANCE = this;
	}

	@Override
	public void create()
	{
		assetManager = new AssetManager();
        Assets assets = new Assets(assetManager);

		assets.createButtonStyleAndFont();

		startScreen = new StartScreen(assets);
		lobbyScreen = new LobbyScreen(assets);
		settingsScreen = new SettingsScreen(assets);
		characterScreen = new CharacterScreen(assets);
		keySettingsScreens = new KeySettingsScreen[4];

		for (int i = 0; i < 4; i++)
			keySettingsScreens[i] = new KeySettingsScreen(i+1, assets);

		pauseScreen = new PauseScreen(assets);
		quitScreen = new QuitScreen(assets);

		setScreen(startScreen);
	}

	@Override
	public void render()
	{
		super.render();
	}

	@Override
	public void dispose()
	{
		if (assetManager != null)
			assetManager.dispose(); //AssetManager entsorgen
	}
}
