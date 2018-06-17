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

package net.sf.gridarta.model.mapmodel;

import java.awt.Point;
import java.util.Iterator;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.match.TypeNrsGameObjectMatcher;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Regression tests for {@link AutoInsertionMode}.
 * @author Andreas Kirschbaum
 */
public class AutoInsertionModeTest {

    /**
     * The archetype type for floor objects.
     */
    private static final int TYPE_FLOOR = 1;

    /**
     * The archetype type for wall objects.
     */
    private static final int TYPE_WALL = 2;

    /**
     * The archetype type for objects to be put below the floor.
     */
    private static final int TYPE_BELOW_FLOOR = 3;

    /**
     * The archetype type for system objects.
     */
    private static final int TYPE_SYSTEM = 4;

    /**
     * A {@link GameObjectMatcher} that matches floor objects.
     */
    @NotNull
    private final GameObjectMatcher floorGameObjectMatcher = new TypeNrsGameObjectMatcher(TYPE_FLOOR);

    /**
     * A {@link GameObjectMatcher} that matches wall objects.
     */
    @NotNull
    private final GameObjectMatcher wallGameObjectMatcher = new TypeNrsGameObjectMatcher(TYPE_WALL);

    /**
     * A {@link GameObjectMatcher} that matches objects to be put below the
     * floor.
     */
    @NotNull
    private final GameObjectMatcher belowFloorGameObjectMatcher = new TypeNrsGameObjectMatcher(TYPE_BELOW_FLOOR);

    /**
     * A {@link GameObjectMatcher} that matches system objects.
     */
    @NotNull
    private final GameObjectMatcher systemGameObjectMatcher = new TypeNrsGameObjectMatcher(TYPE_SYSTEM);

    /**
     * The auto-insertion mode.
     */
    @NotNull
    private final InsertionMode<TestGameObject, TestMapArchObject, TestArchetype> autoInsertionMode = new AutoInsertionMode<TestGameObject, TestMapArchObject, TestArchetype>(floorGameObjectMatcher, wallGameObjectMatcher, belowFloorGameObjectMatcher, systemGameObjectMatcher);

    /**
     * The topmost-insertion mode.
     */
    @NotNull
    private final InsertionMode<TestGameObject, TestMapArchObject, TestArchetype> topmostInsertionMode = new TopmostInsertionMode<TestGameObject, TestMapArchObject, TestArchetype>();

    /**
     * The {@link TestMapModelCreator} instance.
     */
    private TestMapModelCreator mapModelCreator;

    /**
     * Checks that the auto-insertion mode works as expected.
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testInsertSystemObject1() throws DuplicateArchetypeException {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("test");
        try {
            mapModelCreator.addGameObjectToMap(mapModel, "floor", "1", 0, 0, topmostInsertionMode);
            mapModelCreator.addGameObjectToMap(mapModel, "wall", "2", 0, 0, topmostInsertionMode);
            mapModelCreator.addGameObjectToMap(mapModel, "sys", "3", 0, 0, topmostInsertionMode);
            mapModelCreator.addGameObjectToMap(mapModel, "wall", "4", 0, 0, autoInsertionMode);
        } finally {
            mapModel.endTransaction();
        }
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare = mapModel.getMapSquare(new Point(0, 0));
        final Iterator<TestGameObject> it = mapSquare.iterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("1", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("4", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("3", it.next().getBestName());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Checks that the auto-insertion mode works as expected.
     */
    @Test
    public void testInsertSystemObject2() {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("test");
        try {
            mapModelCreator.addGameObjectToMap(mapModel, "floor", "1", 0, 0, topmostInsertionMode);
            mapModelCreator.addGameObjectToMap(mapModel, "sys", "3", 0, 0, topmostInsertionMode);
            mapModelCreator.addGameObjectToMap(mapModel, "wall", "4", 0, 0, autoInsertionMode);
        } finally {
            mapModel.endTransaction();
        }
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare = mapModel.getMapSquare(new Point(0, 0));
        final Iterator<TestGameObject> it = mapSquare.iterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("1", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("4", it.next().getBestName());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("3", it.next().getBestName());
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Initializes the tests.
     * @throws DuplicateArchetypeException if the test fails
     */
    @Before
    public void setUp() throws DuplicateArchetypeException {
        mapModelCreator = new TestMapModelCreator(false);

        final TestArchetype floorArchetype = mapModelCreator.newArchetype("floor");
        floorArchetype.setAttributeInt(BaseObject.TYPE, TYPE_FLOOR);
        mapModelCreator.getArchetypeSet().addArchetype(floorArchetype);

        final TestArchetype wallArchetype = mapModelCreator.newArchetype("wall");
        wallArchetype.setAttributeInt(BaseObject.TYPE, TYPE_WALL);
        mapModelCreator.getArchetypeSet().addArchetype(wallArchetype);

        final TestArchetype sysArchetype = mapModelCreator.newArchetype("sys");
        sysArchetype.setAttributeInt(BaseObject.TYPE, TYPE_SYSTEM);
        mapModelCreator.getArchetypeSet().addArchetype(sysArchetype);
    }

}
