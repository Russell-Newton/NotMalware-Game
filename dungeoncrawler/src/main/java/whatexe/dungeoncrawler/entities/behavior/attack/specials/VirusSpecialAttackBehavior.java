package whatexe.dungeoncrawler.entities.behavior.attack.specials;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.effects.ThornsEffect;
import whatexe.dungeoncrawler.entities.player.Player;

import java.util.List;

public class VirusSpecialAttackBehavior extends SpecialAttackBehavior {
    public VirusSpecialAttackBehavior(Player owningEntity,
                                      int maxRecharge) {
        super(owningEntity, maxRecharge);
    }

    @Override
    protected List<? extends Entity> getDefaultAttackEntities() {
        // TODO - activate thorns
        owningEntity.getStatusEffects().add(new ThornsEffect(owningEntity, 3000));
        return List.of();
    }

    @Override
    protected void setAttackDirection() {

    }
}
