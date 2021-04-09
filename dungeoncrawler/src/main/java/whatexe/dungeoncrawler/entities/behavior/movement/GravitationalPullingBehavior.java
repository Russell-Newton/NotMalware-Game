package whatexe.dungeoncrawler.entities.behavior.movement;

import javafx.beans.property.SimpleDoubleProperty;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.List;
import java.util.function.Supplier;

public class GravitationalPullingBehavior extends MovementBehavior<Entity> {

    private final Supplier<List<? extends Entity>> targetsSupplier;
    private final SimpleDoubleProperty pullMultiplier;
    private Vector currentMovement;

    public GravitationalPullingBehavior(Entity owningEntity,
                                        Supplier<List<? extends Entity>> targetsSupplier,
                                        double pullMultiplier,
                                        Vector initialMovement) {
        super(owningEntity);

        this.targetsSupplier = targetsSupplier;
        this.pullMultiplier = new SimpleDoubleProperty(pullMultiplier);
        currentMovement = initialMovement;
    }

    @Override
    public Vector getMovement() {
        List<? extends Entity> targets = targetsSupplier.get();
        if (targets.size() == 0) {
            return currentMovement;
        }

        Vector pull = new Vector(0, 0);

        for (Entity target : targets) {
            Vector toTarget = owningEntity.vectorToOtherEntity(target);
            double r = toTarget.magnitude();
            pull = pull.plus(toTarget.unit().scaledBy(getPullMultiplier() / (r * r)));
        }
        Vector motion = currentMovement.plus(pull);

        currentMovement = motion.unit()
                                .scaledBy(owningEntity.getEntityStatistics().getModifiedSpeed());

        return currentMovement;
    }


    public double getPullMultiplier() {
        return pullMultiplier.get();
    }

    public void setPullMultiplier(double pullMultiplier) {
        this.pullMultiplier.set(pullMultiplier);
    }

    public SimpleDoubleProperty pullMultiplierProperty() {
        return pullMultiplier;
    }
}
