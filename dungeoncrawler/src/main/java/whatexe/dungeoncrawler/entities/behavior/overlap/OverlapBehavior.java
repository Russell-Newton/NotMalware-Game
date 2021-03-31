package whatexe.dungeoncrawler.entities.behavior.overlap;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.EntityBehavior;

import java.util.List;

public abstract class OverlapBehavior<T extends Entity>
        extends EntityBehavior<T> {
    public OverlapBehavior(T owningEntity) {
        super(owningEntity);
    }

    public abstract void handleOverlap(Entity otherEntity);

    public abstract List<? extends Entity> getPossibleOverlapTargets();
}
