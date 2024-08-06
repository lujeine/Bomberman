package com.mygdx.bomberman;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class IncreasedBomb extends PowerUp
{
    public IncreasedBomb(float x, float y, TextureRegion PowerUpTexture)
    {
        super(x,y, PowerUpTexture);
    }

    @Override
    public void activate(Player player)
    {
        player.increaseNumOfBombs();
        this.dispose();
    }
}