package whatexe.tileengine.fromtmx.tilesetchildren;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#wangcornercolor >
 *     The TXM Map Format specification</a>.
 */
public class WangColor {

    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(isAttribute = true)
    private String color;
    @JacksonXmlProperty(isAttribute = true)
    private int tile;
    @JacksonXmlProperty(isAttribute = true)
    private double probability;


    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getTile() {
        return tile;
    }

    public double getProbability() {
        return probability;
    }
}
