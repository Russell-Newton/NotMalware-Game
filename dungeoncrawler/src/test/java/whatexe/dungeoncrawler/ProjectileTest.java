package whatexe.dungeoncrawler;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import whatexe.dungeoncrawler.controllers.LevelController;

import static org.testfx.api.FxAssert.verifyThat;

@Disabled
@Deprecated(since = "Since player attacking overhaul")
public class ProjectileTest extends ApplicationTest {

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
     * Makes sure the circular projectiles appear when player presses the fire key while the player
     * is moving.
     */
    @Test
    public void testPlayerFiringMoving() {
        LevelController levelController =
                (LevelController) SceneManager.getInstance().getSceneController("Level");
        press(KeyCode.DOWN);
        for (int i = 0; i < 7; i++) {
            press(KeyCode.SPACE);
        }
        verifyThat(new Circle(2), Node::isVisible);
    }

    /**
     * Makes sure the player is able to fire from a stationary point.
     */
    @Test
    public void testPlayerFiringStationary() {
        LevelController levelController =
                (LevelController) SceneManager.getInstance().getSceneController("Level");
        for (int i = 0; i < 7; i++) {
            press(KeyCode.SPACE);
        }
        verifyThat(new Circle(2), Node::isVisible);
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }
}
