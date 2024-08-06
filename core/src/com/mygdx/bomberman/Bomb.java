package com.mygdx.bomberman;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Bomb extends Actor
{
    private static final int SIZE = 40;
    private int bombRange = 1;
    private float stateTime;
    Assets assets;
    private final Animation<TextureRegion> bombTickingAnimation;
    private  Sprite sprite;
    private final Group explosionGroup;
    private Field field;
    private final Player owner;
    private Timer timer;

    public Bomb(float x, float y, Field field, Player owner, Assets assets)
    {
        this.assets = assets;
        this.owner = owner;
        this.setName("Bombe von " + this.owner.getName());
        this.owner.increaseBombs();

        TextureRegion[] Frames =
        {
            assets.getRegion("BombAnimation.atlas", "B1"),
            assets.getRegion("BombAnimation.atlas", "B2")
        };

        bombTickingAnimation = new Animation<>(0.2f, Frames);

        try
        {
            this.sprite = new Sprite(Frames[0]);
        }
        catch (Exception e)
        {
            if (e instanceof NullPointerException)
                System.out.println("NullPointerException: Skipped Sprite because of Test");
            else
                System.out.println("Exception occurred: " + e.getMessage());
        }

        this.setPosition(x, y);
        this.setSize(SIZE, SIZE);

        try
        {
            this.sprite.setSize(this.getWidth(), this.getHeight());
            this.sprite.setPosition(this.getX(), this.getY());
            this.sprite.setRegion(bombTickingAnimation.getKeyFrame(stateTime, true));
        }
        catch (Exception e)
        {
            if (e instanceof NullPointerException)
                System.out.println("NullPointerException: Skipped Sprite because of Test");
            else
                System.out.println("Exception occurred: " + e.getMessage());
        }

        this.startTicking();
        this.field = field;
        this.bombRange = owner.getBombRange();
        this.explosionGroup = this.field.getExplosionGroup();
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        TextureRegion frame = bombTickingAnimation.getKeyFrame(stateTime, true);
        stateTime += delta;
        this.sprite.setRegion(frame);
    }

    public void moveBombUp(float delta)
    {
        Actor object = field.getObjectFrom(DIRECTION.UP, this);

        if ((object == null) || (object instanceof PowerUp) || (object instanceof Explosion))
        {
            this.moveBy(0, 40);
            sprite.setY(this.getY());
            stateTime += delta;
        }
    }

    public void moveBombDown(float delta)
    {
        Actor object = field.getObjectFrom(DIRECTION.DOWN, this);

        if ((object == null) || (object instanceof Explosion) || (object instanceof PowerUp))
        {
            this.moveBy(0, -(40));
            sprite.setY(this.getY());
            stateTime += delta;
        }
    }

    public void moveBombLeft(float delta)
    {
        Actor object = field.getObjectFrom(DIRECTION.LEFT, this);

        if ((object == null) || (object instanceof Explosion) || (object instanceof PowerUp))
        {
            this.moveBy(-(40), 0);
            sprite.setX(this.getX());
            stateTime += delta;
        }
    }

    public void moveBombRight(float delta)
    {
        Actor object = field.getObjectFrom(DIRECTION.RIGHT, this);

        if ((object == null) || (object instanceof Explosion) || (object instanceof PowerUp))
        {
            this.moveBy(40, 0);

            try
            {
                sprite.setX(this.getX());
            }
            catch (Exception e)
            {
                System.out.println(e);
            }

            stateTime += delta;
        }
    }

    public void startTicking()
    {
        timer = new Timer();
        timer.scheduleTask(new Timer.Task()
        {
            private Bomb bomb;
            private Timer.Task init(Bomb bomb)
            {
                this.bomb = bomb;
                return this;
            }
            @Override
            public void run()
            {
                this.bomb.explode();
            }
        }.init(this), 1.5f);
    }

    public void explode()
    {
        float explosionCenterX = (this.getRight() - this.getX()) / 2 + this.getX();
        float explosionCenterY = (this.getTop() - this.getY()) / 2 + this.getY();

        Map<String, int[]> directionMap = new HashMap<>();
        directionMap.put("North", new int[]{0, 1});
        directionMap.put("East", new int[]{1, 0});
        directionMap.put("South", new int[]{0, -1});
        directionMap.put("West", new int[]{-1, 0});

        createExplosion(this.getX(), this.getY(), "center");

        for (Map.Entry<String, int[]> entry : directionMap.entrySet())
        {
            String directionName = entry.getKey();  // Zuweisung der Himmelsrichtung
            int[] directionVector = entry.getValue(); //Entsprechender Richtungsvektor der zuvor zugewiesenen Himmelsrichtung

            for (int currentRange = 1; currentRange <= bombRange; currentRange++)
            {
                float newBombSegmentX = this.getX() + directionVector[0] * currentRange * 40;
                float newBombSegmentY = this.getY() + directionVector[1] * currentRange * 40;

                Actor object = field.getObject(explosionCenterX + directionVector[0] * currentRange * 40, explosionCenterY + directionVector[1] * currentRange * 40);

                boolean checkVertical = Objects.equals(directionName, "North") || Objects.equals(directionName, "South");
                boolean checkHorizontal = Objects.equals(directionName, "East") || Objects.equals(directionName, "West");
                boolean checkEdge = (currentRange == bombRange);

                if (object instanceof Block) //Prüfen, ob sich ein Block im Weg befindet, ein zerstörbarer Block
                {
                    Block block = (Block) object;

                    if (block.getBlockType() == BlockType.DESTROYABLE)
                        createExplosion(newBombSegmentX, newBombSegmentY, "edge" + directionName);

                    break;
                }
                else
                {
                    //Je nach Fall werden in der Methode createExplosion() Konstruktoraufrufe von Explosion mit
                    //verschiedenen Regions des TextureAtlas getätigt. Wichtig für die verschiedenen Texturen
                    if (checkEdge)
                        createExplosion(newBombSegmentX, newBombSegmentY,"edge" + directionName);
                    else if (checkVertical)
                        createExplosion(newBombSegmentX, newBombSegmentY,"vertical");
                    else if (checkHorizontal)
                        createExplosion(newBombSegmentX, newBombSegmentY,"horizontal");
                }
            }
        }

        this.dispose();
    }

    private void createExplosion(float x, float y, String explosionType)
    {
        this.field.addObject(new Explosion(x, y, field, assets, explosionType));
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        sprite.draw(batch);

        for (Actor actor : explosionGroup.getChildren())
            actor.draw(batch, 1);
    }

    public void dispose()
    {
        timer.clear();

        if (this.owner != null)
            this.owner.decreaseBombs();
        if (this.getParent() != null)
            this.getParent().removeActor(this);
    }

    public int getBombRange()
    {
        return this.bombRange;
    }

    public Player getOwner()
    {
        return this.owner;
    }

    public void setField(Field field)
    {
        this.field = field;
    }
}
