package whatexe.tileengine.fromtmx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import whatexe.tileengine.fromtmx.tilesetchildren.*;

import java.util.List;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#tileset >
 *     The TXM Map Format specification</a>.
 */
@JacksonXmlRootElement(localName = "tileset")
public class TileSet {

    // TileSet attributes
    @JacksonXmlProperty(isAttribute = true, localName = "firstgid")
    private int firstGid;
    @JacksonXmlProperty(isAttribute = true)
    private String source;
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(isAttribute = true, localName = "tilewidth")
    private int tileWidth;
    @JacksonXmlProperty(isAttribute = true, localName = "tileheight")
    private int tileHeight;
    @JacksonXmlProperty(isAttribute = true)
    private int spacing;
    @JacksonXmlProperty(isAttribute = true)
    private int margin;
    @JacksonXmlProperty(isAttribute = true, localName = "tilecount")
    private int tileCount;
    @JacksonXmlProperty(isAttribute = true)
    private int columns;
    @JacksonXmlProperty(isAttribute = true, localName = "objectalignment")
    private String objectAlignment;


    // Tileset children
    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    private List<TmxProperty> properties;
    @JacksonXmlProperty(localName = "tileoffset")
    private TileOffset tileOffset;
    @JacksonXmlProperty
    private Grid grid;
    @JacksonXmlProperty
    private Image image;
    @JacksonXmlElementWrapper(localName = "terraintypes")
    @JacksonXmlProperty(localName = "terrain")
    private List<Terrain> terrainTypes;
    @JacksonXmlElementWrapper(localName = "wangsets")
    @JacksonXmlProperty(localName = "wangset")
    private List<WangSet> wangSets;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "tile")
    private List<Tile> tiles;


    public int getFirstGid() {
        return firstGid;
    }

    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getSpacing() {
        return spacing;
    }

    public int getMargin() {
        return margin;
    }

    public int getTileCount() {
        return tileCount;
    }

    public int getColumns() {
        return columns;
    }

    public String getObjectAlignment() {
        return objectAlignment;
    }

    public List<TmxProperty> getProperties() {
        return properties;
    }

    public TileOffset getTileOffset() {
        return tileOffset;
    }

    public Grid getGrid() {
        return grid;
    }

    public Image getImage() {
        return image;
    }

    public List<Terrain> getTerrainTypes() {
        return terrainTypes;
    }

    public List<WangSet> getWangSets() {
        return wangSets;
    }

    public List<Tile> getTiles() {
        return tiles;
    }
}
