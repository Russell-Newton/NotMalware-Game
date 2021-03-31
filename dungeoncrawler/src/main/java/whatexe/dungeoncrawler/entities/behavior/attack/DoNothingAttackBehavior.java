package whatexe.dungeoncrawler.entities.behavior.attack;

import whatexe.dungeoncrawler.entities.Entity;

import java.util.List;

public class DoNothingAttackBehavior extends AttackBehavior<Entity> {
    public DoNothingAttackBehavior(Entity owningEntity) {
        super(owningEntity);
    }

    @Override
    public List<? extends Entity> getDefaultAttackEntities() {
        return List.of();
    }

    @Override
    protected void setAttackDirection() {

    }
}
