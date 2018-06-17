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

package net.sf.gridarta.gui.panel.selectedsquare;

import java.awt.Point;
import java.io.File;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.TestMapControlCreator;
import net.sf.gridarta.model.mapmodel.CannotInsertGameObjectException;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapmodel.TestMapModelHelper;
import net.sf.gridarta.utils.Size2D;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link SelectedSquareActions}.
 * @author Andreas Kirschbaum
 */
public class SelectedSquareActionsTest {

    /**
     * The first map file.
     */
    private static final File MAP_FILE1 = new File("a");

    /**
     * The first map name.
     */
    private static final String MAP_NAME1 = "name1";

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareTop(boolean)} does
     * work for single-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareTopSingle() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(1, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point = new Point(0, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare = mapModel.getMapSquare(point);
        final TestGameObject ob2 = mapModelHelper.insertFloor(mapModel, point);
        final TestGameObject ob1 = mapModelHelper.insertExit(mapModel, point);

        TestMapModelHelper.checkContents(mapSquare, ob2, ob1);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareTop(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareTop(true));

        selectedSquareModel.setSelectedMapSquare(mapSquare, null);
        selectedSquareModel.setSelectedGameObject(ob2);

        // [ob2, ob1] => ob2 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareTop(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareTop(true));
        TestMapModelHelper.checkContents(mapSquare, ob1, ob2);

        // [ob1, ob2] => ob2 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareTop(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareTop(true));
        TestMapModelHelper.checkContents(mapSquare, ob1, ob2);
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareTop(boolean)} does
     * work for multi-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareTopMulti() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(2, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point pointHead = new Point(0, 0);
        final Point pointTail = new Point(1, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareHead = mapModel.getMapSquare(pointHead);
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareTail = mapModel.getMapSquare(pointTail);
        final TestGameObject ob1Head = mapModelHelper.insertFloor(mapModel, pointHead);
        final TestGameObject ob1Tail = mapModelHelper.insertFloor(mapModel, pointTail);
        final TestGameObject ob2Head = mapModelHelper.insertMob21(mapModel, pointHead);
        final TestGameObject ob2Tail = ob2Head.getMultiNext();

        TestMapModelHelper.checkContents(mapSquareHead, ob1Head, ob2Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob1Tail, ob2Tail);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareTop(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareTop(true));

        selectedSquareModel.setSelectedMapSquare(mapSquareHead, null);
        selectedSquareModel.setSelectedGameObject(ob1Head);

        // [ob1, ob2] => ob1 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareTop(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareTop(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob1Tail, ob2Tail);

        // [ob2, ob1] => ob1 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareTop(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareTop(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob1Tail, ob2Tail);

        selectedSquareModel.setSelectedMapSquare(mapSquareTail, null);
        selectedSquareModel.setSelectedGameObject(ob1Tail);

        // [ob1, ob2] => ob1 can move // XXX: this probably should be changed: moving tail parts is not sensible
        Assert.assertTrue(selectedSquareActions.doMoveSquareTop(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareTop(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob2Tail, ob1Tail);

        // [ob2, ob1] => ob1 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareTop(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareTop(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob2Tail, ob1Tail);
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareUp(boolean)} does
     * work for single-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareUpSingle() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(1, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point = new Point(0, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare = mapModel.getMapSquare(point);
        final TestGameObject ob2 = mapModelHelper.insertFloor(mapModel, point);
        final TestGameObject ob1 = mapModelHelper.insertExit(mapModel, point);

        TestMapModelHelper.checkContents(mapSquare, ob2, ob1);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareUp(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareUp(true));

        selectedSquareModel.setSelectedMapSquare(mapSquare, null);
        selectedSquareModel.setSelectedGameObject(ob2);

        // [ob2, ob1] => ob2 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareUp(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareUp(true));
        TestMapModelHelper.checkContents(mapSquare, ob1, ob2);

        // [ob1, ob2] => ob2 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareUp(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareUp(true));
        TestMapModelHelper.checkContents(mapSquare, ob1, ob2);
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareUp(boolean)} does
     * work for multi-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareUpMulti() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(2, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point pointHead = new Point(0, 0);
        final Point pointTail = new Point(1, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareHead = mapModel.getMapSquare(pointHead);
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareTail = mapModel.getMapSquare(pointTail);
        final TestGameObject ob2Head = mapModelHelper.insertFloor(mapModel, pointHead);
        final TestGameObject ob2Tail = mapModelHelper.insertFloor(mapModel, pointTail);
        final TestGameObject ob1Head = mapModelHelper.insertMob21(mapModel, pointHead);
        final TestGameObject ob1Tail = ob1Head.getMultiNext();

        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob2Tail, ob1Tail);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareUp(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareUp(true));

        selectedSquareModel.setSelectedMapSquare(mapSquareHead, null);
        selectedSquareModel.setSelectedGameObject(ob2Head);

        // [ob2, ob1] => ob2 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareUp(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareUp(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob1Head, ob2Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob2Tail, ob1Tail);

        // [ob1, ob2] => ob2 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareUp(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareUp(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob1Head, ob2Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob2Tail, ob1Tail);

        selectedSquareModel.setSelectedMapSquare(mapSquareTail, null);
        selectedSquareModel.setSelectedGameObject(ob2Tail);

        // [ob2, ob1] => ob2 can move // XXX: this probably should be changed: moving tail parts is not sensible
        Assert.assertTrue(selectedSquareActions.doMoveSquareUp(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareUp(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob1Head, ob2Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob1Tail, ob2Tail);

        // [ob1, ob2] => ob2 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareUp(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareUp(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob1Head, ob2Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob1Tail, ob2Tail);
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareDown(boolean)} does
     * work for single-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareDownSingle() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(1, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point = new Point(0, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare = mapModel.getMapSquare(point);
        final TestGameObject ob1 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob2 = mapModelHelper.insertFloor(mapModel, point);

        TestMapModelHelper.checkContents(mapSquare, ob1, ob2);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareDown(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareDown(true));

        selectedSquareModel.setSelectedMapSquare(mapSquare, null);
        selectedSquareModel.setSelectedGameObject(ob2);

        // [ob1, ob2] => ob2 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareDown(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareDown(true));
        TestMapModelHelper.checkContents(mapSquare, ob2, ob1);

        // [ob2, ob1] => ob2 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareDown(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareDown(true));
        TestMapModelHelper.checkContents(mapSquare, ob2, ob1);
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareDown(boolean)} does
     * work for multi-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareDownMulti() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(2, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point pointHead = new Point(0, 0);
        final Point pointTail = new Point(1, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareHead = mapModel.getMapSquare(pointHead);
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareTail = mapModel.getMapSquare(pointTail);
        final TestGameObject ob1Head = mapModelHelper.insertMob21(mapModel, pointHead);
        final TestGameObject ob1Tail = ob1Head.getMultiNext();
        final TestGameObject ob2Head = mapModelHelper.insertFloor(mapModel, pointHead);
        final TestGameObject ob2Tail = mapModelHelper.insertFloor(mapModel, pointTail);

        TestMapModelHelper.checkContents(mapSquareHead, ob1Head, ob2Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob1Tail, ob2Tail);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareDown(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareDown(true));

        selectedSquareModel.setSelectedMapSquare(mapSquareHead, null);
        selectedSquareModel.setSelectedGameObject(ob2Head);

        // [ob2, ob1] => ob2 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareDown(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareDown(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob1Tail, ob2Tail);

        // [ob1, ob2] => ob2 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareDown(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareDown(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob1Tail, ob2Tail);

        selectedSquareModel.setSelectedMapSquare(mapSquareTail, null);
        selectedSquareModel.setSelectedGameObject(ob2Tail);

        // [ob2, ob1] => ob2 can move // XXX: this probably should be changed: moving tail parts is not sensible
        Assert.assertTrue(selectedSquareActions.doMoveSquareDown(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareDown(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob2Tail, ob1Tail);

        // [ob1, ob2] => ob2 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareDown(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareDown(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob2Tail, ob1Tail);
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareBottom(boolean)}
     * does work for single-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareBottomSingle() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(1, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point = new Point(0, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare = mapModel.getMapSquare(point);
        final TestGameObject ob1 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob2 = mapModelHelper.insertFloor(mapModel, point);

        TestMapModelHelper.checkContents(mapSquare, ob1, ob2);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareBottom(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareBottom(true));

        selectedSquareModel.setSelectedMapSquare(mapSquare, null);
        selectedSquareModel.setSelectedGameObject(ob2);

        // [ob1, ob2] => ob2 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareBottom(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareBottom(true));
        TestMapModelHelper.checkContents(mapSquare, ob2, ob1);

        // [ob2, ob1] => ob2 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareBottom(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareBottom(true));
        TestMapModelHelper.checkContents(mapSquare, ob2, ob1);
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareBottom(boolean)}
     * does work for multi-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareBottomMulti() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(2, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point pointHead = new Point(0, 0);
        final Point pointTail = new Point(1, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareHead = mapModel.getMapSquare(pointHead);
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareTail = mapModel.getMapSquare(pointTail);
        final TestGameObject ob1Head = mapModelHelper.insertMob21(mapModel, pointHead);
        final TestGameObject ob1Tail = ob1Head.getMultiNext();
        final TestGameObject ob2Head = mapModelHelper.insertFloor(mapModel, pointHead);
        final TestGameObject ob2Tail = mapModelHelper.insertFloor(mapModel, pointTail);

        TestMapModelHelper.checkContents(mapSquareHead, ob1Head, ob2Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob1Tail, ob2Tail);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareBottom(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareBottom(true));

        selectedSquareModel.setSelectedMapSquare(mapSquareHead, null);
        selectedSquareModel.setSelectedGameObject(ob2Head);

        // [ob2, ob1] => ob2 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareBottom(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareBottom(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob1Tail, ob2Tail);

        // [ob1, ob2] => ob2 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareBottom(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareBottom(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob1Tail, ob2Tail);

        selectedSquareModel.setSelectedMapSquare(mapSquareTail, null);
        selectedSquareModel.setSelectedGameObject(ob2Tail);

        // [ob2, ob1] => ob2 can move // XXX: this probably should be changed: moving tail parts is not sensible
        Assert.assertTrue(selectedSquareActions.doMoveSquareBottom(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareBottom(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob2Tail, ob1Tail);

        // [ob1, ob2] => ob2 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareBottom(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareBottom(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob2Head, ob1Head);
        TestMapModelHelper.checkContents(mapSquareTail, ob2Tail, ob1Tail);
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareEnv(boolean)} does
     * work for single-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareEnvSingle1() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(1, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point = new Point(0, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare = mapModel.getMapSquare(point);
        final TestGameObject ob1 = mapModelHelper.insertExit(mapModel, point);

        TestMapModelHelper.checkContents(mapSquare, ob1);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(true));

        selectedSquareModel.setSelectedMapSquare(mapSquare, null);
        selectedSquareModel.setSelectedGameObject(ob1);

        // [ob1] => ob1 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(true));
        TestMapModelHelper.checkContents(mapSquare, ob1);
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareEnv(boolean)} does
     * work for single-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareEnvSingle2() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(1, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point = new Point(0, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare = mapModel.getMapSquare(point);
        final TestGameObject ob1 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob2 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob3 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob4 = mapModelHelper.insertExit(ob2);

        TestMapModelHelper.checkContents(mapSquare, ob1, ob2, ob3);
        TestMapModelHelper.checkContents(ob2, ob4);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(true));

        selectedSquareModel.setSelectedMapSquare(mapSquare, null);
        selectedSquareModel.setSelectedGameObject(ob4);

        // ob4 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareEnv(true));
        TestMapModelHelper.checkContents(mapSquare, ob1, ob4, ob2, ob3);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4, selectedSquareModel.getSelectedGameObject());

        // ob4 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(true));
        TestMapModelHelper.checkContents(mapSquare, ob1, ob4, ob2, ob3);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4, selectedSquareModel.getSelectedGameObject());
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareEnv(boolean)} does
     * work for single-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareEnvSingle3() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(1, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point = new Point(0, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare = mapModel.getMapSquare(point);
        final TestGameObject ob1 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob2 = mapModelHelper.insertExit(ob1);
        final TestGameObject ob3 = mapModelHelper.insertExit(ob2);
        final TestGameObject ob4 = mapModelHelper.insertExit(ob2);
        final TestGameObject ob5 = mapModelHelper.insertExit(ob2);

        TestMapModelHelper.checkContents(mapSquare, ob1);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3, ob4, ob5);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(true));

        selectedSquareModel.setSelectedMapSquare(mapSquare, null);
        selectedSquareModel.setSelectedGameObject(ob4);

        // ob4 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareEnv(true));
        TestMapModelHelper.checkContents(mapSquare, ob1);
        TestMapModelHelper.checkContents(ob1, ob4, ob2);
        TestMapModelHelper.checkContents(ob2, ob3, ob5);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4, selectedSquareModel.getSelectedGameObject());

        // ob4 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareEnv(true));
        TestMapModelHelper.checkContents(mapSquare, ob4, ob1);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3, ob5);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4, selectedSquareModel.getSelectedGameObject());

        // ob4 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(true));
        TestMapModelHelper.checkContents(mapSquare, ob4, ob1);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3, ob5);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4, selectedSquareModel.getSelectedGameObject());
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareEnv(boolean)} does
     * work for multi-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareEnvMulti3() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(2, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point = new Point(0, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareHead = mapModel.getMapSquare(point);
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareTail = mapModel.getMapSquare(new Point(1, 0));
        final TestGameObject ob1 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob2 = mapModelHelper.insertExit(ob1);
        final TestGameObject ob3 = mapModelHelper.insertExit(ob2);
        final TestGameObject ob4Head = mapModelHelper.insertMob21(ob2);
        final TestGameObject ob5 = mapModelHelper.insertExit(ob2);

        TestMapModelHelper.checkContents(mapSquareHead, ob1);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3, ob4Head, ob5);
        TestMapModelHelper.checkContents(mapSquareTail);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(true));

        selectedSquareModel.setSelectedMapSquare(mapSquareHead, null);
        selectedSquareModel.setSelectedGameObject(ob4Head);

        // ob4Head can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareEnv(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob1);
        TestMapModelHelper.checkContents(ob1, ob4Head, ob2);
        final TestGameObject ob4Tail1 = ob4Head.getMultiNext();
        Assert.assertNull(ob4Tail1);
        TestMapModelHelper.checkContents(ob2, ob3, ob5);
        TestMapModelHelper.checkContents(mapSquareTail);
        Assert.assertEquals(mapSquareHead, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4Head, selectedSquareModel.getSelectedGameObject());

        // ob4Head can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareEnv(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob4Head, ob1);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3, ob5);
        final TestGameObject ob4Tail2 = ob4Head.getMultiNext();
        Assert.assertNotNull(ob4Tail2);
        TestMapModelHelper.checkContents(mapSquareTail, ob4Tail2);
        Assert.assertEquals(mapSquareHead, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4Head, selectedSquareModel.getSelectedGameObject());

        // ob4Head cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob4Head, ob1);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3, ob5);
        Assert.assertEquals(ob4Tail2, ob4Head.getMultiNext());
        TestMapModelHelper.checkContents(mapSquareTail, ob4Tail2);
        Assert.assertEquals(mapSquareHead, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4Head, selectedSquareModel.getSelectedGameObject());
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareEnv(boolean)} does
     * work for multi-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareEnvMulti4() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(1, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point = new Point(0, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareHead = mapModel.getMapSquare(point);
        final TestGameObject ob1 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob2Head = mapModelHelper.insertMob21(ob1);

        TestMapModelHelper.checkContents(mapSquareHead, ob1);
        TestMapModelHelper.checkContents(ob1, ob2Head);

        selectedSquareModel.setSelectedMapSquare(mapSquareHead, null);
        selectedSquareModel.setSelectedGameObject(ob2Head);

        // ob2Head cannot move: would not fit into map
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob1);
        TestMapModelHelper.checkContents(ob1, ob2Head);
        Assert.assertNull(ob2Head.getMultiNext());
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareInv(boolean)} does
     * work for single-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareInvSingle1() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(1, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point = new Point(0, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare = mapModel.getMapSquare(point);
        final TestGameObject ob4 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob3 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob2 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob1 = mapModelHelper.insertExit(mapModel, point);

        TestMapModelHelper.checkContents(mapSquare, ob4, ob3, ob2, ob1);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(true));

        // select ob3
        selectedSquareModel.setSelectedMapSquare(mapSquare, null);
        selectedSquareModel.setSelectedGameObject(ob3);

        // ob3 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(true));
        TestMapModelHelper.checkContents(mapSquare, ob4, ob2, ob1);
        TestMapModelHelper.checkContents(ob2, ob3);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob3, selectedSquareModel.getSelectedGameObject());

        // ob3 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(true));
        TestMapModelHelper.checkContents(mapSquare, ob4, ob2, ob1);
        TestMapModelHelper.checkContents(ob2, ob3);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob3, selectedSquareModel.getSelectedGameObject());

        // select ob2
        selectedSquareModel.setSelectedGameObject(ob2);

        // ob2 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(true));
        TestMapModelHelper.checkContents(mapSquare, ob4, ob1);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob2, selectedSquareModel.getSelectedGameObject());

        // ob2 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(true));
        TestMapModelHelper.checkContents(mapSquare, ob4, ob1);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob2, selectedSquareModel.getSelectedGameObject());

        // select ob4
        selectedSquareModel.setSelectedGameObject(ob4);

        // ob4 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(true));
        TestMapModelHelper.checkContents(mapSquare, ob1);
        TestMapModelHelper.checkContents(ob1, ob4, ob2);
        TestMapModelHelper.checkContents(ob2, ob3);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4, selectedSquareModel.getSelectedGameObject());

        // ob4 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(true));
        TestMapModelHelper.checkContents(mapSquare, ob1);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob4, ob3);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4, selectedSquareModel.getSelectedGameObject());

        // ob4 can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(true));
        TestMapModelHelper.checkContents(mapSquare, ob1);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3);
        TestMapModelHelper.checkContents(ob3, ob4);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4, selectedSquareModel.getSelectedGameObject());

        // ob4 cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(true));
        TestMapModelHelper.checkContents(mapSquare, ob1);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3);
        TestMapModelHelper.checkContents(ob3, ob4);
        Assert.assertEquals(mapSquare, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob4, selectedSquareModel.getSelectedGameObject());
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareInv(boolean)} does
     * work for multi-square game objects.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareInvMulti1() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(2, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point = new Point(0, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareHead = mapModel.getMapSquare(point);
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareTail = mapModel.getMapSquare(new Point(1, 0));
        final TestGameObject ob3Head = mapModelHelper.insertMob21(mapModel, point);
        final TestGameObject ob1 = mapModelHelper.insertExit(mapModel, point);
        final TestGameObject ob2 = mapModelHelper.insertExit(ob1);

        final TestGameObject ob3Tail = ob3Head.getMultiNext();
        Assert.assertNotNull(ob3Tail);
        TestMapModelHelper.checkContents(mapSquareHead, ob3Head, ob1);
        TestMapModelHelper.checkContents(mapSquareTail, ob3Tail);
        TestMapModelHelper.checkContents(ob1, ob2);

        // empty selection => nothing to move
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(true));

        // select ob3Head
        selectedSquareModel.setSelectedMapSquare(mapSquareHead, null);
        selectedSquareModel.setSelectedGameObject(ob3Head);

        // ob3Head can move; ob3Tail is removed
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob1);
        TestMapModelHelper.checkContents(mapSquareTail);
        TestMapModelHelper.checkContents(ob1, ob3Head, ob2);
        Assert.assertNull(ob3Head.getMultiNext());
        Assert.assertEquals(mapSquareHead, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob3Head, selectedSquareModel.getSelectedGameObject());

        // ob3Head can move
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob1);
        TestMapModelHelper.checkContents(mapSquareTail);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3Head);
        Assert.assertNull(ob3Head.getMultiNext());
        Assert.assertEquals(mapSquareHead, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob3Head, selectedSquareModel.getSelectedGameObject());

        // ob3Head cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(true));
        TestMapModelHelper.checkContents(mapSquareHead, ob1);
        TestMapModelHelper.checkContents(mapSquareTail);
        TestMapModelHelper.checkContents(ob1, ob2);
        TestMapModelHelper.checkContents(ob2, ob3Head);
        Assert.assertNull(ob3Head.getMultiNext());
        Assert.assertEquals(mapSquareHead, selectedSquareModel.getSelectedMapSquare());
        Assert.assertEquals(ob3Head, selectedSquareModel.getSelectedGameObject());
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareInv(boolean)} always
     * inserts into the head-part.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testDoMoveSquareInvIntoHead1() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(3, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point point0 = new Point(0, 0);
        final Point point1 = new Point(1, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare0 = mapModel.getMapSquare(point0);
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare1 = mapModel.getMapSquare(point1);
        final TestGameObject ob2 = mapModelHelper.insertMob21(mapModel, point1);
        final TestGameObject ob1 = mapModelHelper.insertMob21(mapModel, point0);

        TestMapModelHelper.checkMapContents(mapModel, "mob21|mob21,mob21b|mob21b");

        // select ob2
        selectedSquareModel.setSelectedMapSquare(mapSquare1, null);
        selectedSquareModel.setSelectedGameObject(ob2);

        // move ob2 into ob1
        Assert.assertTrue(selectedSquareActions.doMoveSquareInv(true));

        TestMapModelHelper.checkMapContents(mapModel, "mob21|mob21b|");
        TestMapModelHelper.checkContents(mapSquare0, ob1);
        final TestGameObject ob1Tail = ob1.getMultiNext();
        Assert.assertNotNull(ob1Tail);
        TestMapModelHelper.checkContents(mapSquare1, ob1Tail);
        TestMapModelHelper.checkContents(ob1, ob2);
    }

    /**
     * Checks that tail parts cannot be moved.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testMoveTailPart() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(2, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final Point pointHead = new Point(0, 0);
        final Point pointTail = new Point(1, 0);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquareTail = mapModel.getMapSquare(pointTail);
        mapModelHelper.insertExit(mapModel, pointHead);
        mapModelHelper.insertExit(mapModel, pointTail);
        final TestGameObject ob2Head = mapModelHelper.insertMob21(mapModel, pointHead);
        mapModelHelper.insertExit(mapModel, pointHead);
        mapModelHelper.insertExit(mapModel, pointTail);

        TestMapModelHelper.checkMapContents(mapModel, "exit,mob21,exit|exit,mob21b,exit");

        // select ob2Tail
        selectedSquareModel.setSelectedMapSquare(mapSquareTail, null);
        selectedSquareModel.setSelectedGameObject(ob2Head.getMultiNext());

        // ob2Tail cannot move
        Assert.assertFalse(selectedSquareActions.doMoveSquareTop(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareUp(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareDown(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareBottom(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareInv(false));
        Assert.assertFalse(selectedSquareActions.doMoveSquareEnv(false));
    }

    /**
     * Checks that {@link SelectedSquareActions#doMoveSquareEnv(boolean)}
     * inserts into selected map square.
     * @throws CannotInsertGameObjectException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testMoveEnvIntoSelectedMapSquare() throws CannotInsertGameObjectException, DuplicateArchetypeException {
        final TestMapControlCreator testMapControlCreator = new TestMapControlCreator();
        final TestMapModelHelper mapModelHelper = testMapControlCreator.newMapModelCreator();
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = testMapControlCreator.newMapControl(MAP_FILE1, MAP_NAME1, new Size2D(3, 1));
        final SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareModel = new SelectedSquareModel<TestGameObject, TestMapArchObject, TestArchetype>();
        final SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype> selectedSquareActions = new SelectedSquareActions<TestGameObject, TestMapArchObject, TestArchetype>(selectedSquareModel);

        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("TEST");
        final TestGameObject ob1 = mapModelHelper.insertMob21(mapModel, new Point(0, 0));
        final TestGameObject ob2 = mapModelHelper.insertMob21(ob1);

        TestMapModelHelper.checkMapContents(mapModel, "mob21|mob21b|");

        // select ob2 in (1,0)
        selectedSquareModel.setSelectedMapSquare(mapModel.getMapSquare(new Point(1, 0)), null);
        selectedSquareModel.setSelectedGameObject(ob2);

        // move ob2 into (1,0)
        Assert.assertTrue(selectedSquareActions.doMoveSquareEnv(true));

        TestMapModelHelper.checkMapContents(mapModel, "mob21|mob21,mob21b|mob21b");
    }

}
