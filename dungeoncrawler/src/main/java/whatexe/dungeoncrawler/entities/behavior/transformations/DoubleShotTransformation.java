package whatexe.dungeoncrawler.entities.behavior.transformations;

import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.projectiles.PlayerProjectile;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;

import java.util.ArrayList;
import java.util.List;

public class DoubleShotTransformation extends BehaviorTransformation<Entity> {
    public DoubleShotTransformation(Entity owningEntity) {
        super(owningEntity, 5);
    }

    @Override
    public void onAdd() {

    }

    @Override
    public void onRemove() {

    }

    @Override
    public List<? extends Entity> transformAttack(List<? extends Entity> inputEntities) {
        List<Entity> out = new ArrayList<>();
        for (Entity entity : inputEntities) {
            if (entity instanceof Projectile) {
                Projectile casted = (Projectile) entity;
                Vector movement = casted.getBehaviorSet().getMovement();
                Vector shift = new Vector(0, casted.getHitbox().getHeight() / 2);

                Vector oldPosition = new Vector(casted.getDisplayNode().getTranslateX(),
                                                casted.getDisplayNode().getTranslateY());
                Vector newPosition1
                        = oldPosition.plus(shift.rotatedBy(movement.angleTo(new Vector(1, 0))));
                Vector newPosition2
                        = oldPosition.plus(shift.scaledBy(-1).rotatedBy(
                        movement.angleTo(new Vector(1, 0))));

                casted.setEntityPosition(newPosition1.get(0), newPosition1.get(1));

                Projectile newShot = new PlayerProjectile(
                        Sprite.asCircle((int) (casted.getDisplayNode().getWidth() / 2),
                                        Color.BLACK),
                        casted.getOwningRoom(),
                        casted.getBehaviorSet().getMovement(),
                        casted.getLifetime(),
                        casted.getEntityStatistics()
                );

                newShot.setEntityPosition(newPosition2.get(0), newPosition2.get(1));
                out.add(entity);
                out.add(newShot);
            }
        }
        return out;
    }

    @Override
    public Vector transformMovement(Vector inputMovement) {
        return inputMovement;
    }

    @Override
    public void postDeath() {

    }

    @Override
    public void postHandleEntityOverlap(Entity otherEntity) {

    }

    @Override
    public void postHandleEntityCollision(Entity otherEntity) {

    }

    @Override
    public void postHandleBoundaryCollision() {

    }
}
