package whatexe.dungeoncrawler.entities.behavior.overlap;

import whatexe.dungeoncrawler.entities.Entity;

import java.util.List;

public class DoNothingOverlapBehavior extends OverlapBehavior<Entity> {
    public DoNothingOverlapBehavior(Entity owningEntity) {
        super(owningEntity);
    }

    @Override
    public void handleOverlap(Entity otherEntity) {
    }

    @Override
    public List<? extends Entity> getPossibleOverlapTargets() {
        return List.of();
    }
}
