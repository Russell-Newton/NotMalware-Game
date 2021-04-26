package whatexe.dungeoncrawler.layout.rooms;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.BoundingBox;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.doors.Door;
import whatexe.dungeoncrawler.entities.effects.InvisibilityEffect;
import whatexe.dungeoncrawler.entities.enemies.Enemy;
import whatexe.dungeoncrawler.entities.friends.Friend;
import whatexe.dungeoncrawler.entities.items.Item;
import whatexe.dungeoncrawler.entities.motionsupport.AABbTree;
import whatexe.dungeoncrawler.entities.motionsupport.BoundaryRectangle;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.player.Player;
import whatexe.dungeoncrawler.layout.Direction;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.tileengine.MapObject;
import whatexe.tileengine.RectangleObject;
import whatexe.tileengine.TiledMap;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Room {
    private final Timeline tickTimer;
    protected Image backgroundImage;
    protected Level owningLevel;
    protected TiledMap fromTiledMap;
    protected ObservableList<Door> doors;
    protected ObservableList<Enemy> enemies;
    protected ObservableList<Entity> miscEntities;
    protected ObservableList<Friend> friends;
    protected ObservableList<Entity> removeQueue;
    protected ObservableList<Entity> addQueue;
    protected SimpleObjectProperty<Player> player;

    protected boolean hasVisited;

    protected ObservableList<BoundaryRectangle> boundaryRectangles;
    protected AABbTree collisionTree;

    public Room(TiledMap fromTiledMap, Level owningLevel, Direction... exitDirections) {
        if (fromTiledMap == null) {
            this.backgroundImage = null;
        } else {
            this.backgroundImage = SwingFXUtils.toFXImage(fromTiledMap.getMapImage(), null);
        }
        this.owningLevel = owningLevel;
        this.fromTiledMap = fromTiledMap;

        this.doors = FXCollections.observableArrayList();
        this.enemies = FXCollections.observableArrayList();
        this.miscEntities = FXCollections.observableArrayList();
        this.friends = FXCollections.observableArrayList();
        this.boundaryRectangles = FXCollections.observableArrayList();
        this.removeQueue = FXCollections.observableArrayList();
        this.addQueue = FXCollections.observableArrayList();
        this.player = new SimpleObjectProperty<>(null);
        this.hasVisited = false;

        // Init from tmx
        if (fromTiledMap != null) {
            initDoors(exitDirections);
            initBlockedSpace();
            initEntities();
        }

        // Init collision tree
        this.collisionTree = new AABbTree(
                boundaryRectangles.size()
                        + enemies.size()
                        + doors.size()
                        + miscEntities.size()
                        + friends.size()
                        + 10);
        this.collisionTree.addAll(boundaryRectangles);
        this.collisionTree.addAll(enemies);
        this.collisionTree.addAll(doors);
        this.collisionTree.addAll(miscEntities);
        this.collisionTree.addAll(friends);
        addListenerForAABbInsert(enemies);
        addListenerForAABbInsert(doors);
        addListenerForAABbInsert(miscEntities);
        addListenerForAABbInsert(friends);

        tickTimer = new Timeline(
                new KeyFrame(Duration.millis(5), "RoomTick", event -> tickEntities()));
        tickTimer.setCycleCount(Animation.INDEFINITE);

        // Add the special move recharge listener
        this.enemies.addListener(new ListChangeListener<Enemy>() {
            @Override
            public void onChanged(Change<? extends Enemy> change) {
                if (enemies.size() == 0) {
                    if (getPlayer() != null) {
                        getPlayer().recharge();
                    }
                    handleOnRoomClear();
                }
            }
        });

        this.enemies.addListener(new ListChangeListener<Enemy>() {
            @Override
            public void onChanged(Change<? extends Enemy> change) {
                change.next();
                if (change.wasRemoved()) {
                    if (getPlayer() != null) {
                        getPlayer().addEnemiesKilled(change.getRemovedSize());
                    }
                }
            }
        });
    }

    private void addListenerForAABbInsert(ObservableList<? extends Entity> list) {
        list.addListener(new ListChangeListener<Entity>() {
            @Override
            public void onChanged(Change<? extends Entity> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        collisionTree.addAll(c.getAddedSubList());
                    }
                    if (c.wasRemoved()) {
                        collisionTree.removeAll(c.getRemoved());
                    }
                }
            }
        });
    }

    protected abstract void initDoors(Direction[] directionsToGenerate);

    /**
     * Populate the {@link Room#miscEntities} list.
     */
    protected abstract void initEntities();

    protected void initBlockedSpace() {
        if (!fromTiledMap.getObjectGroups().containsKey("OutOfBounds")) {
            return;
        }

        for (MapObject mapObject : fromTiledMap.getObjectGroups().get("OutOfBounds").getObjects()) {
            // don't allow for rotations
            if (mapObject instanceof RectangleObject) {
                Rectangle2D newBoundaryShape = new Rectangle2D(mapObject.getX(),
                                                               mapObject.getY(),
                                                               mapObject.getWidth(),
                                                               mapObject.getHeight());
                if (((RectangleObject) mapObject).getRotation() != 0) {
                    System.err.println("Rotated rectangles not supported!");
                }

                boundaryRectangles.add(new BoundaryRectangle(this,
                                                             newBoundaryShape));
            } else {
                System.err.println("Use of shapes other than rectangles not supported!");
            }
        }
    }


    protected abstract void handleOnRoomClear();

    public AABbTree.ConstrainedVelocityData getConstrainedVelocity(Entity entity,
                                                                   Vector attemptVelocity) {
        return collisionTree.getConstrainedVelocity(entity, attemptVelocity);
    }

    /**
     * Currently ticks entities and handles overlapping with player only.
     */
    protected void tickEntities() {
        if (getPlayer() == null) {
            return;
        }

        if (getPlayer().canTick()) {
            getPlayer().tick();
            collisionTree.update(getPlayer());
        }

        tickList(doors);
        tickList(friends);
        tickList(enemies);
        tickList(miscEntities);

        resolveAddQueue();
        resolveRemoveQueue();
    }

    protected void tickList(ObservableList<? extends Entity> list) {
        for (Entity entity : list) {
            if (entity.canTick()) {
                entity.tick();

                collisionTree.update(entity);

                handleOverlappable(entity);
            }
        }
    }

    private void handleOverlappable(Entity entity) {
        List<? extends Entity> targetList = entity.getBehaviorSet().getOverlapBehavior()
                                                  .getPossibleOverlapTargets();
        if (targetList.size() == 0) {
            return;
        }
        List<Entity> currentOverlaps = collisionTree.queryOverlaps(entity);
        currentOverlaps.forEach(target -> {
            if (targetList.contains(target)) {
                entity.getBehaviorSet().handleEntityOverlap(target);
            }
        });
    }

    public Player getPlayer() {
        return player.get();
    }

    public void setPlayer(Player player) {
        if (player != null) {
            player.setCurrentRoom(this);
            collisionTree.insert(player);
        } else {
            collisionTree.remove(getPlayer());
        }
        this.player.set(player);
    }

    public void updateDoorLocks() {
        for (Door door : doors) {
            if (this.hasVisited) {
                door.unlock();
            }
            if (door.getDirection() == null) {
                door.updateDoorLock();
                continue;
            }

            Room neighbor = owningLevel.getNeighborRoom(door.getDirection());
            if (neighbor != null && neighbor.hasVisited) {
                door.unlock();
            }
            door.updateDoorLock();
        }

    }

    public void lockAllDoors() {
        for (Door door: doors) {
            door.lock();
        }
    }

    public void unlockAllDoors() {
        for (Door door: doors) {
            door.unlock();
        }
    }

    public <T extends Entity> List<Entity> getVisibleEntities(List<T> fromList) {
        // Filter each entity by whether or not they have the InvisibilityEffect
        return fromList.stream()
                       .filter(entity ->
                                       entity.getStatusEffects().stream()
                                             .noneMatch(effect -> effect
                                                     instanceof InvisibilityEffect))
                       .collect(Collectors.toList());
    }

    public ObservableList<Entity> getMiscEntities() {
        return miscEntities;
    }

    public ObservableList<Door> getDoors() {
        return doors;
    }

    public ObservableList<Enemy> getEnemies() {
        return enemies;
    }

    public ObservableList<Friend> getFriends() {
        return friends;
    }

    public SimpleObjectProperty<Player> playerProperty() {
        return player;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public Level getOwningLevel() {
        return owningLevel;
    }

    public void startTicking() {
        tickTimer.play();
    }

    public void stopTicking() {
        tickTimer.pause();
    }

    public boolean isTicking() {
        return tickTimer.getStatus() == Animation.Status.RUNNING;
    }

    public ObservableList<BoundaryRectangle> getBoundaryRectangles() {
        return boundaryRectangles;
    }

    public void removeEntity(Entity toRemove) {
        if (toRemove == getPlayer()) {
            setPlayer(null);
            return;
        }

        if (enemies.contains(toRemove)) {
            removeQueue.add(toRemove);
        } else if (friends.contains(toRemove)) {
            removeQueue.add(toRemove);
        } else if (miscEntities.contains(toRemove)) {
            removeQueue.add(toRemove);
        }
    }

    public void addEntity(Entity toAdd) {
        if (toAdd != null) {
            addQueue.add(toAdd);
        }
    }

    public void dropItem(Item item, Entity owningEntity, int pickupTimer) {
        item.getDisplayNode().setLayoutX(0);
        item.getDisplayNode().setLayoutY(0);
        item.setEntityPosition(owningEntity.getDisplayNode().getTranslateX(),
                               owningEntity.getDisplayNode().getTranslateY());
        item.setNoPickupTimer(pickupTimer);
        addEntity(item);
    }

    public void dropItem(Item item, Entity owningEntity) {
        dropItem(item, owningEntity, 0);
    }

    public void addEntities(Collection<? extends Entity> toAdd) {
        for (Entity entity : toAdd) {
            addEntity(entity);
        }
    }

    public void removeEntities(Collection<? extends Entity> toRemove) {
        for (Entity entity : toRemove) {
            removeEntity(entity);
        }
    }

    protected BoundingBox getBoundsOfImage() {
        return new BoundingBox(0, 0,
                               backgroundImage.getWidth(), backgroundImage.getHeight());
    }

    private void resolveRemoveQueue() {
        for (Entity entity : removeQueue) {
            if (entity instanceof Enemy) {
                enemies.remove(entity);
            }
            if (entity instanceof Friend) {
                friends.remove(entity);
            } else {
                miscEntities.remove(entity);
            }
        }
        removeQueue.clear();
    }

    private void resolveAddQueue() {
        for (Entity toAdd : addQueue) {
            if (toAdd instanceof Player) {
                setPlayer((Player) toAdd);
            } else if (toAdd instanceof Friend) {
                friends.add((Friend) toAdd);
            } else if (toAdd instanceof Enemy) {
                enemies.add((Enemy) toAdd);
            } else if (toAdd instanceof Door) {
                doors.add((Door) toAdd);
            } else {
                miscEntities.add(toAdd);
            }
        }

        addQueue.clear();
    }

    static class MapObjectComparator implements Comparator<MapObject> {

        @Override
        public int compare(MapObject o1, MapObject o2) {
            if (o1.equals(o2)) {
                return 0;
            }
            if (o1.getY() < o2.getY()) {
                return -1;
            } else if (o1.getY() == o2.getY()) {
                if (o1.getX() < o2.getX()) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        }
    }
}
