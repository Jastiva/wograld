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

package net.sf.gridarta.model.mapmodel;

import java.awt.Point;
import java.util.Iterator;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link TopLevelGameObjectIterator}.
 * @author Andreas Kirschbaum
 */
public class TopLevelGameObjectIteratorTest {

    /**
     * Checks the forward iterator on an empty map.
     */
    @Test
    public void testIteratorForwardEmpty() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final Iterator<TestGameObject> it = new TopLevelGameObjectIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, new Point(1, 1), +1, false);
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks the backward iterator on an empty map.
     */
    @Test
    public void testIteratorBackwardEmpty() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final Iterator<TestGameObject> it = new TopLevelGameObjectIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, new Point(1, 1), -1, false);
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks the forward iterator.
     */
    @Test
    public void testIteratorForward() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = createMap();
        final Iterator<TestGameObject> it = new TopLevelGameObjectIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, new Point(1, 1), +1, false);
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n4", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n5", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n1", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n2", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n3", it.next().getBestName());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks the backward iterator.
     */
    @Test
    public void testIteratorBackward() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = createMap();
        final Iterator<TestGameObject> it = new TopLevelGameObjectIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, new Point(1, 1), -1, false);
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n4", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n3", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n1", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n2", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n5", it.next().getBestName());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks the forward iterator with skipped first element.
     */
    @Test
    public void testIteratorForwardSkip() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = createMap();
        final Iterator<TestGameObject> it = new TopLevelGameObjectIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, new Point(1, 1), +1, true);
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n5", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n1", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n2", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n3", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n4", it.next().getBestName());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks the backward iterator with skipped first element.
     */
    @Test
    public void testIteratorBackwardSkip() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = createMap();
        final Iterator<TestGameObject> it = new TopLevelGameObjectIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, new Point(1, 1), -1, true);
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n3", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n1", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n2", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n5", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("n4", it.next().getBestName());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Creates a new {@link MapModel} instance filled with game objects.
     * @return the new map model instance
     */
    @NotNull
    private static MapModel<TestGameObject, TestMapArchObject, TestArchetype> createMap() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final InsertionMode<TestGameObject, TestMapArchObject, TestArchetype> insertionMode = mapModelCreator.getTopmostInsertionMode();
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "n1", 0, 0, insertionMode);
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "n2", 0, 0, insertionMode);
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "n3", 2, 0, insertionMode);
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "n4", 1, 1, insertionMode);
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "n5", 2, 1, insertionMode);

        //noinspection ConstantConditions
        mapModelCreator.insertGameObject(mapModel.getMapSquare(new Point(2, 0)).getFirst(), "arch", "i1");

        return mapModel;
    }

}
