package whatexe.dungeoncrawler.entities.behavior.attack.specials;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.effects.InvisibilityEffect;
import whatexe.dungeoncrawler.entities.player.Player;

import java.util.List;

public class TrojanSpecialAttackBehavior extends SpecialAttackBehavior {
    public TrojanSpecialAttackBehavior(Player owningEntity,
                                       int maxRecharge) {
        super(owningEntity, maxRecharge);
    }

    @Override
    protected List<? extends Entity> getDefaultAttackEntities() {
        // TODO - Activate invisibility
        owningEntity.getStatusEffects().add(new InvisibilityEffect(owningEntity, 3000));
        return List.of();
    }

    @Override
    protected void setAttackDirection() {

    }
}
