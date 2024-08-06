package com.mygdx.bomberman;

import java.util.Random;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Block extends Actor
{
    private final BlockType blockType;
    private Sprite sprite = null;
    private final Assets assets;
    private final Field field;
    private static final int SIZE = 40;


    public Block(Assets assets, BlockType blockType, float x, float y, Field field)
    {
        this.assets = assets;
        this.blockType = blockType;
        this.field = field;
        TextureRegion texture;
        this.setPosition(x, y);
        this.setSize(SIZE, SIZE);

        try
        {
            if (blockType == BlockType.DESTROYABLE)
                texture = assets.getRegion("GameAssets.atlas", GlobalSettings.chosenMap + "Soft_Wall");
            else
                texture = assets.getRegion("GameAssets.atlas", GlobalSettings.chosenMap + "Hard_Wall");

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

    public void spawnPowerUp()
    {
        Random probability = new Random();
        int probSpawnPowerUp = probability.nextInt(10);

        if (probSpawnPowerUp < 5)
        {
            Random random = new Random();
            int randPowerUp = random.nextInt(4);

            switch (randPowerUp)
            {
                //Je nachdem welche zufällige Zahl generiert wird, wird das entsprechende Objekt des Power-Ups erzeugt
                case 0:
                    this.field.addObject(new IncreasedBomb(this.getX(), this.getY(), assets.getRegion("GameAssets.atlas", "IncreasedBomb")));
                    break;
                case 1:
                    this.field.addObject(new BombRange(this.getX(), this.getY(), assets.getRegion("GameAssets.atlas", "BombRange")));
                    break;
                case 2:
                    this.field.addObject(new IncreasedSpeed(this.getX(), this.getY(), assets.getRegion("GameAssets.atlas", "IncreasedSpeed")));
                    break;
                case 3:
                    this.field.addObject(new KickingBomb(this.getX(), this.getY(), assets.getRegion("GameAssets.atlas", "KickingBomb")));
                    break;
            }
        }
    }

    public void destroy()
    {
        if (this.blockType == BlockType.DESTROYABLE)
        {
            spawnPowerUp();
            this.field.removeObject(this);
        }
    }
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

    public BlockType getBlockType()
    {
        return this.blockType;
    }
}
