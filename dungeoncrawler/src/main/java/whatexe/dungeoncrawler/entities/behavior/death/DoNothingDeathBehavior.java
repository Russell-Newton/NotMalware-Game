package whatexe.dungeoncrawler.entities.behavior.death;

import whatexe.dungeoncrawler.entities.Entity;

public class DoNothingDeathBehavior extends DeathBehavior<Entity> {
    public DoNothingDeathBehavior(Entity owningEntity) {
        super(owningEntity);
    }

    @Override
    public void die() {
    }
}
