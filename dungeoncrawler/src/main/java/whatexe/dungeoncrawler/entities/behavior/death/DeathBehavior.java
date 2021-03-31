package whatexe.dungeoncrawler.entities.behavior.death;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.EntityBehavior;

public abstract class DeathBehavior<T extends Entity> extends EntityBehavior<T> {

    public DeathBehavior(T owningEntity) {
        super(owningEntity);
    }

    public abstract void die();

}
