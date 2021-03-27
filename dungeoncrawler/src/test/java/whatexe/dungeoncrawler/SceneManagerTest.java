package whatexe.dungeoncrawler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Disabled
public class SceneManagerTest extends ApplicationTest {

    private static Stage primaryStage;
    private static String currentScene;

    /**
     * Switches to a new scene. Loads the new scene and unloads the current (if present) scene.
     *
     * @param sceneName the name of the new scene, as held in the {@link SceneManager}.
     * @throws IOException if the FXML file saved in the {@link SceneManager} cannot be loaded.
     * @see MainApp#switchScene(String, Map)
     * @see SceneManager
     * @see SceneManager#loadScene(String)
     * @see SceneManager#unloadScene(String)
     */
    public static void switchScene(String sceneName) throws IOException {
        switchScene(sceneName, Map.of());
    }

    /**
     * Switches to a new scene. Loads the new scene and unloads the current (if present) scene.
     *
     * @param sceneName      the name of the new scene, as held in the {@link SceneManager}.
     * @param loadParameters optional parameters passed into the {@link ManagedController}'s
     *                       {@link ManagedController#init} method.
     * @throws IOException if the FXML file saved in the {@link SceneManager} cannot be loaded.
     * @see SceneManager
     * @see SceneManager#loadScene(String, Map)
     * @see SceneManager#unloadScene(String)
     * @see ManagedController#init(Map)
     */
    public static void switchScene(String sceneName, Map<String, Object> loadParameters)
            throws IOException {
        // System.out.println("Loading and switching to " + sceneName);

        // Set the primaryStage scene to the new scene
        primaryStage.setScene(SceneManager.getInstance().loadScene(sceneName, loadParameters));
        if (currentScene != null) {
            // System.out.println("Unloading " + currentScene);

            // Unload the current scene from
            SceneManager.getInstance().unloadScene(currentScene);
        }

        currentScene = sceneName;
    }

    /**
     * Add the scenes to the SceneManager, only once necessary.
     */
    @BeforeAll
    public static void setUp() {
        SceneManager.getInstance().
                addScene("Scene A",
                         SceneManagerTest.class.getResource("controllers/AController.fxml"));
        SceneManager.getInstance().
                addScene("Scene B",
                         SceneManagerTest.class.getResource("controllers/BController.fxml"));
    }

    @AfterAll
    public static void clear() {
        SceneManager.getInstance().clear();
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        switchScene("Scene A");

        stage.show();
        stage.toFront();
    }

    /**
     * Release the keyboard and mouse function, unload the current scene to prevent conflicts
     *
     * @throws Exception if the stage can't be hidden
     */
    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().unloadScene(currentScene);
        currentScene = null;
    }

    /**
     * Test that switching scenes from a controller works. Requires loading and unloading in the
     * SceneManager.
     */
    @Test
    public void testSwitchSceneFromController() {
        clickOn("#switchButton");
        assertEquals(currentScene, "Scene B");
        // Check unloading
        assertNull(SceneManager.getInstance().getLoaderMap().get("Scene A").getController());


        clickOn("#switchButton");
        assertEquals(currentScene, "Scene A");
        // Check unloading
        assertNull(SceneManager.getInstance().getLoaderMap().get("Scene B").getController());
    }

    /**
     * Test that loadParams passed into {@link ManagedController#init(Map)} can be used as expected.
     */
    @Test
    public void testPassLoadParams() {
        // Test no-param initialization
        Text thisText = lookup("#thisText").queryText();
        Text randText = lookup("#randText").queryText();
        assertEquals(thisText.getText(), "Scene A");
        assertEquals(randText.getText(), "Text");

        // Test switch to new scene, with parameters
        clickOn("#switchButton");
        thisText = lookup("#thisText").queryText();
        assertEquals(thisText.getText(), "Scene B, sent from Scene A");

        // Test switching back, new initialization
        clickOn("#switchButton");
        thisText = lookup("#thisText").queryText();
        randText = lookup("#randText").queryText();
        assertEquals(thisText.getText(), "Scene A, sent from Scene B");
        assertEquals(randText.getText(), "This is no longer random text");

    }

}
