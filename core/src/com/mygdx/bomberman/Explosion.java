package com.mygdx.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Explosion extends Actor
{
    private final int WIDTH = 40;
    private final int HEIGHT = 40;
    private boolean isFirstExplosion = true;
    private final Sprite sprite;
    private final Field field;
    private final Animation<TextureRegion> explosionAnimation;
    TextureRegion frame;
    private float stateTime;
    Sound explosionSound;
    private boolean soundPlayed = false;

    public Explosion(float x, float y, Field field, Assets assets, String region)
    {
        this.sprite = new Sprite();
        this.field = field;
        this.setWidth(WIDTH);
        this.setHeight(HEIGHT);
        this.sprite.setSize(this.getWidth(), this.getHeight());
        int numberOfFrames = 10;
        TextureRegion[] Frames = new TextureRegion[numberOfFrames];

        for (int i = 0; i < numberOfFrames; i++)
            Frames[i] = assets.getRegion("BombAnimation.atlas", region + i);

        explosionAnimation = new Animation<>(0.1f, Frames);
        sprite.setRegion(explosionAnimation.getKeyFrame(stateTime, false));

        this.setTouchable(Touchable.disabled);
        this.setPosition(x, y);
        this.sprite.setPosition(this.getX(), this.getY());
        this.startExtinguished();

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("Music/explosion.mp3"));
    }


    @Override
    public void act(float delta)
    {
        super.act(delta);
        frame = explosionAnimation.getKeyFrame(stateTime, false);
        stateTime += delta;

        try
        {
            sprite.setRegion(frame);
        }
        catch (Exception e)
        {
            if (e instanceof NullPointerException)
                System.out.println("NullPointerException: Skipped Sprite because of Test");
            else
                System.out.println("Exception occurred: " + e.getMessage());
        }
        if (!soundPlayed)
        {
            explosionSound.play();
            soundPlayed = true;
        }

        this.burn();
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        sprite.draw(batch);
    }

    public void startExtinguished()
    {
        Timer.schedule(new Timer.Task()
        {
            private Explosion explosion;
            private Timer.Task init(Explosion explosion)
            {
                this.explosion = explosion;
                return this;
            }
            @Override
            public void run()
            {
                this.explosion.dispose();
            }
        }.init(this), 1);
    }

    public void burn () //Es wird die Umgebung der Explosion auf andere Objekte überprüft und dementsprechend gehandelt
    {
        float centerX = (float) WIDTH / 2 + this.getX();
        float centerY = (float) HEIGHT / 2 + this.getY();
        Actor object = field.getObject(centerX, centerY);

        if (object instanceof Block)
        {
            Block block = (Block) object;
            block.destroy();
        }
        else if (object instanceof Player)
        {
            Player player = (Player) object;
            player.setIsDead(true);
        }
        else if (object instanceof PowerUp && isFirstExplosion)
            this.field.removeObject(object);
        else if (object instanceof Bomb)
        {
            Bomb bomb = (Bomb) object;
            bomb.explode();
        }

        isFirstExplosion = false;
    }

    public void dispose()
    {
        if (this.getParent() != null)
            this.getParent().removeActor(this);
    }
}