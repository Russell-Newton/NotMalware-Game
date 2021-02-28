package whatexe.tileengine.fromtmx.objectchildren;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#text >
 *     The TXM Map Format specification</a>.
 */
public class Text {

    @JacksonXmlProperty(isAttribute = true, localName = "fontfamily")
    private String fontFamily;
    @JacksonXmlProperty(isAttribute = true, localName = "pixelsize")
    private int size;
    @JacksonXmlProperty(isAttribute = true)
    private int wrap;
    @JacksonXmlProperty(isAttribute = true)
    private String color;
    @JacksonXmlProperty(isAttribute = true)
    private int bold;
    @JacksonXmlProperty(isAttribute = true)
    private int italic;
    @JacksonXmlProperty(isAttribute = true)
    private int underline;
    @JacksonXmlProperty(isAttribute = true)
    private int strikeout;
    @JacksonXmlProperty(isAttribute = true)
    private int kerning;
    @JacksonXmlProperty(isAttribute = true, localName = "halign")
    private String hAlign;
    @JacksonXmlProperty(isAttribute = true, localName = "valign")
    private String vAlign;

    @JacksonXmlText
    private String value;


    public String getFontFamily() {
        return fontFamily;
    }

    public int getSize() {
        return size;
    }

    public int getWrap() {
        return wrap;
    }

    public String getColor() {
        return color;
    }

    public int getBold() {
        return bold;
    }

    public int getItalic() {
        return italic;
    }

    public int getUnderline() {
        return underline;
    }

    public int getStrikeout() {
        return strikeout;
    }

    public int getKerning() {
        return kerning;
    }

    public String gethAlign() {
        return hAlign;
    }

    public String getvAlign() {
        return vAlign;
    }

    public String getValue() {
        return value;
    }
}
