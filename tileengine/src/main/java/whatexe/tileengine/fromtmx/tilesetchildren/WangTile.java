package whatexe.tileengine.fromtmx.tilesetchildren;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#wangtile >
 *     The TXM Map Format specification</a>.
 */
public class WangTile {

    @JacksonXmlProperty(isAttribute = true, localName = "tileid")
    private int tileId;
    @JacksonXmlProperty(isAttribute = true, localName = "wangid")
    private long wangId;
    @JacksonXmlProperty(isAttribute = true, localName = "hflip")
    private boolean hFlip;
    @JacksonXmlProperty(isAttribute = true, localName = "vflip")
    private boolean vFlip;
    @JacksonXmlProperty(isAttribute = true, localName = "dflip")
    private boolean dFlip;


    public int getTileId() {
        return tileId;
    }

    public long getWangId() {
        return wangId;
    }

    public boolean ishFlip() {
        return hFlip;
    }

    public boolean isvFlip() {
        return vFlip;
    }

    public boolean isdFlip() {
        return dFlip;
    }
}
