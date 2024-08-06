package com.mygdx.bomberman;

public class PlayerData
{
    private int id;
    private int points;
    private boolean inGame;

    public PlayerData(int id)
    {
        this.id = id;
        this.points = 0;
        this.inGame = true;
    }

    public boolean inGame()
    {
        return inGame;
    }

    public void resetPlayer(int id)
    {
        this.id = id;
        this.points = 0;
        this.inGame = true;
    }

    public int getId()
    {
        return id;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public void setInGame(boolean left)
    {
        this.inGame = left;
    }
}

