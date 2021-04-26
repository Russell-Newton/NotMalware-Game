package whatexe.dungeoncrawler;


import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.entities.player.Player;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.dungeoncrawler.layout.generation.RoomPosition;
import whatexe.dungeoncrawler.layout.rooms.ShopRoom;
import whatexe.dungeoncrawler.music.MusicManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ItemShopTest extends ApplicationTest {
    @BeforeAll
    public static void primer() {
        MusicManager.getInstance().setInTest(true);
    }

    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        stage.show();
        stage.toFront();

    }

    @BeforeEach
    public void startUp() {
        clickOn(MouseButton.PRIMARY);
        press(KeyCode.ENTER);
        clickOn("Play");
        clickOn("#nameInput").write("Debug");
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
    public void testItemShopPickUpSuccess() {
        LevelController levelController = (LevelController) SceneManager.getInstance()
                                                                        .getSceneController(
                                                                                "Level");
        Level owner = levelController.getPlayer().getOwningRoom().getOwningLevel();
        Player player = levelController.getPlayer();
        interact(() -> {
            Map map = owner.getMaze();
            RoomPosition shop = null;
            for (Object key : map.keySet()) {
                if (map.get(key) instanceof ShopRoom) {
                    shop = (RoomPosition) key;
                    break;

                }
            }
            owner.currentRoomPositionProperty().set(shop);
            Platform.runLater(() -> {
                player.setEntityPosition(640, 288);
            });
            Platform.runLater(() -> assertNotEquals(40, player.getMoney()));
        });
        try {
            WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS, () -> player.getMoney() < 1000);
        } catch (TimeoutException e) {
            fail();
        }
    }

    @Test
    public void testItemShopPickUpFailure() {
        LevelController levelController = (LevelController) SceneManager.getInstance()
                                                                        .getSceneController(
                                                                                "Level");
        Level owner = levelController.getPlayer().getOwningRoom().getOwningLevel();
        Player player = levelController.getPlayer();
        interact(() -> {
            try {
                player.setMoney(10);
                Map map = owner.getMaze();
                RoomPosition shop = null;
                for (Object key : map.keySet()) {
                    if (map.get(key) instanceof ShopRoom) {
                        shop = (RoomPosition) key;
                        break;

                    }
                }
                owner.currentRoomPositionProperty().set(shop);
                Platform.runLater(() -> player.setEntityPosition(640, 288));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        try {
            WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS, () -> player.getMoney() < 10);
            fail();
        } catch (TimeoutException e) {
        }
    }
}
