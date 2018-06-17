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

import java.awt.Point;
import java.io.File;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.exitconnector.ExitConnectorModel;
import net.sf.gridarta.model.exitconnector.ExitLocation;
import net.sf.gridarta.model.exitconnector.TestExitConnectorModel;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.TestMapControlCreator;
import net.sf.gridarta.model.mapmanager.TestFileControl;
import net.sf.gridarta.model.mapmodel.CannotInsertGameObjectException;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.TestMapModelHelper;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Regression tests for {@link ExitConnectorActions}.
 * @author Andreas Kirschbaum
 */
public class ExitConnectorActionsTest {

    /**
     * The first map file.
     */
    private static final File MAP_FILE1 = new File("a");

    /**
     * The first map name.
     */
    private static final String MAP_NAME1 = "name1";

    /**
     * The second map file.
     */
    private static final File MAP_FILE2 = new File("b");

    /**
     * The second map name.
     */
    private static final String MAP_NAME2 = "name2";

    /**
     * The {@link TestMapControlCreator} for creating maps.
     */
    @Nullable
    private TestMapControlCreator testMapControlCreator;

    /**
     * The {@link TestMapModelHelper} instance.
     */
    @Nullable
    private TestMapModelHelper mapModelHelper;

    /**
     * Checks that {@link ExitConnectorActions#doExitCopy(boolean, MapControl,
     * Point)} does work.
     */
    @Test
    public void testExitCopy1() {
        final ExitConnectorModel model = new TestExitConnectorModel();
        final ExitConnectorActions<TestGameObject, TestMapArchObject, TestArchetype> actions = createActions(model);
        final Size2D mapSize = new Size2D(5, 5);
        assert testMapControlCreator != null;
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, mapSize);

        // active ==> enabled
        final Point point1 = new Point(3, 4);
        Assert.assertTrue(actions.doExitCopy(false, mapControl, point1));
        Assert.assertNull(model.getExitLocation());

        // perform copy
        Assert.assertTrue(actions.doExitCopy(true, mapControl, point1));
        assert testMapControlCreator != null;
        Assert.assertEquals(new ExitLocation(MAP_FILE1, point1, MAP_NAME1, testMapControlCreator.getPathManager()), model.getExitLocation());

        // unsaved map ==> disabled
        mapControl.getMapModel().setMapFile(null);
        Assert.assertFalse(actions.doExitCopy(false, mapControl, point1));
        assert testMapControlCreator != null;
        Assert.assertEquals(new ExitLocation(MAP_FILE1, point1, MAP_NAME1, testMapControlCreator.getPathManager()), model.getExitLocation()); // disabled -> unchanged model
    }

    /**
     * Checks that {@link ExitConnectorActions#doExitPaste(boolean, MapControl,
     * Point)} does work.
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testExitPaste1() throws CannotInsertGameObjectException {
        final ExitConnectorModel model = new TestExitConnectorModel();
        final ExitConnectorActions<TestGameObject, TestMapArchObject, TestArchetype> actions = createActions(model);
        final Size2D mapSize = new Size2D(5, 5);
        assert testMapControlCreator != null;
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, mapSize);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();

        final Point point1 = new Point(3, 4);
        final Point point2 = new Point(1, 2);

        assert testMapControlCreator != null;
        model.setExitLocation(new ExitLocation(MAP_FILE2, point2, MAP_NAME2, testMapControlCreator.getPathManager()));

        // no exit at cursor and no auto-create ==> disabled
        Assert.assertFalse(actions.doExitPaste(false, mapControl, point1));

        mapModel.beginTransaction("TEST");
        try {
            assert mapModelHelper != null;
            mapModelHelper.insertFloor(mapModel, point1);
        } finally {
            mapModel.endTransaction();
        }

        // no exit at cursor and no auto-create ==> disabled
        Assert.assertFalse(actions.doExitPaste(false, mapControl, point1));

        mapModel.beginTransaction("TEST");
        try {
            assert mapModelHelper != null;
            mapModelHelper.insertExit(mapModel, point1);
        } finally {
            mapModel.endTransaction();
        }

        // exit at cursor ==> enabled
        Assert.assertTrue(actions.doExitPaste(false, mapControl, point1));
        checkExit(mapModel, point1, 1, "", new Point(0, 0)); // "check only" ==> exit is unchanged

        // perform paste
        Assert.assertTrue(actions.doExitPaste(true, mapControl, point1));
        checkExit(mapModel, point1, 1, "b", point2);
    }

    /**
     * Checks that {@link ExitConnectorActions#doExitPaste(boolean, MapControl,
     * Point)} does not crash when connecting to an unsaved map.
     */
    @Test
    public void testExitPaste2() {
        final ExitConnectorModel model = new TestExitConnectorModel();
        final ExitConnectorActions<TestGameObject, TestMapArchObject, TestArchetype> actions = createActions(model);
        final Size2D mapSize = new Size2D(5, 5);

        assert testMapControlCreator != null;
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl1 = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, mapSize);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel1 = mapControl1.getMapModel();

        final Point point1 = new Point(3, 4);
        final Point point2 = new Point(1, 2);

        assert testMapControlCreator != null;
        model.setExitLocation(new ExitLocation(MAP_FILE2, point2, MAP_NAME2, testMapControlCreator.getPathManager()));

        model.setAutoCreateExit(true);

        // copy must fail for unsaved map
        mapModel1.setMapFile(null); // pretend unsaved map
        Assert.assertFalse(actions.doExitCopy(true, mapControl1, point1));

        // paste must succeed for unsaved map
        Assert.assertTrue(actions.doExitPaste(true, mapControl1, point1));
    }

    /**
     * Checks that {@link ExitConnectorActions#doExitConnect(boolean,
     * MapControl, Point)} does work.
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testExitConnect1() throws CannotInsertGameObjectException {
        final ExitConnectorModel model = new TestExitConnectorModel();
        final ExitConnectorActions<TestGameObject, TestMapArchObject, TestArchetype> actions = createActions(model);

        final Size2D mapSize = new Size2D(5, 5);
        assert testMapControlCreator != null;
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl1 = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, mapSize);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel1 = mapControl1.getMapModel();

        assert testMapControlCreator != null;
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl2 = testMapControlCreator.newMapControl(MAP_FILE2, MAP_NAME2, mapSize);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel2 = mapControl2.getMapModel();

        final Point point1 = new Point(3, 4);
        final Point point2 = new Point(1, 2);

        assert testMapControlCreator != null;
        model.setExitLocation(new ExitLocation(MAP_FILE2, point2, MAP_NAME2, testMapControlCreator.getPathManager()));

        // no exit at cursor and no auto-create ==> disabled
        Assert.assertFalse(actions.doExitConnect(false, mapControl1, point1));

        mapModel1.beginTransaction("TEST");
        try {
            assert mapModelHelper != null;
            mapModelHelper.insertFloor(mapModel1, point1);
        } finally {
            mapModel1.endTransaction();
        }

        // no exit at cursor and no auto-create ==> disabled
        Assert.assertFalse(actions.doExitConnect(false, mapControl1, point1));

        mapModel1.beginTransaction("TEST");
        try {
            assert mapModelHelper != null;
            mapModelHelper.insertExit(mapModel1, point1);
        } finally {
            mapModel1.endTransaction();
        }

        // no exit at source and no auto-create ==> disabled
        Assert.assertFalse(actions.doExitConnect(false, mapControl1, point1));

        mapModel2.beginTransaction("TEST");
        try {
            assert mapModelHelper != null;
            mapModelHelper.insertExit(mapModel2, point2);
        } finally {
            mapModel2.endTransaction();
        }

        // exit at source and cursor ==> enabled
        Assert.assertTrue(actions.doExitConnect(false, mapControl1, point1));
        checkExit(mapModel1, point1, 1, "", new Point(0, 0)); // "check only" ==> exit is unchanged
        checkExit(mapModel2, point2, 0, "", new Point(0, 0));

        // perform connect
        Assert.assertTrue(actions.doExitConnect(true, mapControl1, point1));
        checkExit(mapModel1, point1, 1, "b", point2);
        checkExit(mapModel2, point2, 0, "a", point1);
    }

    /**
     * Checks that {@link ExitConnectorActions#doExitConnect(boolean,
     * MapControl, Point)} does work when auto-creating exit objects.
     */
    @Test
    public void testExitConnectAuto1() {
        final ExitConnectorModel model = new TestExitConnectorModel();
        final ExitConnectorActions<TestGameObject, TestMapArchObject, TestArchetype> actions = createActions(model);

        final Size2D mapSize = new Size2D(5, 5);
        assert testMapControlCreator != null;
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl1 = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, mapSize);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel1 = mapControl1.getMapModel();

        assert testMapControlCreator != null;
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl2 = testMapControlCreator.newMapControl(MAP_FILE2, MAP_NAME2, mapSize);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel2 = mapControl2.getMapModel();

        final Point point1 = new Point(3, 4);
        final Point point2 = new Point(1, 2);

        assert testMapControlCreator != null;
        model.setExitLocation(new ExitLocation(MAP_FILE2, point2, MAP_NAME2, testMapControlCreator.getPathManager()));
        model.setAutoCreateExit(true);

        model.setExitArchetypeName("undefined");

        // fails due to undefined archetype
        Assert.assertFalse(actions.doExitConnect(true, mapControl1, point1));
        checkExit(mapModel1, point1, 0, null, null);
        checkExit(mapModel2, point2, 0, null, null);

        model.setExitArchetypeName("exit");

        // perform connect
        Assert.assertTrue(actions.doExitConnect(true, mapControl1, point1));
        checkExit(mapModel1, point1, 0, "b", point2);
        checkExit(mapModel2, point2, 0, "a", point1);

        // perform connect; auto-create will re-use existing objects
        Assert.assertTrue(actions.doExitConnect(true, mapControl1, point1));
        checkExit(mapModel1, point1, 0, "b", point2);
        checkExit(mapModel1, point1, 1, null, null);
        checkExit(mapModel2, point2, 0, "a", point1);
        checkExit(mapModel2, point2, 1, null, null);
    }

    /**
     * Checks that exit paths are correctly generated.
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testPath1() throws CannotInsertGameObjectException {
        // both root ==> relative
        testPath("/a", "/b", "b"); // both root ==> relative

        // to or from root ==> absolute
        testPath("/HallOfSelection", "/world/world_104_115", "/world/world_104_115");
        testPath("/world/world_104_115", "/HallOfSelection", "/HallOfSelection");

        // same top-level component ==> relative
        testPath("/a/b/c/d", "/a/d/e", "../../d/e");
        testPath("/a/b/c/d", "/a/b/d", "../d");
        testPath("/a/b/c", "/a/b/d", "d");
        testPath("/a/b/c", "/a/b/c/d", "c/d");

        // else ==> absolute
        testPath("/a/b/c", "/b/c/d", "/b/c/d");
        testPath("/a/b", "/b", "/b");
        testPath("/a", "/b/c", "/b/c");
    }

    /**
     * Checks that an exit path is correctly generated.
     * @param mapPathFrom the map path to connect from; this map contains the
     * exit object
     * @param mapPathTo the map path to connect to
     * @param expectedExitPath the expected map in the exit object
     * @throws CannotInsertGameObjectException if the test fails
     */
    private void testPath(@NotNull final String mapPathFrom, @NotNull final String mapPathTo, @NotNull final String expectedExitPath) throws CannotInsertGameObjectException {
        final ExitConnectorModel model = new TestExitConnectorModel();
        final ExitConnectorActions<TestGameObject, TestMapArchObject, TestArchetype> actions = createActions(model);

        assert testMapControlCreator != null;
        final File mapDir = testMapControlCreator.getGlobalSettings().getMapsDirectory();
        final File mapFileFrom = new File(mapDir, mapPathFrom);
        final File mapFileTo = new File(mapDir, mapPathTo);

        final Size2D mapSize = new Size2D(5, 5);
        assert testMapControlCreator != null;
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(mapFileFrom, MAP_NAME1, mapSize);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();

        final Point pointFrom = new Point(3, 4);
        final Point pointTo = new Point(1, 2);

        assert testMapControlCreator != null;
        model.setExitLocation(new ExitLocation(mapFileTo, pointTo, MAP_NAME2, testMapControlCreator.getPathManager()));
        mapModel.beginTransaction("TEST");
        try {
            assert mapModelHelper != null;
            mapModelHelper.insertExit(mapModel, pointFrom);
        } finally {
            mapModel.endTransaction();
        }

        // perform connect
        Assert.assertTrue(actions.doExitPaste(true, mapControl, pointFrom));
        checkExit(mapModel, pointFrom, 0, expectedExitPath, pointTo);
    }

    /**
     * Initializes the test case.
     * @throws DuplicateArchetypeException if the test case fails
     */
    @Before
    public void setUp() throws DuplicateArchetypeException {
        testMapControlCreator = new TestMapControlCreator();
        mapModelHelper = testMapControlCreator.newMapModelCreator();
    }

    /**
     * Creates a new {@link ExitConnectorActions} instance.
     * @param model the exit connector model to use
     * @return the new exit connector actions instance
     */
    private ExitConnectorActions<TestGameObject, TestMapArchObject, TestArchetype> createActions(@NotNull final ExitConnectorModel model) {
        final TestMapControlCreator tmpTestMapControlCreator = testMapControlCreator;
        assert tmpTestMapControlCreator != null;
        return new ExitConnectorActions<TestGameObject, TestMapArchObject, TestArchetype>(model, tmpTestMapControlCreator.getExitMatcher(), tmpTestMapControlCreator.getArchetypeSet(), tmpTestMapControlCreator.getMapManager(), new TestFileControl(), tmpTestMapControlCreator.getPathManager(), tmpTestMapControlCreator.getInsertionModeSet());
    }

    /**
     * Checks that a map model contains an exit game object.
     * @param mapModel the map model to check
     * @param point the position to check
     * @param index the index to check
     * @param exitPath the expected exit path or <code>null</code> if no exit
     * game object is expected
     * @param exitPoint the expected exit destination or <code>null</code> if no
     * exit game object is expected
     */
    private static void checkExit(@NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel, @NotNull final Point point, final int index, @Nullable final String exitPath, @Nullable final Point exitPoint) {
        int thisIndex = 0;
        for (final BaseObject<?, ?, ?, ?> gameObject : mapModel.getMapSquare(point)) {
            if (thisIndex == index) {
                if (gameObject.getTypeNo() != TestMapModelHelper.EXIT_TYPE) {
                    break;
                }

                if (exitPath == null || exitPoint == null) {
                    Assert.fail("exit found but none expected");
                    throw new AssertionError();
                }

                Assert.assertEquals(exitPath, gameObject.getAttributeString(BaseObject.SLAYING));
                Assert.assertEquals(exitPoint.x, gameObject.getAttributeInt(BaseObject.HP));
                Assert.assertEquals(exitPoint.y, gameObject.getAttributeInt(BaseObject.SP));
                return;
            }

            thisIndex++;
        }

        if (exitPath != null || exitPoint != null) {
            Assert.fail("no exit found");
        }
    }

}
