package whatexe.dungeoncrawler.entities.effects;

import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.miscellaneous.Particle;

public class AttackBoostEffect extends StatusEffect {

    public AttackBoostEffect(Entity owningEntity, int lifetime) {
        super(owningEntity, lifetime);
    }

    @Override
    protected void onAdd() {
        owningEntity.getEntityStatistics()
                .setAttackDamageModifier(owningEntity.getEntityStatistics()
                                                     .getAttackDamageModifier() * 2);

    }

    @Override
    protected void onRemove() {
        owningEntity.getEntityStatistics()
                .setAttackDamageModifier(owningEntity.getEntityStatistics()
                                                     .getAttackDamageModifier() * .5);
    }

    @Override
    protected void onTick() {
    }

    @Override
    protected Particle getDefaultParticle() {
        return new Particle(owningEntity.getOwningRoom(), 5, Color.DEEPSKYBLUE, 40);
    }
}
