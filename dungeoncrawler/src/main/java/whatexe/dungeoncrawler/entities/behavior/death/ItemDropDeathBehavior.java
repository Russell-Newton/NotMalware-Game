package whatexe.dungeoncrawler.entities.behavior.death;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.EntityChooser;
import whatexe.dungeoncrawler.entities.items.Currency;
import whatexe.dungeoncrawler.entities.items.Item;

/**
 * Includes the chance to drop an item after death.
 */
public class ItemDropDeathBehavior extends RemoveOnDeathBehavior {

    protected final double dropChance;
    protected final boolean includeModifiers;

    public ItemDropDeathBehavior(Entity owningEntity, double dropChance, boolean includeModifiers) {
        super(owningEntity);

        this.dropChance = dropChance;
        this.includeModifiers = includeModifiers;
    }

    @Override
    public void die() {
        if (Math.random() < dropChance) {
            Item drop = null;
            while (drop == null || drop instanceof Currency) {
                drop = EntityChooser.getInstance()
                                    .getRandomItem(owningEntity.getOwningRoom(),
                                                   includeModifiers);
            }
            owningEntity.getOwningRoom().addEntity(drop);

            drop.setEntityPosition(owningEntity.getDisplayNode().getTranslateX(),
                                   owningEntity.getDisplayNode().getTranslateY());
        }
        super.die();
    }
}
