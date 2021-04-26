package whatexe.dungeoncrawler.entities.enemies.bosses;

import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.behavior.death.RemoveOnDeathBehavior;
import whatexe.dungeoncrawler.entities.enemies.Enemy;
import whatexe.dungeoncrawler.layout.rooms.Room;

public abstract class Boss extends Enemy {
    public Boss(Sprite displayNode,
                Room owningRoom,
                EntityStatistics entityStatistics) {
        super(displayNode,
              owningRoom,
              entityStatistics);
        canTick = true;
        getEntityStatistics()
                .setMaxHealth(getEntityStatistics().getMaxHealth()
                                      * owningRoom.getOwningLevel().getMaxDanger());
        setHealth(getEntityStatistics().getMaxHealth());
        getEntityStatistics()
                .setAttackDamage(owningRoom.getOwningLevel().getDifficulty().ordinal()
                                         + owningRoom.getOwningLevel().getDepth()
                                         + 2);
        getBehaviorSet().setDeathBehavior(new RemoveOnDeathBehavior(this));
    }
}
