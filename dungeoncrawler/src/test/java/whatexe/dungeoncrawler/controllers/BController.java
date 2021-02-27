package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import whatexe.dungeoncrawler.ManagedController;
import whatexe.dungeoncrawler.SceneManagerTest;

import java.io.IOException;
import java.util.Map;

public class BController extends ManagedController {

    @FXML
    private Text thisText;
    @FXML
    private Button switchButton;

    @FXML
    public void switchToSceneA() throws IOException {
        SceneManagerTest.switchScene("Scene A",
                                     Map.of("sender", "Scene B", "randText", "This is no "
                                             + "longer random text"));
    }

    @Override
    public void init() {

    }

    @Override
    public void init(Map<String, Object> loadParameters) {
        if (loadParameters.containsKey("sender")) {
            thisText.setText("Scene B, sent from " + loadParameters.get("sender"));
        }
    }

    public Text getThisText() {
        return thisText;
    }

    public Button getSwitchButton() {
        return switchButton;
    }
}
