package whatexe.dungeoncrawler.entities.behavior.overlap;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.miscellaneous.Explosion;

import java.util.ArrayList;
import java.util.List;

public class ExplosionOverlapBehavior extends DamageOnOverlapBehavior<Explosion> {

    private final List<Entity> alreadyHit;

    public ExplosionOverlapBehavior(Explosion owningEntity) {
        super(owningEntity);
        alreadyHit = new ArrayList<>();
    }

    @Override
    public void handleOverlap(Entity otherEntity) {
        super.handleOverlap(otherEntity);
        alreadyHit.add(otherEntity);
    }

    @Override
    public List<? extends Entity> getPossibleOverlapTargets() {
        List<Entity> targets = new ArrayList<>();
        for (Entity entity : owningEntity.getOwningRoom().getFriends()) {
            if (!alreadyHit.contains(entity)) {
                targets.add(entity);
            }
        }
        for (Entity entity : owningEntity.getOwningRoom().getEnemies()) {
            if (!alreadyHit.contains(entity)) {
                targets.add(entity);
            }
        }
        if (owningEntity.getOwningRoom().getPlayer() != null
                && !alreadyHit.contains(owningEntity.getOwningRoom().getPlayer())) {
            targets.add(owningEntity.getOwningRoom().getPlayer());
        }
        return targets;
    }
}
