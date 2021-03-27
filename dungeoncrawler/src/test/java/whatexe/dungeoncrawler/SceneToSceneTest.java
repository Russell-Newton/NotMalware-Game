package whatexe.dungeoncrawler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

@Disabled
public class SceneToSceneTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);
        stage.show();
        stage.toFront();
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }

    /**
     * Ensures that the scene switches from the Welcome Screen to the Configuration Screen
     */
    @Test
    public void testFromWelcomeToConfig() {
        clickOn("#playGame");
        verifyThat("Welcome, Please set your Configurations", NodeMatchers.isVisible());
        assertEquals("Configuration", MainApp.getCurrentScene());
    }

    /**
     * Ensure that the scene switches from the Configuration Screen to the First Level
     * Note: All previous scenes must load properly first to pass this test
     */
    @Test
    public void testFromConfigToLevelScene() {
        clickOn("#playGame");
        clickOn("#nameInput").write("Burdell");
        clickOn("#proceed");
        // verifyThat("#moneyLabel", NodeMatchers.isVisible());
        assertEquals("Level", MainApp.getCurrentScene());
    }


}
