package whatexe.dungeoncrawler.entities.player;

import javafx.scene.Node;
import javafx.scene.SubScene;
import whatexe.dungeoncrawler.SceneManager;
import whatexe.dungeoncrawler.controllers.InventoryController;
import whatexe.dungeoncrawler.entities.items.Consumable;
import whatexe.dungeoncrawler.entities.items.Item;
import whatexe.dungeoncrawler.entities.items.modifiers.Modifier;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class Inventory {
    private final SubScene parent;
    private final Player player;
    private final int inventoryRows = 3;
    private final int inventoryCols = 8;
    private HashMap<Point, Item> inventoryItems = new HashMap<>();
    private HashMap<Point, Item> modifiers = new HashMap<>();
    private HashMap<Node, Item> itemMap = new HashMap<>();


    public Inventory(Player player) {
        this.player = player;
        SubScene parent1;
        try {
            SceneManager.getInstance().addScene("Inventory",
                                                InventoryController.class
                                                        .getResource("Inventory.fxml"));
            parent1 = new SubScene(SceneManager.getInstance().loadParent("Inventory"),
                                   500, 500);
        } catch (IOException e) {
            parent1 = null;
            e.printStackTrace();
        }
        parent = parent1;
        parent.setId("inventorySubScene");
        controllerAccess();
    }

    private void controllerAccess() {
        ((InventoryController) SceneManager.getInstance().
                getSceneController("Inventory")).setInventory(this);
    }

    public boolean isInventoryFull() {
        return inventoryItems.size() >= inventoryRows * inventoryCols;
    }

    public boolean addItem(Item addedItem) {
        if (isInventoryFull()) {
            return false;
        }
        addedItem.setOwningEntity(player);
        addedItem.getDisplayNode().setTranslateX(0);
        addedItem.getDisplayNode().setTranslateY(0);
        addedItem.getDisplayNode().setLayoutX(0);
        addedItem.getDisplayNode().setLayoutY(0);
        addedItem.getDisplayNode().setMouseTransparent(false);
        inventoryItems.put(nextEmptyInventorySpot(), addedItem);
        itemMap.put(addedItem.getDisplayNode(), addedItem);
        return true;
    }

    public boolean removeItem(Item item) {
        removeFromItemGrid(item);
        return itemMap.values().remove(item);
    }

    public void addItemAtCoord(Item addedItem, Point point) {
        inventoryItems.put(point, addedItem);
    }

    public boolean removeFromItemGrid(Item item) {
        if (inventoryItems.values().remove(item)) {
            modifiers.values().remove(item);
            return true;
        }
        return false;
    }

    public void addMotifierAtCoord(Item addedItem, Point point) {
        if (addedItem instanceof Modifier) {
            player.getBehaviorSet()
                  .addBehaviorTransformation(((Modifier) addedItem).getEquipTransformation());
        }
        modifiers.put(point, addedItem);
    }

    public void removeModifier(Item item) {
        if (item instanceof Modifier) {
            player.getBehaviorSet()
                  .removeBehaviorTransformation(((Modifier) item).getEquipTransformation());
        }
        removeFromItemGrid(item);
    }

    public void useConsumable(Consumable consumable) {
        player.getStatusEffects().add(consumable.getConsumeEffect());
        removeItem((Item) consumable);
    }

    public SubScene getPane() {
        parent.setWidth(SceneManager.getInstance()
                                    .getParent("Level").getLayoutBounds().getWidth());
        parent.setHeight(SceneManager.getInstance()
                                     .getParent("Level").getLayoutBounds().getHeight());
        parent.getRoot().setScaleX(2);
        parent.getRoot().setScaleY(2);
        return parent;
    }

    public Player getPlayer() {
        return player;
    }

    public Item getItemFromNode(Node node) {
        return itemMap.get(node);
    }

    public HashMap<Point, Item> getInventoryItems() {
        return inventoryItems;
    }

    public HashMap<Point, Item> getModifiers() {
        return modifiers;
    }

    private Point nextEmptyInventorySpot() {
        if (isInventoryFull()) {
            return null;
        }
        for (int i = 0; i < inventoryRows; i++) {
            for (int j = 0; j < inventoryCols; j++) {
                Point point = new Point(i, j);
                if (inventoryItems.get(point) == null) {
                    return point;
                }
            }
        }
        return null;
    }

    public boolean isInventoryEmpty() {
        return inventoryItems.isEmpty();
    }


}
