package whatexe.tileengine.fromtmx;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import whatexe.tileengine.fromtmx.objectchildren.Polygon;
import whatexe.tileengine.fromtmx.objectchildren.Text;

import java.util.List;

/**
 * TMX Deserialization is based off of
 * <a href=https://doc.mapeditor.org/en/stable/reference/tmx-map-format/#object >
 *     The TXM Map Format specification</a>.
 */
public class MapObject {

    @JacksonXmlProperty(isAttribute = true)
    private int id;
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(isAttribute = true)
    private String type;
    @JacksonXmlProperty(isAttribute = true)
    private double x;
    @JacksonXmlProperty(isAttribute = true)
    private double y;
    @JacksonXmlProperty(isAttribute = true)
    private double width;
    @JacksonXmlProperty(isAttribute = true)
    private double height;
    @JacksonXmlProperty(isAttribute = true)
    private double rotation;
    @JacksonXmlProperty(isAttribute = true, localName = "gid")
    private int visible;
    @JacksonXmlProperty(isAttribute = true)
    private String template;


    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    private List<TmxProperty> properties;
    @JacksonXmlProperty
    private String ellipse;
    @JacksonXmlProperty
    private String point;
    @JacksonXmlProperty
    private Polygon polygon;
    @JacksonXmlProperty
    private Polygon polyline;
    @JacksonXmlProperty
    private Text text;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getRotation() {
        return rotation;
    }

    public int getVisible() {
        return visible;
    }

    public String getTemplate() {
        return template;
    }

    public List<TmxProperty> getProperties() {
        return properties;
    }

    public String getEllipse() {
        return ellipse;
    }

    public String getPoint() {
        return point;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public Polygon getPolyline() {
        return polyline;
    }

    public Text getText() {
        return text;
    }
}
