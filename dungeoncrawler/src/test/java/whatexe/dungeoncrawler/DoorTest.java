package whatexe.dungeoncrawler;

import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.entities.player.MalwareType;
import whatexe.dungeoncrawler.entities.doors.Door;
import whatexe.dungeoncrawler.layout.Direction;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class DoorTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        stage.toFront();
    }

    @BeforeEach
    public void startUp() {
        interact(() -> {
            try {
                MainApp.switchScene("Configuration");
                MainApp.switchScene("Level", Map.of(
                        "difficulty", Difficulty.NORMAL,
                        "name", "Burdell",
                        "weapon", MalwareType.VIRUS,
                        "seed", 0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        SceneManager.getInstance().clear();
    }

    /**
     * Checks if all all the adjacent rooms to the start room have locked doors
     * that only unlock when all the enemies are killed
     */
    @Test
    public void testDoorUnlocking() {
        LevelController levelController =
                (LevelController) SceneManager.getInstance().getSceneController("Level");
        interact(() -> {
            for (Direction direction : Direction.values()) {
                levelController.getLevel().move(direction);
                ObservableList<Door> roomDoors = levelController.getCurrentRoom().getDoors();
                checkDoorsAreLocked(roomDoors, direction.getOpposite());
                levelController.getCurrentRoom().getEnemies().clear();
                checkDoorsAreUnlocked(roomDoors);
                levelController.getLevel().move(direction.getOpposite());
            }
        });
    }

    /**
     * Checks the corner case where players are allowed to retreat to past cleared rooms
     */
    @Test
    public void testVisitedRoomDoorsUnlocked() {
        LevelController levelController =
                (LevelController) SceneManager.getInstance().getSceneController("Level");
        interact(() -> {
            /*
            Current player at C
            [S, C,
             S, S]
             */
            levelController.getLevel().move(Direction.LEFT);
            levelController.getCurrentRoom().getEnemies().clear();
            levelController.getLevel().move(Direction.DOWN);
            /*
            [S, S,
             C, S]
             Room at C should only have the top door unlocked since that's where it came from
             */
            checkDoorsAreLocked(levelController.getCurrentRoom().getDoors(), Direction.UP);
            levelController.getLevel().move(Direction.UP);
            levelController.getLevel().move(Direction.RIGHT);
            levelController.getLevel().move(Direction.DOWN);
            /*
            [S, S,
             S, C]
             Player got here by going back up, right, and down
             Only the top door should be unlocked because even though the room to the left is
             visited,
                it wasn't cleared of enemies
             */
            checkDoorsAreLocked(levelController.getCurrentRoom().getDoors(), Direction.UP);
            levelController.getCurrentRoom().getEnemies().clear();
            levelController.getLevel().move(Direction.UP);
            levelController.getLevel().move(Direction.LEFT);
            levelController.getLevel().move(Direction.DOWN);
            /*
            [S, S,
             C, S]
             All the doors in this room should be unlocked since all adjacent rooms are now
             cleared of enemies
             */
            checkDoorsAreUnlocked(levelController.getCurrentRoom().getDoors());
        });
    }


    /**
     * Assert is true if all of the doors in the room are locked
     * Helper Method
     *
     * @param doors             Doors for the room
     * @param excludeDirections The doors that are expected to be unlocked
     */
    private void checkDoorsAreLocked(ObservableList<Door> doors, Direction... excludeDirections) {
        for (Door door : doors) {
            if (Arrays.asList(excludeDirections).contains(door.getDirection())) {
                continue;
            }
            assertTrue(door.isLocked());
        }
    }

    /**
     * Assert is true if all of the doors in the room are unlocked
     * Helper Method
     *
     * @param doors             Doors for the room
     * @param excludeDirections The doors that are expected to be locked
     */
    private void checkDoorsAreUnlocked(ObservableList<Door> doors, Direction... excludeDirections) {
        for (Door door : doors) {
            if (Arrays.asList(excludeDirections).contains(door.getDirection())) {
                continue;
            }
            assertFalse(door.isLocked());
        }
    }
}
