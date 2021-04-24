package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import whatexe.dungeoncrawler.MainApp;
import whatexe.dungeoncrawler.ManagedController;
import whatexe.dungeoncrawler.SceneManager;

public class PauseMenuController extends ManagedController {
    @Override
    public void init() {

    }

    @FXML
    protected void continuePlay() {
        ((LevelController) SceneManager.getInstance().getSceneController("Level"))
                .getPlayer()
                .operatePauseMenu();
    }

    @FXML
    protected void quit() {
        MainApp.getPrimaryStage().close();
    }
}
