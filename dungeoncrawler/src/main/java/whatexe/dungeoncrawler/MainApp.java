package whatexe.dungeoncrawler;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import whatexe.dungeoncrawler.controllers.MainAppController;
import whatexe.dungeoncrawler.music.MusicManager;

import java.io.IOException;
import java.util.Map;

public class MainApp extends Application {

    private static Stage primaryStage;
    private static String currentScene;
    private static MainAppController controller;
    private static Scene container;
    private static boolean isFullscreen;
    private static ObservableList<EventHandler<? super KeyEvent>> keyPressHandlers;
    private static ObservableList<EventHandler<? super KeyEvent>> keyReleaseHandlers;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Switches to a new scene. Loads the new scene and unloads the current (if present) scene.
     *
     * @param sceneName the name of the new scene, as held in the {@link SceneManager}.
     * @throws IOException if the FXML file saved in the {@link SceneManager} cannot be loaded.
     * @see MainApp#switchScene(String, Map)
     * @see SceneManager
     * @see SceneManager#loadScene(String)
     * @see SceneManager#unloadScene(String)
     */
    public static void switchScene(String sceneName) throws IOException {
        switchScene(sceneName, Map.of());
    }

    /**
     * Switches to a new scene. Loads the new scene and unloads the current (if present) scene.
     *
     * @param sceneName      the name of the new scene, as held in the {@link SceneManager}.
     * @param loadParameters optional parameters passed into the {@link ManagedController}'s
     *                       {@link ManagedController#init} method.
     * @throws IOException if the FXML file saved in the {@link SceneManager} cannot be loaded.
     * @see SceneManager
     * @see SceneManager#loadScene(String, Map)
     * @see SceneManager#unloadScene(String)
     * @see ManagedController#init(Map)
     */
    public static void switchScene(String sceneName, Map<String, Object> loadParameters)
            throws IOException {
        String previousScene = currentScene;
        currentScene = sceneName;

        clearKeyPressHandlers();
        clearKeyReleaseHandlers();

        // Set the primaryStage scene to the new scene
        Pane root = (Pane) SceneManager.getInstance().loadParent(sceneName, loadParameters);
        controller.switchSubRoot(root);

        Platform.runLater(() -> {
            SceneManager.getInstance().getSceneController(sceneName).postInit();
            if (!isFullscreen) {
                primaryStage.sizeToScene();
                primaryStage.centerOnScreen();
            }
            if (previousScene != null) {
                SceneManager.getInstance().unloadScene(previousScene);
            }
        });
    }

    public static void resetToWelcomeScreen() throws IOException {
        SceneManager.getInstance().clear();
        currentScene = null;

        // Switch to Welcome Screen
        SceneManager.getInstance().
                addScene("Welcome Screen",
                         MainApp.class.getResource("controllers/WelcomeScreen.fxml"));
        switchScene("Welcome Screen");
    }

    public static void addKeyPressHandler(EventHandler<? super KeyEvent> handler) {
        keyPressHandlers.add(handler);
    }

    public static void removeKeyPressHandler(EventHandler<? super KeyEvent> handler) {
        keyPressHandlers.remove(handler);
    }

    public static void clearKeyPressHandlers() {
        keyPressHandlers.clear();
    }

    public static void addKeyReleaseHandler(EventHandler<? super KeyEvent> handler) {
        keyReleaseHandlers.add(handler);
    }

    public static void removeKeyReleaseHandler(EventHandler<? super KeyEvent> handler) {
        keyReleaseHandlers.remove(handler);
    }

    public static void clearKeyReleaseHandlers() {
        keyReleaseHandlers.clear();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static String getCurrentScene() {
        return currentScene;
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        isFullscreen = true;
        currentScene = null;
        MainApp.primaryStage = primaryStage;

        keyPressHandlers = FXCollections.observableArrayList();
        keyPressHandlers.addListener((ListChangeListener<EventHandler<? super KeyEvent>>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    for (EventHandler<? super KeyEvent> handler : c.getRemoved()) {
                        container.removeEventFilter(KeyEvent.KEY_PRESSED, handler);
                    }
                }
                if (c.wasAdded()) {
                    for (EventHandler<? super KeyEvent> handler : c.getAddedSubList()) {
                        container.addEventFilter(KeyEvent.KEY_PRESSED, handler);
                    }
                }
            }
        });

        keyReleaseHandlers = FXCollections.observableArrayList();
        keyReleaseHandlers.addListener((ListChangeListener<EventHandler<? super KeyEvent>>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    for (EventHandler<? super KeyEvent> handler : c.getRemoved()) {
                        container.removeEventFilter(KeyEvent.KEY_RELEASED, handler);
                    }
                }
                if (c.wasAdded()) {
                    for (EventHandler<? super KeyEvent> handler : c.getAddedSubList()) {
                        container.addEventFilter(KeyEvent.KEY_RELEASED, handler);
                    }
                }
            }
        });

        initMusicTracks();

        // Load containing scene and controller
        FXMLLoader loader =
                new FXMLLoader(MainAppController.class.getResource("MainApp.fxml"));
        container = new Scene(loader.load());
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.F11) {
                isFullscreen = !isFullscreen;
                if (isFullscreen) {
                    primaryStage.setFullScreen(true);
                } else {
                    primaryStage.setFullScreen(false);
                    primaryStage.sizeToScene();
                    primaryStage.centerOnScreen();
                }
            }
        });
        controller = loader.getController();
        primaryStage.setScene(container);

        // Manage primaryStage config
        primaryStage.setTitle("What.exe Dungeon Crawler");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(isFullscreen);

        primaryStage.show();
        resetToWelcomeScreen();
    }

    private void initMusicTracks() {
        MusicManager.getInstance().addTrack("Title").setVolume(0.2);
        MusicManager.getInstance().addTrack("RegularPlay").setVolume(0.6);
        MusicManager.getInstance().addTrack("Boss").setVolume(0.6);
        MusicManager.getInstance().addTrack("Elevator").setVolume(0.18);
    }
}
