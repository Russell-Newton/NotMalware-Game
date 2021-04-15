package whatexe.dungeoncrawler;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.attack.AttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.transformations.BehaviorTransformation;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class BehaviorModifiersTest extends ApplicationTest {

    @Test
    public void testModifierPriority() {
        interact(() -> {
            DummyEntity dummy = new DummyEntity();

            DummyEntity transformedAttack = (DummyEntity) dummy.getBehaviorSet().attack().get(0);
            assertEquals(transformedAttack.transformations, "");

            dummy.getBehaviorSet()
                 .addBehaviorTransformation(new DummyTransformation(dummy, 1, "1"));
            dummy.getBehaviorSet()
                 .addBehaviorTransformation(new DummyTransformation(dummy, 2, "2"));
            transformedAttack = (DummyEntity) dummy.getBehaviorSet().attack().get(0);
            assertEquals(transformedAttack.transformations, "21");

            dummy.getBehaviorSet()
                 .addBehaviorTransformation(new DummyTransformation(dummy, -1, "3"));
            transformedAttack = (DummyEntity) dummy.getBehaviorSet().attack().get(0);
            assertEquals(transformedAttack.transformations, "213");
        });
    }

    @Test
    public void testModifierReusability() {
        interact(() -> {
            DummyEntity dummy = new DummyEntity();

            dummy.getBehaviorSet()
                 .addBehaviorTransformation(new DummyTransformation(dummy, 0, "1"));
            DummyEntity transformedAttack = (DummyEntity) dummy.getBehaviorSet().attack().get(0);
            assertEquals(transformedAttack.transformations, "1");
            assertEquals(dummy.transformations, "");

            dummy.getBehaviorSet().die();
            assertEquals(dummy.transformations, "dead");
        });
    }

    private static class DummyAttackBehavior extends AttackBehavior<DummyEntity> {
        public DummyAttackBehavior(DummyEntity owningEntity) {
            super(owningEntity);
        }

        @Override
        protected List<? extends Entity> getDefaultAttackEntities() {
            ticksSinceAttack = 0;
            return List.of(new DummyEntity());
        }

        @Override
        protected void setAttackDirection() {
            attackDirection = new Vector(1, 0);
        }
    }

    private static class DummyEntity extends Entity {

        private String transformations;

        public DummyEntity() {
            super(Sprite.asRectangle(1, 1, Color.BLACK), null);
            transformations = "";

            getBehaviorSet().setAttackBehavior(new DummyAttackBehavior(this));
        }

        private void appendToTransformations(String transformation) {
            transformations += transformation;
        }
    }

    private static class DummyTransformation extends BehaviorTransformation<DummyEntity> {

        private final String toAdd;

        public DummyTransformation(DummyEntity owningEntity, int priority, String toAdd) {
            super(owningEntity, priority);
            this.toAdd = toAdd;
        }

        @Override
        public void onAdd() {

        }

        @Override
        public void onRemove() {

        }

        @Override
        public List<? extends Entity> transformAttack(List<? extends Entity> inputEntities) {
            // System.out.print(inputEntities);
            DummyEntity entity = (DummyEntity) inputEntities.get(0);
            entity.appendToTransformations(toAdd);
            // System.out.println(inputEntities);
            return List.of(entity);
        }

        @Override
        public Vector transformMovement(Vector inputMovement) {
            return inputMovement;
        }

        @Override
        public void postDeath() {
            owningEntity.appendToTransformations("dead");
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

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("DummyTransformation{");
            sb.append("priority=").append(priority);
            sb.append("toAdd='").append(toAdd).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

}
