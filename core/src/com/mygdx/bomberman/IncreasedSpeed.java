package com.mygdx.bomberman;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class IncreasedSpeed extends PowerUp
{
    public IncreasedSpeed(float x, float y, TextureRegion PowerUpTexture)
    {
        super(x,y, PowerUpTexture);
    }

    @Override
    public void activate(Player player)
    {
        player.increaseSpeed();
        this.dispose();
    }
}