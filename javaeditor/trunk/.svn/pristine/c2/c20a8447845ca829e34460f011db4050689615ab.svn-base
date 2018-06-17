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

package net.sf.gridarta.model.autojoin;

import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapmodel.CannotInsertGameObjectException;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import net.sf.gridarta.model.mapmodel.TestMapModelHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link AutojoinLists}.
 * @author Andreas Kirschbaum
 */
public class AutojoinListsTest {

    /**
     * Checks that a wrong number of archetypes is detected.
     * @throws IllegalAutojoinListException if the test fails
     */
    @Test
    public void testWrongCount() throws IllegalAutojoinListException {
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper();
        autojoinListsHelper.newAutojoinListsFail("autojoin list with less than 16 valid entries");
        autojoinListsHelper.newAutojoinListsFail("autojoin list with less than 16 valid entries", "a0");
        autojoinListsHelper.newAutojoinListsFail("autojoin list with less than 16 valid entries", "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14");
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14", "a15");
        autojoinListsHelper.newAutojoinListsFail("autojoin list with more than 16 valid entries", "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14", "a15", "a16");
    }

    /**
     * Checks that duplicate archetypes are detected.
     * @throws IllegalAutojoinListException if the test fails
     */
    @Test
    public void testDuplicateArchetype1() throws IllegalAutojoinListException {
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper();
        //Check disabled for now as Daimonin uses this feature:
        //autojoinListsHelper.newAutojoinListsFail("autojoin list contains duplicate archetype 'a0'", "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14", "a0");
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14", "a0");
    }

    /**
     * Checks that duplicate archetypes are detected.
     * @throws IllegalAutojoinListException if the test fails
     */
    @Test
    public void testDuplicateArchetype2() throws IllegalAutojoinListException {
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper();
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14", "a15");
        autojoinListsHelper.newAutojoinListsFail("archetype 'a4' contained in more than one autojoin list", "b0", "b1", "b2", "b3", "a4", "b5", "b6", "b7", "b8", "b9", "b10", "b11", "b12", "b13", "b14", "b15");
        autojoinListsHelper.newAutojoinListsFail("autojoin list contains duplicate archetype 'c0'", "c0|c1|c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15");
        autojoinListsHelper.newAutojoinListsFail("autojoin list is empty", "d0", "d1", "d2", "", "d4", "d5", "d6", "d7", "d8", "d9", "d10", "d11", "d12", "d13", "d14", "d15");
    }

    /**
     * Checks that inserting with autojoin works as expected.
     * @throws IllegalAutojoinListException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testInsert1() throws CannotInsertGameObjectException, DuplicateArchetypeException, IllegalAutojoinListException, UndefinedArchetypeException {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper(mapModelCreator);
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14", "a15");
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final TestMapModelHelper testMapModelHelper = mapModelCreator.newTestMapModelHelper();
        final TestArchetype a0 = mapModelCreator.getArchetype("a0");
        final TestArchetype a1 = mapModelCreator.getArchetype("a1");
        final TestArchetype a6 = mapModelCreator.getArchetype("a6");
        final TestArchetype a8 = mapModelCreator.getArchetype("a8");
        mapModel.beginTransaction("TEST");
        testMapModelHelper.insertArchetype(mapModel, 1, 0, a1, false);
        testMapModelHelper.insertArchetype(mapModel, 0, 1, a8, false);

        mapModelCreator.getMapViewSettings().setAutojoin(true);

        // insert using autojoin
        testMapModelHelper.insertArchetype(mapModel, 1, 1, a0, true);
        TestMapModelHelper.checkMapContents(mapModel, "|a5|", "a10|a9|");

        // insert duplicate archetype is ignored
        try {
            testMapModelHelper.insertArchetype(mapModel, 1, 1, a0, true);
            Assert.fail();
        } catch (final CannotInsertGameObjectException ignored) {
            // ignore
        }
        TestMapModelHelper.checkMapContents(mapModel, "|a5|", "a10|a9|");

        // insert using autojoin
        testMapModelHelper.insertArchetype(mapModel, 2, 1, a6, true);
        TestMapModelHelper.checkMapContents(mapModel, "|a5|", "a10|a11|a8");

        // insert using autojoin but disabled autojoin globally
        mapModelCreator.getMapViewSettings().setAutojoin(false);
        testMapModelHelper.insertArchetype(mapModel, 2, 0, a6, true);
        TestMapModelHelper.checkMapContents(mapModel, "|a5|a6", "a10|a11|a8");
    }

    /**
     * Checks that removing with autojoin works as expected.
     * @throws IllegalAutojoinListException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testRemove1() throws CannotInsertGameObjectException, DuplicateArchetypeException, IllegalAutojoinListException, UndefinedArchetypeException {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper(mapModelCreator);
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14", "a15");
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final TestMapModelHelper testMapModelHelper = mapModelCreator.newTestMapModelHelper();
        final TestArchetype a4 = mapModelCreator.getArchetype("a4");
        final TestArchetype a8 = mapModelCreator.getArchetype("a8");
        final TestArchetype a10 = mapModelCreator.getArchetype("a10");
        final TestArchetype a11 = mapModelCreator.getArchetype("a11");
        mapModel.beginTransaction("TEST");
        final TestGameObject g10 = testMapModelHelper.insertArchetype(mapModel, 1, 0, a4, false);
        final TestGameObject g01 = testMapModelHelper.insertArchetype(mapModel, 0, 1, a10, false);
        testMapModelHelper.insertArchetype(mapModel, 1, 1, a11, false);
        final TestGameObject g21 = testMapModelHelper.insertArchetype(mapModel, 2, 1, a8, false);

        // remove using autojoin
        mapModelCreator.getMapViewSettings().setAutojoin(true);
        mapModel.removeGameObject(g21, true);
        TestMapModelHelper.checkMapContents(mapModel, "|a4|", "a10|a9|");

        // remove using autojoin but globally disabled
        mapModelCreator.getMapViewSettings().setAutojoin(false);
        mapModel.removeGameObject(g10, true);
        TestMapModelHelper.checkMapContents(mapModel, "||", "a10|a9|");

        // remove using autojoin
        mapModelCreator.getMapViewSettings().setAutojoin(true);
        mapModel.removeGameObject(g01, true);
        TestMapModelHelper.checkMapContents(mapModel, "||", "|a1|");
    }

    /**
     * Checks that inserting alternative archetypes with autojoin works as
     * expected.
     * @throws IllegalAutojoinListException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testInsertAlt1() throws CannotInsertGameObjectException, DuplicateArchetypeException, IllegalAutojoinListException, UndefinedArchetypeException {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper(mapModelCreator);
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5|b5", "a6", "a7", "a8", "a9", "a10|b10", "a11", "a12", "a13", "a14", "a15");
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final TestMapModelHelper testMapModelHelper = mapModelCreator.newTestMapModelHelper();
        final TestArchetype a0 = mapModelCreator.getArchetype("a0");
        final TestArchetype a3 = mapModelCreator.getArchetype("a3");
        final TestArchetype a8 = mapModelCreator.getArchetype("a8");
        final TestArchetype b5 = mapModelCreator.getArchetype("b5");
        final TestArchetype b10 = mapModelCreator.getArchetype("b10");
        mapModel.beginTransaction("TEST");
        testMapModelHelper.insertArchetype(mapModel, 0, 0, b5, false);
        testMapModelHelper.insertArchetype(mapModel, 2, 0, b10, false);
        testMapModelHelper.insertArchetype(mapModel, 0, 1, a3, false);
        testMapModelHelper.insertArchetype(mapModel, 1, 1, a8, false);

        mapModelCreator.getMapViewSettings().setAutojoin(true);

        // insert using autojoin
        testMapModelHelper.insertArchetype(mapModel, 1, 0, a0, true);
        TestMapModelHelper.checkMapContents(mapModel, "b5|a6|b10", "a3|a9|");
    }

    /**
     * Checks that inserting alternative archetypes with autojoin works as
     * expected.
     * @throws IllegalAutojoinListException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testInsertAlt2() throws CannotInsertGameObjectException, DuplicateArchetypeException, IllegalAutojoinListException, UndefinedArchetypeException {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper(mapModelCreator);
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5|b5", "a6", "a7", "a8", "a9", "a10|b10", "a11", "a12", "a13", "a14", "a15");
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final TestMapModelHelper testMapModelHelper = mapModelCreator.newTestMapModelHelper();
        final TestArchetype a3 = mapModelCreator.getArchetype("a3");
        final TestArchetype a8 = mapModelCreator.getArchetype("a8");
        final TestArchetype b5 = mapModelCreator.getArchetype("b5");
        final TestArchetype b10 = mapModelCreator.getArchetype("b10");
        mapModel.beginTransaction("TEST");
        testMapModelHelper.insertArchetype(mapModel, 0, 0, b5, false);
        testMapModelHelper.insertArchetype(mapModel, 2, 0, b10, false);
        testMapModelHelper.insertArchetype(mapModel, 0, 1, a3, false);
        testMapModelHelper.insertArchetype(mapModel, 1, 1, a8, false);

        mapModelCreator.getMapViewSettings().setAutojoin(true);

        // insert using autojoin
        testMapModelHelper.insertArchetype(mapModel, 1, 0, b10, true);
        TestMapModelHelper.checkMapContents(mapModel, "b5|b10|b10", "a3|a8|");
    }

    /**
     * Checks that inserting alternative archetypes with autojoin works as
     * expected.
     * @throws IllegalAutojoinListException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testInsertAlt3() throws CannotInsertGameObjectException, DuplicateArchetypeException, IllegalAutojoinListException, UndefinedArchetypeException {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper(mapModelCreator);
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5|b5", "a6", "a7", "a8", "a9", "a10|b10", "a11", "a12", "a13", "a14", "a15");
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final TestMapModelHelper testMapModelHelper = mapModelCreator.newTestMapModelHelper();
        final TestArchetype a3 = mapModelCreator.getArchetype("a3");
        final TestArchetype a8 = mapModelCreator.getArchetype("a8");
        final TestArchetype b5 = mapModelCreator.getArchetype("b5");
        final TestArchetype b10 = mapModelCreator.getArchetype("b10");
        mapModel.beginTransaction("TEST");
        testMapModelHelper.insertArchetype(mapModel, 0, 0, b5, false);
        testMapModelHelper.insertArchetype(mapModel, 2, 0, b10, false);
        testMapModelHelper.insertArchetype(mapModel, 0, 1, a3, false);
        testMapModelHelper.insertArchetype(mapModel, 1, 1, a8, false);

        mapModelCreator.getMapViewSettings().setAutojoin(true);

        // insert using autojoin
        testMapModelHelper.insertArchetype(mapModel, 1, 0, b5, true);
        TestMapModelHelper.checkMapContents(mapModel, "b5|b5|b10", "a3|a9|");
    }

    /**
     * Checks that removing alternative archetypes with autojoin works as
     * expected.
     * @throws IllegalAutojoinListException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testRemoveAlt1() throws CannotInsertGameObjectException, DuplicateArchetypeException, IllegalAutojoinListException, UndefinedArchetypeException {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper(mapModelCreator);
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5|b5", "a6", "a7", "a8", "a9", "a10|b10", "a11", "a12", "a13", "a14", "a15");
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final TestMapModelHelper testMapModelHelper = mapModelCreator.newTestMapModelHelper();
        final TestArchetype a10 = mapModelCreator.getArchetype("a10");
        final TestArchetype b10 = mapModelCreator.getArchetype("b10");
        final TestArchetype a11 = mapModelCreator.getArchetype("a11");
        mapModel.beginTransaction("TEST");
        testMapModelHelper.insertArchetype(mapModel, 0, 0, a10, false);
        final TestGameObject g10 = testMapModelHelper.insertArchetype(mapModel, 1, 0, b10, false);
        testMapModelHelper.insertArchetype(mapModel, 2, 0, a10, false);
        testMapModelHelper.insertArchetype(mapModel, 1, 1, a11, false);

        mapModelCreator.getMapViewSettings().setAutojoin(true);

        // remove using autojoin
        mapModel.removeGameObject(g10, true);
        TestMapModelHelper.checkMapContents(mapModel, "a8||a2", "|a11|");
    }

    /**
     * Checks that removing alternative archetypes with autojoin works as
     * expected.
     * @throws IllegalAutojoinListException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testRemoveAlt2() throws CannotInsertGameObjectException, DuplicateArchetypeException, IllegalAutojoinListException, UndefinedArchetypeException {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper(mapModelCreator);
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5|b5", "a6", "a7", "a8", "a9", "a10|b10", "a11", "a12", "a13", "a14", "a15");
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final TestMapModelHelper testMapModelHelper = mapModelCreator.newTestMapModelHelper();
        final TestArchetype b5 = mapModelCreator.getArchetype("b5");
        final TestArchetype b10 = mapModelCreator.getArchetype("b10");
        final TestArchetype a11 = mapModelCreator.getArchetype("a11");
        final TestArchetype a12 = mapModelCreator.getArchetype("a12");
        mapModel.beginTransaction("TEST");
        testMapModelHelper.insertArchetype(mapModel, 0, 0, b10, false);
        final TestGameObject g10 = testMapModelHelper.insertArchetype(mapModel, 1, 0, a12, false);
        testMapModelHelper.insertArchetype(mapModel, 2, 0, b5, false);
        testMapModelHelper.insertArchetype(mapModel, 1, 1, a11, false);

        mapModelCreator.getMapViewSettings().setAutojoin(true);

        // remove using autojoin
        mapModel.removeGameObject(g10, true);
        TestMapModelHelper.checkMapContents(mapModel, "b10||b5", "|a10|");
    }

    /**
     * Checks that removing alternative archetypes with autojoin works as
     * expected.
     * @throws IllegalAutojoinListException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testRemoveAlt3() throws CannotInsertGameObjectException, DuplicateArchetypeException, IllegalAutojoinListException, UndefinedArchetypeException {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper(mapModelCreator);
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5|b5", "a6", "a7", "a8", "a9", "a10|b10", "a11", "a12", "a13", "a14", "a15");
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 2);
        final TestMapModelHelper testMapModelHelper = mapModelCreator.newTestMapModelHelper();
        final TestArchetype b10 = mapModelCreator.getArchetype("b10");
        final TestArchetype a11 = mapModelCreator.getArchetype("a11");
        mapModel.beginTransaction("TEST");
        testMapModelHelper.insertArchetype(mapModel, 0, 0, b10, false);
        final TestGameObject g10 = testMapModelHelper.insertArchetype(mapModel, 1, 0, b10, false);
        testMapModelHelper.insertArchetype(mapModel, 2, 0, b10, false);
        testMapModelHelper.insertArchetype(mapModel, 1, 1, a11, false);

        mapModelCreator.getMapViewSettings().setAutojoin(true);

        // remove using autojoin
        mapModel.removeGameObject(g10, true);
        TestMapModelHelper.checkMapContents(mapModel, "b10||b10", "|a11|");
    }

    /**
     * Checks that inserting duplicate archetypes with autojoin works as
     * expected.
     * @throws IllegalAutojoinListException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testInsertDuplicate1() throws CannotInsertGameObjectException, DuplicateArchetypeException, IllegalAutojoinListException, UndefinedArchetypeException {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper(mapModelCreator);
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5|b5", "a6", "a7", "a8", "a9", "a10|b10", "a11", "a12", "a13", "a14", "a15");
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 3);
        final TestMapModelHelper testMapModelHelper = mapModelCreator.newTestMapModelHelper();
        final TestArchetype a5 = mapModelCreator.getArchetype("a5");
        final TestArchetype b5 = mapModelCreator.getArchetype("b5");
        final TestArchetype a10 = mapModelCreator.getArchetype("a10");
        final TestArchetype b10 = mapModelCreator.getArchetype("b10");
        final TestArchetype a15 = mapModelCreator.getArchetype("a15");
        mapModel.beginTransaction("TEST");
        testMapModelHelper.insertArchetype(mapModel, 1, 0, a10, false);
        testMapModelHelper.insertArchetype(mapModel, 0, 1, b10, false);
        testMapModelHelper.insertArchetype(mapModel, 1, 1, a15, false);
        testMapModelHelper.insertArchetype(mapModel, 2, 1, a5, false);
        testMapModelHelper.insertArchetype(mapModel, 1, 2, b5, false);

        mapModelCreator.getMapViewSettings().setAutojoin(true);

        try {
            testMapModelHelper.insertArchetype(mapModel, 1, 1, a10, true);
            Assert.fail();
        } catch (final CannotInsertGameObjectException ignored) {
            // ignore
        }
        try {
            testMapModelHelper.insertArchetype(mapModel, 0, 1, b10, true);
            Assert.fail();
        } catch (final CannotInsertGameObjectException ignored) {
            // ignore
        }
        TestMapModelHelper.checkMapContents(mapModel, "|a10|", "b10|a15|a5", "|b5|");
    }

    /**
     * Checks that replacing archetypes works as expected.
     * @throws IllegalAutojoinListException if the test fails
     * @throws DuplicateArchetypeException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     * @throws CannotInsertGameObjectException if the test fails
     */
    @Test
    public void testInsertReplace1() throws CannotInsertGameObjectException, DuplicateArchetypeException, IllegalAutojoinListException, UndefinedArchetypeException {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final AutojoinListsHelper autojoinListsHelper = new AutojoinListsHelper(mapModelCreator);
        autojoinListsHelper.newAutojoinLists("a0", "a1", "a2", "a3", "a4", "a5|b5", "a6", "a7", "a8", "a9", "a10|b10", "a11", "a12", "a13", "a14", "a15");
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(3, 3);
        final TestMapModelHelper testMapModelHelper = mapModelCreator.newTestMapModelHelper();
        final TestArchetype a0 = mapModelCreator.getArchetype("a0");
        final TestArchetype a5 = mapModelCreator.getArchetype("a5");
        final TestArchetype b5 = mapModelCreator.getArchetype("b5");
        final TestArchetype a10 = mapModelCreator.getArchetype("a10");
        final TestArchetype b10 = mapModelCreator.getArchetype("b10");
        mapModel.beginTransaction("TEST");
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                testMapModelHelper.insertArchetype(mapModel, x, y, a10, false);
            }
        }

        mapModelCreator.getMapViewSettings().setAutojoin(true);

        TestMapModelHelper.checkMapContents(mapModel, "a10|a10|a10", "a10|a10|a10", "a10|a10|a10");
        testMapModelHelper.insertArchetype(mapModel, 1, 1, b5, true); // replace main->alt
        TestMapModelHelper.checkMapContents(mapModel, "a10|a14|a10", "a8|b5|a2", "a10|a11|a10");
        testMapModelHelper.insertArchetype(mapModel, 1, 1, b10, true); // replace alt->alt, different archetype
        TestMapModelHelper.checkMapContents(mapModel, "a10|a10|a10", "a10|b10|a10", "a10|a10|a10");
        testMapModelHelper.insertArchetype(mapModel, 1, 1, a5, true); // replace alt->main
        TestMapModelHelper.checkMapContents(mapModel, "a10|a14|a10", "a10|a15|a10", "a10|a11|a10");
        testMapModelHelper.insertArchetype(mapModel, 1, 1, b10, true); // replace main->alt
        TestMapModelHelper.checkMapContents(mapModel, "a10|a10|a10", "a10|b10|a10", "a10|a10|a10");
        try {
            testMapModelHelper.insertArchetype(mapModel, 1, 1, b10, true); // replace alt->alt, same archetype
            Assert.fail();
        } catch (final CannotInsertGameObjectException ignored) {
            // ignore
        }
        TestMapModelHelper.checkMapContents(mapModel, "a10|a10|a10", "a10|b10|a10", "a10|a10|a10");
        try {
            testMapModelHelper.insertArchetype(mapModel, 1, 0, a0, true); // replace main->main
            Assert.fail();
        } catch (final CannotInsertGameObjectException ignored) {
            // ignore
        }
        TestMapModelHelper.checkMapContents(mapModel, "a10|a10|a10", "a10|b10|a10", "a10|a10|a10");
    }

}
