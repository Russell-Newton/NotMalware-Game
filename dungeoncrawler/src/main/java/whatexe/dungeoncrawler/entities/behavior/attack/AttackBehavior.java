package whatexe.dungeoncrawler.entities.behavior.attack;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.EntityBehavior;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.List;

public abstract class AttackBehavior<T extends Entity> extends EntityBehavior<T> {

    protected int ticksSinceAttack;
    protected Vector attackDirection = new Vector(0, 0);

    public AttackBehavior(T owningEntity) {
        super(owningEntity);
    }

    /**
     *
     * @return a list of entities to be added to the room.
     */
    public List<? extends Entity> attack() {

        ticksSinceAttack = Math.max(0, ticksSinceAttack - 1);
        if (ticksSinceAttack > 0) {
            return List.of();
        }

        setAttackDirection();

        if (!attackDirection.isZero()) {
            ticksSinceAttack =
                    (int) (owningEntity.getEntityStatistics().getModifiedAttackDelay());
            return getDefaultAttackEntities();
        }
        return List.of();
    }

    protected abstract List<? extends Entity> getDefaultAttackEntities();

    protected abstract void setAttackDirection();

}
