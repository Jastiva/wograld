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

package net.sf.gridarta.model.archetype;

import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.TestAnimationObjects;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.TestFaceObjects;
import net.sf.gridarta.model.gameobject.TestGameObjectFactory;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link AttributeListUtils}.
 * @author Andreas Kirschbaum
 */
public class AttributeListUtilsTest {

    /**
     * Test case for {@link AttributeListUtils#removeAttribute(String,
     * String)}.
     */
    @Test
    public void testRemoveAttribute() {
        checkRemoveAttribute("", "abc", "");

        checkRemoveAttribute("abc def\n", "abc", "");
        checkRemoveAttribute("abc def\n", "ab", "abc def\n");
        checkRemoveAttribute("abc def\n", "abcd", "abc def\n");

        checkRemoveAttribute("abc def\n" + "ghi jkl\n" + "mno pqr\n", "abc", "ghi jkl\n" + "mno pqr\n");
        checkRemoveAttribute("abc def\n" + "ghi jkl\n" + "mno pqr\n", "def", "abc def\n" + "ghi jkl\n" + "mno pqr\n");
        checkRemoveAttribute("abc def\n" + "ghi jkl\n" + "mno pqr\n", "ghi", "abc def\n" + "mno pqr\n");
        checkRemoveAttribute("abc def\n" + "ghi jkl\n" + "mno pqr\n", "mno", "abc def\n" + "ghi jkl\n");

        checkRemoveAttribute("abc def\n" + "ghi jkl\n" + "mno pqr\n", "Abc", "abc def\n" + "ghi jkl\n" + "mno pqr\n");

        checkRemoveAttribute("abc def\n\n" + "ghi jkl\n\n", "xyz", "abc def\n" + "ghi jkl\n");
        checkRemoveAttribute("\n\n" + "abc def\n\n", "ghi", "abc def\n");
        checkRemoveAttribute("\n\n" + "abc def\n\n", "abc", "");
    }

    /**
     * Calls {@link AttributeListUtils#removeAttribute(String, String)} and
     * checks for expected results.
     * @param attributeList the attribute list parameter to pass
     * @param key the key parameter to pass
     * @param expectedResult the expected result value
     */
    private static void checkRemoveAttribute(@NotNull final String attributeList, @NotNull final String key, @NotNull final String expectedResult) {
        Assert.assertEquals(expectedResult, AttributeListUtils.removeAttribute(attributeList, key));
    }

    /**
     * Checks that {@link AttributeListUtils#diffArchTextValues(BaseObject,
     * CharSequence)} returns correct values.
     */
    @Test
    public void checkDiffArchTextValues1() {
        Assert.assertEquals("b 3\nd 4\n", AttributeListUtils.diffArchTextValues(newArchetype("a 1\nb 2\nc 3\n"), "b 3\na 1\nd 4\n"));
        Assert.assertEquals("d 4\nb 3\n", AttributeListUtils.diffArchTextValues(newArchetype("a 1\nb 2\nc 3\n"), "d 4\nb 3\na 1\n"));
        Assert.assertEquals("b 3\nd 4\n", AttributeListUtils.diffArchTextValues(newArchetype("c 3\nb 2\na 1\n"), "b 3\na 1\nd 4\n"));
        Assert.assertEquals("d 4\nb 3\n", AttributeListUtils.diffArchTextValues(newArchetype("c 3\nb 2\na 1\n"), "d 4\nb 3\na 1\n"));
        Assert.assertEquals("e 5\nb 2\nf 6\n", AttributeListUtils.diffArchTextValues(newArchetype("a 1\nc 3\n"), "e 5\nb 2\nf 6\n"));
        Assert.assertEquals("f 6\nb 2\ne 5\n", AttributeListUtils.diffArchTextValues(newArchetype("a 1\nc 3\n"), "f 6\nb 2\ne 5\n"));
    }

    /**
     * Creates a new archetype.
     * @param objectText the object text of the new archetype
     * @return the new archetype
     */
    @NotNull
    private static BaseObject<?, ?, ?, ?> newArchetype(@NotNull final String objectText) {
        final FaceObjects faceObjects = new TestFaceObjects();
        final GUIUtils guiUtils = new GUIUtils();
        final SystemIcons systemIcons = new SystemIcons(guiUtils);
        final FaceObjectProviders faceObjectProviders = new FaceObjectProviders(0, faceObjects, systemIcons);
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final TestGameObjectFactory gameObjectFactory = new TestGameObjectFactory(faceObjectProviders, animationObjects);
        final TestArchetype archetype = gameObjectFactory.newArchetype("arch");
        archetype.setObjectText(objectText);
        return archetype;
    }

}
