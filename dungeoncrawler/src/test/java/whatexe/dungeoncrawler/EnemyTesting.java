package whatexe.dungeoncrawler;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Disabled
public class EnemyTesting extends ApplicationTest {

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        clickOn("#nameInput").write("Bob");
        clickOn("#proceed");
    }

    /**
     * Makes sure there are no enemies in the Starting room
     */
    @Test
    public void testStartRoomNoEnemy() {
        LevelController levelController =
                (LevelController) SceneManager.getInstance().getSceneController("Level");
        assertEquals(0, levelController.getCurrentRoom().getEnemies().size());
    }

    /**
     * Makes sure there is at least one enemy in a room (not starting room)
     */
    @Test
    public void testEnemySpawn() {
        press(KeyCode.K);
        for (int i = 0; i < 15; i++) {
            press(KeyCode.RIGHT);
        }
        LevelController levelController =
                (LevelController) SceneManager.getInstance().getSceneController("Level");
        assertNotEquals(0, levelController.getCurrentRoom().getEnemies().size());
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }
}
