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

package net.sf.gridarta.var.atrinik.gui.map.renderer;

import java.awt.Point;
import java.util.Collections;
import net.sf.gridarta.gui.filter.DefaultFilterControl;
import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.gui.map.renderer.GridMapSquarePainter;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.filter.NamedFilter;
import net.sf.gridarta.model.gameobject.DefaultIsoGameObject;
import net.sf.gridarta.model.gameobject.IsoMapSquareInfo;
import net.sf.gridarta.model.gameobject.MultiPositionData;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.match.NamedGameObjectMatcher;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests that {@link net.sf.gridarta.gui.map.renderer.IsoMapRenderer}
 * correctly interprets {@link DefaultIsoGameObject#ALIGN},  {@link
 * DefaultIsoGameObject#ROTATE}, and {@link DefaultIsoGameObject#ZOOM}.
 * @author Andreas Kirschbaum
 */
public class MapRendererTest {

    /**
     * The {@link TestMapModelCreator} for creating {@link MapModel} instances.
     */
    @NotNull
    private final TestMapModelCreator mapModelCreator = new TestMapModelCreator(true);

    /**
     * Checks rendering no game objects.
     */
    @Test
    public void testPaintEmpty() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        final TestMapRenderer renderer = newRenderer(mapModel);
        renderer.getFullImage();
        Assert.assertEquals("", renderer.getPaintingOperations());
    }

    /**
     * Checks rendering game objects without attributes that affect rendering.
     */
    @Test
    public void testPaintNormal() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("TEST");
        try {
            mapModelCreator.addGameObjectToMap(mapModel, "arch", "name", 0, 0, mapModelCreator.getTopmostInsertionMode());
        } finally {
            mapModel.endTransaction();
        }
        final TestMapRenderer renderer = newRenderer(mapModel);
        renderer.getFullImage();
        Assert.assertEquals("1.00 0.00 0.00 1.00 0.00 0.00\n", renderer.getPaintingOperations());
    }

    /**
     * Checks that {@link DefaultIsoGameObject#ALIGN} shifts the x coordinate.
     */
    @Test
    public void testPaintAlign1() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("TEST");
        try {
            final TestGameObject gameObject = mapModelCreator.newGameObject("arch", "name");
            gameObject.setAttributeInt(DefaultIsoGameObject.ALIGN, 5);
            mapModel.addGameObjectToMap(gameObject, new Point(0, 0), mapModelCreator.getTopmostInsertionMode());
        } finally {
            mapModel.endTransaction();
        }
        final TestMapRenderer renderer = newRenderer(mapModel);
        renderer.getFullImage();
        Assert.assertEquals("1.00 0.00 0.00 1.00 5.00 0.00\n", renderer.getPaintingOperations());
    }

    /**
     * Checks that {@link DefaultIsoGameObject#ALIGN} shifts the x coordinate.
     */
    @Test
    public void testPaintAlign2() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("TEST");
        try {
            final TestGameObject gameObject = mapModelCreator.newGameObject("arch", "name");
            gameObject.setAttributeInt(DefaultIsoGameObject.ALIGN, -5);
            mapModel.addGameObjectToMap(gameObject, new Point(0, 0), mapModelCreator.getTopmostInsertionMode());
        } finally {
            mapModel.endTransaction();
        }
        final TestMapRenderer renderer = newRenderer(mapModel);
        renderer.getFullImage();
        Assert.assertEquals("1.00 0.00 0.00 1.00 -5.00 0.00\n", renderer.getPaintingOperations());
    }

    /**
     * Checks that {@link DefaultIsoGameObject#Z} shifts the y coordinate.
     */
    @Test
    public void testPaintZ1() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("TEST");
        try {
            final TestGameObject gameObject = mapModelCreator.newGameObject("arch", "name");
            gameObject.setAttributeInt(DefaultIsoGameObject.Z, 5);
            mapModel.addGameObjectToMap(gameObject, new Point(0, 0), mapModelCreator.getTopmostInsertionMode());
        } finally {
            mapModel.endTransaction();
        }
        final TestMapRenderer renderer = newRenderer(mapModel);
        renderer.getFullImage();
        Assert.assertEquals("1.00 0.00 0.00 1.00 0.00 -5.00\n", renderer.getPaintingOperations());
    }

    /**
     * Checks that {@link DefaultIsoGameObject#Z} shifts the y coordinate.
     */
    @Test
    public void testPaintZ2() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("TEST");
        try {
            final TestGameObject gameObject = mapModelCreator.newGameObject("arch", "name");
            gameObject.setAttributeInt(DefaultIsoGameObject.Z, -5);
            mapModel.addGameObjectToMap(gameObject, new Point(0, 0), mapModelCreator.getTopmostInsertionMode());
        } finally {
            mapModel.endTransaction();
        }
        final TestMapRenderer renderer = newRenderer(mapModel);
        renderer.getFullImage();
        Assert.assertEquals("1.00 0.00 0.00 1.00 0.00 5.00\n", renderer.getPaintingOperations());
    }

    /**
     * Checks that {@link DefaultIsoGameObject#ZOOM} scales.
     */
    @Test
    public void testPaintZoom1() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("TEST");
        try {
            final TestGameObject gameObject = mapModelCreator.newGameObject("arch", "name");
            gameObject.setAttributeInt(DefaultIsoGameObject.ZOOM, 50);
            mapModel.addGameObjectToMap(gameObject, new Point(0, 0), mapModelCreator.getTopmostInsertionMode());
        } finally {
            mapModel.endTransaction();
        }
        final TestMapRenderer renderer = newRenderer(mapModel);
        renderer.getFullImage();
        Assert.assertEquals("0.500 0.00 0.00 0.500 0.00 0.00\n", renderer.getPaintingOperations());
    }

    /**
     * Checks that {@link DefaultIsoGameObject#ZOOM} scales.
     */
    @Test
    public void testPaintZoom2() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("TEST");
        try {
            final TestGameObject gameObject = mapModelCreator.newGameObject("arch", "name");
            gameObject.setAttributeInt(DefaultIsoGameObject.ZOOM, 100);
            mapModel.addGameObjectToMap(gameObject, new Point(0, 0), mapModelCreator.getTopmostInsertionMode());
        } finally {
            mapModel.endTransaction();
        }
        final TestMapRenderer renderer = newRenderer(mapModel);
        renderer.getFullImage();
        Assert.assertEquals("1.00 0.00 0.00 1.00 0.00 0.00\n", renderer.getPaintingOperations());
    }

    /**
     * Checks that {@link DefaultIsoGameObject#ZOOM} scales.
     */
    @Test
    public void testPaintZoom3() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("TEST");
        try {
            final TestGameObject gameObject = mapModelCreator.newGameObject("arch", "name");
            gameObject.setAttributeInt(DefaultIsoGameObject.ZOOM, 110);
            mapModel.addGameObjectToMap(gameObject, new Point(0, 0), mapModelCreator.getTopmostInsertionMode());
        } finally {
            mapModel.endTransaction();
        }
        final TestMapRenderer renderer = newRenderer(mapModel);
        renderer.getFullImage();
        Assert.assertEquals("1.10 0.00 0.00 1.10 0.00 0.00\n", renderer.getPaintingOperations()); // XXX: why not shift y = -0.1?
    }

    /**
     * Checks that {@link DefaultIsoGameObject#ZOOM} scales.
     */
    @Test
    public void testPaintZoom4() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("TEST");
        try {
            final TestGameObject gameObject = mapModelCreator.newGameObject("arch", "name");
            gameObject.setAttributeInt(DefaultIsoGameObject.ZOOM, 200);
            mapModel.addGameObjectToMap(gameObject, new Point(0, 0), mapModelCreator.getTopmostInsertionMode());
        } finally {
            mapModel.endTransaction();
        }
        final TestMapRenderer renderer = newRenderer(mapModel);
        renderer.getFullImage();
        Assert.assertEquals("2.00 0.00 0.00 2.00 0.00 -1.00\n", renderer.getPaintingOperations());
    }

    /**
     * Checks that a combination of attributes works as expected.
     */
    @Test
    public void testPaintCombined1() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("TEST");
        try {
            final TestGameObject gameObject = mapModelCreator.newGameObject("arch", "name");
            gameObject.setAttributeInt(DefaultIsoGameObject.ALIGN, 5);
            gameObject.setAttributeInt(DefaultIsoGameObject.ROTATE, 20);
            gameObject.setAttributeInt(DefaultIsoGameObject.Z, 15);
            gameObject.setAttributeInt(DefaultIsoGameObject.ZOOM, 75);
            mapModel.addGameObjectToMap(gameObject, new Point(0, 0), mapModelCreator.getTopmostInsertionMode());
        } finally {
            mapModel.endTransaction();
        }
        final TestMapRenderer renderer = newRenderer(mapModel);
        renderer.getFullImage();
        Assert.assertEquals("0.705 -0.257 0.257 0.705 5.00 -15.0\n", renderer.getPaintingOperations());
    }

    /**
     * Creates a new {@link TestMapRenderer} instance for a {@link MapModel}.
     * @param mapModel the map model for the renderer
     * @return the renderer instance
     */
    @NotNull
    private TestMapRenderer newRenderer(@NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel) {
        final MapViewSettings mapViewSettings = mapModelCreator.getMapViewSettings();
        final NamedFilter defaultNamedFilterList = new NamedFilter(Collections.<NamedGameObjectMatcher>emptyList());
        final FilterControl<TestGameObject, TestMapArchObject, TestArchetype> filterControl = new DefaultFilterControl<TestGameObject, TestMapArchObject, TestArchetype>(defaultNamedFilterList);
        final MapGrid mapGrid = new MapGrid(mapModel.getMapArchObject().getMapSize());
        final IsoMapSquareInfo isoMapSquareInfo = new IsoMapSquareInfo(1, 1, 1, 1);
        final MultiPositionData multiPositionData = new MultiPositionData(isoMapSquareInfo);
        final SystemIcons systemIcons = mapModelCreator.getSystemIcons();
        final GridMapSquarePainter gridMapSquarePainter = new GridMapSquarePainter(systemIcons);
        final GameObjectParser<TestGameObject, TestMapArchObject, TestArchetype> gameObjectParser = mapModelCreator.newGameObjectParser();
        return new TestMapRenderer(mapViewSettings, filterControl, mapModel, mapGrid, multiPositionData, isoMapSquareInfo, gridMapSquarePainter, gameObjectParser, systemIcons);
    }

}
