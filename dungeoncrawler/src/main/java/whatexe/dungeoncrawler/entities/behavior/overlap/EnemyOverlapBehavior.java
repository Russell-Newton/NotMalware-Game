package whatexe.dungeoncrawler.entities.behavior.overlap;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

public class EnemyOverlapBehavior extends DamageOnOverlapBehavior<Enemy> {
    public EnemyOverlapBehavior(Enemy owningEntity) {
        super(owningEntity);
    }

    @Override
    public List<? extends Entity> getPossibleOverlapTargets() {
        ArrayList<Entity> list = new ArrayList<>(owningEntity.getOwningRoom().getFriends());
        if (owningEntity.getOwningRoom().getPlayer() != null) {
            list.add(owningEntity.getOwningRoom().getPlayer());
        }
        return list;
    }
}
