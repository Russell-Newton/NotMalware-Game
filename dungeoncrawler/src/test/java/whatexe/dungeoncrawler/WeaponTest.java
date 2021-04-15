package whatexe.dungeoncrawler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.entities.behavior.presets.TrojanBehaviorSet;
import whatexe.dungeoncrawler.entities.behavior.presets.VirusBehaviorSet;
import whatexe.dungeoncrawler.music.MusicManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class WeaponTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        MusicManager.getInstance().setInTest(true);
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        MainApp.switchScene("Configuration");

        stage.toFront();
    }

    @Test
    public void testVirusSwitch() {
        clickOn("#nameInput").write("Bob");
        clickOn("#malwareChoice");
        type(KeyCode.ENTER);
        clickOn("#proceed");

        LevelController levelController =
                (LevelController) SceneManager.getInstance().getSceneController("Level");
        boolean match = levelController.getPlayer().getBehaviorSet() instanceof VirusBehaviorSet;
        assertTrue(match);
    }

    @Test
    public void testTrojanSwitch() {
        clickOn("#nameInput").write("Bob");
        clickOn("#malwareChoice");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#proceed");

        LevelController levelController =
                (LevelController) SceneManager.getInstance().getSceneController("Level");
        boolean match = levelController.getPlayer().getBehaviorSet() instanceof TrojanBehaviorSet;
        assertTrue(match);
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }
}
