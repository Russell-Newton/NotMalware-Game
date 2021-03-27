package whatexe.tileengine;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TiledMapTest {

    @Test
    @Disabled
    public void testLoad() throws IOException {
        TiledMap map = new TiledMap(getClass().getResource("test.tmx"), getClass()::getResource);
        ImageIO.write(map.getMapImage(), "png", new File("TestMapRender.png"));
    }

}
