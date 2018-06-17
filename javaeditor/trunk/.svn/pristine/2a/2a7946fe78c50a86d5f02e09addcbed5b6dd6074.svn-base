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

package net.sf.gridarta.gui.copybuffer;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.test.TestMapControlCreatorUtils;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.TestMapControlCreator;
import net.sf.gridarta.model.mapgrid.SelectionMode;
import net.sf.gridarta.model.mapmodel.CannotInsertGameObjectException;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.TestMapModelHelper;
import net.sf.gridarta.utils.Size2D;
import org.junit.Test;

/**
 * Regression tests for {@link CopyBuffer}.
 * @author Andreas Kirschbaum
 */
public class CopyBufferTest {

    /**
     * The first map file.
     */
    private static final File MAP_FILE1 = new File("a");

    /**
     * The first map name.
     */
    private static final String MAP_NAME1 = "name1";

    /**
     * Checks that {@link CopyBuffer#cut(MapView, Rectangle)} followed by {@link
     * CopyBuffer#paste(MapView, Point)} does work.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testCutPaste1() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(3, 3));

        final Point point = new Point(1, 1);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final TestGameObject ob1 = mapModelHelper.insertFloor(mapModel, point);
        final TestGameObject ob2 = mapModelHelper.insertMob21(ob1);

        TestMapModelHelper.checkMapContents(mapModel, "||", "|floor|", "||");
        TestMapModelHelper.checkContents(ob1, ob2);

        // select + cut
        final CopyBuffer<TestGameObject, TestMapArchObject, TestArchetype> copyBuffer = TestMapControlCreatorUtils.newCopyBuffer(testMapControlCreator);
        final MapView<TestGameObject, TestMapArchObject, TestArchetype> mapView = TestMapControlCreatorUtils.newMapView(mapControl);
        mapView.getMapGrid().select(new Point(1, 1), SelectionMode.ADD);
        copyBuffer.cut(mapView, new Rectangle(1, 1, 1, 1));

        TestMapModelHelper.checkMapContents(mapModel, "||", "||", "||");

        // paste
        copyBuffer.paste(mapView, new Point(1, 1));

        TestMapModelHelper.checkMapContents(mapModel, "||", "|floor|", "||");
    }

}
