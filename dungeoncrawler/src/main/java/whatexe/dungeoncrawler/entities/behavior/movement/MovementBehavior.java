package whatexe.dungeoncrawler.entities.behavior.movement;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.EntityBehavior;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

public abstract class MovementBehavior<T extends Entity> extends EntityBehavior<T> {

    public MovementBehavior(T owningEntity) {
        super(owningEntity);
    }

    public abstract Vector getMovement();

}
