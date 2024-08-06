import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tomgrill.gdxtesting.GdxTestRunnerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import com.mygdx.bomberman.*;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import com.mygdx.bomberman.KeyProfile;
import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GdxTestRunnerExtension.class)
public class KickingBombTest
{
    private KickingBomb kickingBomb;
    private Player player;

    @BeforeEach
    void setUp()
    {
        Field fieldMock = mock(Field.class);
        Assets assetsMock = mock(Assets.class);
        TextureRegion textureRegionMock = mock(TextureRegion.class);
        player = new Player(assetsMock,2, 40, 440, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 0);
        kickingBomb = new KickingBomb(200.0f, 40.0f,textureRegionMock);
    }

    @Test
    void testKickingBombConstructor()
    {
        assertEquals(200.0f, kickingBomb.getX());
        assertEquals(40.0f, kickingBomb.getY());
    }

    @Test
    void testActivate()
    {
        kickingBomb.activate(player);
        assertTrue(player.getIsAbleToKick());
    }
}