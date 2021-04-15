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
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.entities.items.HealthPotion;
import whatexe.dungeoncrawler.music.MusicManager;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class HealthPotionTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        MusicManager.getInstance().setInTest(true);
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void startUp() {
        try {
            MainApp.switchScene("Configuration");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }

    @Test
    public void testHealthPotion() throws InterruptedException {
        clickOn("#nameInput").write("Bob");
        clickOn("#proceed");
        LevelController levelController = (LevelController) SceneManager.getInstance()
                                                                        .getSceneController(
                                                                                "Level");
        interact(() -> {
            levelController.getPlayer().setHealth(70);
        });
        levelController.getPlayer().getStatusEffects().add(
                new HealthPotion(levelController.getPlayer()).getConsumeEffect());
        TimeUnit.SECONDS.sleep(5);
        assertEquals(100, levelController.getPlayer().getHealth());

    }
}