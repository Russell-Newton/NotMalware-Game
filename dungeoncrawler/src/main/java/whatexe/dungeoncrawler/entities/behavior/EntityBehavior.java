package whatexe.dungeoncrawler.entities.behavior;

import whatexe.dungeoncrawler.entities.Entity;

public abstract class EntityBehavior<T extends Entity> {

    protected final T owningEntity;

    public EntityBehavior(T owningEntity) {
        this.owningEntity = owningEntity;
    }

}
