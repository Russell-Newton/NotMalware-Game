package whatexe.dungeoncrawler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.entities.player.DebugPlayer;
import whatexe.dungeoncrawler.entities.player.MalwareType;
import whatexe.dungeoncrawler.layout.generation.RoomPosition;
import whatexe.dungeoncrawler.music.MusicManager;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChallengeRoomTest extends ApplicationTest {
    private LevelController levelController;

    public void start(Stage stage) throws Exception {
        MusicManager.getInstance().setInTest(true);
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        stage.show();
        stage.toFront();
    }

    @BeforeAll
    public static void primer() {
        MusicManager.getInstance().setInTest(true);
    }

    @BeforeEach
    public void startUp() throws IOException {
        interact(() -> {
            try {
                MainApp.switchScene("Main Menu");
                MainApp.switchScene("Configuration");
                MainApp.switchScene("Level", Map.of(
                        "difficulty", Difficulty.NORMAL,
                        "name", "Debug",
                        "player", new DebugPlayer(MalwareType.VIRUS),
                        "seed", 2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        levelController = (LevelController) SceneManager
                .getInstance().getSceneController("Level");
        press(KeyCode.ENTER);
        interact(() -> levelController.getLevel().
                    currentRoomPositionProperty().set(new RoomPosition(2, 0)));
        interact(() -> levelController.getLevel().moveUp());
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }

    @Test
    public void testChallengeRoomEnemySpawning() {
        assertTrue(levelController.getCurrentRoom().getEnemies().isEmpty());
        interact(() -> levelController.getLevel().currentRoomPositionProperty().
                set(new RoomPosition(2, 0)));
        assertFalse(levelController.getCurrentRoom().getEnemies().isEmpty());
    }
}
