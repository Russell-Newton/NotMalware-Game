module whatexe.tileengine {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;

    requires java.desktop;

    exports whatexe.tileengine;
    opens whatexe.tileengine.fromtmx to com.fasterxml.jackson.databind;
    opens whatexe.tileengine.fromtmx.datachildren to com.fasterxml.jackson.databind;
    opens whatexe.tileengine.fromtmx.objectchildren to com.fasterxml.jackson.databind;
    opens whatexe.tileengine.fromtmx.tilesetchildren to com.fasterxml.jackson.databind;
}