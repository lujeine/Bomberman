import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tomgrill.gdxtesting.GdxTestRunnerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import com.mygdx.bomberman.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;
import com.mygdx.bomberman.KeyProfile;
import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GdxTestRunnerExtension.class)
public class BombRangeTest
{
    private BombRange bombRange;
    private Player player;

    @BeforeEach
    void setUp()
    {
        Field fieldMock = mock(Field.class);
        TextureRegion textureRegionMock = mock(TextureRegion.class);
        Assets assetsMock = mock(Assets.class);
        bombRange = new BombRange(200.0f, 40.0f,textureRegionMock);
        player = new Player(assetsMock, 1, 40,40, new KeyProfile(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E), fieldMock, 1, 0);
    }

    @Test
    void testBombRangeConstructor()
    {
        assertEquals(200.0f, bombRange.getX());
        assertEquals(40.0f, bombRange.getY());
    }

    @Test
    void testActivate()
    {
        bombRange.activate(player);
        assertEquals(2, player.getBombRange());
    }
}