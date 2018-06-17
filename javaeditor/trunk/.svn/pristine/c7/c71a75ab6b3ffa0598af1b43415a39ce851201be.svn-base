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

package net.sf.gridarta.actions;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import net.sf.gridarta.gui.undo.UndoControl;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.autojoin.AutojoinListsHelper;
import net.sf.gridarta.model.autojoin.IllegalAutojoinListException;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.TestMapControlCreator;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.CannotInsertGameObjectException;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import net.sf.gridarta.model.mapmodel.TestMapModelHelper;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link UndoActions}.
 * @author Andreas Kirschbaum
 */
public class UndoActionsTest {

    /**
     * Checks that undo correctly sets the "face" attribute.
     * @throws IllegalAutojoinListException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testInsert1() throws CannotInsertGameObjectException, DuplicateArchetypeException, IllegalAutojoinListException {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        final TestMapControlCreator mapControlCreator = new TestMapControlCreator();
        final TestMapModelCreator mapModelCreator = mapControlCreator.getMapModelCreator();
        final AutojoinListsHelper autojoinListsHelper = mapControlCreator.newAutojoinListsHelper();
        for (int i = 0; i < 16; i++) {
            final TestArchetype a = mapModelCreator.getArchetype("a" + i);
            a.setAttributeString(BaseObject.FACE, "face" + i);
        }
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14", "a15");

        final MapManager<TestGameObject, TestMapArchObject, TestArchetype> mapManager = mapControlCreator.newMapManager();
        final UndoControl<TestGameObject, TestMapArchObject, TestArchetype> undoControl = new UndoControl<TestGameObject, TestMapArchObject, TestArchetype>(mapManager, mapModelCreator.getGameObjectFactory(), mapModelCreator.getGameObjectMatchers());

        final TestMapArchObject mapArchObject = new TestMapArchObject();
        mapArchObject.setMapSize(new Size2D(2, 1));
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = mapManager.newMap(null, mapArchObject, null, true);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        final TestMapModelHelper testMapModelHelper = mapModelCreator.newTestMapModelHelper();

        final TestArchetype a0 = mapModelCreator.getArchetype("a0");

        mapModelCreator.getMapViewSettings().setAutojoin(true);
        mapModel.beginTransaction("TEST");
        try {
            testMapModelHelper.insertArchetype(mapModel, 0, 0, a0, false);
        } finally {
            mapModel.endTransaction();
        }

        TestMapModelHelper.checkMapContents(mapModel, "a0|");
        checkFace(mapModel, 0, 0, "face0");

        mapModel.beginTransaction("TEST");
        try {
            testMapModelHelper.insertArchetype(mapModel, 1, 0, a0, true);
        } finally {
            mapModel.endTransaction();
        }

        TestMapModelHelper.checkMapContents(mapModel, "a2|a8");
        checkFace(mapModel, 0, 0, "face2");
        checkFace(mapModel, 1, 0, "face8");

        undoControl.undo();
        TestMapModelHelper.checkMapContents(mapModel, "a0|");
        checkFace(mapModel, 0, 0, "face0");
    }

    /**
     * Checks the face name of the first object in a map square.
     * @param mapModel the map model to check
     * @param x the x coordinate of the map square to check
     * @param y the y coordinate of the map square to check
     * @param face the expected face name
     */
    private static void checkFace(@NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel, final int x, final int y, @NotNull final String face) {
        final MapSquare<?, ?, ?> mapSquare = mapModel.getMapSquare(new Point(x, y));
        final BaseObject<?, ?, ?, ?> object = mapSquare.getFirst();
        Assert.assertNotNull(object);
        Assert.assertEquals(face, object.getAttributeString(BaseObject.FACE));
    }

}
