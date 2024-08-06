import com.mygdx.bomberman.*;
import de.tomgrill.gdxtesting.GdxTestRunnerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(GdxTestRunnerExtension.class)
public class BlockTest
{
    @Test
    public void BlockConstructorTest()
    {
        Field fieldMock = mock(Field.class);
        Assets assetsMock = mock(Assets.class);
        Block block1 = new Block(assetsMock, BlockType.DESTROYABLE, 1, 2, fieldMock);
        assertEquals(BlockType.DESTROYABLE, block1.getBlockType());

        Block block2 = new Block(assetsMock, BlockType.UNDESTROYABLE, 1, 2, fieldMock);
        assertEquals(BlockType.UNDESTROYABLE, block2.getBlockType());

        Block block = new Block(assetsMock, BlockType.DESTROYABLE, 1, 2, fieldMock);

        block.setPosition(10, 20);
        block.setSize(40, 40);

        assertEquals(10, block.getX());
        assertEquals(20, block.getY());
        assertEquals(40, block.getWidth());
        assertEquals(40, block.getHeight());
    }
}
