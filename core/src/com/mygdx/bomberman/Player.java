package com.mygdx.bomberman;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.audio.Sound;

public class Player extends Actor
{
    private  Sprite sprite = null;
    private float movingSpeed = 100f;
    private int numOfBombs = 1;
    private final int id;
    private int bombRange = 1;
    private int bombs = 0;
    private boolean isAbleToKick = false;
    private boolean canMove = false;
    private boolean isDead;
    private final KeyProfile keyProfile;
    private final Field field;
    private final Animation<TextureRegion> moveUpAnimation;
    private final Animation<TextureRegion> moveDownAnimation;
    private final Animation<TextureRegion> moveLeftAnimation;
    private final Animation<TextureRegion> moveRightAnimation;
    private final Animation<TextureRegion> deathAnimation;
    private final Assets assets;
    private float stateTime;
    private float deathStateTime;
    private final Group bombGroup;
    private final int skinIndex;
    private int currentPoints;
    private final Sound deathSound;

    public Player(Assets assets, int id, int x, int y, KeyProfile keyProfile, Field field, int skinIndex, int currentPoints)
    {
        this.assets = assets;
        this.id = id;
        this.setName("Player_" + id);
        int HEIGHT = 38;
        int WIDTH = 28;
        this.setSize(WIDTH, HEIGHT);
        this.setPosition(x, y);

        try
        {
            this.sprite = new Sprite(assets.getRegion("skin" + skinIndex + ".atlas", "down2"));
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

        this.keyProfile = keyProfile;
        this.field = field;
        this.field.addObject(this);
        this.bombGroup = this.field.getBombGroup();
        this.skinIndex = skinIndex;
        this.currentPoints = currentPoints;

        moveUpAnimation = new Animation<>(0.2f, createAnimationFrames("up"));
        moveDownAnimation = new Animation<>(0.2f, createAnimationFrames("down"));
        moveLeftAnimation = new Animation<>(0.2f, createAnimationFrames("left"));
        moveRightAnimation = new Animation<>(0.2f, createAnimationFrames("right"));
        deathAnimation = new Animation<>(0.2f, createDeathFrames());

        deathSound = Gdx.audio.newSound(Gdx.files.internal("Music/death.mp3"));
    }

    private TextureRegion[] createAnimationFrames(String direction)
    {
        return new TextureRegion[]
                {
                assets.getRegion("skin" + skinIndex + ".atlas", direction + "1"),
                assets.getRegion("skin" + skinIndex + ".atlas", direction + "2"),
                assets.getRegion("skin" + skinIndex + ".atlas", direction + "3"),
                assets.getRegion("skin" + skinIndex + ".atlas", direction + "2"),
        };
    }

    private TextureRegion[] createDeathFrames()
    {
        TextureRegion[] frames = new TextureRegion[6];

        for (int i = 0; i < 6; i++)
            frames[i] = assets.getRegion("skin" + skinIndex + ".atlas", "death" + (i + 1));

        return frames;
    }

    public void increaseCurrentPoints()
    {
        this.currentPoints++;
    }

    @Override
    public void act(float delta)
    {
        if (isDead)
            createDeathAnimation(delta);
        if (!canMove)
            return;

        super.act(delta);

        if (Gdx.input.isKeyPressed(keyProfile.getMoveUpKey()))
            moveUp(delta);
        if (Gdx.input.isKeyPressed(keyProfile.getMoveDownKey()))
            moveDown(delta);
        if (Gdx.input.isKeyPressed(keyProfile.getMoveLeftKey()))
            moveLeft(delta);
        if (Gdx.input.isKeyPressed(keyProfile.getMoveRightKey()))
            moveRight(delta);
        if (Gdx.input.isKeyJustPressed(keyProfile.getPlaceBombKey()))
        {
            if (this.bombs < this.numOfBombs)
                placeBomb();
        }
    }

    public void placeBomb()
    {
        int blockCoordX = (Math.round(this.getX()/40)) * 40;
        int blockCoordY = (Math.round(this.getY()/40)) * 40;
        this.field.addObject(new Bomb(blockCoordX, blockCoordY, field, this, assets));
    }

    public void moveUp(float delta)
    {
        int blockCoordCenterX = ((Math.round(this.getX()/40)) * 40) + 40/2;
        int blockCoordCenterY = ((Math.round(this.getY()/40)) * 40) + 40/2;

        Actor objectUnderMe = this.field.getObject(blockCoordCenterX, blockCoordCenterY);
        Actor object = field.getObjectFrom(DIRECTION.UP, this);

        if (object instanceof Block) //Blöcke werden als blockierendes Hindernis wahrgenommen
            return;
        else if (object instanceof Bomb) //Bomben werden auch als Hindernis wahrgenommen, außer wenn der Spieler das Power-Up KickingBomb besitzt
        {
            if (object != objectUnderMe)
            {
                if (isAbleToKick)
                    ((Bomb) object).moveBombUp(delta);
                return;
            }
        }

        this.moveBy(0, movingSpeed * delta);
        sprite.setY(this.getY()); //Koordinaten des Spielers werden nach erfolgreichem Bewegen aktualisiert
        sprite.setRegion(moveUpAnimation.getKeyFrame(stateTime, true));
        stateTime += delta;

        if (object instanceof PowerUp)
            ((PowerUp) object).activate(this);
    }

    public void moveDown(float delta)
    {
        int blockCoordCenterX = ((Math.round(this.getX()/40)) * 40) + 40/2;
        int blockCoordCenterY = ((Math.round(this.getY()/40)) * 40) + 40/2;

        Actor objectUnderMe = this.field.getObject(blockCoordCenterX, blockCoordCenterY);
        Actor object = field.getObjectFrom(DIRECTION.DOWN, this);

        if (object instanceof Block)
            return;
        else if (object instanceof Bomb)
        {
            if (object != objectUnderMe)
            {
                if (isAbleToKick)
                    ((Bomb) object).moveBombDown(delta);
                return;
            }
        }

        this.moveBy(0, -(movingSpeed * delta));
        sprite.setY(this.getY());
        sprite.setRegion(moveDownAnimation.getKeyFrame(stateTime, true));
        stateTime += delta;

        if (object instanceof PowerUp)
            ((PowerUp) object).activate(this);
    }

    public void moveLeft(float delta)
    {
        int blockCoordCenterX = ((Math.round(this.getX()/40)) * 40) + 40/2;
        int blockCoordCenterY = ((Math.round(this.getY()/40)) * 40) + 40/2;

        Actor objectUnderMe = this.field.getObject(blockCoordCenterX, blockCoordCenterY);
        Actor object = field.getObjectFrom(DIRECTION.LEFT, this);

        if (object instanceof Block)
            return;
        else if (object instanceof Bomb)
        {
            if (object != objectUnderMe)
            {
                if (isAbleToKick)
                    ((Bomb) object).moveBombLeft(delta);
                return;
            }
        }

        this.moveBy(-(movingSpeed * delta), 0);
        sprite.setX(this.getX());
        sprite.setRegion(moveLeftAnimation.getKeyFrame(stateTime, true));
        stateTime += delta;

        if (object instanceof PowerUp)
            ((PowerUp) object).activate(this);
    }

    public void moveRight(float delta)
    {
        int blockCoordCenterX = ((Math.round(this.getX()/40)) * 40) + 40/2;
        int blockCoordCenterY = ((Math.round(this.getY()/40)) * 40) + 40/2;

        Actor objectUnderMe = this.field.getObject(blockCoordCenterX, blockCoordCenterY);
        Actor object = field.getObjectFrom(DIRECTION.RIGHT, this);

        if (object instanceof Block)
            return;
        else if (object instanceof Bomb)
        {
            if (object != objectUnderMe)
            {
                if (isAbleToKick)
                    ((Bomb) object).moveBombRight(delta);
                return;
            }
        }

        this.moveBy(movingSpeed * delta, 0);

        try
        {
            sprite.setX(this.getX());
            sprite.setRegion(moveRightAnimation.getKeyFrame(stateTime, true));
        }
        catch (Exception e)
        {
            if (e instanceof NullPointerException)
                System.out.println("NullPointerException: Skipped Sprite because of Test");
            else
                System.out.println("Exception occurred: " + e.getMessage());
        }

        stateTime += delta;

        if (object instanceof PowerUp)
            ((PowerUp) object).activate(this);
    }

    public void createDeathAnimation(float delta)
    {
        canMove = false;

        if (deathStateTime == 0)
            deathSound.play();  //Sound nur beim ersten Aufruf abspielen

        sprite.setRegion(deathAnimation.getKeyFrame(deathStateTime, false));
        deathStateTime += delta;

        if (deathAnimation.isAnimationFinished(deathStateTime))
        {
            field.removeObject(Player.this);
            deathStateTime = 0;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        for (Actor actor : bombGroup.getChildren())
            actor.draw(batch, 1);

        sprite.draw(batch);
    }

    public void increaseBombs()
    {
        this.bombs++;
    }

    public void decreaseBombs()
    {
        this.bombs--;
    }

    public void increaseSpeed()
    {
        this.movingSpeed  += 50f;
    }

    public void increaseBombRange()
    {
        this.bombRange++;
    }

    public void increaseNumOfBombs()
    {
        if (numOfBombs <=2)
            this.numOfBombs++;
    }

    public void dispose()
    {
        if (this.getParent() != null)
            this.getParent().removeActor(this);
    }

    public boolean getCanMove()
    {
        return canMove;
    }

    public int getCurrentPoints()
    {
        return currentPoints;
    }

    public int getBombRange()
    {
        return bombRange;
    }

    public int getID()
    {
        return this.id;
    }

    public float getMovingSpeed()
    {
        return movingSpeed;
    }

    public int getNumOfBombs()
    {
        return this.numOfBombs;
    }

    public boolean getIsAbleToKick()
    {
        return this.isAbleToKick;
    }

    public void setCanMove(boolean canMove)
    {
        this.canMove = canMove;
    }

    public void setAbleToKick()
    {
        this.isAbleToKick = true;
    }

    public void setIsDead(boolean isDead)
    {
        this.isDead = isDead;
    }
}
