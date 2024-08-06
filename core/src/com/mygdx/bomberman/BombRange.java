package com.mygdx.bomberman;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BombRange extends PowerUp
{
    public BombRange(float x, float y, TextureRegion PowerUpTexture)
    {
        super(x,y, PowerUpTexture);
    }

    @Override
    public void activate(Player player)
    {
        player.increaseBombRange();
        this.dispose();
    }
}