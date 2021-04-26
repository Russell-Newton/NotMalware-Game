package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import whatexe.dungeoncrawler.Difficulty;
import whatexe.dungeoncrawler.MainApp;
import whatexe.dungeoncrawler.ManagedController;
import whatexe.dungeoncrawler.SceneManager;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.player.DebugPlayer;
import whatexe.dungeoncrawler.entities.player.MalwareType;
import whatexe.dungeoncrawler.entities.player.Player;

import java.io.IOException;
import java.util.Map;

public class ConfigurationController extends ManagedController {
    @FXML
    private TextField nameInput;
    @FXML
    private RadioButton easyButton;
    @FXML
    private RadioButton normalButton;
    @FXML
    private RadioButton hardButton;
    @FXML
    private Label malwareDescription;
    @FXML
    private Pane virusIcon;
    @FXML
    private Pane wormIcon;
    @FXML
    private Pane trojanIcon;
    private MalwareType malwareChoice = MalwareType.VIRUS;

    private static boolean isValidName(String name) {
        return !(name == null || name.trim().equals(""));
    }

    @Override
    public void init() {
        if (!SceneManager.getInstance().getLoaderMap().containsKey("Level")) {
            SceneManager.getInstance().addScene("Level",
                                                getClass().getResource("LevelDisplay.fxml"));
        }

        double iconScale = 95. / 32.;
        virusIcon.setOnMouseClicked(event -> {
            updateMalwareChoice(MalwareType.VIRUS);
            if (!virusIcon.getStyleClass().contains("selected-icon-pane")) {
                virusIcon.getStyleClass().add("selected-icon-pane");
            }
            wormIcon.getStyleClass().remove("selected-icon-pane");
            trojanIcon.getStyleClass().remove("selected-icon-pane");
        });
        Sprite virusSprite = MalwareType.VIRUS.getDefaultSprite();
        virusSprite.getTransforms().add(new Translate(3, 3));
        virusSprite.getTransforms().add(new Scale(iconScale, iconScale, 0, 0));
        virusIcon.getChildren().add(virusSprite);
        updateMalwareChoice(MalwareType.VIRUS);

        wormIcon.setOnMouseClicked(event -> {
            updateMalwareChoice(MalwareType.WORM);
            virusIcon.getStyleClass().remove("selected-icon-pane");
            if (!wormIcon.getStyleClass().contains("selected-icon-pane")) {
                wormIcon.getStyleClass().add("selected-icon-pane");
            }
            trojanIcon.getStyleClass().remove("selected-icon-pane");
        });
        Sprite wormSprite = MalwareType.WORM.getDefaultSprite();
        wormSprite.getTransforms().add(new Translate(3, 3));
        wormSprite.getTransforms().add(new Scale(iconScale, iconScale, 0, 0));
        wormIcon.getChildren().add(wormSprite);

        trojanIcon.setOnMouseClicked(event -> {
            updateMalwareChoice(MalwareType.TROJAN);
            virusIcon.getStyleClass().remove("selected-icon-pane");
            wormIcon.getStyleClass().remove("selected-icon-pane");
            if (!trojanIcon.getStyleClass().contains("selected-icon-pane")) {
                trojanIcon.getStyleClass().add("selected-icon-pane");
            }
        });
        Sprite trojanSprite = MalwareType.TROJAN.getDefaultSprite();
        trojanSprite.getTransforms().add(new Translate(3, 3));
        trojanSprite.getTransforms().add(new Scale(iconScale, iconScale, 0, 0));
        trojanIcon.getChildren().add(trojanSprite);
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
    protected void proceed() throws IOException {

        if (!isValidName(nameInput.getText())) {
            Alert noName = new Alert(Alert.AlertType.ERROR);
            noName.setHeaderText("Try Again");
            noName.setTitle("No Name input");
            noName.setContentText("Name cannot be empty");
            noName.initModality(Modality.WINDOW_MODAL);
            noName.initOwner(MainApp.getPrimaryStage());
            noName.showAndWait();
        } else {
            if (SceneManager.getInstance().getLoaderMap().containsKey("Inventory")) {
                SceneManager.getInstance().unloadScene("Inventory");
                SceneManager.getInstance().removeScene("Inventory");
            }
            Player player;
            if (nameInput.getText().equals("Debug") || nameInput.getText().equals("Burdell")) {
                player = new DebugPlayer(malwareChoice);
            } else {
                player = new Player(malwareChoice);
            }
            //to room 1
            MainApp.switchScene("Level", Map.of(
                    "difficulty", getDifficulty(),
                    "name", nameInput.getText(),
                    "player", player));
        }
    }

    private Difficulty getDifficulty() {
        if (easyButton.isSelected()) {
            return Difficulty.EASY;

        }
        if (hardButton.isSelected()) {
            return Difficulty.HARD;
        }
        return Difficulty.NORMAL;
    }

    @FXML
    protected void updateMalwareChoice(MalwareType newType) {
        malwareChoice = newType;
        malwareDescription.setText(malwareChoice.getDescription());
    }

}
