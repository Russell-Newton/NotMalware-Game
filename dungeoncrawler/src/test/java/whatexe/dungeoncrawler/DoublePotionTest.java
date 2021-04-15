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
import whatexe.dungeoncrawler.entities.items.SpeedPotion;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class DoublePotionTest extends ApplicationTest {
    private static Stage primaryStage;
    private static String currentScene;

    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        primaryStage = stage;
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
    public void testDoublePotion() {
        clickOn("#nameInput").write("Bob");
        clickOn("#proceed");
        LevelController levelController = (LevelController) SceneManager.getInstance()
                                                                        .getSceneController(
                                                                                "Level");
        final double oldDmg =
                levelController.getPlayer().getEntityStatistics().getAttackDamageModifier();
        final double oldSpeed = levelController.getPlayer()
                                               .getEntityStatistics().getSpeedModifier();

        levelController.getPlayer().getStatusEffects().add(
                new AttackPotion(levelController.getPlayer()).getConsumeEffect());
        levelController.getPlayer().getStatusEffects().add(
                new SpeedPotion(levelController.getPlayer()).getConsumeEffect());

        assertEquals(oldDmg * 2,
                     levelController.getPlayer().getEntityStatistics().getAttackDamageModifier());
        assertEquals(oldSpeed * 2,
                     levelController.getPlayer().getEntityStatistics().getSpeedModifier());

    }
}
