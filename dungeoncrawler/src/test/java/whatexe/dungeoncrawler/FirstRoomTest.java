package whatexe.dungeoncrawler;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class FirstRoomTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        SceneManager.getInstance().addScene("First Room",
                                            getClass().getResource("controllers/FirstRoom.fxml"));
        // added a dummy scene that can be consistently switched to reload the first room
        SceneManager.getInstance().addScene("Example",
                                            getClass().getResource(
                                                    "controllers/ExampleController.fxml"));

        stage.toFront();
    }

    /**
     * Tests that the default difficulty set in the first room without parameters works as intended,
     * by comparing the label and its contents to text that it is supposed to display.
     */
    @Test
    public void testDefaultDifficulty() {
        // Scenes can only be switched from the JavaFX application thead
        // interact queues the scene switch into the thread
        interact(() -> {
            try {
                MainApp.switchScene("First Room");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Label healthText = lookup("#healthLabel").queryAs(Label.class);
        Label moneyText = lookup("#moneyLabel").queryAs(Label.class);

        assertEquals(healthText.getText(), "Health: 50 / 50");
        assertEquals(moneyText.getText(), "Money: 50 / 50");
    }

    /**
     * Tests that the various difficulties set in the first room works as intended,
     * by comparing the label and its contents to text that it is supposed to display.
     */
    @Test
    public void testDifficultyOptions() {
        interact(() -> {
            try {
                MainApp.switchScene("First Room", Map.of("difficulty", Difficulty.NORMAL));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Label healthText = lookup("#healthLabel").queryAs(Label.class);
        Label moneyText = lookup("#moneyLabel").queryAs(Label.class);

        assertEquals(healthText.getText(), "Health: 50 / 50");
        assertEquals(moneyText.getText(), "Money: 50 / 50");

        interact(() -> {
            try {
                MainApp.switchScene("Example");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        interact(() -> {
            try {
                MainApp.switchScene("First Room", Map.of("difficulty", Difficulty.HARD));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        healthText = lookup("#healthLabel").queryAs(Label.class);
        moneyText = lookup("#moneyLabel").queryAs(Label.class);

        assertEquals(healthText.getText(), "Health: 100 / 100");
        assertEquals(moneyText.getText(), "Money: 100 / 100");

        interact(() -> {
            try {
                MainApp.switchScene("Example");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        interact(() -> {
            try {
                MainApp.switchScene("First Room", Map.of("difficulty", Difficulty.EASY));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        healthText = lookup("#healthLabel").queryAs(Label.class);
        moneyText = lookup("#moneyLabel").queryAs(Label.class);

        assertEquals(healthText.getText(), "Health: 20 / 20");
        assertEquals(moneyText.getText(), "Money: 20 / 20");
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }
}
