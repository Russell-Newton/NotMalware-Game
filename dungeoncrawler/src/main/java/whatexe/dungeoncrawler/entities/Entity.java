package whatexe.dungeoncrawler.entities;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.*;
import javafx.scene.transform.Scale;
import whatexe.dungeoncrawler.entities.behavior.BehaviorSet;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.behavior.presets.DoNothingBehaviorSet;
import whatexe.dungeoncrawler.entities.effects.StatusEffect;
import whatexe.dungeoncrawler.entities.effects.ThornsEffect;
import whatexe.dungeoncrawler.entities.motionsupport.AABbTree;
import whatexe.dungeoncrawler.entities.motionsupport.BoundaryRectangle;
import whatexe.dungeoncrawler.entities.motionsupport.Line;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class Entity {

    protected final SimpleObjectProperty<Rectangle2D> hitbox;
    protected final SimpleIntegerProperty health;
    protected final SimpleObjectProperty<BehaviorSet> behaviorSet;
    protected final EntityStatistics entityStatistics;
    protected final ObservableList<StatusEffect> statusEffects;
    private final SimpleObjectProperty<Sprite> displayNode;
    private final SimpleDoubleProperty displayScale;
    protected Room owningRoom;
    protected boolean canTick;

    public Entity(Sprite displayNode, Room owningRoom) {
        this(displayNode, owningRoom, new EntityStatistics());
    }

    public Entity(Sprite displayNode, Room owningRoom, EntityStatistics entityStatistics) {
        this(displayNode, owningRoom, 0, entityStatistics);
    }

    public Entity(Sprite displayNode, Room owningRoom, int startingHealth) {
        this(displayNode,
             new Rectangle2D(displayNode.getTranslateX(),
                             displayNode.getTranslateY(),
                             displayNode.getWidth(),
                             displayNode.getHeight()),
             owningRoom,
             startingHealth,
             new EntityStatistics());
    }

    public Entity(Sprite displayNode,
                  Room owningRoom,
                  int startingHealth,
                  EntityStatistics entityStatistics) {
        this(displayNode,
             new Rectangle2D(displayNode.getTranslateX(),
                             displayNode.getTranslateY(),
                             displayNode.getWidth(),
                             displayNode.getHeight()),
             owningRoom,
             startingHealth,
             entityStatistics);
    }

    public Entity(Sprite displayNode, Rectangle2D hitbox, Room owningRoom) {
        this(displayNode, hitbox, owningRoom, 0, new EntityStatistics());
    }

    public Entity(Sprite displayNode, Rectangle2D hitbox, Room owningRoom, int startingHealth,
                  EntityStatistics entityStatistics) {
        this.displayNode = new SimpleObjectProperty<>(displayNode);
        this.hitbox = new SimpleObjectProperty<>(hitbox);
        this.owningRoom = owningRoom;
        this.behaviorSet = new SimpleObjectProperty<>(new DoNothingBehaviorSet(this));
        this.entityStatistics = entityStatistics;
        this.statusEffects = FXCollections.observableArrayList();
        this.displayScale = new SimpleDoubleProperty(1);
        canTick = false;

        health = new SimpleIntegerProperty(startingHealth);
        if (startingHealth == 0) {
            health.set(entityStatistics.getMaxHealth());
        } else {
            entityStatistics.setMaxHealth(startingHealth);
        }
        health.addListener((healthNumber, oldValue, newValue) -> {
            if (newValue.intValue() <= 0) {
                handleDeath();
            }
        });

        this.displayNode.addListener((__, oldValue, newValue) -> {
            updateHitboxPosition();
        });
        getDisplayNode().viewportProperty().addListener((__, oldValue, newValue) -> {
            updateHitboxPosition();
        });
        this.displayScale.addListener((__, oldValue, newValue) -> {
            double oldScale = oldValue.doubleValue();
            double newScale = newValue.doubleValue();
            getDisplayNode().getTransforms().clear();

            // Scale from the point (0, 0)  --  Defaults to scaling from the center, which is bad!
            getDisplayNode().getTransforms().add(new Scale(newScale, newScale, 0, 0));

            // Update hitbox size
            setHitbox(new Rectangle2D(0,
                                      0,
                                      getHitbox().getWidth() * newScale / oldScale,
                                      getHitbox().getHeight() * newScale / oldScale));

            // Update hitbox position
            updateHitboxPosition();
        });

        // Add listeners for updating the hitbox when the getDisplayNode() moves
        this.getDisplayNode().translateXProperty().addListener((__, oldValue, newValue) -> {
            this.getDisplayNode().applyCss();
            updateHitboxPosition();
        });
        this.getDisplayNode().translateYProperty().addListener((__, oldValue, newValue) -> {
            this.getDisplayNode().applyCss();
            updateHitboxPosition();
        });

        updateHitboxPosition();
    }

    public static void forEachPathLine(Path path, Consumer<Line> lineAction) {
        Vector start = new Vector(0, 0);
        Vector previous = new Vector(0, 0);

        for (PathElement element : path.getElements()) {
            Line newLine;
            if (element instanceof MoveTo) {
                MoveTo to = (MoveTo) element;
                start = new Vector(to.getX(), to.getY());
                previous = start;
                continue;
            } else if (element instanceof LineTo) {
                LineTo to = (LineTo) element;
                Vector next = new Vector(to.getX(), to.getY());
                newLine = new Line(previous, next);

                previous = next;
            } else if (element instanceof ClosePath) {
                newLine = new Line(previous, start);

                previous = start;
            } else {
                continue;
            }

            if (newLine.getStart().isAlmost(newLine.getEnd())) {
                continue;
            }

            lineAction.accept(newLine);
        }
    }

    /**
     * Adjust the health of one Entity as an attack (or healing) from another Entity.
     *
     * @param from    the Entity the attack is coming from.
     * @param to      the Entity being attacked.
     * @param dHealth the change in health dealt by the attack.
     */
    public static void adjustHealthAsAttack(Entity from, Entity to, int dHealth) {
        if (to.statusEffects.stream().anyMatch(effect -> effect instanceof ThornsEffect)) {
            if (dHealth == 1) {
                dHealth = 2;
            }
            from.adjustHealth((int) Math.ceil(dHealth / 2.));
            to.adjustHealth((int) Math.ceil(dHealth / 2.));
        } else {
            to.adjustHealth(dHealth);
        }
    }

    public void updateHitboxPosition() {
        getDisplayNode().applyCss();

        if (getHitbox().getWidth() == 0 || getHitbox().getHeight() == 0) {
            setHitbox(new Rectangle2D(getDisplayNode().getTranslateX(),
                                      getDisplayNode().getTranslateY(),
                                      getDisplayNode().getWidth() * getDisplayScale(),
                                      getDisplayNode().getHeight() * getDisplayScale()));
        }

        double dWidth =
                getHitbox().getWidth() - getDisplayNode().getWidth() * getDisplayScale();
        double dHeight =
                getHitbox().getHeight() - getDisplayNode().getHeight() * getDisplayScale();

        setHitbox(new Rectangle2D(getDisplayNode().getTranslateX() - dWidth * 0.5,
                                  getDisplayNode().getTranslateY() - dHeight * 0.5,
                                  getHitbox().getWidth(),
                                  getHitbox().getHeight()));
    }

    protected void handleDeath() {
        getBehaviorSet().die();
    }

    public Sprite getDisplayNode() {
        return displayNode.get();
    }

    public void setDisplayNode(Sprite displayNode) {
        this.displayNode.set(displayNode);
    }

    public SimpleObjectProperty<Sprite> displayNodeProperty() {
        return displayNode;
    }

    public boolean canTick() {
        return canTick;
    }

    public void moveNode(Vector attemptVelocity) {
        if (attemptVelocity.isZero()) {
            return;
        }

        double x = getDisplayNode().getTranslateX();
        double y = getDisplayNode().getTranslateY();

        final double xMin = getDisplayNode().getParent().getLayoutBounds().getMinX();
        final double yMin = getDisplayNode().getParent().getLayoutBounds().getMinY();
        final double xMax = getDisplayNode().getParent().getLayoutBounds().getMaxX()
                - getDisplayNode().getLayoutBounds().getWidth();
        final double yMax = getDisplayNode().getParent().getLayoutBounds().getMaxY()
                - getDisplayNode().getLayoutBounds().getHeight();

        updateHitboxPosition();

        AABbTree.ConstrainedVelocityData constrainedVelocityData =
                owningRoom.getConstrainedVelocity(this, attemptVelocity);
        Vector constrainedVelocity = constrainedVelocityData.getConstrainedVelocity();
        List<Entity> collidedWith = constrainedVelocityData.getCollidedWith();
        if (collidedWith.size() > 0) {
            if (collidedWith instanceof BoundaryRectangle) {
                getBehaviorSet().handleBoundaryCollision();
            } else {
                for (Entity collidedEntity : collidedWith) {
                    getBehaviorSet().handleEntityCollision(collidedEntity);
                }
            }
        }

        //Ensures that the player can't move outside of the parent node bounds
        double translateX = Math.max(xMin, Math.min(x + constrainedVelocity.get(0), xMax));
        double translateY = Math.max(yMin, Math.min(y + constrainedVelocity.get(1), yMax));
        setEntityPosition(translateX, translateY);
    }

    public void moveNode(double dx, double dy) {
        moveNode(new Vector(dx, dy));
    }

    public Rectangle2D getHitbox() {
        return hitbox.get();
    }

    public void setHitbox(Rectangle2D hitbox) {
        this.hitbox.set(hitbox);
    }

    public SimpleObjectProperty<Rectangle2D> hitboxProperty() {
        return hitbox;
    }

    public void setEntityPosition(double x, double y) {
        getDisplayNode().setTranslateX(x);
        getDisplayNode().setTranslateY(y);

        updateHitboxPosition();
    }

    public void tick() {
        for (int i = 0; i < statusEffects.size(); i++) {
            StatusEffect effect = statusEffects.get(i);
            effect.tick();
            if (effect.shouldRemove()) {
                statusEffects.remove(effect);
                i--;
            }
        }

        moveNode(getBehaviorSet().getMovement());

        owningRoom.addEntities(getBehaviorSet().attack());
    }

    public SimpleIntegerProperty healthProperty() {
        return health;
    }

    public void adjustHealth(int dHealth) {
        health.set(Math.min(health.getValue() + dHealth, entityStatistics.getMaxHealth()));
    }

    public int getHealth() {
        return health.get();
    }

    public void setHealth(int health) {
        this.health.set(health);
    }

    public Room getOwningRoom() {
        return owningRoom;
    }

    public void setOwningRoom(Room owningRoom) {
        this.owningRoom = owningRoom;
    }

    public BehaviorSet getBehaviorSet() {
        return behaviorSet.get();
    }

    public SimpleObjectProperty<BehaviorSet> behaviorSetProperty() {
        return behaviorSet;
    }

    public Vector vectorToOtherEntity(Entity other) {
        if (other == null) {
            return new Vector(0, 0);
        }
        Rectangle2D targetBounds = other.getHitbox();
        Rectangle2D thisBounds = this.getHitbox();

        Vector targetPosition = new Vector(targetBounds.getMinX() + targetBounds.getWidth() / 2,
                                           targetBounds.getMinY() + targetBounds.getHeight() / 2);
        Vector thisPosition = new Vector(thisBounds.getMinX() + thisBounds.getWidth() / 2,
                                         thisBounds.getMinY() + thisBounds.getHeight() / 2);

        return targetPosition.minus(thisPosition);
    }

    public Entity getClosestEntityFromList(List<? extends Entity> targets) {
        if (targets.size() == 0) {
            return null;
        }

        return targets.stream().sorted((o1, o2) -> {
            if (o1.equals(o2)) {
                return 0;
            }
            if (this.vectorToOtherEntity(o1).magnitude()
                    - this.vectorToOtherEntity(o2).magnitude() < 0) {
                return -1;
            }
            return 1;
        }).collect(Collectors.toList()).get(0);
    }

    public EntityStatistics getEntityStatistics() {
        return entityStatistics;
    }

    public ObservableList<StatusEffect> getStatusEffects() {
        return statusEffects;
    }

    public double getDisplayScale() {
        return displayScale.get();
    }

    public void setDisplayScale(double displayScale) {
        this.displayScale.set(displayScale);
    }

    public SimpleDoubleProperty displayScaleProperty() {
        return displayScale;
    }
}
