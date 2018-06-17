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

package net.sf.gridarta.model.floodfill;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapmodel.CannotInsertGameObjectException;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import net.sf.gridarta.model.mapmodel.TestMapModelHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link FillUtils}.
 * @author Andreas Kirschbaum
 */
public class FillUtilsTest {

    /**
     * Checks that {@link FillUtils#fill(MapModel, Collection,
     * net.sf.gridarta.model.mapmodel.InsertionMode, List, int, boolean)} works
     * correctly when skipping adjacent squares.
     * @throws DuplicateArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testFillAdjacent() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final TestMapModelHelper mapModelHelper = mapModelCreator.newTestMapModelHelper();
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(4, 5);

        final TestArchetype head = mapModelCreator.getArchetype("2x2a");
        head.setAttributeInt(BaseObject.TYPE, 1);
        final TestArchetype tail1 = mapModelCreator.getArchetype("2x2b");
        tail1.setMultiX(1);
        head.addTailPart(tail1);
        final TestArchetype tail2 = mapModelCreator.getArchetype("2x2c");
        tail2.setMultiY(1);
        head.addTailPart(tail2);
        final TestArchetype tail3 = mapModelCreator.getArchetype("2x2d");
        tail3.setMultiX(1);
        tail3.setMultiY(1);
        head.addTailPart(tail3);

        mapModel.beginTransaction("TEST");
        mapModelHelper.insertArchetype(mapModel, new Point(0, 2), head, false);

        final Collection<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>> selection = new ArrayList<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>>();
        selection.add(mapModel.getMapSquare(new Point(2, 0)));
        final List<TestGameObject> gameObjects = new ArrayList<TestGameObject>();
        gameObjects.add(mapModelCreator.getGameObjectFactory().createGameObject(head));

        FillUtils.fill(mapModel, selection, mapModelCreator.getInsertionModeSet().getTopmostInsertionMode(), gameObjects, 100, true);
        Assert.assertTrue(mapModel.getMapSquare(new Point(2, 0)).isEmpty());

        FillUtils.fill(mapModel, selection, mapModelCreator.getInsertionModeSet().getTopmostInsertionMode(), gameObjects, 100, false);
        Assert.assertFalse(mapModel.getMapSquare(new Point(2, 0)).isEmpty());
    }

}
