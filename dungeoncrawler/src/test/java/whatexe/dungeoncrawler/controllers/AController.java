package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import whatexe.dungeoncrawler.ManagedController;
import whatexe.dungeoncrawler.SceneManagerTest;

import java.io.IOException;
import java.util.Map;

public class AController extends ManagedController {

    @FXML
    private Text thisText;
    @FXML
    private Button switchButton;
    @FXML
    private Text randText;

    @FXML
    public void switchToSceneB() throws IOException {
        SceneManagerTest.switchScene("Scene B", Map.of("sender", "Scene A"));
    }

    @Override
    public void init() {
        randText.setText(Integer.toString((int) (Math.random() * 50)));
    }

    @Override
    public void init(Map<String, Object> loadParameters) {
        if (loadParameters.containsKey("sender")) {
            thisText.setText("Scene A, sent from " + loadParameters.get("sender"));
        }
        if (loadParameters.containsKey("randText")) {
            randText.setText((String) loadParameters.get("randText"));
        }
    }

    public Text getThisText() {
        return thisText;
    }

    public Button getSwitchButton() {
        return switchButton;
    }

    public Text getRandText() {
        return randText;
    }
}
