package whatexe.tileengine.fromtmx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#layer >
 *     The TXM Map Format specification</a>.
 */
public class TileLayer extends MapLayer {
    @JacksonXmlProperty(isAttribute = true)
    private int x;
    @JacksonXmlProperty(isAttribute = true)
    private int y;
    @JacksonXmlProperty(isAttribute = true)
    private int width;
    @JacksonXmlProperty(isAttribute = true)
    private int height;
    @JacksonXmlProperty
    private TmxData data;


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TmxData getData() {
        return data;
    }
}
