package whatexe.dungeoncrawler.entities.behavior.overlap;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.items.Item;
import whatexe.dungeoncrawler.entities.player.Player;

public class CurrencyOverlapBehavior extends ItemOverlapBehavior {
    public CurrencyOverlapBehavior(Item owningEntity) {
        super(owningEntity);
    }

    @Override
    public void handleOverlap(Entity otherEntity) {
        Player other = (Player) otherEntity;
        other.adjustMoney(5);
        owningEntity.setHealth(-1);
    }
}
