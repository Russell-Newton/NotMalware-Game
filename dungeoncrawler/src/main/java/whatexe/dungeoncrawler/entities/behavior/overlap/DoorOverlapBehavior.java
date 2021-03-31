package whatexe.dungeoncrawler.entities.behavior.overlap;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.doors.Door;

import java.util.List;

public class DoorOverlapBehavior extends OverlapBehavior<Door> {
    public DoorOverlapBehavior(Door owningEntity) {
        super(owningEntity);
    }

    @Override
    public void handleOverlap(Entity otherEntity) {
        if (!owningEntity.isLocked()) {
            owningEntity.getOwningLevel().move(owningEntity.getDirection());
        }
    }

    @Override
    public List<? extends Entity> getPossibleOverlapTargets() {
        if (owningEntity.getOwningRoom().getPlayer() != null) {
            return List.of(owningEntity.getOwningRoom().getPlayer());
        }
        return List.of();
    }
}
