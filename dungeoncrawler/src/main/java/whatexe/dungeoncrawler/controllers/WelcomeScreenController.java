package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import whatexe.dungeoncrawler.MainApp;
import whatexe.dungeoncrawler.ManagedController;
import whatexe.dungeoncrawler.SceneManager;
import whatexe.dungeoncrawler.music.MusicManager;

import java.io.IOException;

public class WelcomeScreenController extends ManagedController {
    @FXML
    protected Pane parentRoot;

    @FXML
    protected void playGame() throws IOException {
        MainApp.switchScene("Main Menu");
    }

    @Override
    public void init() {
        if (!SceneManager.getInstance().getLoaderMap().containsKey("Main Menu")) {
            SceneManager.getInstance().addScene("Main Menu",
                                                getClass().getResource("MainMenu.fxml"));
        }
    }

    @Override
    public void postInit() {
        MusicManager.getInstance().stopAllTracks();
        MusicManager.getInstance().playTrack("Title");
        MainApp.addKeyPressHandler(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                MainApp.getPrimaryStage().close();
            } else if (!e.getCode().isFunctionKey()) {
                try {
                    playGame();
                } catch (IOException io) {
                    System.out.println("Issue starting the game.");
                }
            }
        });
    }
}
