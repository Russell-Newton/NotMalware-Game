package whatexe.tileengine.fromtmx;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#map >
 *     The TXM Map Format specification</a>.
 */
@JacksonXmlRootElement(localName = "map")
public class TmxMap {

    // Map attributes
    @JacksonXmlProperty(isAttribute = true)
    private String version;
    @JacksonXmlProperty(isAttribute = true, localName = "tiledversion")
    private String tiledVersion;
    @JacksonXmlProperty(isAttribute = true)
    private String orientation;
    @JacksonXmlProperty(isAttribute = true, localName = "renderorder")
    private String renderOrder;
    @JacksonXmlProperty(isAttribute = true)
    private int width;
    @JacksonXmlProperty(isAttribute = true)
    private int height;
    @JacksonXmlProperty(isAttribute = true, localName = "tilewidth")
    private int tileWidth;
    @JacksonXmlProperty(isAttribute = true, localName = "tileheight")
    private int tileHeight;
    @JacksonXmlProperty(isAttribute = true, localName = "hexsidelength")
    private int hexSideLength;
    @JacksonXmlProperty(isAttribute = true, localName = "staggeraxis")
    private String staggerAxis;
    @JacksonXmlProperty(isAttribute = true, localName = "staggerindex")
    private String staggerIndex;
    @JacksonXmlProperty(isAttribute = true, localName = "backgroundcolor")
    private String backgroundColor;
    @JacksonXmlProperty(isAttribute = true, localName = "nextlayerid")
    private int nextLayerId;
    @JacksonXmlProperty(isAttribute = true, localName = "nextobjectid")
    private int nextObjectId;
    @JacksonXmlProperty(isAttribute = true)
    private int infinite;


    // Map children
    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    private List<TmxProperty> properties;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "tileset")
    private List<TileSet> tileSets;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "layer")
    private List<TileLayer> tileLayers;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "objectgroup")
    private List<ObjectLayer> objectLayers;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "imagelayer")
    private List<ImageLayer> imageLayers;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "group")
    private List<GroupLayer> groupLayers;


    public String getVersion() {
        return version;
    }

    public String getTiledVersion() {
        return tiledVersion;
    }

    public String getOrientation() {
        return orientation;
    }

    public String getRenderOrder() {
        return renderOrder;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getHexSideLength() {
        return hexSideLength;
    }

    public String getStaggerAxis() {
        return staggerAxis;
    }

    public String getStaggerIndex() {
        return staggerIndex;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public int getNextLayerId() {
        return nextLayerId;
    }

    public int getNextObjectId() {
        return nextObjectId;
    }

    public int getInfinite() {
        return infinite;
    }

    public List<TmxProperty> getProperties() {
        return properties;
    }

    public List<TileSet> getTileSets() {
        return tileSets;
    }

    public List<TileLayer> getTileLayers() {
        return tileLayers;
    }

    public List<ObjectLayer> getObjectLayers() {
        return objectLayers;
    }

    public List<ImageLayer> getImageLayers() {
        return imageLayers;
    }

    public List<GroupLayer> getGroupLayers() {
        return groupLayers;
    }
}
