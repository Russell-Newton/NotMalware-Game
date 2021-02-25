package whatexe.tileengine.fromtmx.tilesetchildren;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import whatexe.tileengine.fromtmx.TmxProperty;

import java.util.List;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#terrain >
 *     The TXM Map Format specification</a>.
 */
public class Terrain {

    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(isAttribute = true)
    private int tile;

    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    private List<TmxProperty> properties;


    public String getName() {
        return name;
    }

    public int getTile() {
        return tile;
    }

    public List<TmxProperty> getProperties() {
        return properties;
    }
}
