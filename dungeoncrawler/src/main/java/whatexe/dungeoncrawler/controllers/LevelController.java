package whatexe.dungeoncrawler.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import whatexe.dungeoncrawler.Difficulty;
import whatexe.dungeoncrawler.MainApp;
import whatexe.dungeoncrawler.ManagedController;
import whatexe.dungeoncrawler.SceneManager;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.doors.Door;
import whatexe.dungeoncrawler.entities.player.Player;
import whatexe.dungeoncrawler.layout.Direction;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.dungeoncrawler.layout.generation.LevelGenerator;
import whatexe.dungeoncrawler.layout.generation.RoomPosition;
import whatexe.dungeoncrawler.layout.generation.SimpleLevelGenerator;
import whatexe.dungeoncrawler.layout.rooms.Room;
import whatexe.dungeoncrawler.music.MusicManager;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiFunction;

public class LevelController extends ManagedController {

    /**
     * Encodes information of a {@link LevelController} and {@link RoomPosition} into a
     * consistent, unique String that can be used to add scenes to the {@link SceneManager}.
     */
    private static final BiFunction<LevelController, RoomPosition, String>
            SCENE_NAME_ENCODER = LevelController::encodeSceneName;

    private final HudFormatter hudFormatter;
    @FXML
    protected AnchorPane parentPane;
    @FXML
    protected SubScene roomSubScene;
    @FXML
    protected Pane minimapContainer;
    @FXML
    protected VBox leftSideGroup;
    @FXML
    protected Label healthLabel;
    @FXML
    protected ProgressBar healthBar;
    @FXML
    protected VBox healthGroup;
    @FXML
    protected Label moneyLabel;
    @FXML
    protected Pane moneyIcon;
    @FXML
    protected HBox moneyGroup;
    @FXML
    protected Label specialRechargeLabel;
    @FXML
    protected ProgressBar specialRechargeBar;
    @FXML
    protected VBox specialRechargeGroup;
    private String currentRoomSceneName;
    private Player player;
    private Room currentRoom;
    private Level level;
    private Difficulty difficulty = Difficulty.NORMAL;
    private Number seed;
    private LevelGenerator generator;
    private SimpleIntegerProperty depth;

    public LevelController() throws IOException {
        currentRoomSceneName = null;
        currentRoom = null;
        level = null;
        hudFormatter = new HudFormatter();
    }

    private static String encodeSceneName(LevelController levelController,
                                          RoomPosition roomPosition) {
        return levelController.depth.get()
                + System.identityHashCode(levelController)
                + roomPosition.toString();
    }

    @Override
    public void init(Map<String, Object> loadParameters) {
        player = (Player) loadParameters.get("player");
        difficulty = (Difficulty) loadParameters.getOrDefault("difficulty", Difficulty.NORMAL);
        seed = (Number) loadParameters.getOrDefault("seed", Double.NaN);
        init();
    }

    @Override
    public void init() {
        if (!SceneManager.getInstance().getLoaderMap().containsKey("Expo")) {
            SceneManager.getInstance().addScene("Expo", getClass().getResource("ExpoText.fxml"));
        }
        roomSubScene.widthProperty().bind(parentPane.prefWidthProperty());
        roomSubScene.heightProperty().bind(parentPane.prefHeightProperty());

        depth = new SimpleIntegerProperty(0);
        depth.addListener((__, oldValue, newValue) -> {
            nextLevel();
        });

        if (Double.isNaN(seed.doubleValue())) {
            generator = new SimpleLevelGenerator(difficulty);
        } else {
            generator = new SimpleLevelGenerator(difficulty, seed.longValue());
        }

        MusicManager.getInstance().stopAllTracks();
        MusicManager.getInstance().playTrack("RegularPlay");

        nextLevel();

        initPlayer();

        if (!SceneManager.getInstance().getLoaderMap().containsKey("EndScreen")) {
            SceneManager.getInstance().addScene("EndScreen",
                                                getClass().getResource("EndScreen.fxml"));
        }
    }

    @Override
    public void postInit() {
        MainApp.getPrimaryStage().getScene().setCursor(Cursor.NONE);
        player.initControls();
    }

    @Override
    public void setState(Map<String, Object> stateParameters) {
        depth.set((int) stateParameters.getOrDefault("depth", depth.get()));
    }

    @Override
    public void deinit() {
        currentRoom.stopTicking();
    }

    public void transitionToEndScreen(boolean won) {
        currentRoom.setPlayer(null);
        currentRoom.stopTicking();
        SceneManager.getInstance().unloadScene(currentRoomSceneName);
        MusicManager.getInstance().stopAllTracks();
        MusicManager.getInstance().playTrack("Title");
        try {
            MainApp.switchScene("EndScreen", Map.of(
                    "ending", won,
                    "time", player.getTicks() * 5,
                    "killed", player.getEnemiesKilled(),
                    "damageTaken", player.getDmgTaken()));
        } catch (Exception e) {
            e.printStackTrace();
            MainApp.getPrimaryStage().close();
        }
    }

    private void nextLevel() {
        Pane expoPane = null;
        try {
            expoPane = (Pane) SceneManager.getInstance()
                                          .loadParent("Expo",
                                                      Map.of("depth", depth.get()));
            ((Pane) MainApp.getPrimaryStage().getScene().getRoot()).getChildren().add(expoPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pane finalExpoPane = expoPane;
        Platform.runLater(() -> {
            // initialize Level
            if (depth.get() != 0) {
                minimapContainer.getChildren().remove(level.getMinimap());
            }
            level = generator.generate(depth.get());
            level.currentRoomPositionProperty()
                 .addListener((observer, oldValue, newValue) -> updateRoom(oldValue, newValue));

            minimapContainer.getChildren().add(level.getMinimap());

            // Add room scenes to SceneManger
            for (RoomPosition position : level.getMaze().keySet()) {
                SceneManager.getInstance().addScene(SCENE_NAME_ENCODER.apply(this, position),
                                                    getClass().getResource("RoomDisplay.fxml"));
            }

            updateRoom(null, level.getRoomPosition());

            if (finalExpoPane != null) {
                currentRoom.stopTicking();
                SceneManager.getInstance().setSceneState("Expo", Map.of("ready", true));
                MainApp.addKeyPressHandler(new EventHandler<>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (!event.isConsumed() && event.getCode() != KeyCode.F11) {
                            ((Pane) MainApp.getPrimaryStage().getScene().getRoot())
                                    .getChildren().remove(finalExpoPane);
                            MainApp.removeKeyPressHandler(this);
                            SceneManager.getInstance().unloadScene("Expo");
                            event.consume();
                            currentRoom.startTicking();
                        }
                    }
                });
            }
        });
    }

    private void updateRoom(RoomPosition oldValue, RoomPosition newValue) {
        if (currentRoom != null) {
            currentRoom.setPlayer(null);
            currentRoom.stopTicking();
        }
        currentRoom = level.getCurrentRoom();

        if (currentRoomSceneName != null) {
            SceneManager.getInstance().unloadScene(currentRoomSceneName);
        }
        currentRoomSceneName =
                SCENE_NAME_ENCODER.apply(this, newValue);


        try {
            roomSubScene.setRoot(SceneManager.getInstance()
                                             .loadParent(currentRoomSceneName,
                                                         Map.of("room", currentRoom)));

        } catch (IOException e) {
            throw new RuntimeException("Cannot load next room! " + currentRoomSceneName);
        }

        currentRoom.updateDoorLocks();
        parentPane.prefWidthProperty().bind(((Pane) roomSubScene.getRoot()).prefWidthProperty());
        parentPane.prefHeightProperty().bind(((Pane) roomSubScene.getRoot()).prefHeightProperty());

        // TODO - why is this necessary??
        Button tempControl = new Button("temp");
        parentPane.getChildren().add(tempControl);
        parentPane.getChildren().remove(tempControl);

        adjustPlayerIncomingPosition(oldValue, newValue);

        currentRoom.setPlayer(player);
        currentRoom.startTicking();
    }

    protected void initPlayer() {
        // Set health data
        int startingHealth =
                (int) (difficulty.getDefaultHealth() * player.getMalwareType()
                                                             .getMaxHealthModifier());
        player.getEntityStatistics().setMaxHealth(startingHealth);
        player.setHealth(startingHealth);
        healthLabel.setText(hudFormatter.healthFormat(player.getHealth()));
        player.healthProperty().addListener((text, oldValue, newValue) -> {
            double currentHealth = newValue.intValue();
            healthBar.setProgress(currentHealth / (player.getEntityStatistics().getMaxHealth()));
            healthLabel.setText(hudFormatter.healthFormat(newValue.intValue()));
        });

        // Set money data
        int startingMoney = difficulty.getDefaultMoney();
        moneyLabel.setText(hudFormatter.moneyFormat(startingMoney));
        player.setMoney(startingMoney);
        player.moneyProperty().addListener((text, oldValue, newValue) ->
                                                   moneyLabel.setText(hudFormatter.moneyFormat(
                                                           newValue.intValue())));

        // Set recharge data
        player.specialAttackChargeProperty().addListener((__, oldValue, newValue) -> {
            double progress = newValue.doubleValue() / player.getSpecialAttackMaxCharge();
            if (progress == 1) {
                if (specialRechargeBar.getStyleClass().remove("special-attack-progress-bar")) {
                    specialRechargeBar.getStyleClass().add("special-attack-progress-bar-filled");
                }
                specialRechargeBar.setProgress(-1);
            } else {
                if (specialRechargeBar.getStyleClass()
                                      .remove("special-attack-progress-bar-filled")) {
                    specialRechargeBar.getStyleClass().add("special-attack-progress-bar");
                }
                specialRechargeBar.setProgress(progress);
            }
        });
    }

    /**
     * When the player moves to the next room, it adjusts the player position so it looks like
     * they come out of that door
     * <p>
     * NOTE: This assumes that the doors are buttons. This method is going to have to change once
     * we change what the doors actually are!!!
     *
     * @param lastRoom the position of the room last visited
     * @param newRoom  the position of the room just entering
     */
    private void adjustPlayerIncomingPosition(RoomPosition lastRoom, RoomPosition newRoom) {
        if (lastRoom == null) {
            player.setEntityPosition(roomSubScene.getWidth() / 2, roomSubScene.getHeight() / 2);
            return;
        }
        Direction moveDirection = RoomPosition.getPrimaryDirectionFrom(newRoom, lastRoom);
        Door playerExitDoor = Door.getDoorFromList(currentRoom.getDoors(), moveDirection);
        if (playerExitDoor == null) {
            player.setEntityPosition(roomSubScene.getWidth() / 2, roomSubScene.getHeight() / 2);
            return;
        }
        Sprite doorNode = playerExitDoor.getDisplayNode();
        double width = doorNode.getWidth();
        double height = doorNode.getHeight();

        double dX;
        double dY;

        switch (moveDirection) {
        case DOWN:
            dX = width / 2 - player.getDisplayNode().getWidth() / 2;
            dY = -(player.getDisplayNode().getHeight() + 10);
            break;
        case RIGHT:
            dX = -(player.getDisplayNode().getWidth() + 10);
            dY = height / 2 - player.getDisplayNode().getHeight() / 2;
            break;
        case LEFT:
            dX = 10 + height;
            dY = height / 2 - player.getDisplayNode().getHeight() / 2;
            break;
        case UP:
            dX = width / 2 - player.getDisplayNode().getWidth() / 2;
            dY = 10 + height;
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + moveDirection);
        }

        player.setEntityPosition(dX + doorNode.getTranslateX(),
                                 dY + doorNode.getTranslateY());
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public SubScene getRoomSubScene() {
        return roomSubScene;
    }

    private class HudFormatter {
        private String moneyFormat(int money) {
            return String.format("x %d", money);
        }

        private String healthFormat(int health) {
            return String.format("Health: %d/%d",
                                 health,
                                 player.getEntityStatistics().getMaxHealth());
        }
    }
}
