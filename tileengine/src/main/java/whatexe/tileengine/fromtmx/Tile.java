package whatexe.tileengine.fromtmx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import whatexe.tileengine.fromtmx.tilesetchildren.Image;
import whatexe.tileengine.fromtmx.tilesetchildren.TileFrame;

import java.util.List;

public class Tile {

    @JacksonXmlProperty(isAttribute = true)
    private int id;
    @JacksonXmlProperty(isAttribute = true)
    private String type;
    @JacksonXmlProperty(isAttribute = true)
    private String terrain;
    @JacksonXmlProperty(isAttribute = true)
    private double probability;


    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    private List<TmxProperty> properties;
    @JacksonXmlProperty
    private Image image;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "objectgroup")
    private List<ObjectLayer> objectLayers;
    @JacksonXmlElementWrapper(localName = "animation")
    @JacksonXmlProperty(localName = "frame")
    private List<TileFrame> frames;


    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTerrain() {
        return terrain;
    }

    public double getProbability() {
        return probability;
    }

    public List<TmxProperty> getProperties() {
        return properties;
    }

    public Image getImage() {
        return image;
    }

    public List<ObjectLayer> getObjectLayers() {
        return objectLayers;
    }

    public List<TileFrame> getFrames() {
        return frames;
    }
}
