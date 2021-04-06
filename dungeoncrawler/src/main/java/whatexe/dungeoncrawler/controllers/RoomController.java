package whatexe.dungeoncrawler.controllers;

import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import whatexe.dungeoncrawler.ManagedController;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.layout.rooms.NullRoom;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.Map;

public class RoomController extends ManagedController {
    private static final Text BAD_ROOM_TEXT = new Text(50, 50, "Bad room!");

    static {
        BAD_ROOM_TEXT.setFill(Color.RED);
        BAD_ROOM_TEXT.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
    }

    @FXML
    protected AnchorPane roomDisplay;
    private Room room;

    @Override
    public void init(Map<String, Object> loadParameters) {
        room = (Room) loadParameters.getOrDefault("room", new NullRoom());

        initRoomDisplay();
    }

    @Override
    public void init() {
        room = new NullRoom();

        initRoomDisplay();
    }

    public void initRoomDisplay() {
        if (room.getBackgroundImage() != null) {
            Image image = room.getBackgroundImage();

            // set size
            roomDisplay.setMaxWidth(image.getWidth());
            roomDisplay.setPrefWidth(image.getWidth());
            roomDisplay.setMaxHeight(image.getHeight());
            roomDisplay.setPrefHeight(image.getHeight());

            // set image
            roomDisplay.setBackground(
                    new Background(new BackgroundImage(room.getBackgroundImage(),
                                                       BackgroundRepeat.NO_REPEAT,
                                                       BackgroundRepeat.NO_REPEAT,
                                                       BackgroundPosition.DEFAULT,
                                                       BackgroundSize.DEFAULT)));

            // TODO uncomment this to display blocked space
            // roomDisplay.getChildren().add(room.getBlockedSpace());
        } else {
            roomDisplay.getChildren().add(BAD_ROOM_TEXT);
        }

        // Add initial entities
        roomDisplay.getChildren().addAll(
                room.getDoors().stream().map(Entity::getDisplayNode).toArray(Node[]::new));
        roomDisplay.getChildren().addAll(
                room.getEnemies().stream().map(Entity::getDisplayNode).toArray(Node[]::new));
        roomDisplay.getChildren().addAll(
                room.getMiscEntities().stream().map(Entity::getDisplayNode).toArray(Node[]::new));
        roomDisplay.getChildren().addAll(
                room.getFriends().stream().map(Entity::getDisplayNode).toArray(Node[]::new));

        // Add player, if it already exists
        if (room.getPlayer() != null) {
            roomDisplay.getChildren().add(room.getPlayer().getDisplayNode());
        }

        // Allow for adding and removing of Entity Nodes via the room
        room.getDoors().addListener(this::handleRoomListChange);
        room.getEnemies().addListener(this::handleRoomListChange);
        room.getMiscEntities().addListener(this::handleRoomListChange);
        room.getFriends().addListener(this::handleRoomListChange);

        // Allow for updating the Player Node via the room
        room.playerProperty().addListener(this::handleRoomPropertyChange);
    }

    private void handleRoomListChange(ListChangeListener.Change<? extends Entity> change) {
        while (change.next()) {
            if (change.wasRemoved()) {
                for (Entity entity : change.getRemoved()) {
                    roomDisplay.getChildren().remove(entity.getDisplayNode());
                }
            }
            if (change.wasAdded()) {
                for (Entity entity : change.getAddedSubList()) {
                    roomDisplay.getChildren().add(entity.getDisplayNode());
                }
            }
        }
    }

    private void handleRoomPropertyChange(Observable observable, Entity oldValue, Entity newValue) {
        if (oldValue != null) {
            roomDisplay.getChildren().remove(oldValue.getDisplayNode());
        }
        if (newValue != null) {
            roomDisplay.getChildren().add(newValue.getDisplayNode());
        }
    }
}
