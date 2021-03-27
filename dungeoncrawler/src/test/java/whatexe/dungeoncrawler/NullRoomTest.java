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
import whatexe.dungeoncrawler.layout.rooms.NullRoom;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.Map;

import static org.testfx.api.FxAssert.verifyThat;

@Disabled
public class NullRoomTest extends ApplicationTest {

    private static Room currentRoom;

    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        SceneManager.getInstance().addScene("Room",
                                            getClass().getResource("controllers"
                                                                           + "/RoomDisplay.fxml"));
        currentRoom = new NullRoom();
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
     * Tests to make sure when there is just a null room passed in, a Bad Room! shown.
     */
    @Test
    public void nullRoom() {
        verifyThat("Bad room!", NodeMatchers.isNotNull());
    }
}
