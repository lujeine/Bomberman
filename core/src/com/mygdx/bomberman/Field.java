package com.mygdx.bomberman;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

public class Field extends Group
{
    private static final int WIDTH = 600, HEIGHT = 520;
    private static final int ROWS = 13, COLUMNS = 15;
    private static final int BLOCKSIZE = 40;
    private final Assets assets;
    private final Group blockGroup;
    private final Group bombGroup;
    private final Group explosionGroup;
    private final Group powerUpGroup;
    private Group playerGroup;

    public Field(Assets assets)
    {
        this.assets = assets;

        blockGroup = new Group();
        playerGroup = new Group();
        bombGroup = new Group();
        explosionGroup = new Group();
        powerUpGroup = new Group();

        this.addActor(blockGroup);
        this.addActor(playerGroup);
        this.addActor(bombGroup);
        this.addActor(powerUpGroup);
        this.addActor(explosionGroup);

        placePlayer();
    }

    private void placePlayer() //Player-Objekte aller Spieler mit verschiedenen Startpositionen werden erzeugt
    {
        int[] point1 =  new int[]{BLOCKSIZE, BLOCKSIZE};
        int[] point2 =  new int[]{BLOCKSIZE, HEIGHT - 2*BLOCKSIZE};
        int[] point3 =  new int[]{WIDTH - 2*BLOCKSIZE, HEIGHT - 2*BLOCKSIZE};
        int[] point4 =  new int[]{WIDTH - 2*BLOCKSIZE, BLOCKSIZE};

        List<int[]> spawnPoint = Arrays.asList(point1, point2, point3, point4);

        for (int i = 0; i < GlobalSettings.noOfPlayers; i++)
        {
            PlayerData playerData = GlobalSettings.playerData[i];

            if (playerData.inGame())
                playerGroup.addActor(new Player(assets, playerData.getId(), spawnPoint.get(i)[0], spawnPoint.get(i)[1], GlobalSettings.keyProfiles[i], this, GlobalSettings.skins[i], playerData.getPoints()));
        }
    }

    public void placeBlocks()
    {
        placeOuterWalls();

        if (GlobalSettings.chosenMap == 0)
            placeInnerUndestroyableBlocksMap0();
        else if (GlobalSettings.chosenMap == 1)
            placeInnerUndestroyableBlocksMap1();
        else if (GlobalSettings.chosenMap == 2)
            placeInnerUndestroyableBlocksMap2();

        placeDestroyableBlocks();
    }

    public void placeOuterWalls()
    {
        for (int i = 0; i < ROWS; i++)
        {
            blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, 0, BLOCKSIZE * i,this));
            blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, WIDTH - BLOCKSIZE, BLOCKSIZE * i, this));
        }
        for (int i = 0; i < COLUMNS; i++)
        {
            blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * i, 0, this));
            blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * i, HEIGHT - BLOCKSIZE, this));
        }
    }

    public void placeInnerUndestroyableBlocksMap0()
    {
        for (int y = 2; y < ROWS - 1; y += 2)
        {
            for (int x = 2; x < COLUMNS - 1; x += 2)
                blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
        }
    }

    public void placeInnerUndestroyableBlocksMap1()
    {
        for (int y = 2; y < ROWS - 2; y ++)
        {
            for (int x = 2; x < COLUMNS - 2; x ++)
            {
                if ((y == 2 || y == 10) && ((x == 2 || x == 3)|| x == 11 || x == 12))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
                else if ((y == 7 || y == 5) && ((x == 4 || x == 10)))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
                else if ((y == 4 || y == 8) && (x <= 9 && x >= 5) && (x != 6 && x != 8))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
                else if ((y == 2 || y == 6 || y == 10) && (x <= 8 && x >= 6))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
                else if ((y <= 7 && y >= 5) && (x == 2 || x == 12))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
                else if ((y == 4 || y == 8) && (x == 4 || x == 10) )
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
                else if ((y == 3 || y== 9) && (x == 2 || x == 12))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
            }
        }
    }

    public void placeInnerUndestroyableBlocksMap2()
    {
        for (int y = 2; y < ROWS - 2; y ++)
        {
            for (int x = 2; x < COLUMNS - 2; x ++)
            {
                if ((y ==2 || y == 10) && (x == 2 || x == 12))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
                else if ((y == 3 || y == 9) && (x == 3 || x == 11))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
                else if ((y == 4 || y == 8) && ((x == 4 || x == 10) || (x == 6 || x== 8 )))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
                else if ((y == 6 ) && (x == 3 || x == 11))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
                else if ((y == 2 || y == 6 || y == 10) && (x >= 6 && x<= 8 ))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
                else if ((y == 7 || y == 5) && (x == 2 || x == 12))
                    blockGroup.addActor(new Block(assets, BlockType.UNDESTROYABLE, BLOCKSIZE * x, BLOCKSIZE * y, this));
            }
        }
    }

    public void placeDestroyableBlocks()
    {
        int softWallCount = 0;

        while (softWallCount < 80)
        {
            //Zufällige Koordinaten für einen Block
            int randX = ThreadLocalRandom.current().nextInt(1, COLUMNS-1);
            int randY = ThreadLocalRandom.current().nextInt(1, ROWS-1);

            // isCorner und isExpandedCorner sorgen für eine Bewegungsfreiheit für jeden Spieler am Anfang jeder Runde
            boolean isCorner = ((randX == 1 && randY == 1) ||
                                (randX == 1 && randY == (ROWS - 2)) ||
                                (randX == (COLUMNS - 2) && randY == 1) ||
                                (randX == (COLUMNS - 2) && randY == (ROWS - 2)));

            boolean isExpandedCorner = (
                    (randX == 1 && randY == 2) || (randX == 2 && randY == 1) ||
                    (randX == 1 && randY == (ROWS - 3)) || (randX == 2 && randY == (ROWS - 2)) ||
                    (randX == (COLUMNS - 2) && randY == 2) || (randX == (COLUMNS - 3) && randY == 1) ||
                    (randX == (COLUMNS - 2) && randY == (ROWS - 3)) || (randX == (COLUMNS - 3) && randY == (ROWS - 2))
            );

            boolean isOccupied = false;

            for (Actor block : blockGroup.getChildren())
            {
                if ((randX == (block.getX() / 40)) && (randY == (block.getY() / 40)))
                {
                    isOccupied = true;
                    break;
                }
            }

            if (!isCorner && !isExpandedCorner && !isOccupied)
            {
                blockGroup.addActor(new Block(assets, BlockType.DESTROYABLE, BLOCKSIZE * randX, BLOCKSIZE * randY, this));
                softWallCount++;
            }
        }
    }

    public boolean isLastPlayer()
    {
        return playerGroup.getChildren().size == 1;
    }

    public int howManyPlayersInGame()
    {
        int activePlayers =0;

        for (int i = 0; i<GlobalSettings.noOfPlayers; i++)
        {
            if (GlobalSettings.playerData[i].inGame())
                activePlayers++;
        }

        return  activePlayers;
    }

    public void removeObject(Actor actor)
    {
        if (actor instanceof Block)
        {
            Block block = (Block) actor;
            block.dispose();
        }
        else if (actor instanceof Player)
        {
            Player player = (Player) actor;
            player.dispose();

            if (isLastPlayer())
                GameScreen.handleRoundOver(getLastRemainingPlayer());
        }
        else if (actor instanceof Bomb)
        {
            Bomb bomb = (Bomb) actor;
            bomb.dispose();
        }
        else if (actor instanceof Explosion)
        {
            Explosion explosion = (Explosion) actor;
            explosion.dispose();
        }
        else if (actor instanceof PowerUp)
        {
            PowerUp powerUp = (PowerUp) actor;
            powerUp.dispose();
        }
    }

    public void addObject(Actor actor)
    {
        if (actor instanceof Block)
        {
            Block block = (Block) actor;
            this.blockGroup.addActor(block);
        }
        else if (actor instanceof Player)
        {
            Player player = (Player) actor;
            this.playerGroup.addActor(player);
        }
        else if (actor instanceof Bomb)
        {
            Bomb bomb = (Bomb) actor;
            this.bombGroup.addActor(bomb);
        }
        else if (actor instanceof Explosion)
        {
            Explosion explosion = (Explosion) actor;
            this.explosionGroup.addActor(explosion);
        }
        else if (actor instanceof PowerUp)
        {
            PowerUp powerUp = (PowerUp) actor;
            this.powerUpGroup.addActor(powerUp);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        this.drawChildren(batch, 0);
    }

    public BlockType getBlockTypeAtPosition(int x, int y) //zum Testen des Blockes
    {
        Actor object = getObject(BLOCKSIZE * x, BLOCKSIZE * y);

        if (object instanceof Block)
        {
            Block block = (Block) object;
            return block.getBlockType();
        }

        return null;  //falls kein Block an der Stelle ist
    }

    public Group getBombGroup()
    {
        return this.bombGroup;
    }

    public Group getExplosionGroup()
    {
        return this.explosionGroup;
    }

    public Group getPlayerGroup()
    {
        return this.playerGroup;
    }

    public Group getBlockGroup()
    {
        return this.blockGroup;
    }

    public Player getLastRemainingPlayer()
    {
        if (playerGroup.getChildren().size == 1)
            return (Player) playerGroup.getChildren().first();

        return null;
    }

    public Actor getObject(float x, float y)
    {
        return this.hit(x, y, true);
    }

    public Actor getObjectFrom(DIRECTION direction, Actor actor) //Umgebung des Actor erkennen
    {
        Actor object;
        Actor objectFromPos1 = null;
        Actor objectFromPos2 = null;
        Actor objectFromPos3 = null;

        int TOLERANCE = 2;

        switch (direction)
        {
            case UP:
                objectFromPos1 = getObject(actor.getX() + TOLERANCE, actor.getTop() + 1);
                objectFromPos2 = getObject(actor.getX() + actor.getWidth()/2, actor.getTop() + 1);
                objectFromPos3 = getObject(actor.getRight() - TOLERANCE, actor.getTop() + 1);
                break;
            case RIGHT:
                objectFromPos1 = getObject(actor.getRight() + 1, actor.getY() + TOLERANCE);
                objectFromPos2 = getObject(actor.getRight() + 1, actor.getY() + actor.getHeight()/2);
                objectFromPos3 = getObject(actor.getRight() + 1, actor.getTop() - TOLERANCE);
                break;
            case DOWN:
                objectFromPos1 = getObject(actor.getX() + TOLERANCE, actor.getY() - 1);
                objectFromPos2 = getObject(actor.getX() + actor.getWidth()/2, actor.getY() - 1);
                objectFromPos3 = getObject(actor.getRight() - TOLERANCE, actor.getY() - 1);
                break;
            case LEFT:
                objectFromPos1 = getObject(actor.getX() - 1, actor.getY() + TOLERANCE);
                objectFromPos2 = getObject(actor.getX() - 1, actor.getY() + actor.getHeight()/2);
                objectFromPos3 = getObject(actor.getX() - 1, actor.getTop() - TOLERANCE);
                break;
        }

        if (objectFromPos1 instanceof Block)
            object = objectFromPos1;
        else if (objectFromPos2 instanceof Block)
            object = objectFromPos2;
        else if (objectFromPos3 instanceof Block)
            object = objectFromPos3;
        else
            object = objectFromPos2;

        return object;
    }

    public void setPlayerGroup(Group playerGroup)
    {
        this.playerGroup = playerGroup;
    }

}
