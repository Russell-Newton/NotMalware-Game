package whatexe.tileengine.fromtmx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#objectgroup >
 *     The TXM Map Format specification</a>.
 */
public class ObjectLayer extends MapLayer {

    @JacksonXmlProperty(isAttribute = true)
    private int x;
    @JacksonXmlProperty(isAttribute = true)
    private int y;
    @JacksonXmlProperty(isAttribute = true)
    private int width;
    @JacksonXmlProperty(isAttribute = true)
    private int height;
    @JacksonXmlProperty(isAttribute = true, localName = "draworder")
    private String drawOrder;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "object")
    private List<MapObject> objects;

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

    public String getDrawOrder() {
        return drawOrder;
    }

    public List<MapObject> getObjects() {
        return objects;
    }
}
