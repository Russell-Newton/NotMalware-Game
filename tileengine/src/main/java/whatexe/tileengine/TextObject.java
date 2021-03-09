package whatexe.tileengine;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class TextObject extends MapObject {
    private Font font;
    private boolean wrap;
    private String color;
    private String hAlign;
    private String vAlign;
    private String text;

    TextObject(whatexe.tileengine.fromtmx.MapObject fromTmx) {
        super(fromTmx);
        build();
    }

    @Override
    protected void build() {
        Map<TextAttribute, Object> textAttributes = new HashMap<>();

        whatexe.tileengine.fromtmx.objectchildren.Text fromTmx = getFromTmx().getText();

        textAttributes.put(TextAttribute.FAMILY, fromTmx.getFontFamily());
        textAttributes.put(TextAttribute.SIZE, fromTmx.getSize());
        textAttributes.put(TextAttribute.WEIGHT, fromTmx.getBold() == 0
                ? TextAttribute.WEIGHT_REGULAR : TextAttribute.WEIGHT_BOLD);
        textAttributes.put(TextAttribute.POSTURE, fromTmx.getItalic() == 0
                ? TextAttribute.POSTURE_REGULAR : TextAttribute.POSTURE_OBLIQUE);
        textAttributes.put(TextAttribute.UNDERLINE, fromTmx.getUnderline() - 1);
        textAttributes.put(TextAttribute.STRIKETHROUGH, fromTmx.getUnderline() == 1);
        textAttributes.put(TextAttribute.KERNING, fromTmx.getKerning());
        font = new Font(textAttributes);

        x = getFromTmx().getX();
        y = getFromTmx().getY();
        width = getFromTmx().getWidth();
        height = getFromTmx().getHeight();
        wrap = fromTmx.getWrap() == 1;
        color = fromTmx.getColor();
        hAlign = fromTmx.gethAlign();
        vAlign = fromTmx.getvAlign();
        text = fromTmx.getValue();
    }

    public Font getFont() {
        return font;
    }

    public boolean isWrap() {
        return wrap;
    }

    public String getColor() {
        return color;
    }

    public String gethAlign() {
        return hAlign;
    }

    public String getvAlign() {
        return vAlign;
    }

    public String getText() {
        return text;
    }
}
