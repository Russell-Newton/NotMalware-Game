package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import whatexe.dungeoncrawler.MainApp;
import whatexe.dungeoncrawler.ManagedController;
import whatexe.dungeoncrawler.SceneManager;
import whatexe.dungeoncrawler.music.MusicManager;

import java.io.IOException;
import java.util.Map;

public class EndScreenController extends ManagedController {
    @FXML
    protected Label damage;
    @FXML
    protected Label enemies;
    @FXML
    protected Label time;
    @FXML
    protected Label text;

    @FXML
    protected void restart() throws IOException {
        SceneManager.getInstance().removeScene("Level");
        MainApp.switchScene("Configuration");
    }

    @FXML
    protected void quit() {
        MainApp.getPrimaryStage().close();
    }

    @Override
    public void init(Map<String, Object> loadParameters) {
        boolean win = (boolean) loadParameters.get("ending");
        if (win) {
            text.setText("Successful Infiltration!\n\tCongratulations!");
        } else {
            text.setText("System: Malware found and destroyed.\n\tYou died :(");
        }

        int millisTotal = (Integer) loadParameters.get("time");
        int seconds = (millisTotal / 1000) % 60;
        int minutes = (millisTotal / (1000 * 60)) % 60;
        int millis = millisTotal % 1000;
        time.setText(String.format("%02d:%02d.%03d", minutes, seconds, millis));
        damage.setText(" " + loadParameters.get("damageTaken"));
        enemies.setText(" " + loadParameters.get("killed"));

        init();
    }

    @Override
    public void init() {
        MusicManager.getInstance().stopAllTracks();
        MusicManager.getInstance().playTrack("Title");
    }

    @Override
    public void postInit() {
        MainApp.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
    }
}
