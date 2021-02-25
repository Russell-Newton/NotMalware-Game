package whatexe.tileengine.fromtmx.tilesetchildren;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import whatexe.tileengine.fromtmx.TmxProperty;

import java.util.List;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#wangset >
 *     The TXM Map Format specification</a>.
 */
public class WangSet {

    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(isAttribute = true)
    private int tile;


    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    private List<TmxProperty> properties;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "wangcornercolor")
    private List<WangColor> cornerColors;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "wangedgecolor")
    private List<WangColor> edgeColors;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "wangtile")
    private List<WangTile> wangTiles;


    public String getName() {
        return name;
    }

    public int getTile() {
        return tile;
    }

    public List<TmxProperty> getProperties() {
        return properties;
    }

    public List<WangColor> getCornerColors() {
        return cornerColors;
    }

    public List<WangColor> getEdgeColors() {
        return edgeColors;
    }

    public List<WangTile> getWangTiles() {
        return wangTiles;
    }
}
