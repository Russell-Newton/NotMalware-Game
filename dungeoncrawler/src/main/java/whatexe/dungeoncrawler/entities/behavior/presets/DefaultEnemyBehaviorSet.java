package whatexe.dungeoncrawler.entities.behavior.presets;

import whatexe.dungeoncrawler.entities.behavior.BehaviorSet;
import whatexe.dungeoncrawler.entities.behavior.attack.DoNothingAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.collision.EnemyCollisionBehavior;
import whatexe.dungeoncrawler.entities.behavior.death.ItemDropDeathBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.WanderMovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.overlap.EnemyOverlapBehavior;
import whatexe.dungeoncrawler.entities.behavior.transformations.DropCurrencyOnDeathTransformation;
import whatexe.dungeoncrawler.entities.enemies.Enemy;

public class DefaultEnemyBehaviorSet extends BehaviorSet {
    public DefaultEnemyBehaviorSet(Enemy owningEntity) {
        super(new DoNothingAttackBehavior(owningEntity),
              new WanderMovementBehavior(owningEntity),
              new ItemDropDeathBehavior(owningEntity,
                                        0.167,
                                        false),
              new EnemyOverlapBehavior(owningEntity),
              new EnemyCollisionBehavior(owningEntity));

        addBehaviorTransformation(
                new DropCurrencyOnDeathTransformation(owningEntity,
                                                      0.25));
    }
}
