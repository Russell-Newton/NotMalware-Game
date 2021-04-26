package whatexe.dungeoncrawler.layout.rooms;

import javafx.scene.shape.Rectangle;
import whatexe.dungeoncrawler.entities.EntityChooser;
import whatexe.dungeoncrawler.entities.behavior.overlap.ItemCaseOverlapBehavior;
import whatexe.dungeoncrawler.entities.items.Currency;
import whatexe.dungeoncrawler.entities.items.HealthPotion;
import whatexe.dungeoncrawler.entities.items.Item;
import whatexe.dungeoncrawler.entities.player.Player;
import whatexe.dungeoncrawler.layout.Direction;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.tileengine.TiledMap;

import java.util.List;
import java.util.stream.Collectors;

public class ShopRoom extends SimpleRoom {

    private static final int NUM_HEALTH_POTIONS = 3;
    private static final int NUM_MODIFIERS = 1;

    public ShopRoom(TiledMap fromTiledMap,
                    Level owningLevel,
                    Direction... exitDirections) {
        super(fromTiledMap, owningLevel, exitDirections);
    }

    @Override
    protected void initEntities() {
        List<Rectangle> spawningRegions =
                fromTiledMap.getObjectGroups().get("Items").getObjects().stream()
                            .map(mapObject -> new Rectangle(
                                    mapObject.getX(),
                                    mapObject.getY(),
                                    mapObject.getWidth(),
                                    mapObject.getHeight()))
                            .collect(Collectors.toList());
        int numHealthPotions = 0;
        int numModifiers = 0;
        int maxHealthPotions = NUM_HEALTH_POTIONS - (int) (2 * Math.random());
        int maxModifiers = (int) (Math.random() * (NUM_MODIFIERS + 1));
        for (Rectangle spawnRegion : spawningRegions) {
            Item item = null;
            if (numHealthPotions < maxHealthPotions) {
                item = new HealthPotion(this);
                numHealthPotions++;
            } else if (numModifiers < maxModifiers) {
                item = EntityChooser.getInstance().getRandomModifier(this);
                numModifiers++;
            } else {
                while (item == null
                        || item instanceof Currency
                        || item instanceof HealthPotion) {
                    item = EntityChooser.getInstance()
                                        .getRandomItem(this, false);
                }
            }
            miscEntities.add(item);
            item.setEntityPosition(spawnRegion.getX(),
                                   spawnRegion.getY());
            item.getBehaviorSet().setOverlapBehavior(new ItemCaseOverlapBehavior(item));
        }
    }

    @Override
    public void setPlayer(Player player) {
        super.setPlayer(player);
        if (player != null) {
            hasVisited = true;
            updateDoorLocks();
        }
    }
}
