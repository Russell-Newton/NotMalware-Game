package whatexe.dungeoncrawler.entities.behavior.overlap;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.items.Item;
import whatexe.dungeoncrawler.entities.player.Player;

import java.util.List;

public class ItemOverlapBehavior extends OverlapBehavior<Item> {
    public ItemOverlapBehavior(Item owningEntity) {
        super(owningEntity);
    }

    @Override
    public void handleOverlap(Entity otherEntity) {
        Player other = (Player) otherEntity;
        if (!other.isInventoryFull()) {
            owningEntity.getOwningRoom().removeEntity(owningEntity);
            owningEntity.setEntityPosition(0, 0);
            other.addItem(owningEntity);
        }
    }

    @Override
    public List<? extends Entity> getPossibleOverlapTargets() {
        if (owningEntity.getOwningRoom().getPlayer() != null
                && owningEntity.getNoPickupTimer() == 0) {
            return List.of(owningEntity.getOwningRoom().getPlayer());
        }
        return List.of();
    }
}
