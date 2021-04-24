package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import whatexe.dungeoncrawler.MainApp;
import whatexe.dungeoncrawler.ManagedController;

import java.io.IOException;

public class HowToPlayController extends ManagedController {
    @Override
    public void init() {

    }

    @Override
    public void postInit() {
        MainApp.addKeyPressHandler(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                try {
                    MainApp.switchScene("Main Menu");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    @FXML
    protected void toMenu() throws IOException {
        MainApp.switchScene("Main Menu");
    }
}
