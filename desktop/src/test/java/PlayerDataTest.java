import com.mygdx.bomberman.*;
import de.tomgrill.gdxtesting.GdxTestRunnerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GdxTestRunnerExtension.class)
public class PlayerDataTest
{
    @Test
    public void PlayerDataConstructorTest() //Überprüft den Konstruktor
    {
        PlayerData player = new PlayerData(1);
        assertEquals(1, player.getId());
        assertEquals(0, player.getPoints());
        assertTrue(player.inGame());
    }

    @Test
    public void SetPointsTest() //Überprüft das Setzen von Punkten
    {
        PlayerData player = new PlayerData(1);
        player.setPoints(1);
        assertEquals(1, player.getPoints());
    }

    @Test
    public void SetInGameTest() //Überprüft inGame Attribut
    {
        PlayerData player = new PlayerData(1);
        player.setInGame(false);
        assertFalse(player.inGame());
    }

    @Test
    public void ResetPointsTest()   //Überprüft die Playerzurücksetzung
    {
        PlayerData player = new PlayerData(2);
        player.setPoints(50);
        player.setInGame(false);
        player.resetPlayer(2);

        assertEquals(2, player.getId());
        assertEquals(0, player.getPoints());
        assertTrue(player.inGame());
    }
}
