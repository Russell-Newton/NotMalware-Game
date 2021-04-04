package whatexe.dungeoncrawler.entities.items;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.transformations.BehaviorTransformation;

public interface Equippable {

    /**
     * @return the {@link BehaviorTransformation} to be applied from equipping this Equippable.
     */
    BehaviorTransformation<? extends Entity> getEquipTransformation();

}
