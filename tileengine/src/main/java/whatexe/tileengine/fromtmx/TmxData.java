package whatexe.tileengine.fromtmx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import whatexe.tileengine.fromtmx.datachildren.Chunk;
import whatexe.tileengine.fromtmx.datachildren.Tile;

import java.util.List;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#data >
 *     The TXM Map Format specification</a>.
 */
public class TmxData {

    @JacksonXmlProperty(isAttribute = true)
    private String encoding;
    @JacksonXmlProperty(isAttribute = true)
    private String compression;
    @JacksonXmlText
    private String data;


    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "tile")
    private List<Tile> tiles;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "chunk")
    private List<Chunk> chunks;


    public String getEncoding() {
        return encoding;
    }

    public String getCompression() {
        return compression;
    }

    public String getData() {
        return data;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }
}
