import com.mygdx.bomberman.*;
import de.tomgrill.gdxtesting.GdxTestRunnerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GdxTestRunnerExtension.class)
public class BombTest
{
    private Bomb bomb;
    private Field fieldMock;
    private Player playerMock;
    private final float initialX = 100.0f;
    private final float initialY = 100.0f;

    @BeforeEach
    void setUp()
    {
        fieldMock = mock(Field.class);
        playerMock = mock(Player.class);
        when(playerMock.getBombRange()).thenReturn(2);
        Assets assetsMock = mock(Assets.class);
        bomb = new Bomb(initialX, initialY, fieldMock, playerMock,assetsMock);
    }

    @Test
    void testBombConstructorWithPlayer()
    {
        assertEquals(initialX, bomb.getX());
        assertEquals(initialY, bomb.getY());
        assertEquals(2, bomb.getBombRange());
        assertEquals(playerMock, bomb.getOwner());
    }
}
