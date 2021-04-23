package whatexe.dungeoncrawler.entities;

import org.reflections8.Reflections;
import org.reflections8.scanners.SubTypesScanner;
import org.reflections8.util.ClasspathHelper;
import org.reflections8.util.ConfigurationBuilder;
import whatexe.dungeoncrawler.entities.enemies.Enemy;
import whatexe.dungeoncrawler.entities.enemies.bosses.Boss;
import whatexe.dungeoncrawler.entities.items.Item;
import whatexe.dungeoncrawler.entities.items.modifiers.Modifier;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class EntityChooser {

    private static EntityChooser instance;

    private final Map<String, Set<Class<Object>>> classTypesMap;

    private EntityChooser() {
        classTypesMap = new HashMap<>();
    }

    public static EntityChooser getInstance() {
        if (instance == null) {
            instance = new EntityChooser();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T> void populateMapWithSubTypesOf(Reflections reflections, Class<T> superType) {
        Set<Class<? extends T>> classes = reflections.getSubTypesOf(superType);

        // Map each class from Class<? extends T> to Class<Object>
        // then filter out a class if it is an interface or abstract
        Set<Class<Object>> genericClasses =
                classes.stream()
                       .map(clazz -> (Class<Object>) clazz)
                       .filter(clazz -> !(clazz.isInterface()
                               || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())))
                       .collect(Collectors.toSet());
        classTypesMap.put(superType.getName(), genericClasses);
    }

    @SuppressWarnings("unchecked")
    private <T> void populateMapWithSubTypesOf(Reflections reflections,
                                               Class<T> superType,
                                               Class<? extends T> excludeSubTypesOf) {
        Set<Class<? extends T>> classes = reflections.getSubTypesOf(superType);

        // Map each class from Class<? extends T> to Class<Object>
        // then filter out a class if it is an interface or abstract
        // then filter out a class if it is a subclass of excludeSubTypesOf
        Set<Class<Object>> genericClasses =
                classes.stream()
                       .map(clazz -> (Class<Object>) clazz)
                       .filter(clazz -> !(clazz.isInterface()
                               || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())))
                       .filter(clazz -> !(excludeSubTypesOf.isAssignableFrom(clazz)))
                       .collect(Collectors.toSet());
        classTypesMap.put(String.format("%s-%s",
                                        superType.getName(),
                                        excludeSubTypesOf.getName()),
                          genericClasses);
    }

    public <T> void populateMapWithSubTypesOf(Class<T> superType,
                                               Class<? extends T> excludeSubTypesOf) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forClass(getClass()))
                        .setScanners(new SubTypesScanner(false)));
        populateMapWithSubTypesOf(reflections, superType, excludeSubTypesOf);
    }

    private <T> void populateMapWithSubTypesOf(Class<T> superType) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forClass(getClass()))
                        .setScanners(new SubTypesScanner(false)));
        populateMapWithSubTypesOf(reflections, superType);
    }

    @SuppressWarnings("unchecked")
    private <T> Class<? extends T> getRandomClassFromList(List<Class<Object>> classes) {
        return (Class<? extends T>) classes.get((int) (Math.random() * classes.size()));
    }

    private <T> Class<? extends T> getRandomSubTypeOf(Class<T> superType) {
        if (!classTypesMap.containsKey(superType.getName())) {
            populateMapWithSubTypesOf(superType);
        }
        ArrayList<Class<Object>> classes = new ArrayList<>(classTypesMap.get(superType.getName()));
        return getRandomClassFromList(classes);
    }

    private <T> Class<? extends T> getRandomSubTypeOf(Class<T> superType,
                                                      Class<? extends T> excludeSubTypesOf) {
        String exclusionName = String.format("%s-%s",
                                             superType.getName(),
                                             excludeSubTypesOf.getName());
        if (!classTypesMap.containsKey(exclusionName)) {
            populateMapWithSubTypesOf(superType, excludeSubTypesOf);
        }
        ArrayList<Class<Object>> classes = new ArrayList<>(classTypesMap.get(exclusionName));
        return getRandomClassFromList(classes);
    }

    private Class<? extends Enemy> getRandomEnemyClass() {
        return getRandomSubTypeOf(Enemy.class, Boss.class);
    }

    public Class<? extends Item> getRandomItemClass(boolean includeModifiers) {
        if (includeModifiers) {
            return getRandomSubTypeOf(Item.class);
        }

        return getRandomSubTypeOf(Item.class, Modifier.class);
    }

    public Enemy getRandomEnemy(Room owningRoom) {
        Class<? extends Enemy> enemyClass = getRandomEnemyClass();
        try {
            Constructor<? extends Enemy> constructor = enemyClass.getConstructor(Room.class);
            return constructor.newInstance(owningRoom);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Enemy %s, but cannot find the constructor %s(Room)!",
                                  enemyClass.getName(), enemyClass.getName()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Enemy %s, but cannot use the constructor %s(Room)!",
                                  enemyClass.getName(), enemyClass.getName()));

        }
    }

    /**
     * For when you want to add the Item from an Entity.
     * @param owningEntity the Entity owning the Item.
     * @param includeModifiers whether or not to include Modifiers in the selection.
     * @return the new Item.
     */
    public Item getRandomItem(Entity owningEntity, boolean includeModifiers) {
        Class<? extends Item> itemClass = getRandomItemClass(includeModifiers);
        try {
            Constructor<? extends Item> constructor = itemClass.getConstructor(Entity.class);
            return constructor.newInstance(owningEntity);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Item %s, but cannot find the constructor %s(Entity)!",
                                  itemClass.getName(), itemClass.getName()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Item %s, but cannot use the constructor %s(Entity)!",
                                  itemClass.getName(), itemClass.getName()));
        }
    }

    public Item getItemFromClass(Class<Object> clazz, Entity owningEntity) {
        try {
            Constructor<Object> constructor = clazz.getConstructor(Entity.class);
            return (Item) constructor.newInstance(owningEntity);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Item %s, but cannot find the constructor %s(Entity)!",
                                  clazz.getName(), clazz.getName()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Item %s, but cannot use the constructor %s(Entity)!",
                                  clazz.getName(), clazz.getName()));
        }
    }

    /**
     * For when you want to drop the Item into a Room.
     * @param owningRoom the Room owning the Item.
     * @param includeModifiers whether or not to include Modifiers in the selection.
     * @return the new Item.
     */
    public Item getRandomItem(Room owningRoom, boolean includeModifiers) {
        Class<? extends Item> itemClass = getRandomItemClass(includeModifiers);
        try {
            Constructor<? extends Item> constructor = itemClass.getConstructor(Room.class);
            return constructor.newInstance(owningRoom);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Item %s, but cannot find the constructor %s(Room)!",
                                  itemClass.getName(), itemClass.getName()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Item %s, but cannot use the constructor %s(Room)!",
                                  itemClass.getName(), itemClass.getName()));

        }
    }

    /**
     * For when you want to add the Modifier from an Entity.
     * @param owningEntity the Entity owning the Modifier.
     * @return the new Modifier.
     */
    public Modifier getRandomModifier(Entity owningEntity) {
        Class<? extends Modifier> modifierClass = getRandomSubTypeOf(Modifier.class);
        try {
            Constructor<? extends Modifier> constructor =
                    modifierClass.getConstructor(Entity.class);
            return constructor.newInstance(owningEntity);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Item %s, but cannot find the constructor %s(Entity)!",
                                  modifierClass.getName(), modifierClass.getName()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Item %s, but cannot use the constructor %s(Entity)!",
                                  modifierClass.getName(), modifierClass.getName()));

        }
    }

    /**
     * For when you want to drop the Modifier into a Room.
     * @param owningRoom the Room owning the Modifier.
     * @return the new Modifier.
     */
    public Modifier getRandomModifier(Room owningRoom) {
        Class<? extends Modifier> modifierClass = getRandomSubTypeOf(Modifier.class);
        try {
            Constructor<? extends Modifier> constructor =
                    modifierClass.getConstructor(Room.class);
            return constructor.newInstance(owningRoom);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Item %s, but cannot find the constructor %s(Room)!",
                                  modifierClass.getName(), modifierClass.getName()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    String.format("Chose the Item %s, but cannot use the constructor %s(Room)!",
                                  modifierClass.getName(), modifierClass.getName()));

        }
    }

    public Map<String, Set<Class<Object>>> getClassTypesMap() {
        return classTypesMap;
    }

}
