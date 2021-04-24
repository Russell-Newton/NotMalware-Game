package whatexe.dungeoncrawler.entities.enemies;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.AnimatedSprite;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.behavior.attack.AttackNearestTargetBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.WanderMovementBehavior;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.projectiles.EnemyProjectile;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.List;

public class ShieldEnemy extends Enemy {

    public ShieldEnemy(Room owningRoom) {
        super(new AnimatedSprite(
                new Image(Enemy.class.getResourceAsStream("recycle_bin.png")),
                32,
                32,
                4,
                4,
                1,
                1000), 50, owningRoom);

        entityStatistics.setAttackDamage(5);
        entityStatistics.setAttackDelay(400);
        entityStatistics.setSpeed(0.5);

        // Set motion
        Vector roomCenter = new Vector(owningRoom.getBackgroundImage().getWidth() / 2,
                                       owningRoom.getBackgroundImage().getHeight() / 2);
        Vector toCenter =
                roomCenter.minus(new Vector(getDisplayNode().getBoundsInParent().getCenterX(),
                                            getDisplayNode().getBoundsInParent().getCenterY()));
        int xScale = toCenter.dot(new Vector(1, 0)) > 0 ? -1 : 1;
        int yScale = toCenter.dot(new Vector(0, 1)) > 0 ? -1 : 1;

        getBehaviorSet().setMovementBehavior(
                new WanderMovementBehavior(this, 400));

        getBehaviorSet().setAttackBehavior(new AttackNearestTargetBehavior(
                this,
                getBehaviorSet().getOverlapBehavior()::getPossibleOverlapTargets) {

            @Override
            public List<? extends Entity> getDefaultAttackEntities() {
                EntityStatistics bulletStats = new EntityStatistics();
                bulletStats.setSpeed(1.25);
                bulletStats.setAttackDamage(owningEntity.getEntityStatistics()
                                                        .getModifiedAttackDamage());

                Projectile bullet = new EnemyProjectile(
                        Sprite.asCircle(7, Color.DARKRED),
                        owningRoom,
                        attackDirection,
                        300,
                        bulletStats);

                bullet.setEntityPosition(owningEntity.getDisplayNode()
                                                     .getBoundsInParent()
                                                     .getCenterX(),
                                         owningEntity.getDisplayNode()
                                                     .getBoundsInParent()
                                                     .getCenterY());
                return List.of(bullet);
            }
        });
    }

    @Override
    public int getDanger() {
        return 2;
    }
}
