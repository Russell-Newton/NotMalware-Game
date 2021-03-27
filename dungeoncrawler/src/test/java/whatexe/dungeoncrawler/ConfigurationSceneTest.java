package whatexe.dungeoncrawler;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;

@Disabled
public class ConfigurationSceneTest extends ApplicationTest {
    private static Stage primaryStage;
    private static String currentScene;


    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        MainApp.switchScene("Configuration");

        primaryStage = stage;
        stage.show();
        stage.toFront();
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().removeScene("Welcome Screen");
        SceneManager.getInstance().removeScene("Configuration");
        SceneManager.getInstance().removeScene("Level");
    }

    /**
     * Tests to ensure that the alert pops up when nothing is entered as the player's name
     */
    @Test
    public void testNullName() {
        clickOn("#proceed");
        verifyThat(".error", NodeMatchers.isVisible());
    }

    /**
     * Tests to ensure that the alert pops ups when only spaces are entered as the player's name
     */
    @Test
    public void testSpaceName() {
        clickOn("#nameInput").write("    ");
        clickOn("#proceed");
        verifyThat(".error", Node::isVisible);
    }
}
