package com.mygdx.bomberman;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class PowerUp extends Actor
{
    private  Sprite sprite;
    private static final int SIZE = 40;

    public PowerUp(float x, float y, TextureRegion texture)
    {
        this.setPosition(x,y);
        this.setSize(SIZE, SIZE);

        try
        {
            this.sprite = new Sprite(texture);
            this.sprite.setPosition(this.getX(), this.getY());
            this.sprite.setSize(this.getWidth(), this.getHeight());
        }
        catch (Exception e)
        {
            if (e instanceof NullPointerException)
                System.out.println("NullPointerException: Skipped Sprite because of Test");
            else
                System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    public abstract void activate(Player player);

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        sprite.draw(batch);
    }

    public void dispose()
    {
        if (this.getParent() != null)
            this.getParent().removeActor(this);
    }
}
