package whatexe.dungeoncrawler.entities.behavior.death;

import whatexe.dungeoncrawler.entities.Entity;

public class RemoveOnDeathBehavior extends DeathBehavior<Entity> {

    public RemoveOnDeathBehavior(Entity owningEntity) {
        super(owningEntity);
    }

    @Override
    public void die() {
        owningEntity.getOwningRoom().removeEntity(owningEntity);
    }
}
