import static org.junit.jupiter.api.Assertions.*;
import com.mygdx.bomberman.GlobalSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GlobalSettingsTest
{
    @BeforeEach
    void setUp()
    {
        GlobalSettings.noOfPlayers = 2;
        GlobalSettings.updateSelectedSkins();
    }

    @Test
    void testInitialValues()
    {
        assertEquals(2, GlobalSettings.noOfPlayers);
        assertEquals(1, GlobalSettings.pointsToWin);
        assertNotNull(GlobalSettings.keyProfiles);
        assertEquals(2, GlobalSettings.skins.length);
    }

    @Test
    void testUpdateSelectedSkinsArray()
    {
        GlobalSettings.updateSelectedSkins();
        assertEquals(2, GlobalSettings.skins.length);

        for (int i = 0; i < GlobalSettings.noOfPlayers; i++)
            assertEquals(i, GlobalSettings.skins[i]);
    }

    @Test
    void testIsSkinAlreadySelected()
    {
        assertFalse(GlobalSettings.isSkinAlreadySelected(2));
        assertTrue(GlobalSettings.isSkinAlreadySelected(0));
    }
}
