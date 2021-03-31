package whatexe.dungeoncrawler.entities.behavior.presets;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.BehaviorSet;
import whatexe.dungeoncrawler.entities.behavior.attack.DoNothingAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.collision.DoNothingCollisionBehavior;
import whatexe.dungeoncrawler.entities.behavior.death.DoNothingDeathBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.DoNothingMovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.overlap.DoNothingOverlapBehavior;

public class DoNothingBehaviorSet extends BehaviorSet {

    public DoNothingBehaviorSet(Entity owningEntity) {
        super(new DoNothingAttackBehavior(owningEntity),
              new DoNothingMovementBehavior(owningEntity),
              new DoNothingDeathBehavior(owningEntity),
              new DoNothingOverlapBehavior(owningEntity),
              new DoNothingCollisionBehavior(owningEntity));
    }

}
