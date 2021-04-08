package whatexe.dungeoncrawler.entities.behavior.attack;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.player.Player;
import whatexe.dungeoncrawler.entities.projectiles.PlayerProjectile;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;

import java.util.List;

public class PlayerAttackBehavior extends AttackBehavior<Player> {

    private boolean attackingUp;
    private boolean attackingLeft;
    private boolean attackingRight;
    private boolean attackingDown;

    public PlayerAttackBehavior(Player owningEntity) {
        super(owningEntity);
    }

    @Override
    public List<? extends Entity> getDefaultAttackEntities() {
        Projectile bullet =
                new PlayerProjectile(owningEntity.getOwningRoom(),
                                     5,
                                     attackDirection,
                                     (int) (owningEntity.getEntityStatistics()
                                                        .getModifiedAttackDamage()),
                                     2);
        bullet.setEntityPosition(owningEntity.getDisplayNode().getBoundsInParent().getCenterX()
                                         - bullet.getDisplayNode().getWidth() / 2,
                                 owningEntity.getDisplayNode().getBoundsInParent().getCenterY()
                                         - bullet.getDisplayNode().getHeight() / 2);
        return List.of(bullet);
    }

    @Override
    protected void setAttackDirection() {
        int dx = 0;
        int dy = 0;
        if (attackingUp) {
            dy--;
        }
        if (attackingDown) {
            dy++;
        }
        if (attackingRight) {
            dx++;
        }
        if (attackingLeft) {
            dx--;
        }

        attackDirection = new Vector(dx, dy);
    }

    public void setAttackingUp(boolean attackingUp) {
        this.attackingUp = attackingUp;
    }

    public void setAttackingLeft(boolean attackingLeft) {
        this.attackingLeft = attackingLeft;
    }

    public void setAttackingRight(boolean attackingRight) {
        this.attackingRight = attackingRight;
    }

    public void setAttackingDown(boolean attackingDown) {
        this.attackingDown = attackingDown;
    }
}
