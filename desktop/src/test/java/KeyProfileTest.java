import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.Input;
import com.mygdx.bomberman.KeyProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KeyProfileTest
{
    private KeyProfile keyProfile;

    @BeforeEach
    void setUp()
    {
        keyProfile = new KeyProfile(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E);
    }

    @Test
    void testKeyProfileConstructorAndGetters()
    {
        assertEquals(Input.Keys.W, keyProfile.getMoveUpKey());
        assertEquals(Input.Keys.S, keyProfile.getMoveDownKey());
        assertEquals(Input.Keys.A, keyProfile.getMoveLeftKey());
        assertEquals(Input.Keys.D, keyProfile.getMoveRightKey());
        assertEquals(Input.Keys.E, keyProfile.getPlaceBombKey());
    }

    @Test
    void testKeyProfileWithDifferentKeySets()
    {
        KeyProfile keyProfile1 = new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER);
        assertEquals(Input.Keys.UP, keyProfile1.getMoveUpKey());
        assertEquals(Input.Keys.DOWN, keyProfile1.getMoveDownKey());
        assertEquals(Input.Keys.LEFT, keyProfile1.getMoveLeftKey());
        assertEquals(Input.Keys.RIGHT, keyProfile1.getMoveRightKey());
        assertEquals(Input.Keys.ENTER, keyProfile1.getPlaceBombKey());

        KeyProfile keyProfile2 = new KeyProfile(Input.Keys.I, Input.Keys.K, Input.Keys.J, Input.Keys.L, Input.Keys.P);
        assertEquals(Input.Keys.I, keyProfile2.getMoveUpKey());
        assertEquals(Input.Keys.K, keyProfile2.getMoveDownKey());
        assertEquals(Input.Keys.J, keyProfile2.getMoveLeftKey());
        assertEquals(Input.Keys.L, keyProfile2.getMoveRightKey());
        assertEquals(Input.Keys.P, keyProfile2.getPlaceBombKey());

        KeyProfile keyProfile3 = new KeyProfile(Input.Keys.Y, Input.Keys.H, Input.Keys.NUM_7, Input.Keys.M, Input.Keys.N);
        assertEquals(Input.Keys.Y, keyProfile3.getMoveUpKey());
        assertEquals(Input.Keys.H, keyProfile3.getMoveDownKey());
        assertEquals(Input.Keys.NUM_7, keyProfile3.getMoveLeftKey());
        assertEquals(Input.Keys.M, keyProfile3.getMoveRightKey());
        assertEquals(Input.Keys.N, keyProfile3.getPlaceBombKey());
        
    }

    @Test
    void testSetters()
    {
        keyProfile.setMoveUpKey(Input.Keys.UP);
        assertEquals(Input.Keys.UP, keyProfile.getMoveUpKey());
    }
}
