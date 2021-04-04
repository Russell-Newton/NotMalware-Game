package whatexe.dungeoncrawler.entities.behavior.attack.specials;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.player.Player;

import java.util.List;

public class DoNothingSpecialAttack extends SpecialAttackBehavior {
    public DoNothingSpecialAttack(Player owningEntity) {
        super(owningEntity, 0);
    }

    @Override
    public List<? extends Entity> getDefaultAttackEntities() {
        return List.of();
    }

    @Override
    protected void setAttackDirection() {

    }
}
