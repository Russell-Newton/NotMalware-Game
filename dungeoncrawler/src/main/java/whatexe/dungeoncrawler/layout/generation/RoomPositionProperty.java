package whatexe.dungeoncrawler.layout.generation;

import javafx.beans.property.ObjectPropertyBase;

public class RoomPositionProperty extends ObjectPropertyBase<RoomPosition> {

    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";

    private final Object bean;
    private final String name;

    public RoomPositionProperty(RoomPosition initialValue) {
        this(initialValue, DEFAULT_BEAN, DEFAULT_NAME);
    }

    public RoomPositionProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    public RoomPositionProperty(RoomPosition initialValue, Object bean, String name) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    @Override
    public Object getBean() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
