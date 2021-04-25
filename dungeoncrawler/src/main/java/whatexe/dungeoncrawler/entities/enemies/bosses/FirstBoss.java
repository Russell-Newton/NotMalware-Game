package whatexe.dungeoncrawler.entities.enemies.bosses;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.AnimatedSprite;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.behavior.attack.AttackNearestTargetBehavior;
import whatexe.dungeoncrawler.entities.enemies.Enemy;
import whatexe.dungeoncrawler.entities.projectiles.EnemyProjectile;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.List;

public class FirstBoss extends Boss {

    /*
    Synchronize the attacks with the music. 250 ms long beats, with 5 ms tick will burst 4 on
    beat. Burst every 2 bars (bars are 2000 ms long).
    */
    private static final int BURST_DELAY_INTERVAL = 250 / 5;
    private static final int BURST_COUNT = 4;
    private static final int PAUSE_INTERVAL = 3000 / 5 + BURST_DELAY_INTERVAL;

    public FirstBoss(Room owningRoom) {
        super(defaultSprite(), owningRoom, defaultStatistics());
        getEntityStatistics().setAttackDelay(2000 / 5);
        getBehaviorSet().setAttackBehavior(new FirstBossAttackBehavior(this));
    }

    private static Sprite defaultSprite() {
        Sprite sprite = new AnimatedSprite(
                new Image(Enemy.class.getResourceAsStream("floppy_disk.png")),
                128,
                128,
                4,
                4,
                1,
                1000);
        return sprite;
    }

    private static EntityStatistics defaultStatistics() {
        return new EntityStatistics(100,
                                    1,
                                    1,
                                    1);
    }

    @Override
    public int getDanger() {
        return 10;
    }

    private static class FirstBossAttackBehavior extends AttackNearestTargetBehavior {

        private int currentBurstCount = -1;

        public FirstBossAttackBehavior(FirstBoss owningEntity) {
            super(owningEntity,
                  owningEntity.getBehaviorSet().getOverlapBehavior()::getPossibleOverlapTargets);
        }

        @Override
        public List<? extends Entity> getDefaultAttackEntities() {
            if (currentBurstCount == -1) {
                currentBurstCount = 1;
                owningEntity.getEntityStatistics().setAttackDelay(BURST_DELAY_INTERVAL);
                return List.of();
            }
            if (currentBurstCount == 0) {
                owningEntity.getEntityStatistics().setAttackDelay(BURST_DELAY_INTERVAL);
            }
            currentBurstCount++;
            if (currentBurstCount == BURST_COUNT) {
                currentBurstCount = 0;
                owningEntity.getEntityStatistics().setAttackDelay(PAUSE_INTERVAL);
            }
            return List.of(getBullet());
        }

        private Projectile getBullet() {
            EntityStatistics bulletStats = new EntityStatistics();
            bulletStats.setSpeed(4);
            bulletStats.setAttackDamage(owningEntity.getEntityStatistics()
                                                    .getModifiedAttackDamage());
            Projectile bullet = new EnemyProjectile(Sprite.asCircle(8, Color.HOTPINK),
                                                    owningEntity.getOwningRoom(),
                                                    attackDirection,
                                                    2000,
                                                    bulletStats);

            bullet.setEntityPosition(owningEntity.getDisplayNode()
                                                 .getBoundsInParent()
                                                 .getCenterX(),
                                     owningEntity.getDisplayNode()
                                                 .getBoundsInParent()
                                                 .getCenterY());
            return bullet;
        }
    }
}
