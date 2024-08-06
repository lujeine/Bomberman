import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.bomberman.*;
import de.tomgrill.gdxtesting.GdxTestRunnerExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GdxTestRunnerExtension.class)
public class PlayerTest
{
    Field fieldMock = mock(Field.class);
    Assets assetsMock = mock(Assets.class);

    @Test
    public void getCurrentPointsTest()
    {
        Player player2 = new Player(assetsMock,2, 40, 440, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 10);
        int currentPoints = player2.getCurrentPoints();
        assertEquals(10,currentPoints);
    }

    @Test
    public void increaseCurrentPointsTest()
    {
        Player player3 = new Player(assetsMock,2, 40, 440, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 50);
        player3.increaseCurrentPoints();
        assertEquals(51, player3.getCurrentPoints());
    }

    @Test
    public void increaseBombsTest()
    {
        Player player4 = new Player(assetsMock,2, 40, 440, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 10);
        player4.increaseBombs();
        assertEquals(1, player4.getNumOfBombs());
    }

    @Test
    public void getBombRangeTest()
    {
        Player player5 = new Player(assetsMock,2, 40, 440, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 10);
        player5.increaseBombRange();
        int result = player5.getBombRange();
        assertEquals(2, result);
    }

    @Test
    public void setCanMoveTest()
    {
        Player player6 = new Player(assetsMock,2, 40, 440, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 10);
        player6.setCanMove(false);
        boolean canMove = player6.getCanMove();
        assertFalse(false, String.valueOf(canMove));
    }

    @Test
    public void placeBombTest()
    {
        Player player7 = new Player(assetsMock,2, 40, 440, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 10);
        player7.placeBomb();
        Actor temp = fieldMock.getObject(80,120);
        assertNull(temp);
    }

    @Test
    public void testGetBlockDOWN()
    {
        Assets assetsmock = mock(Assets.class);
        Field field = new Field(assetsmock);
        Block newBlock = new Block(assetsmock, BlockType.DESTROYABLE, 5, 5, field);
        field.addObject(newBlock);  //Block wird an Position (5, 5) platziert
        Player player = new Player(assetsMock,1, 5, 6, new KeyProfile(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E), fieldMock, 1, 0);
        player.setPosition(5, 6);   //Setzen der Spielerposition oberhalb des Blocks

        Actor temp = field.getObjectFrom(DIRECTION.DOWN, player);   //Überprüfung, ob sich unterhalb des Spielers ein Block befindet

        assertTrue(temp instanceof Block);  //Überprüfung, ob das zurückgegebene Objekt eine Instanz von Block ist
    }

    @Test
    public void testGetBlockUp()
    {
        Assets assetsmock = mock(Assets.class);
        Field field = new Field(assetsmock);
        Block newBlock = new Block(assetsmock, BlockType.DESTROYABLE, 5, 5, field);
        field.addObject(newBlock);  //Block wird an Position (5, 6) platziert
        Player player = new Player(assetsMock, 1, 5, 5, new KeyProfile(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E), fieldMock, 1, 0);
        player.setPosition(5, 5);   //Setzen der Spielerposition unterhalb des Blocks

        Actor temp = field.getObjectFrom(DIRECTION.UP, player); //Überprüfung, ob sich oberhalb des Spielers ein Block befindet

        assertTrue(temp instanceof Block);  //Überprüfung, ob das zurückgegebene Objekt eine Instanz von Block ist
    }

    @Test
    public void testGetBlockLeft()
    {
            Assets assetsmock = mock(Assets.class);
            Field field = new Field(assetsmock);
            Block newBlock = new Block(assetsmock, BlockType.DESTROYABLE, 5, 5, field);
            field.addObject(newBlock);  //Block wird an Position (5, 5) platziert
            Player player = new Player(assetsMock, 1, 6, 5, new KeyProfile(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E), fieldMock, 1, 0);
            player.setPosition(6, 5);   //Setzen der Spielerposition rechts vom Blocks

            Actor temp = field.getObjectFrom(DIRECTION.LEFT, player);   //Überprüfung, ob sich oberhalb des Spielers ein Block befindet

            assertTrue(temp instanceof Block);  //Überprüfung, ob das zurückgegebene Objekt eine Instanz von Block ist
    }

    @Test
    public void testGetBlockRight()
    {
        Assets assetsmock = mock(Assets.class);
        Field field = new Field(assetsmock);
        Block newBlock = new Block(assetsmock, BlockType.DESTROYABLE, 5, 5, field);
        field.addObject(newBlock);  //Block wird an Position (5, 5) platziert
        Player player = new Player(assetsMock, 1, 4, 5, new KeyProfile(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E), fieldMock, 1, 0);
        player.setPosition(4, 5);   //Setzen der Spielerposition links vom Blocks

        Actor temp = field.getObjectFrom(DIRECTION.RIGHT, player);  //Überprüfung, ob sich oberhalb des Spielers ein Block befindet

        assertTrue(temp instanceof Block);  //Überprüfung, ob das zurückgegebene Objekt eine Instanz von Block ist
    }

    @Test
    public void testMovementRightBlocked()
    {
        Assets assetsmock = mock(Assets.class);
        Field field = new Field(assetsmock);
        Player player8 = new Player(assetsMock,2, 90, 90, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), field, 2, 10);
        Block newBlock = new Block(assetsmock, BlockType.DESTROYABLE, 91, 90, field);

        field.addObject(newBlock);  //Block wird an Position (91, 90) platziert
        player8.moveRight(1f);
        assertEquals(90, player8.getX());
        assertEquals(90, player8.getY());
    }

    @Test
    public void testMovementRightNotBlocked()
    {
        Assets assetsmock = mock(Assets.class);
        Field field = new Field(assetsmock);
        Player player8 = new Player(assetsMock,2, 90, 90, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), field, 2, 10);

        player8.moveRight(1f);
        assertEquals(190, player8.getX());
        assertEquals(90, player8.getY());
    }


    @Test
    public void testMovementRightBombkickPossible()
    {
        fieldMock = mock(Field.class);
        Assets assetsMock = mock(Assets.class);

        Player player8 = new Player(assetsMock,2, 90, 90, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 10);
        Bomb bomb = new Bomb(200, 300, fieldMock, player8,assetsMock );

        player8.setAbleToKick();
        player8.setWidth(40);
        player8.setHeight(40);
        player8.moveRight(1f);
        assertEquals(190, player8.getX());
        assertEquals(90, player8.getY());
        assertEquals(200,bomb.getX());
    }

    @Test
    public void testMovementRightBombkickNotPossible()
    {
        fieldMock = mock(Field.class);
        Assets assetsMock = mock(Assets.class);

        Player player8 = new Player(assetsMock,2, 90, 90, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 10);
        Bomb bomb = new Bomb(200, 300, fieldMock, player8,assetsMock );

        player8.setAbleToKick();
        player8.setWidth(40);
        player8.setHeight(40);
        player8.moveRight(1f);
        assertEquals(190, player8.getX());
        assertEquals(90, player8.getY());
        assertEquals(200,bomb.getX());
    }

    @Test
    public void placeBomb()
    {
        Player player8 = new Player(assetsMock,2, 40, 440, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 10);
        player8.setWidth(40);
        player8.setHeight(40);
        player8.placeBomb();
        verify(fieldMock, times(2)).addObject(any());
    }
}