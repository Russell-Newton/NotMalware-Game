package whatexe.tileengine.fromtmx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#group >
 *     The TXM Map Format specification</a>.
 */
public class GroupLayer extends MapLayer {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "layer")
    private List<TileLayer> tileSublayers;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "objectgroup")
    private List<ObjectLayer> objectSublayers;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "imagelayer")
    private List<ImageLayer> imageSublayers;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "group")
    private List<GroupLayer> groupSublayers;


    public List<TileLayer> getTileSublayers() {
        return tileSublayers;
    }

    public List<ObjectLayer> getObjectSublayers() {
        return objectSublayers;
    }

    public List<ImageLayer> getImageSublayers() {
        return imageSublayers;
    }

    public List<GroupLayer> getGroupSublayers() {
        return groupSublayers;
    }
}
