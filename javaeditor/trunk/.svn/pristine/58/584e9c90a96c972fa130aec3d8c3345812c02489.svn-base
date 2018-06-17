/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.sf.gridarta.gui.map.test;

import java.awt.Image;
import java.awt.image.BufferedImage;
import net.sf.gridarta.gui.copybuffer.CopyBuffer;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewFactory;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.map.mapview.TestMapView;
import net.sf.gridarta.gui.map.mapview.TestMapViewFactory;
import net.sf.gridarta.gui.map.renderer.RendererFactory;
import net.sf.gridarta.gui.map.renderer.TestRendererFactory;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.io.CacheFiles;
import net.sf.gridarta.model.io.TestCacheFiles;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.TestMapControlCreator;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class for creating {@link MapControl} instances for regression tests.
 * @author Andreas Kirschbaum
 */
public class TestMapControlCreatorUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private TestMapControlCreatorUtils() {
    }

    /**
     * Creates a new {@link MapView} instance.
     * @param mapControl the associated map control
     * @return the map view instance
     */
    @NotNull
    public static MapView<TestGameObject, TestMapArchObject, TestArchetype> newMapView(@NotNull final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl) {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        final MapGrid mapGrid = new MapGrid(mapModel.getMapArchObject().getMapSize());
        return new TestMapView(mapControl, mapGrid, new MapCursor<TestGameObject, TestMapArchObject, TestArchetype>(mapGrid, mapModel));
    }

    /**
     * Returns a new {@link CopyBuffer} instance.
     * @param mapControlCreator the map control creator
     * @return the copy buffer instance
     */
    @NotNull
    public static CopyBuffer<TestGameObject, TestMapArchObject, TestArchetype> newCopyBuffer(@NotNull final TestMapControlCreator mapControlCreator) {
        final TestMapModelCreator mapModelCreator = mapControlCreator.getMapModelCreator();
        return new CopyBuffer<TestGameObject, TestMapArchObject, TestArchetype>(mapModelCreator.getMapViewSettings(), mapModelCreator.getGameObjectFactory(), mapControlCreator.getMapArchObjectFactory(), mapControlCreator.getMapModelFactory(), mapModelCreator.getInsertionModeSet());
    }

    /**
     * Creates a new {@link MapViewsManager} instance.
     * @param mapControlCreator the map control creator
     * @return the map views manager instance
     */
    @NotNull
    public static MapViewsManager<TestGameObject, TestMapArchObject, TestArchetype> newMapViewsManager(@NotNull final TestMapControlCreator mapControlCreator) {
        final MapViewFactory<TestGameObject, TestMapArchObject, TestArchetype> mapViewFactory = new TestMapViewFactory();
        return new MapViewsManager<TestGameObject, TestMapArchObject, TestArchetype>(mapControlCreator.getMapModelCreator().getMapViewSettings(), mapViewFactory, mapControlCreator.getMapManager(), mapControlCreator.getPickmapManager());
    }

    /**
     * Creates a new {@link MapImageCache} instance.
     * @param mapControlCreator the map control creator
     * @return the map image cache instance
     */
    @NotNull
    public static MapImageCache<TestGameObject, TestMapArchObject, TestArchetype> newMapImageCache(@NotNull final TestMapControlCreator mapControlCreator) {
        final RendererFactory<TestGameObject, TestMapArchObject, TestArchetype> rendererFactory = new TestRendererFactory();
        final Image defaultIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        final Image defaultPreview = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        final CacheFiles cacheFiles = new TestCacheFiles();
        return new MapImageCache<TestGameObject, TestMapArchObject, TestArchetype>(mapControlCreator.getMapManager(), defaultIcon, defaultPreview, rendererFactory, cacheFiles);
    }

}
