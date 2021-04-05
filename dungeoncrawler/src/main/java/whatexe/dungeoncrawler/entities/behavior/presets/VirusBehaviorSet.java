package whatexe.dungeoncrawler.entities.behavior.presets;

import whatexe.dungeoncrawler.entities.behavior.attack.VirusAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.attack.specials.VirusSpecialAttackBehavior;
import whatexe.dungeoncrawler.entities.player.Player;

public class VirusBehaviorSet extends PlayerBehaviorSet {
    public VirusBehaviorSet(Player owner) {
        super(owner, new VirusSpecialAttackBehavior(owner, 3));

        attackBehaviorProperty().set(new VirusAttackBehavior(owner));
    }
}
