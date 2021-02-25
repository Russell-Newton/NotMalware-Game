package whatexe.tileengine.fromtmx.datachildren;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#tmx-tilelayer-tile >
 *     The TXM Map Format specification</a>.
 */
public class Tile {

    @JacksonXmlProperty(isAttribute = true, localName = "gid")
    private int gId;
}
