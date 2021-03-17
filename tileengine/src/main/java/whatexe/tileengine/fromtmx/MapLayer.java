package whatexe.tileengine.fromtmx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public abstract class MapLayer {

    @JacksonXmlProperty(isAttribute = true)
    private int id;
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(isAttribute = true)
    private double opacity;
    @JacksonXmlProperty(isAttribute = true)
    private int visible;
    @JacksonXmlProperty(isAttribute = true, localName = "tintcolor")
    private String tintColor;
    @JacksonXmlProperty(isAttribute = true, localName = "offsetx")
    private int offsetX;
    @JacksonXmlProperty(isAttribute = true, localName = "offsety")
    private int offsetY;
    @JacksonXmlProperty(isAttribute = true)
    private String color;


    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    private List<TmxProperty> properties;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getOpacity() {
        return opacity;
    }

    public int getVisible() {
        return visible;
    }

    public String getTintColor() {
        return tintColor;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public List<TmxProperty> getProperties() {
        return properties;
    }
}
