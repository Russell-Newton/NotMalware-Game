package whatexe.dungeoncrawler;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import whatexe.dungeoncrawler.entities.EntityChooser;
import whatexe.dungeoncrawler.entities.enemies.bosses.Boss;
import whatexe.dungeoncrawler.entities.enemies.bosses.FirstBoss;
import whatexe.dungeoncrawler.entities.enemies.bosses.SecondBoss;
import whatexe.dungeoncrawler.entities.enemies.bosses.ThirdBoss;
import whatexe.dungeoncrawler.entities.items.modifiers.Modifier;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EntityChooserTest extends ApplicationTest {

    @Test
    public void testRandItemNoModifiers() {
        for (int i = 0; i < 200; i++) {
            assertFalse(
                    EntityChooser
                            .getInstance()
                            .getRandomItem((Room) null, false) instanceof Modifier);
        }
    }

    @Test
    public void testEntityChooserPopulateMap() {
        EntityChooser.getInstance().populateMapWithSubTypesOf(Boss.class, SecondBoss.class);
        Set<Class<Object>> populatedClasses = EntityChooser.getInstance().getClassTypesMap().get(
                String.format("%s-%s", Boss.class.getName(), SecondBoss.class.getName()));
        assertEquals(populatedClasses.size(), 3);
        assertFalse(populatedClasses.contains(SecondBoss.class));
        assertTrue(populatedClasses.contains(FirstBoss.class));
        assertTrue(populatedClasses.contains(ThirdBoss.class));
    }

}
