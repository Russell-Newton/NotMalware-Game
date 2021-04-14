package whatexe.dungeoncrawler.entities.behavior.attack.specials;

import whatexe.dungeoncrawler.entities.AnimatedSprite;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.BehaviorSet;
import whatexe.dungeoncrawler.entities.behavior.attack.AttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.attack.AttackNearestTargetBehavior;
import whatexe.dungeoncrawler.entities.behavior.attack.WormAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.collision.DoNothingCollisionBehavior;
import whatexe.dungeoncrawler.entities.behavior.death.RemoveOnDeathBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.MovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.WanderMovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.overlap.DoNothingOverlapBehavior;
import whatexe.dungeoncrawler.entities.friends.Friend;
import whatexe.dungeoncrawler.entities.player.Player;
import whatexe.dungeoncrawler.entities.projectiles.PlayerProjectile;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;

import java.util.List;

public class WormSpecialAttackBehavior extends SpecialAttackBehavior {
    public WormSpecialAttackBehavior(Player owningEntity, int maxRecharge) {
        super(owningEntity, maxRecharge);
    }

    @Override
    public List<? extends Entity> getDefaultAttackEntities() {
        return List.of(new DummyPlayer());
    }

    @Override
    protected void setAttackDirection() {
    }

    private class DummyPlayer extends Friend {

        private int lifetime = 2000;    // 5 ms * 2000 = 10 seconds

        public DummyPlayer() {
            super(AnimatedSprite.copyFrom((AnimatedSprite) owningEntity.getDisplayNode()),
                  owningEntity.getOwningRoom());
            canTick = true;

            setEntityPosition(owningEntity.getDisplayNode().getTranslateX(),
                              owningEntity.getDisplayNode().getTranslateY());

            entityStatistics.copyFrom(owningEntity.getEntityStatistics());
            entityStatistics.setMaxHealth(getEntityStatistics().getMaxHealth() * 10);
            entityStatistics.setAttackDelay(
                    5 * owningEntity.getEntityStatistics().getAttackDelay());
            entityStatistics.setAttackSpeedModifier(owningEntity.getEntityStatistics()
                                                                .getAttackSpeedModifier());
            entityStatistics.setAttackDamage(
                    owningEntity.getEntityStatistics().getAttackDamage() / 2);

            behaviorSetProperty().set(
                    new BehaviorSet(getDefaultAttackBehavior(),
                                    getDefaultMovementBehavior(),
                                    new RemoveOnDeathBehavior(this),
                                    new DoNothingOverlapBehavior(this),
                                    new DoNothingCollisionBehavior(this)));
        }

        @Override
        public void tick() {
            super.tick();
            if (lifetime > 0) {
                lifetime--;
            }
            if (lifetime == 0) {
                setHealth(-1);
            }
        }

        public MovementBehavior<Entity> getDefaultMovementBehavior() {
            return new WanderMovementBehavior(this);
        }

        public AttackBehavior<Entity> getDefaultAttackBehavior() {
            return new AttackNearestTargetBehavior(this,
                                                   owningRoom::getEnemies) {
                @Override
                public List<? extends Entity> getDefaultAttackEntities() {
                    Projectile bullet = new PlayerProjectile(
                            WormAttackBehavior.getDefaultDisplayNode(),
                            owningRoom,
                            attackDirection,
                            WormAttackBehavior.getDefaultLifetime(),
                            WormAttackBehavior.getDefaultProjectileStatistics(owningEntity));
                    bullet.setEntityPosition(owningEntity.getDisplayNode()
                                                         .getBoundsInParent()
                                                         .getCenterX(),
                                             owningEntity.getDisplayNode()
                                                         .getBoundsInParent()
                                                         .getCenterY());
                    return List.of(bullet);
                }
            };
        }
    }
}
