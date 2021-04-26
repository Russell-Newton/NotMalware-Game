package whatexe.dungeoncrawler;

import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.music.MusicManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BurdellPlayerTest extends ApplicationTest {
    private LevelController levelController;

    public void start(Stage stage) throws Exception {
        MusicManager.getInstance().setInTest(true);
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        stage.show();
        stage.toFront();
    }

    @BeforeAll
    public static void primer() {
        MusicManager.getInstance().setInTest(true);
    }

    @BeforeEach
    public void startUp() throws IOException {
        interact(() -> {
            try {
                MainApp.switchScene("Main Menu");
                MainApp.switchScene("Configuration");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clickOn("#nameInput").write("Debug");
        clickOn("#proceed");
        levelController = (LevelController) SceneManager
                .getInstance().getSceneController("Level");
    }

    @Test
    public void testBurdellPlayerInventory() {
        assertEquals(1000, levelController.getPlayer().getMoney());
        assertFalse(levelController.getPlayer().getInventory().isInventoryEmpty());
    }
}
