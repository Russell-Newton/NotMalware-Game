package whatexe.tileengine.fromtmx.tilesetchildren;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import whatexe.tileengine.fromtmx.TmxData;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#image >
 *     The TXM Map Format specification</a>.
 */
public class Image {

    @JacksonXmlProperty(isAttribute = true)
    private String source;
    @JacksonXmlProperty(isAttribute = true)
    private String trans;
    @JacksonXmlProperty(isAttribute = true)
    private int width;
    @JacksonXmlProperty(isAttribute = true)
    private int height;

    @JacksonXmlProperty
    private TmxData data;


    public String getSource() {
        return source;
    }

    public String getTrans() {
        return trans;
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
