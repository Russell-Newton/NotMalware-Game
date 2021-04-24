package whatexe.dungeoncrawler;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import whatexe.dungeoncrawler.controllers.LevelController;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class EndRoomTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        SceneManager.getInstance().addScene("Level",
                                            getClass().getResource("controllers/LevelDisplay"
                                                                           + ".fxml"));

        stage.toFront();
    }

    @Test
    public void testDeathScreenSwitch() {
        interact(() -> {
            try {
                MainApp.switchScene("Level");
                LevelController levelController =
                        (LevelController) SceneManager.getInstance().getSceneController("Level");
                levelController.getPlayer().setHealth(0);
                assertEquals("EndScreen", MainApp.getCurrentScene());
                Button restartButton = lookup("#restartButton").queryAs(Button.class);
                assertEquals("RESTART", restartButton.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testEndScreenSwitch() {
        SceneManager.getInstance().addScene("EndScreen",
                                            getClass().getResource(
                                                    "controllers/EndScreen.fxml"));
        interact(() -> {
            try {
                MainApp.switchScene("EndScreen");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clickOn("#restartButton");
        assertEquals("Configuration", MainApp.getCurrentScene());

    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }
}
