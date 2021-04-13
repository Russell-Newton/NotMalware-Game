package whatexe.dungeoncrawler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import whatexe.dungeoncrawler.layout.Direction;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.dungeoncrawler.layout.generation.RoomPosition;
import whatexe.dungeoncrawler.layout.generation.SimpleLevelGenerator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class SimpleRoomGeneratorTest extends ApplicationTest {

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }

    @Test
    public void testValidity() {
        SimpleLevelGenerator generator = new SimpleLevelGenerator(Difficulty.NORMAL);
        for (int i = 0; i < 100; i++) {
            Level level = generator.generate();
            String rep = level.mazeToString();

            // Check validity of boss room position
            assertNotNull(generator.getBossRoomPosition());
            assertTrue(
                    generator.getBossRoomPosition().distance(generator.getStartingPosition()) >= 6);
            assertTrue(rep.contains("B"));

            // Check validity of room count
            assertTrue(generator.getRooms().size() >= 8);

            // Check validity of starting room exits
            assertTrue(generator.getRooms().contains(
                    generator.getStartingPosition().getNeighbor(Direction.UP)));
            assertTrue(generator.getRooms().contains(
                    generator.getStartingPosition().getNeighbor(Direction.DOWN)));
            assertTrue(generator.getRooms().contains(
                    generator.getStartingPosition().getNeighbor(Direction.LEFT)));
            assertTrue(generator.getRooms().contains(
                    generator.getStartingPosition().getNeighbor(Direction.RIGHT)));

            // Print 5 reps, for sanity
            if (i % 20 == 0) {
                System.out.println("SimpleLevelGenerator: Generation #" + i);
                System.out.println(rep);
            }
        }
    }

    @Test
    public void testConstantSeed() {
        SimpleLevelGenerator controlGenerator = new SimpleLevelGenerator(Difficulty.NORMAL, 1);
        Level controlLevel = controlGenerator.generate();
        Set<RoomPosition> controlRooms = controlGenerator.getRooms();
        String controlRep = controlLevel.mazeToString();

        // Validate controlLevel's validity
        // Check validity of boss room position
        assertNotNull(controlGenerator.getBossRoomPosition());
        assertTrue(
                controlGenerator.getBossRoomPosition()
                                .distance(controlGenerator.getStartingPosition()) >= 6);
        assertTrue(controlRep.contains("B"));

        // Check validity of room count
        assertTrue(controlGenerator.getRooms().size() >= 8);

        // Check validity of starting room exits
        assertTrue(controlGenerator.getRooms().contains(
                controlGenerator.getStartingPosition().getNeighbor(Direction.UP)));
        assertTrue(controlGenerator.getRooms().contains(
                controlGenerator.getStartingPosition().getNeighbor(Direction.DOWN)));
        assertTrue(controlGenerator.getRooms().contains(
                controlGenerator.getStartingPosition().getNeighbor(Direction.LEFT)));
        assertTrue(controlGenerator.getRooms().contains(
                controlGenerator.getStartingPosition().getNeighbor(Direction.RIGHT)));
        System.out.println("SimpleLevelGenerator, seed = 1: Generation #" + 0);
        System.out.println(controlRep);


        for (int i = 1; i < 100; i++) {
            SimpleLevelGenerator experimentGenerator = new SimpleLevelGenerator(
                    Difficulty.NORMAL, 1);
            Level level = experimentGenerator.generate();
            String experimentalRep = level.mazeToString();

            assertEquals(experimentGenerator.getRooms(), controlRooms);
            assertEquals(experimentalRep, controlRep);

            // Print 5 reps, for sanity
            if (i % 20 == 0) {
                System.out.println("SimpleLevelGenerator, seed = 1: Generation #" + i);
                System.out.println(experimentalRep);
            }

        }
    }
}
