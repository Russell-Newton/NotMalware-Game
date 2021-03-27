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

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Disabled
public class PlayerMovementTest extends ApplicationTest {
    private static Stage primaryStage;
    private static String currentScene;


    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        // MainApp.switchScene("Configuration");

        primaryStage = stage;
        stage.show();
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
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }

    /**
     * Tests to ensure that the player can move down.
     */
    @Test
    public void testDownMovement() {
        clickOn("#nameInput").write("Bob");
        clickOn("#proceed");

        LevelController levelController = (LevelController) SceneManager.getInstance()
                                                                        .getSceneController(
                                                                                "Level");
        levelController.getLevel().getCurrentRoom().getDoors().clear();

        double startY = levelController.getPlayer().getDisplayNode().getTranslateY();
        press(KeyCode.DOWN);
        assertNotEquals(startY, levelController.getPlayer().getDisplayNode().getTranslateY());
    }

    /**
     * Tests to ensure that the player can move right.
     */
    @Test
    public void testRightMovement() {
        clickOn("#nameInput").write("Bob");
        clickOn("#proceed");

        LevelController levelController =
                (LevelController) SceneManager.getInstance().getSceneController("Level");
        levelController.getLevel().getCurrentRoom().getDoors().clear();

        double startX = levelController.getPlayer().getDisplayNode().getTranslateX();
        press(KeyCode.RIGHT);
        assertNotEquals(startX, levelController.getPlayer().getDisplayNode().getTranslateX());
    }
}
