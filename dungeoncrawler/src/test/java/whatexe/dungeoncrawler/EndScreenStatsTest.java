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
import org.testfx.matcher.base.NodeMatchers;
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.music.MusicManager;

import static org.testfx.api.FxAssert.verifyThat;

public class EndScreenStatsTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        stage.toFront();
    }

    @BeforeAll
    public static void primer() {
        MusicManager.getInstance().setInTest(true);
    }


    @BeforeEach
    public void startUp() {
        clickOn(MouseButton.PRIMARY);
        press(KeyCode.ENTER);
        clickOn("Play");
        press(KeyCode.ENTER);
        clickOn("#nameInput").write("Bob");
        clickOn("#proceed");
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }

    @Test
    public void testEndScreenStats() {
        interact(() -> {
            try {
                LevelController levelController =
                        (LevelController) SceneManager.getInstance().getSceneController("Level");
                levelController.getPlayer().setHealth(0);
                verifyThat("Total Time:", NodeMatchers.isVisible());
                verifyThat("Enemies Killed:", NodeMatchers.isVisible());
                verifyThat("Damage Taken:", NodeMatchers.isVisible());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testCorrectDeathEndScreen() {
        interact(() -> {
            try {
                LevelController levelController =
                        (LevelController) SceneManager.getInstance().getSceneController("Level");
                levelController.getPlayer().setHealth(0);
                verifyThat("System: Malware found and destroyed.\n\tYou died :(",
                           NodeMatchers.isVisible());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
