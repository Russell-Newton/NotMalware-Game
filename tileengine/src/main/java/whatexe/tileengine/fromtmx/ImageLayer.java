package whatexe.tileengine.fromtmx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import whatexe.tileengine.fromtmx.tilesetchildren.Image;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#imagelayer >
 *     The TXM Map Format specification</a>.
 */
public class ImageLayer extends MapLayer {

    @JacksonXmlProperty
    private Image image;


    public Image getImage() {
        return image;
    }
}
