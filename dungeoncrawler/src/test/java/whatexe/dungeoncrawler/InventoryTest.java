package whatexe.dungeoncrawler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import whatexe.dungeoncrawler.controllers.ConfigurationController;
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.entities.items.HealthPotion;
import whatexe.dungeoncrawler.entities.player.MalwareType;
import whatexe.dungeoncrawler.entities.player.Player;
import whatexe.dungeoncrawler.layout.rooms.Room;
import whatexe.dungeoncrawler.music.MusicManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static javafx.scene.input.KeyCode.*;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class InventoryTest extends ApplicationTest {
    private Room room;
    private Player player;

    @Override
    public void start(Stage stage) throws Exception {
        MusicManager.getInstance().setInTest(true);
        new MainApp().start(stage);
        player = new Player(MalwareType.VIRUS);
        SceneManager.getInstance().addScene("Level",
                                            ConfigurationController.class.getResource(
                                                    "LevelDisplay.fxml"));
        MainApp.switchScene("Level", Map.of("difficulty", Difficulty.NORMAL,
                                            "name", "Burdell",
                                            "player", player));
        stage.toFront();
    }

    @BeforeEach
    public void initializeInstanceVariables() {
        LevelController level =
                (LevelController) SceneManager.getInstance().getSceneController("Level");
        room = level.getCurrentRoom();
    }

    @AfterEach
    public void reset() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }

    @Test
    public void testUseInventoryHealth() {
        HealthPotion healthPotion = new HealthPotion(player);
        healthPotion.getDisplayNode().setId("TestHealthPotion");
        assertFalse(player.getInventory().getInventoryItems().containsValue(healthPotion));
        player.addItem(healthPotion);
        assertTrue(player.getInventory().getInventoryItems().containsValue(healthPotion));
        interact(() -> player.setHealth(50));
        assertEquals(50, player.getHealth());
        press(E);
        release(E);
        clickOn(healthPotion.getDisplayNode(), MouseButton.SECONDARY);
        press(E);
        release(E);
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        assertTrue(player.getHealth() > 50);
    }

    @Test
    public void testDropFromInventory() {
        HealthPotion healthPotion = new HealthPotion(player);
        assertFalse(player.getInventory().getInventoryItems().containsValue(healthPotion));
        player.addItem(healthPotion);
        assertTrue(player.getInventory().getInventoryItems().containsValue(healthPotion));
        interact(() -> player.setHealth(50));
        assertEquals(50, player.getHealth());
        press(E);
        release(E);
        clickOn(healthPotion.getDisplayNode());
        moveBy(-200, 0);
        clickOn(MouseButton.PRIMARY);
        press(E);
        release(E);
        assertFalse(player.getInventory().getInventoryItems().containsValue(healthPotion));
        assertTrue(room.getMiscEntities().contains(healthPotion));
        try {
            WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS, () ->
                    room.getMiscEntities().size() == 0);
        } catch (TimeoutException e) {
            fail();
        }
        assertTrue(player.getInventory().getInventoryItems().containsValue(healthPotion));
        assertFalse(room.getMiscEntities().contains(healthPotion));
    }
}
