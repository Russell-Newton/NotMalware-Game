package whatexe.tileengine.fromtmx.tilesetchildren;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#tileoffset >
 *     The TXM Map Format specification</a>.
 */
public class TileOffset {

    @JacksonXmlProperty(isAttribute = true)
    private int x;
    @JacksonXmlProperty(isAttribute = true)
    private int y;


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
