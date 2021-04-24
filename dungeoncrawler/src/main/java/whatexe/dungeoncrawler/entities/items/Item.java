package whatexe.dungeoncrawler.entities.items;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.overlap.ItemOverlapBehavior;
import whatexe.dungeoncrawler.entities.behavior.presets.DoNothingBehaviorSet;
import whatexe.dungeoncrawler.layout.rooms.Room;

public abstract class Item extends Entity {
    protected Entity owningEntity;
    protected String name;
    protected String description;
    private int noPickupTimer;

    public Item(Sprite displayNode, Entity owningEntity) {
        this(displayNode, owningEntity.getOwningRoom());
        setOwningEntity(owningEntity);
    }

    public Item(Sprite displayNode, Room owningRoom) {
        super(displayNode, owningRoom);
        behaviorSet.set(new DoNothingBehaviorSet(this));
        getBehaviorSet().setOverlapBehavior(new ItemOverlapBehavior(this));
        noPickupTimer = 0;
        canTick = true;
    }

    public Entity getOwningEntity() {
        return owningEntity;
    }

    public void setOwningEntity(Entity owningEntity) {
        this.owningEntity = owningEntity;
    }

    public String getName() {
        return "Random Item Name";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return "Random item description";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNoPickupTimer() {
        return noPickupTimer;
    }

    public void setNoPickupTimer(int timer) {
        noPickupTimer = timer;
    }

    public abstract int getPrice();

    @Override
    public void tick() {
        super.tick();
        noPickupTimer = Math.max(0, noPickupTimer - 1);
    }
}
