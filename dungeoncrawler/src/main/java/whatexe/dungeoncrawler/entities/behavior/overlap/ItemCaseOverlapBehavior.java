package whatexe.dungeoncrawler.entities.behavior.overlap;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.items.Item;
import whatexe.dungeoncrawler.entities.player.Player;

import java.util.List;

public class ItemCaseOverlapBehavior extends OverlapBehavior<Item> {
    public ItemCaseOverlapBehavior(Item owningEntity) {
        super(owningEntity);
    }

    @Override
    public void handleOverlap(Entity otherEntity) {
        Player other = (Player) otherEntity;
        if (!other.isInventoryFull()) {
            if (other.getMoney() >= owningEntity.getPrice()) {
                owningEntity.getOwningRoom().removeEntity(owningEntity);
                owningEntity.setEntityPosition(0, 0);
                other.addItem(owningEntity);
                other.adjustMoney(-owningEntity.getPrice());
            }
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
