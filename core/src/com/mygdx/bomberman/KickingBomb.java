package com.mygdx.bomberman;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class KickingBomb extends PowerUp
{
    public KickingBomb(float x, float y, TextureRegion PowerUpTexture)
    {
        super(x,y, PowerUpTexture);
    }

    @Override
    public void activate(Player player)
    {
        player.setAbleToKick();
        this.dispose();
    }
}
