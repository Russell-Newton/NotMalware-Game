package whatexe.dungeoncrawler;

import javafx.scene.Node;
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
import whatexe.dungeoncrawler.entities.player.MalwareType;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class RoomSwitchingTest extends ApplicationTest {
    private Node sceneParentNode;
    private Node subsceneParentNode;
    private LevelController levelController;

    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        MainApp.switchScene("Configuration");
        MainApp.switchScene("Level", Map.of(
                "difficulty", Difficulty.NORMAL,
                "name", "Burdell",
                "weapon", MalwareType.SPYWARE));

        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void initializeUsefulVariables() {
        sceneParentNode = MainApp.getPrimaryStage().getScene().getRoot();
        subsceneParentNode = sceneParentNode.lookup("#roomSubScene");
        levelController = (LevelController) SceneManager
                .getInstance().getSceneController("Level");
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }

    @Test
    public void testStartRoomIsOrigin() {
        String currentPosition = levelController.getLevel().getRoomPosition().toString();
        assertEquals("RoomPosition{x=0, y=0}", currentPosition);
    }

    @Test
    public void testChangeRoomsUpAndBack() {
        interact(() -> levelController.getLevel().moveUp());
        String currentPosition = levelController.getLevel().getRoomPosition().toString();
        assertEquals("RoomPosition{x=0, y=-1}", currentPosition);
        interact(() -> levelController.getLevel().moveDown());
        currentPosition = levelController.getLevel().getRoomPosition().toString();
        assertEquals("RoomPosition{x=0, y=0}", currentPosition);
    }

    @Test
    public void testChangeRoomsUPThenDown() {
        interact(() -> levelController.getLevel().moveDown());
        String currentPosition = levelController.getLevel().getRoomPosition().toString();
        assertEquals("RoomPosition{x=0, y=1}", currentPosition);
        interact(() -> levelController.getLevel().moveUp());
        currentPosition = levelController.getLevel().getRoomPosition().toString();
        assertEquals("RoomPosition{x=0, y=0}", currentPosition);
    }

    @Test
    public void testChangeRoomsLeftThenRight() {
        interact(() -> levelController.getLevel().moveLeft());
        String currentPosition = levelController.getLevel().getRoomPosition().toString();
        assertEquals("RoomPosition{x=-1, y=0}", currentPosition);
        interact(() -> levelController.getLevel().moveRight());
        currentPosition = levelController.getLevel().getRoomPosition().toString();
        assertEquals("RoomPosition{x=0, y=0}", currentPosition);
    }

}
