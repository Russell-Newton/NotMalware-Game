package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import whatexe.dungeoncrawler.MainApp;
import whatexe.dungeoncrawler.ManagedController;
import whatexe.dungeoncrawler.SceneManager;

import java.io.IOException;

public class MainMenuController extends ManagedController {
    @Override
    public void init() {
        if (!SceneManager.getInstance().getLoaderMap().containsKey("Configuration")) {
            SceneManager.getInstance().addScene("Configuration",
                                                getClass().getResource("Configuration.fxml"));
        }
        if (!SceneManager.getInstance().getLoaderMap().containsKey("Credits")) {
            SceneManager.getInstance().addScene("Credits",
                                                getClass().getResource("Credits.fxml"));
        }
        if (!SceneManager.getInstance().getLoaderMap().containsKey("How To Play")) {
            SceneManager.getInstance().addScene("How To Play",
                                                getClass().getResource("HowTo.fxml"));
        }
    }

    @Override
    public void postInit() {
        MainApp.addKeyPressHandler(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                try {
                    MainApp.switchScene("Welcome Screen");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    @FXML
    protected void toConfig() throws IOException {
        MainApp.switchScene("Configuration");
    }

    @FXML
    protected void toCredits() throws IOException {
        MainApp.switchScene("Credits");
    }

    @FXML
    protected void toHowTo() throws IOException {
        MainApp.switchScene("How To Play");
    }

    @FXML
    protected void exit() {
        MainApp.getPrimaryStage().close();
    }
}
