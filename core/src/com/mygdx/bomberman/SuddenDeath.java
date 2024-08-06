package com.mygdx.bomberman;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;

public class SuddenDeath
{
    int x = 1,y= 1;
    private final Assets assets;
    int rightBorderX = 13, upperBorderY = 11, leftBorderX = 1, lowerBorderY = 1; //Äußeres Feld mit unzerstörbaren Blöcken abgezogen
    private DIRECTION direction = DIRECTION.UP;
    private static final int BLOCKSIZE = 40;
    private static final float INTERVAL = 0.6f; //Intervall für Wände spawns
    private final Field field;
    private  boolean isPaused = false;
    private boolean speedIncreased =  false;

    public SuddenDeath(Field field, Assets assets)
    {
        this.field = field;
        this.assets = assets;
    }

    public void startTimer()
    {
        final Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task()
        {
            @Override
            public void run()
            {
                if (!isPaused)
                {
                    moveWalls(x, y);

                    if (!speedIncreased)
                    {
                        increaseSpeed();
                        speedIncreased = true;
                    }
                }
            }
        },0, INTERVAL);
    }


    public void moveWalls(int x, int y)
    {
        {
            switch (direction)
            {
                case UP:
                    if (y < upperBorderY)
                        this.y++;
                    else
                    {
                        leftBorderX++;
                        direction = DIRECTION.RIGHT;
                        this.x++;
                    }
                    break;
                case RIGHT:
                    if (x < rightBorderX)
                        this.x++;
                    else
                    {
                        upperBorderY--;
                        direction = DIRECTION.DOWN;
                        this.y--;
                    }
                    break;
                case DOWN:
                    if (y > lowerBorderY)
                        this.y--;
                    else
                    {
                        rightBorderX--;
                        direction = DIRECTION.LEFT;
                        this.x--;
                    }
                    break;
                case LEFT:
                    if (x > leftBorderX)
                        this.x--;
                    else
                    {
                        lowerBorderY++;
                        direction = DIRECTION.UP;
                        this.y++;
                    }
                    break;
            }
        }

        Block newBlock = new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this.field);

        // Spieler bewegen sich immer daher explizit für Kollision/Overlap überprüfen
        try
        {
            Rectangle blockBounds = new Rectangle(newBlock.getX(), newBlock.getY(), BLOCKSIZE, BLOCKSIZE);

            for (Actor actor : field.getPlayerGroup().getChildren())
            {
                Player player = (Player) actor;
                Rectangle playerBounds = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

                if (Intersector.overlaps(playerBounds, blockBounds))
                    player.setIsDead(true);
            }


            // andere Objekte zerstören/dispose : PowerUps, Blocks and Bombs
            Actor object = field.getObject(BLOCKSIZE * x, BLOCKSIZE * y);

            if (object instanceof Block || object instanceof PowerUp || object instanceof Bomb)
                field.removeObject(object);
        }
        catch (Exception e)
        {
            if (e instanceof NullPointerException)
                System.out.println("NullPointerException: Skipped Sprite because of Test");
            else
                System.out.println("Exception occurred: " + e.getMessage());
        }

        field.addObject(newBlock);
    }

    public void increaseSpeed()
    {
        try
        {
            for (Actor actor : field.getPlayerGroup().getChildren())
            {
                if (actor instanceof Player)
                {
                    Player player = (Player) actor;
                    player.increaseSpeed();
                }
            }
        }
        catch (Exception e)
        {
            if (e instanceof NullPointerException)
                System.out.println("NullPointerException: Skipped Sprite because of Test");
            else
                System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    public void pause()
    {
        isPaused = true;
    }

    public void resume()
    {
        isPaused = false;
    }
}
