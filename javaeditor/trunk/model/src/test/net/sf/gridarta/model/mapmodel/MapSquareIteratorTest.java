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
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link MapSquareIterator}.
 * @author Andreas Kirschbaum
 */
public class MapSquareIteratorTest {

    /**
     * Checks that the forward iterator returns all map squares.
     */
    @Test
    public void testIteratorForward() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final Iterator<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>> it = new MapSquareIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, null, +1, false);
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 1)), it.next());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks that the backward iterator returns all map squares.
     */
    @Test
    public void testIteratorBackward() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final Iterator<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>> it = new MapSquareIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, null, -1, false);
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 0)), it.next());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks that the forward iterator returns all map squares.
     */
    @Test
    public void testIteratorForwardStart() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final Iterator<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>> it = new MapSquareIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, new Point(0, 1), +1, false);
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 0)), it.next());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks that the backward iterator returns all map squares.
     */
    @Test
    public void testIteratorBackwardStart() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final Iterator<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>> it = new MapSquareIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, new Point(0, 1), -1, false);
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 1)), it.next());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks that the forward iterator returns all map squares when skip is
     * enabled.
     */
    @Test
    public void testIteratorForwardStartSkip() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final Iterator<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>> it = new MapSquareIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, new Point(0, 1), +1, true);
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 1)), it.next());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks that the backward iterator returns all map squares when skip is
     * enabled.
     */
    @Test
    public void testIteratorBackwardStartSkip() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final Iterator<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>> it = new MapSquareIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, new Point(0, 1), -1, true);
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 0)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(2, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(1, 1)), it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertSame(mapModel.getMapSquare(new Point(0, 1)), it.next());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks that invalid directions are rejected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIteratorDirection0() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final Iterator<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>> it = new MapSquareIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, null, 0, false);
    }

    /**
     * Checks that invalid directions are rejected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIteratorDirection2() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final Iterator<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>> it = new MapSquareIterator<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, null, 2, false);
    }

}
