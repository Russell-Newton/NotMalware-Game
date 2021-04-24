package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import whatexe.dungeoncrawler.MainApp;

public class MainAppController {
    @FXML
    protected Pane expandingBackground;
    @FXML
    protected SubScene container;
    @FXML
    protected Pane containerPane;

    public void switchSubRoot(Pane root) {
        // This part is cringe, and I don't know why it's necessary
        MainApp.getPrimaryStage().getScene().getStylesheets().removeAll(
                containerPane.getStylesheets());
        containerPane.getChildren().clear();
        containerPane.getChildren().addAll(root);
        containerPane.prefWidthProperty().bind(root.prefWidthProperty());
        containerPane.prefHeightProperty().bind(root.prefHeightProperty());

        // container.setRoot(root);
        // container.widthProperty().bind(root.prefWidthProperty());
        // container.heightProperty().bind(root.prefHeightProperty());

        MainApp.getPrimaryStage().getScene().getStylesheets().addAll(
                containerPane.getStylesheets());
    }

}
