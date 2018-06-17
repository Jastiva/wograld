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

package net.sf.gridarta.model.mapmanager;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.TestMapControlCreator;
import net.sf.gridarta.model.mapmodel.CannotInsertGameObjectException;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.TestMapModelHelper;
import net.sf.gridarta.utils.Size2D;
import org.junit.Test;

/**
 * Regression tests for {@link DefaultMapManager}.
 * @author Andreas Kirschbaum
 */
public class DefaultMapManagerTest {

    /**
     * A map that has been resized must still be revertable.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testRevertResizedMap() throws CannotInsertGameObjectException, DuplicateArchetypeException, IOException {
        final File file = File.createTempFile("gridarta", null);
        try {
            final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
            final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
            final Size2D originalSize = new Size2D(2, 1);
            final Size2D newSize = new Size2D(1, 2);
            final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(file, "test", originalSize);
            final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
            final MapManager<TestGameObject, TestMapArchObject, TestArchetype> mapManager = testMapControlCreator.getMapManager();

            mapModel.beginTransaction("test");
            try {
                mapModelHelper.insertFloor(mapModel, new Point(1, 0));
            } finally {
                mapModel.endTransaction();
            }
            mapControl.save();
            TestMapModelHelper.checkMapContents(mapModel, "|floor");

            mapModel.beginTransaction("test");
            try {
                mapModel.getMapArchObject().setMapSize(newSize);
            } finally {
                mapModel.endTransaction();
            }
            mapModel.beginTransaction("test");
            try {
                mapModelHelper.insertFloor(mapModel, new Point(0, 1));
            } finally {
                mapModel.endTransaction();
            }
            TestMapModelHelper.checkMapContents(mapModel, "", "floor");

            mapModel.beginTransaction("test");
            try {
                mapManager.revert(mapControl);
            } finally {
                mapModel.endTransaction();
            }
            TestMapModelHelper.checkMapContents(mapModel, "|floor");
        } finally {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

}
