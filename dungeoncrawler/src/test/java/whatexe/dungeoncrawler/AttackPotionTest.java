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
import whatexe.dungeoncrawler.entities.items.AttackPotion;
import whatexe.dungeoncrawler.music.MusicManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class AttackPotionTest extends ApplicationTest {

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
        interact(() -> {
            try {
                MainApp.switchScene("Configuration");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }

    @Test
    public void testDamagePotion() {
        clickOn("#nameInput").write("Bob");
        clickOn("#proceed");
        LevelController levelController = (LevelController) SceneManager.getInstance()
                                                                        .getSceneController(
                                                                                "Level");
        final double oldDmg =
                levelController.getPlayer().getEntityStatistics().getAttackDamageModifier();
        levelController.getPlayer().getStatusEffects().add(
                new AttackPotion(levelController.getPlayer()).getConsumeEffect());

        assertEquals(oldDmg * 2,
                     levelController.getPlayer().getEntityStatistics().getAttackDamageModifier());

    }
}