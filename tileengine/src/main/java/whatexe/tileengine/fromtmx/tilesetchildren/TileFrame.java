package whatexe.tileengine.fromtmx.tilesetchildren;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#frame >
 *     The TXM Map Format specification</a>.
 */
public class TileFrame {

    @JacksonXmlProperty(isAttribute = true, localName = "tileid")
    private int tileId;
    @JacksonXmlProperty(isAttribute = true)
    private int duration;


    public int getTileId() {
        return tileId;
    }

    public int getDuration() {
        return duration;
    }
}
