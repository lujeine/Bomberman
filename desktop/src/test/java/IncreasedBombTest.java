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
public class IncreasedBombTest
{
    private IncreasedBomb increasedBomb;
    private Player player;

    @BeforeEach
    void setUp()
    {
        Assets assetsMock = mock(Assets.class);
        Field field = new Field(assetsMock);
        TextureRegion textureRegionMock = mock(TextureRegion.class);
        player = new Player(assetsMock,1, 40, 40, new KeyProfile(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E), field, 1, 0);
        increasedBomb = new IncreasedBomb(200.0f, 40.0f,textureRegionMock);
    }

    @Test
    void testIncreasedBombConstructor()
    {
        assertEquals(200.0f, increasedBomb.getX());
        assertEquals(40.0f, increasedBomb.getY());
    }

    @Test
    void testActivate()
    {
        increasedBomb.activate(player);
        assertEquals(2, player.getNumOfBombs()); //Der Spieler muss nach dem ersten IncreasedBomb 2 Bomben besitzen
    }
}