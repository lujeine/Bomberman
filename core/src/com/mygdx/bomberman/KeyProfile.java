package com.mygdx.bomberman;

public class KeyProfile
{
    //int wegen libGDXs ganzzahlige Repräsentation von Tastaturtasten in der Klasse "Input.Keys"
    private int moveDownKey;
    private int moveUpKey;
    private int moveLeftKey;
    private int moveRightKey;
    private int placeBombKey;

    public KeyProfile(int moveUpKey, int moveDownKey, int moveLeftKey, int moveRightKey, int placeBombKey)
    {
        this.moveUpKey = moveUpKey;
        this.moveDownKey = moveDownKey;
        this.moveLeftKey = moveLeftKey;
        this.moveRightKey = moveRightKey;
        this.placeBombKey = placeBombKey;
    }

    public int getMoveDownKey()
    {
        return moveDownKey;
    }

    public int getMoveUpKey()
    {
        return moveUpKey;
    }

    public int getMoveLeftKey()
    {
        return moveLeftKey;
    }

    public int getMoveRightKey()
    {
        return moveRightKey;
    }

    public int getPlaceBombKey()
    {
        return placeBombKey;
    }

    public void setMoveDownKey(int key)
    {
        this.moveDownKey = key;
    }

    public void setMoveUpKey(int key)
    {
        this.moveUpKey = key;
    }

    public void setMoveLeftKey(int key)
    {
        this.moveLeftKey = key;
    }

    public void setMoveRightKey(int key)
    {
        this.moveRightKey = key;
    }

    public void setPlaceBombKey(int key)
    {
        this.placeBombKey = key;
    }
}
