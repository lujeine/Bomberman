import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.bomberman.*;
import de.tomgrill.gdxtesting.GdxTestRunnerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(GdxTestRunnerExtension.class)
public class FieldTest
{
    @Test
    public void FieldRemoveObjectTest()
    {
        Field fieldMock = mock(Field.class);
        Assets assetsMock = mock(Assets.class);
        Player player1 = new Player(assetsMock,1, 40, 40, new KeyProfile(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.E), fieldMock, 1, 0);
        Player player2 = new Player(assetsMock,2, 40, 440, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 0);
        Player player3 = new Player(assetsMock,3, 40, 440, new KeyProfile(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER), fieldMock, 2, 0);

        Group playerGroup = new Group();
        playerGroup.addActor(player1);
        playerGroup.addActor(player2);
        playerGroup.addActor(player3); //3 Spieler damit isLastPlayer nicht getriggert wird beim Entfernen
        Field field = new Field(assetsMock);
        int numberOfPlayers = 3;    //Es wurden drei Spieler hinzugefügt
        field.setPlayerGroup(playerGroup);
        assertEquals(numberOfPlayers, field.getPlayerGroup().getChildren().size);
        field.removeObject(player2);
        numberOfPlayers = 2;    //Es wurde ein Spieler entfernt
        assertEquals(numberOfPlayers, field.getPlayerGroup().getChildren().size);
    }

    @Test
    public void FieldUndestroyableTest()
    {
        Assets assetsMock = mock(Assets.class);
        Field field = new Field(assetsMock);
        field.placeInnerUndestroyableBlocksMap1();  //setze unzerstörbare Blöcke
        field.placeOuterWalls();    //setze nur unzerstörbare Blöcke

        for (Actor actor : field.getBlockGroup().getChildren())
        {
            if (actor instanceof Block)
            {
                Block block = (Block) actor;
                assertEquals(BlockType.UNDESTROYABLE, block.getBlockType());    //prüfe ob nur unzerstörbare Blöcke vorhanden sind
            }
        }
    }
}

