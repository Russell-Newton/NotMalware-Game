package whatexe.dungeoncrawler.entities.effects;

import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.miscellaneous.Particle;

public class HealthBoostEffect extends StatusEffect {
    public HealthBoostEffect(Entity owningEntity) {
        super(owningEntity, 6000);  // last 30 seconds
    }

    @Override
    protected void onAdd() {
        owningEntity.getEntityStatistics()
                    .setMaxHealth(owningEntity.getEntityStatistics().getMaxHealth() + 30);
        owningEntity.adjustHealth(30);
    }

    @Override
    protected void onRemove() {
        owningEntity.getEntityStatistics()
                    .setMaxHealth(owningEntity.getEntityStatistics().getMaxHealth() - 30);
        owningEntity.adjustHealth(0);
    }

    @Override
    protected void onTick() {

    }

    @Override
    protected Particle getDefaultParticle() {
        return new Particle(owningEntity.getOwningRoom(),
                            5,
                            Color.ORANGERED.darker(),
                            40);
    }
}
