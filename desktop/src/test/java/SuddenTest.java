import com.badlogic.gdx.Input;
import com.mygdx.bomberman.*;
import de.tomgrill.gdxtesting.GdxTestRunnerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GdxTestRunnerExtension.class)
public class SuddenTest
{
    @Test
    public void MoveWallsTest()
    {
        Assets assetsMock = mock(Assets.class);
        Field field = mock(Field.class);

        when(field.getBlockTypeAtPosition(1, 1)).thenReturn(null);
        SuddenDeath suddenDeath = new SuddenDeath(field, assetsMock);

        BlockType actualBlockType = field.getBlockTypeAtPosition(1, 1);
        assertNull(actualBlockType); //vor moveWalls Aufruf sollte an dieser Position kein Block sein
        suddenDeath.moveWalls(1, 1);
        when(field.getBlockTypeAtPosition(1, 1)).thenReturn(BlockType.UNDESTROYABLE);
        actualBlockType = field.getBlockTypeAtPosition(1, 1);
        assertEquals(BlockType.UNDESTROYABLE, actualBlockType);  //nach dem Aufruf befindet sich dort ein UNDESTROYABLE Block

        for (int i = 0; i < 5; i++) //teste das Setzen mehrerer Blöcke
        {
            for (int j = 0; j < 5; j++)
            {
                suddenDeath.moveWalls(i, j);
                when(field.getBlockTypeAtPosition(i, j)).thenReturn(BlockType.UNDESTROYABLE);
                actualBlockType = field.getBlockTypeAtPosition(i, j);
                assertEquals(BlockType.UNDESTROYABLE, actualBlockType);
            }
        }
    }

    @Test
    public void IncreaseSpeedTest()
    {
        Assets assetsMock = mock(Assets.class);
        Field fieldMock = new Field(assetsMock);
        SuddenDeath suddenDeath = new SuddenDeath(fieldMock,assetsMock);
        Player player1 = new Player(assetsMock,1, 40, 40, new KeyProfile(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E), fieldMock, 1, 0);
        Player player2 = new Player(assetsMock,2, 40, 440, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 0);
        fieldMock.getPlayerGroup().addActor(player1);
        fieldMock.getPlayerGroup().addActor(player2);

        float speed1Before = player1.getMovingSpeed();  //Erfasse Geschwindigkeiten vor der Geschwindigkeitsänderung
        float speed2Before = player2.getMovingSpeed();

        suddenDeath.increaseSpeed();

        float speed1After = player1.getMovingSpeed();   //Erfasse Geschwindigkeiten nach der Geschwindigkeitsänderung
        float speed2After = player2.getMovingSpeed();

        assertEquals(speed1Before * 1.5f, speed1After);
        assertEquals(speed2Before * 1.5f, speed2After);
    }
}


