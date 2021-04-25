package whatexe.dungeoncrawler.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import whatexe.dungeoncrawler.ManagedController;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.items.Consumable;
import whatexe.dungeoncrawler.entities.items.Currency;
import whatexe.dungeoncrawler.entities.items.Item;
import whatexe.dungeoncrawler.entities.items.modifiers.Modifier;
import whatexe.dungeoncrawler.entities.player.Inventory;
import whatexe.dungeoncrawler.entities.player.Player;

import java.awt.*;

public class InventoryController extends ManagedController {

    private final int gridSize = 32;
    private final int inventoryRows = 3;
    private final int inventoryCols = 8;
    private final int modifierSize = 2;
    @FXML
    private GridPane inventoryGrid;
    @FXML
    private GridPane modifiers;
    @FXML
    private Label healthLabel;
    @FXML
    private ProgressBar healthBar;
    @FXML
    private Pane moneyIcon;
    @FXML
    private Label moneyLabel;
    @FXML
    private Pane inventoryScreen;
    @FXML
    private Pane inventoryBackground;
    @FXML
    private Label itemDescription;
    @FXML
    private StackPane innerInventoryScreen;

    private Inventory inventory;
    private Player player;
    private Node pickedNode;

    @FXML
    public void init() {
        clearInventory();
        inventoryBackground.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            movePickedNode(x, y);

        });

        inventoryBackground.setOnMouseClicked(event -> {
            if (event.getPickResult().getIntersectedNode() instanceof AnchorPane) {
                dropItem();
            }
        });
        innerInventoryScreen.setOnMouseClicked(event -> {
            if (event.getPickResult().getIntersectedNode() instanceof StackPane) {
                dropItem();
            }
        });

        inventoryGrid.setOnMouseMoved(event -> {
            Item item = inventory.getItemFromNode(event.getPickResult().getIntersectedNode());
            setDescriptionLabel(item);
        });
    }

    private void setDescriptionLabel(Item item) {
        if (item != null) {
            itemDescription.setText(item.getDescription());
        }
    }

    private void useInventoryItem(MouseEvent event) {
        Item item = inventory.getItemFromNode(event.getPickResult().getIntersectedNode());
        if (item instanceof Consumable) {
            inventory.useConsumable((Consumable) item);
            updateInventory();
        }
    }

    private void clearInventory() {
        inventoryGrid.getChildren().retainAll(inventoryGrid.getChildren().get(0));
        modifiers.getChildren().retainAll(modifiers.getChildren().get(0));
        for (int i = 0; i < inventoryRows; i++) {
            for (int j = 0; j < inventoryCols; j++) {
                inventoryGrid.add(new Empty(), j, i);
            }
        }
        for (int i = 0; i < modifierSize; i++) {
            Empty empty = new Empty();
            empty.getTransforms().add(new Scale(2, 2, 0, 0));
            modifiers.add(empty, i, 0);
        }
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.player = inventory.getPlayer();
        healthBar.setProgress(1);
        healthLabel.setText(String.format("Health: %d/%d",
                                          player.getHealth(),
                                          player.getEntityStatistics().getMaxHealth()));
        player.healthProperty().addListener((text, oldValue, newValue) -> {
            double currentHealth = (int) newValue;
            healthBar.setProgress(currentHealth / (player.getEntityStatistics().getMaxHealth()));
            healthLabel.setText(String.format("Health: %d/%d",
                                              player.getHealth(),
                                              player.getEntityStatistics().getMaxHealth()));
        });
        player.moneyProperty()
              .addListener((text, oldValue, newValue) -> moneyLabel.setText(String.format(
                      "x %d", player.getMoney())));
        Sprite moneySprite = Currency.getDefaultDisplayNode();
        moneyIcon.getChildren().add(moneySprite);
        moneyIcon.getTransforms().add(new Scale(1.25, 1.25, 0, 0));
    }

    private void movePickedNode(double x, double y) {
        if (pickedNode == null) {
            return;
        }
        pickedNode.setLayoutX(0);
        pickedNode.setLayoutY(0);
        pickedNode.setTranslateX(x - (pickedNode.getLayoutBounds().getWidth() / 2));
        pickedNode.setTranslateY(y - (pickedNode.getLayoutBounds().getHeight() / 2));
    }

    @FXML
    public void inventoryClick(MouseEvent event) {
        if (event.getPickResult().getIntersectedNode() != inventoryGrid
                && !(event.getPickResult().getIntersectedNode() instanceof Line)) {
            if (event.getButton() == MouseButton.PRIMARY) {
                chooseInventoryPickupOption(event);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                useInventoryItem(event);
            }
        }
    }

    private void chooseInventoryPickupOption(MouseEvent event) {
        if (pickedNode == null) {
            //Not item is picked up and the player picks an item in inventory
            if (!(event.getPickResult().getIntersectedNode() instanceof Empty)) {
                Empty empty = new Empty();
                pickedNode = inventoryPickup(event, empty);
            }
        } else {
            //Have picked up item and click on empty node
            if (event.getPickResult().getIntersectedNode() instanceof Empty) {
                inventoryPickup(event, pickedNode);
                pickedNode = null;
                //Have picked up item and click on another item
            } else {
                pickedNode = inventoryPickup(event, pickedNode);
            }
            updateInventory();
        }
    }


    private Node inventoryPickup(MouseEvent event, Node nodeGoingIn) {
        Node nodeBeingClicked = event.getPickResult().getIntersectedNode();
        int row = GridPane.getRowIndex(nodeBeingClicked);
        int col = GridPane.getColumnIndex(nodeBeingClicked);
        //Set the layout of the nodeBeingClicked to where the mouse is (stupid Javafx stuff)
        nodeBeingClicked.setLayoutX(inventoryScreen.getLayoutX() + inventoryGrid.getLayoutX()
                                            - (nodeBeingClicked.getLayoutBounds().getWidth() / 2));
        nodeBeingClicked.setLayoutY(inventoryScreen.getLayoutY() + inventoryGrid.getLayoutY()
                                            - (nodeBeingClicked.getLayoutBounds().getHeight() / 2));
        nodeBeingClicked.setTranslateX(event.getX());
        nodeBeingClicked.setTranslateY(event.getY());
        nodeBeingClicked.setMouseTransparent(true);
        inventoryGrid.getChildren().remove(nodeBeingClicked);
        inventory.removeFromItemGrid(inventory.getItemFromNode(nodeBeingClicked));
        nodeGoingIn.setTranslateX(0);
        nodeGoingIn.setTranslateY(0);
        nodeGoingIn.setMouseTransparent(false);
        inventory.addItemAtCoord(inventory.getItemFromNode(nodeGoingIn), new Point(row, col));


        if (!(event.getPickResult().getIntersectedNode() instanceof Empty)) {
            inventoryBackground.getChildren().add(nodeBeingClicked);
            return nodeBeingClicked;
        }
        return null;
    }

    @FXML
    private void modifierClick(MouseEvent event) {
        if (event.getPickResult().getIntersectedNode() != modifiers
                && !(event.getPickResult().getIntersectedNode() instanceof Line)
                && (inventory.getItemFromNode(pickedNode) instanceof Modifier
                || pickedNode == null)) {
            if (event.getButton() == MouseButton.PRIMARY) {
                modifierPickup(event);
            }
        }
    }

    private void modifierPickup(MouseEvent event) {
        if (pickedNode == null) {
            Empty empty = new Empty();
            pickedNode = modifierPickup(event, empty);
        } else {
            //Have picked up item and click on empty node
            if (event.getPickResult().getIntersectedNode() instanceof Empty) {
                modifierPickup(event, pickedNode);
                pickedNode = null;
                //Have picked up item and click on another item
            } else {
                pickedNode = modifierPickup(event, pickedNode);
            }
            updateInventory();
        }
    }

    private Node modifierPickup(MouseEvent event, Node nodeGoingIn) {
        Node nodeBeingClicked = event.getPickResult().getIntersectedNode();
        int row = GridPane.getRowIndex(nodeBeingClicked);
        int col = GridPane.getColumnIndex(nodeBeingClicked);
        //Set the layout of the nodeBeingClicked to where the mouse is (stupid Javafx stuff)
        nodeBeingClicked.setLayoutX(inventoryScreen.getLayoutX() + modifiers.getLayoutX()
                                            - (nodeBeingClicked.getLayoutBounds().getWidth() / 2));
        nodeBeingClicked.setLayoutY(inventoryScreen.getLayoutY() + modifiers.getLayoutY()
                                            - (nodeBeingClicked.getLayoutBounds().getHeight() / 2));
        nodeBeingClicked.setTranslateX(event.getX());
        nodeBeingClicked.setTranslateY(event.getY());
        nodeBeingClicked.setMouseTransparent(true);
        modifiers.getChildren().remove(nodeBeingClicked);
        inventory.removeFromItemGrid(inventory.getItemFromNode(nodeBeingClicked));
        inventory.removeModifier(inventory.getItemFromNode(nodeBeingClicked));
        nodeGoingIn.setTranslateX(0);
        nodeGoingIn.setTranslateY(0);
        nodeGoingIn.setMouseTransparent(false);
        nodeGoingIn.getTransforms().add(new Scale(2, 2, 0, 0));
        inventory.addMotifierAtCoord(inventory.getItemFromNode(nodeGoingIn), new Point(row, col));

        if (!(event.getPickResult().getIntersectedNode() instanceof Empty)) {
            nodeBeingClicked.getTransforms().clear();
            inventoryBackground.getChildren().add(nodeBeingClicked);
            modifiers.add(nodeGoingIn, col, row);
            return nodeBeingClicked;
        }
        return null;
    }

    public void updateInventory() {
        clearInventory();
        for (Point point : inventory.getInventoryItems().keySet()) {
            if (inventory.getInventoryItems().get(point) != null) {
                inventoryGrid.add(inventory.getInventoryItems().get(point).getDisplayNode(),
                                  (int) point.getY(),
                                  (int) point.getX());
            }
        }
        for (Point point : inventory.getModifiers().keySet()) {
            if (inventory.getModifiers().get(point) != null) {
                modifiers.add(inventory.getModifiers().get(point).getDisplayNode(),
                              (int) point.getY(),
                              (int) point.getX());
            }
        }
    }

    private void dropItem() {
        if (pickedNode == null) {
            return;
        }
        inventoryBackground.getChildren().remove(pickedNode);
        inventory.getItemFromNode(pickedNode).setOwningRoom(player.getOwningRoom());
        player.getOwningRoom().dropItem(inventory.getItemFromNode(pickedNode),
                                        player, 200);

        inventory.removeItem(inventory.getItemFromNode(pickedNode));
        pickedNode = null;
    }


    public class Empty extends Rectangle {
        public Empty() {
            super(InventoryController.this.gridSize, InventoryController.this.gridSize);
            this.setOpacity(0);
        }

        public String getDescription() {
            return "Null item";
        }

    }


}
