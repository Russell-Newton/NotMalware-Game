package whatexe.dungeoncrawler.entities.behavior.attack;

import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.player.Player;
import whatexe.dungeoncrawler.entities.projectiles.PlayerProjectile;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;

import java.util.List;

public class VirusAttackBehavior extends PlayerAttackBehavior {
    public VirusAttackBehavior(Player owningEntity) {
        super(owningEntity);
    }

    public static EntityStatistics getDefaultProjectileStatistics(Entity owningEntity) {
        return new EntityStatistics(0,
                                    owningEntity.getEntityStatistics().getModifiedAttackDamage(),
                                    1,
                                    2,
                                    1,
                                    1,
                                    1);
    }

    public static Sprite getDefaultDisplayNode() {
        return Sprite.asCircle(7, Color.BLACK);
    }

    public static int getDefaultLifetime() {
        return 80;
    }

    @Override
    public List<? extends Entity> getDefaultAttackEntities() {
        Projectile bullet =
                new PlayerProjectile(getDefaultDisplayNode(),
                                     owningEntity.getOwningRoom(),
                                     attackDirection,
                                     getDefaultLifetime(),
                                     getDefaultProjectileStatistics(owningEntity));
        bullet.setEntityPosition(owningEntity.getDisplayNode().getBoundsInParent().getCenterX()
                                         - bullet.getDisplayNode().getWidth() / 2,
                                 owningEntity.getDisplayNode().getBoundsInParent().getCenterY()
                                         - bullet.getDisplayNode().getHeight() / 2);

        // rotate attackDirection by -15 degrees
        Vector direction1 = Vector.rotateBy(attackDirection, -Math.PI / 12);
        Projectile bullet1 =
                new PlayerProjectile(getDefaultDisplayNode(),
                                     owningEntity.getOwningRoom(),
                                     direction1,
                                     getDefaultLifetime(),
                                     getDefaultProjectileStatistics(owningEntity));
        bullet1.setEntityPosition(owningEntity.getDisplayNode().getBoundsInParent().getCenterX()
                                          - bullet.getDisplayNode().getWidth() / 2,
                                  owningEntity.getDisplayNode().getBoundsInParent().getCenterY()
                                          - bullet.getDisplayNode().getHeight() / 2);

        // rotate attackDirection by +15 degrees
        Vector direction2 = Vector.rotateBy(attackDirection, Math.PI / 12);
        Projectile bullet2 =
                new PlayerProjectile(getDefaultDisplayNode(),
                                     owningEntity.getOwningRoom(),
                                     direction2,
                                     getDefaultLifetime(),
                                     getDefaultProjectileStatistics(owningEntity));
        bullet2.setEntityPosition(owningEntity.getDisplayNode().getBoundsInParent().getCenterX()
                                          - bullet.getDisplayNode().getWidth() / 2,
                                  owningEntity.getDisplayNode().getBoundsInParent().getCenterY()
                                          - bullet.getDisplayNode().getHeight() / 2);


        return List.of(bullet, bullet1, bullet2);
    }
}
