package whatexe.dungeoncrawler.entities.behavior.presets;

import whatexe.dungeoncrawler.entities.player.Player;
import whatexe.dungeoncrawler.entities.behavior.attack.WormAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.attack.specials.WormSpecialAttackBehavior;

public class WormBehaviorSet extends PlayerBehaviorSet {
    public WormBehaviorSet(Player owner) {
        super(owner, new WormSpecialAttackBehavior(owner, 5));

        attackBehaviorProperty().set(new WormAttackBehavior(owner));
    }
}
