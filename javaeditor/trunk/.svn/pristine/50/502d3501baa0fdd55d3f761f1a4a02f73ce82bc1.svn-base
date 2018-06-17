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

package net.sf.gridarta.model.validation.checks;

import net.sf.gridarta.model.validation.NoSuchValidatorException;
import net.sf.gridarta.model.validation.ValidationUtils;
import net.sf.gridarta.utils.TestActionBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Regression tests for {@link net.sf.gridarta.model.validation.checks.ValidatorFactory}
 * to create validators.
 * @author Andreas Kirschbaum
 */
public class ValidatorFactoryTest {

    /**
     * Checks that creating an undefined validator results in an exception.
     * @throws NoSuchValidatorException if the test succeeds
     */
    @Test(expected = NoSuchValidatorException.class)
    public void testNewUndefinedChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("abc");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.AttributeRangeChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewAttributeRangeChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.AttributeRangeChecker");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.BlockedMobOrSpawnPointChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewBlockedMobOrSpawnPointChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.BlockedMobOrSpawnPointChecker 1,2,3");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.BlockedSpawnPointChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewBlockedSpawnPointChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.BlockedSpawnPointChecker 1,2,3");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.ConnectedInsideContainerChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewConnectedInsideContainerChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.ConnectedInsideContainerChecker");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.ConnectedPickableChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewConnectedPickableChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.ConnectedPickableChecker");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.ConnectionChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewConnectionChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.ConnectionChecker matcher matcher matcher");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.CustomTypeChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewCustomTypeChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.CustomTypeChecker 1,2 1,2,3");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.DoubleLayerChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewDoubleLayerChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.DoubleLayerChecker");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.DoubleTypeChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewDoubleTypeChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.DoubleTypeChecker");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.EmptySpawnPointChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewEmptySpawnPointChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.EmptySpawnPointChecker 1,2,3");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.EnvironmentChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewEnvironmentChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.EnvironmentChecker");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.ExitChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewExitChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.ExitChecker 1");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.MapDifficultyChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewMapDifficultyChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.MapDifficultyChecker 1 2");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.MobOutsideSpawnPointChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewMobOutsideSpawnPointChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.MobOutsideSpawnPointChecker 1,2,3");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.PaidItemShopSquareChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewPaidItemShopSquareChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.PaidItemShopSquareChecker matcher matcher");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.ShopSquareChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewShopSquareChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.ShopSquareChecker matcher matcher matcher");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.SlayingChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewSlayingChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.SlayingChecker pattern matcher,pattern");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.SquareWithoutFloorChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewSquareWithoutFloorChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.SquareWithoutFloorChecker 1,2,3");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.SysObjectNotOnLayerZeroChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewSysObjectNotOnLayerZeroChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.SysObjectNotOnLayerZeroChecker");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.TilePathsChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewTilePathsChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.TilePathsChecker 4");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.UndefinedArchetypeChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewUndefinedArchetypeChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.UndefinedArchetypeChecker");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.UndefinedFaceChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewUndefinedFaceChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.UndefinedFaceChecker");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.UnsetSlayingChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewUnsetSlayingChecker() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.UnsetSlayingChecker 1,2,3 player");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.MapCheckerScriptChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test(expected = NoSuchValidatorException.class)
    public void testNewMapCheckerScriptChecker1() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.MapCheckerScriptChecker a b c");
    }

    /**
     * Checks that {@link net.sf.gridarta.model.validation.checks.MapCheckerScriptChecker}
     * can be instantiated.
     * @throws NoSuchValidatorException if the test fails
     */
    @Test
    public void testNewMapCheckerScriptChecker2() throws NoSuchValidatorException {
        ValidationUtils.newValidatorFactory().newValidator("net.sf.gridarta.model.validation.checks.MapCheckerScriptChecker a ${MAP} c");
    }

    /**
     * Initializes the action builder.
     */
    @BeforeClass
    public static void setUp() {
        TestActionBuilder.initialize();
    }

}
