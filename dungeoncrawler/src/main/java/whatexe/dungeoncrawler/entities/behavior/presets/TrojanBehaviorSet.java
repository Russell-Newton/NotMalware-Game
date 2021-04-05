package whatexe.dungeoncrawler.entities.behavior.presets;

import whatexe.dungeoncrawler.entities.behavior.attack.TrojanAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.attack.specials.TrojanSpecialAttackBehavior;
import whatexe.dungeoncrawler.entities.player.Player;

public class TrojanBehaviorSet extends PlayerBehaviorSet {
    public TrojanBehaviorSet(Player owner) {
        super(owner, new TrojanSpecialAttackBehavior(owner, 4));

        attackBehaviorProperty().set(new TrojanAttackBehavior(owner));
    }
}
