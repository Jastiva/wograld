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

package net.sf.gridarta.gui.mapmenu;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.TestMapControlCreator;
import net.sf.gridarta.utils.Size2D;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link MapMenuManager}.
 * @author Andreas Kirschbaum
 */
public class MapMenuManagerTest {

    /**
     * Checks that {@link MapMenuManager} does not duplicate bookmark entries
     * when a map is saved.
     * @throws DuplicateArchetypeException if the test fails
     * @throws IOException if the test fails
     */
    @Test
    public void testSaveBookmarks() throws DuplicateArchetypeException, IOException {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final File file = File.createTempFile("gridarta", null);
        final MapMenuPreferences mapMenuPreferences = new TestMapMenuPreferences();
        try {
            final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl1 = testMapControlCreator.newMapControl(file, "name1", new Size2D(1, 1));
            final MapMenuEntryMap mapMenuEntry = new MapMenuEntryMap(new File("test1"), "title1");
            mapMenuPreferences.addEntry(mapMenuEntry);
            Assert.assertEquals(1, mapMenuPreferences.getMapMenu().size());
            mapControl1.save();
            Assert.assertEquals(1, mapMenuPreferences.getMapMenu().size());
        } finally {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

}
