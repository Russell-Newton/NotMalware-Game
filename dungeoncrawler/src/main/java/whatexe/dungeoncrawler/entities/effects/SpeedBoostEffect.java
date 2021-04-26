package whatexe.dungeoncrawler.entities.effects;

import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.miscellaneous.Particle;

public class SpeedBoostEffect extends StatusEffect {
    public SpeedBoostEffect(Entity owningEntity, int lifetime) {
        super(owningEntity, lifetime);
    }

    @Override
    protected void onAdd() {
        owningEntity.getEntityStatistics()
                    .setSpeedModifier(owningEntity.getEntityStatistics()
                                                         .getSpeedModifier() * 2);
    }

    @Override
    protected void onRemove() {
        owningEntity.getEntityStatistics()
                    .setSpeedModifier(owningEntity.getEntityStatistics()
                                                         .getSpeedModifier() * .5);
    }

    @Override
    protected void onTick() {
    }

    @Override
    protected Particle getDefaultParticle() {
        return new Particle(owningEntity.getOwningRoom(), 5, Color.LIGHTGREEN, 40);
    }
}
