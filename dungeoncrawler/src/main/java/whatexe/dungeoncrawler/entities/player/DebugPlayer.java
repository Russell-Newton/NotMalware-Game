package whatexe.dungeoncrawler.entities.player;

import whatexe.dungeoncrawler.entities.EntityChooser;
import whatexe.dungeoncrawler.entities.items.Currency;
import whatexe.dungeoncrawler.entities.items.Item;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.*;

public class DebugPlayer extends Player {
    public DebugPlayer(MalwareType malwareType) {
        super(malwareType);
        addAllItems();
    }

    public void addAllItems() {
        Map<String, Set<Class<Object>>> classTypeMap;
        EntityChooser.getInstance().populateMapWithSubTypesOf(Item.class, Currency.class);
        classTypeMap = EntityChooser.getInstance().getClassTypesMap();
        String nameOfItemList = "whatexe.dungeoncrawler.entities.items.Item-whatexe"
                + ".dungeoncrawler.entities.items.Currency";
        ArrayList<Class<Object>> classes = new ArrayList<>(classTypeMap.get(nameOfItemList));
        classes.sort(Comparator.comparing(Class::toString));
        ArrayList<Item> items = new ArrayList<>();
        for (Class<Object> clazz : classes) {
            items.add(EntityChooser.getInstance().getItemFromClass(clazz, this));
        }
        for (Item item : items) {
            addItem(item);
        }
    }

    @Override
    public void setCurrentRoom(Room currentRoom) {
        super.setCurrentRoom(currentRoom);
        setMoney(Math.max(1000, getMoney()));
    }
}
