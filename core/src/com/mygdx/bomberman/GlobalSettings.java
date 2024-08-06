package com.mygdx.bomberman;

import com.badlogic.gdx.Input;

public class GlobalSettings
{
    public static int noOfPlayers = 2;
    public static int pointsToWin = 1;
    public static int chosenMap = 0;
    public static final KeyProfile[] keyProfiles = new KeyProfile[4];
    public static int[] skins = new int[noOfPlayers];
    public static PlayerData[] playerData = new PlayerData[4];

    static
    {
        for (int i = 0; i < playerData.length; i++)
            playerData[i] = new PlayerData(i);
    }

    public static void updateSelectedSkins()
    {
        skins = new int[noOfPlayers];

        for (int i = 0; i < noOfPlayers; i++)
            skins[i] = i; //Setze die Standardwerte für jeden Spieler
    }
    public static boolean isSkinAlreadySelected(int skinIndex)
    {
        for (int skin : skins)
        {
            if (skin == skinIndex)
                return true;
        }

        return false;
    }

    static
    {
        keyProfiles[0] = new KeyProfile(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E);
        keyProfiles[1] = new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER);
        keyProfiles[2] = new KeyProfile(Input.Keys.I, Input.Keys.K, Input.Keys.J, Input.Keys.L, Input.Keys.P);
        keyProfiles[3] = new KeyProfile(Input.Keys.NUM_2, Input.Keys.NUM_3, Input.Keys.NUM_4, Input.Keys.NUM_5, Input.Keys.NUM_0);
    }
}