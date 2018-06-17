/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
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

package net.sf.gridarta.model.io;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.TestMapControlCreator;
import net.sf.gridarta.model.mapmodel.InsertionMode;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.mapviewsettings.TestMapViewSettings;
import net.sf.gridarta.utils.Size2D;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link DefaultMapReader}.
 * @author Andreas Kirschbaum
 */
public class DefaultMapReaderTest {

    /**
     * Checks that map loading doesn't reorder multi-square game objects.
     * @throws DuplicateArchetypeException if the test fails
     * @throws IOException if the test fails
     */
    @Test
    public void testReorderMultiSquares() throws DuplicateArchetypeException, IOException {
        final TestMapControlCreator mapControlCreator = new TestMapControlCreator();
        final TestMapModelCreator mapModelCreator = mapControlCreator.getMapModelCreator();
        final File mapFile = File.createTempFile("gridarta", null);
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = mapControlCreator.newMapControl(mapFile, "test", new Size2D(2, 1));
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        final InsertionMode<TestGameObject, TestMapArchObject, TestArchetype> insertionMode = mapModelCreator.getTopmostInsertionMode();
        final TestArchetype arch1x1 = mapModelCreator.getArchetype("arch1x1");
        final TestArchetype arch2x1 = mapModelCreator.newArchetype("arch2x1");
        final TestArchetype arch2x1b = mapModelCreator.newArchetype("arch2x1b");
        arch2x1b.setMultiX(1);
        arch2x1.addTailPart(arch2x1b);
        mapModelCreator.getArchetypeSet().addArchetype(arch2x1);
        mapModel.insertArchToMap(arch1x1, null, new Point(0, 0), false);
        mapModel.insertArchToMap(arch1x1, null, new Point(1, 0), false);
        mapModel.insertArchToMap(arch2x1, null, new Point(0, 0), false);
        mapModel.insertArchToMap(arch1x1, null, new Point(0, 0), false);
        mapModel.insertArchToMap(arch1x1, null, new Point(1, 0), false);
        mapControl.save();
        final MapViewSettings mapViewSettings = new TestMapViewSettings();
        final DefaultMapReader<TestGameObject, TestMapArchObject, TestArchetype> reader = mapControlCreator.newMapReader(mapViewSettings, mapFile);
        final List<TestGameObject> objects = reader.getGameObjects();
        Assert.assertEquals(6, objects.size());
        Assert.assertEquals("arch1x1", objects.get(0).getArchetype().getArchetypeName());
        Assert.assertEquals("arch2x1", objects.get(1).getArchetype().getArchetypeName());
        Assert.assertEquals("arch1x1", objects.get(2).getArchetype().getArchetypeName());
        Assert.assertEquals("arch1x1", objects.get(3).getArchetype().getArchetypeName());
        Assert.assertEquals("arch1x1", objects.get(4).getArchetype().getArchetypeName());
        Assert.assertEquals("arch2x1b", objects.get(5).getArchetype().getArchetypeName());
    }

}
