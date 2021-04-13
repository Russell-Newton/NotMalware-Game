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
import whatexe.dungeoncrawler.layout.Direction;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.dungeoncrawler.layout.generation.SimpleLevelGenerator;
import whatexe.dungeoncrawler.layout.rooms.Room;
import whatexe.dungeoncrawler.layout.rooms.SimpleRoom;
import whatexe.tileengine.TiledMap;

import java.util.Map;

import static org.testfx.api.FxAssert.verifyThat;

@Disabled
public class LoadRoomTest extends ApplicationTest {
    private static Room currentRoom;
    private static Level level;

    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        SceneManager.getInstance().addScene("Room",
                                            getClass().getResource("controllers"
                                                                           + "/RoomDisplay.fxml"));
        level = new Level(Difficulty.NORMAL);
        currentRoom = new SimpleRoom(new TiledMap(SimpleLevelGenerator.class.getResource(
                "Calibration.tmx"), SimpleLevelGenerator.class::getResource),
                                     level,
                                     Direction.UP);
        MainApp.switchScene("Room", Map.of(
                "room", currentRoom));
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
     * Tests to make sure when a room was given the UP direction, it would generate a door in the
     * up direction.
     */
    @Test
    public void roomCreation() {
        verifyThat("UP", NodeMatchers.isNotNull());
    }
}
