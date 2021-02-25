package whatexe.tileengine.fromtmx.datachildren;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#chunk >
 *     The TXM Map Format specification</a>.
 */
public class Chunk {

    @JacksonXmlProperty(isAttribute = true)
    private int x;
    @JacksonXmlProperty(isAttribute = true)
    private int y;
    @JacksonXmlProperty(isAttribute = true)
    private int width;
    @JacksonXmlProperty(isAttribute = true)
    private int height;


    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Tile> tiles;


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

    public List<Tile> getTiles() {
        return tiles;
    }
}
