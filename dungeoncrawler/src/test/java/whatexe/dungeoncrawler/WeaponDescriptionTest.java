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

import static org.testfx.api.FxAssert.verifyThat;

@Disabled
public class WeaponDescriptionTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        MainApp mainApp = new MainApp();
        mainApp.start(stage);

        MainApp.switchScene("Configuration");

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
     * Tests if the Melee description will show up if Melee is selected, but to test, the keys
     * are pressed down into the Arrow option then back up to Melee to select Melee, since Melee
     * is already shown as default
     */
    @Test
    public void testMeleeDescription() {
        clickOn("#weaponChoice");
        type(KeyCode.DOWN);
        type(KeyCode.UP);
        verifyThat("Punch your enemies aggressively!! Be careful tho, you'll have to be up "
                           + "close", NodeMatchers.isNotNull());
    }

    /**
     * Tests if Arrow description is selected, the Arrow weapon description will show, then also
     * tests if Hit Scan is selected after Arrow, tests if Hit Scan description will show
     */
    @Test
    public void testArrowHSDescription() {
        clickOn("#weaponChoice");
        type(KeyCode.DOWN);
        verifyThat("The classic bow and arrow. Aim and shoot, make sure you don't miss!!",
                   NodeMatchers.isNotNull());
        clickOn("#weaponChoice");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        verifyThat("Very fast gun. Guaranteed to hit, but shame it does less damage "
                           + "(it shoots peas...why? idk)", NodeMatchers.isNotNull());
    }

    /**
     * Tests if Worm description is selected, the Worm weapon description will show, then also
     * tests if Trojan is selected after Worm, tests if Trojan description will show
     */
    @Test
    public void testWormTrojanDescription() {
        clickOn("#weaponChoice");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        verifyThat("Spread a logic plague to your enemies.",
                NodeMatchers.isNotNull());
        clickOn("#weaponChoice");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        verifyThat("Walk among your foes unseen.", NodeMatchers.isNotNull());
    }

}
