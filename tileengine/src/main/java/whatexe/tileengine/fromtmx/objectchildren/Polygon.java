package whatexe.tileengine.fromtmx.objectchildren;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#polygon >
 *     The TXM Map Format specification</a>.
 */
public class Polygon {

    @JacksonXmlProperty(isAttribute = true)
    private String points;


    public String getPoints() {
        return points;
    }
}
