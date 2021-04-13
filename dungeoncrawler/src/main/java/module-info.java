module whatexe.dungeoncrawler {
    requires whatexe.tileengine;

    requires javafx.controls;       // Add dependency for javafx.controls
    requires javafx.fxml;           // Add dependency for javafx.fxml
    requires javafx.media;          // Add dependency for javafx.media
    requires javafx.swing;
    requires reflections8;

    // Allow javafx.fxml to use reflection for identifying members tagged with @FXML
    opens whatexe.dungeoncrawler to javafx.fxml;
    opens whatexe.dungeoncrawler.controllers to javafx.fxml;

    // Allow reflections8 to identify Entity classes
    opens whatexe.dungeoncrawler.entities to reflections8;

    // Allow other modules to see the outermost whatexe package
    exports whatexe.dungeoncrawler;
}
