package whatexe.dungeoncrawler.entities.items;

import whatexe.dungeoncrawler.entities.effects.StatusEffect;

public interface Consumable {

    /**
     * @return a {@link StatusEffect} that should be applied to the consuming entity.
     */
    StatusEffect getConsumeEffect();

}
