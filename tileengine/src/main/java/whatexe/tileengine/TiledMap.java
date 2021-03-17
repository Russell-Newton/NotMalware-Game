package whatexe.tileengine;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import whatexe.tileengine.construction.TileImage;
import whatexe.tileengine.construction.TileSet;
import whatexe.tileengine.fromtmx.GroupLayer;
import whatexe.tileengine.fromtmx.ObjectLayer;
import whatexe.tileengine.fromtmx.TileLayer;
import whatexe.tileengine.fromtmx.TmxMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.function.Function;

public class TiledMap {

    private final Map<String, ObjectGroup> objectGroups;
    private final BufferedImage mapImage;
    private final URL src;

    public TiledMap(URL src, Function<String, URL> resourceLoader) throws IOException {
        this.src = src;
        objectGroups = new HashMap<>();

        XmlMapper xmlMapper = new XmlMapper();
        TmxMap fromTmx = xmlMapper.readValue(src, TmxMap.class);

        mapImage = new BufferedImage(fromTmx.getWidth() * fromTmx.getTileWidth(),
                                     fromTmx.getHeight() * fromTmx.getTileHeight(),
                                     BufferedImage.TYPE_INT_ARGB);

        build(fromTmx, resourceLoader);
    }

    private static void addFromGroup(GroupLayer group, List<TileLayer> tmxTileLayers,
                                     List<ObjectLayer> tmxObjectGroups) {
        if (group.getTileSublayers() != null) {
            tmxTileLayers.addAll(group.getTileSublayers());
        }
        if (group.getObjectSublayers() != null) {
            tmxObjectGroups.addAll(group.getObjectSublayers());
        }
        if (group.getGroupSublayers() != null) {
            for (GroupLayer subgroup : group.getGroupSublayers()) {
                addFromGroup(subgroup, tmxTileLayers, tmxObjectGroups);
            }
        }
    }

    private void build(TmxMap fromTmx, Function<String, URL> resourceLoader) throws IOException {
        // TODO - Ignores image layers...
        // TODO - Doesn't support group/layer offsets

        // Flatten groups
        List<TileLayer> tmxTileLayers = fromTmx.getTileLayers();
        List<ObjectLayer> tmxObjectLayers = fromTmx.getObjectLayers();
        if (fromTmx.getGroupLayers() != null) {
            for (GroupLayer group : fromTmx.getGroupLayers()) {
                addFromGroup(group, tmxTileLayers, tmxObjectLayers);
            }
        }


        // load tilesets
        List<TileSet> tileSets = new ArrayList<>();
        for (whatexe.tileengine.fromtmx.TileSet tmxTileSet : fromTmx.getTileSets()) {
            tileSets.add(new TileSet(tmxTileSet, resourceLoader));
        }


        // load layers
        // paint layers
        Graphics mapImageGraphics = mapImage.getGraphics();
        for (TileLayer layer : tmxTileLayers) {
            String layerData = layer.getData().getData().replaceAll("\\s+", "");
            long[] tiles = Arrays.stream(layerData.split(","))
                                 .mapToLong(Long::parseLong)
                                 .toArray();
            int x = 0;
            int y = 0;
            for (long tileId : tiles) {
                if (tileId > 0) {
                    for (TileSet tileSet : tileSets) {
                        if (tileSet.isGIdInRange(tileId)) {
                            TileImage image = tileSet.getTileImage(tileId);

                            mapImageGraphics.drawImage(image.getImage(), x,
                                                       y, null);
                            break;
                        }
                    }
                }

                x += fromTmx.getTileWidth();
                if (x >= fromTmx.getWidth() * fromTmx.getTileWidth()) {
                    x = 0;
                    y += fromTmx.getTileWidth();
                }
            }
        }


        // load groups
        for (ObjectLayer objectLayer : tmxObjectLayers) {
            objectGroups.put(objectLayer.getName(), new ObjectGroup(objectLayer));
        }
    }

    public Map<String, ObjectGroup> getObjectGroups() {
        return objectGroups;
    }

    public BufferedImage getMapImage() {
        return mapImage;
    }
}
