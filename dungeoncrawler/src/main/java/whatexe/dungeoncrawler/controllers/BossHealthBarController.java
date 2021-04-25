package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import whatexe.dungeoncrawler.ManagedController;

public class BossHealthBarController extends ManagedController {

    @FXML
    protected ProgressBar healthBar;

    @Override
    public void init() {

    }

    public ProgressBar getHealthBar() {
        return healthBar;
    }
}
