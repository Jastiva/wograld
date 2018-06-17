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

package net.sf.gridarta.model.baseobject;

import java.awt.Point;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.TestAnimationObjects;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetype.TestDefaultArchetype;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.TestFaceObjects;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapmodel.InsertionMode;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import net.sf.gridarta.model.mapmodel.TopmostInsertionMode;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Regression tests for {@link AbstractBaseObject}.
 * @author Andreas Kirschbaum
 */
public class AbstractBaseObjectTest {

    /**
     * The {@link SystemIcons} instance.
     */
    @Nullable
    private SystemIcons systemIcons;

    /**
     * Checks that setting the face name does work.
     */
    @Test
    public void testSetFaceName1() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype = newArchetype("arch", null, faceObjectProviders, animationObjects);
        Assert.assertNull(archetype.getFaceName());
        archetype.setAttributeString("face", "arch_face");
        Assert.assertEquals("arch_face", archetype.getFaceName());
        final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject = newGameObject(archetype, "test", faceObjectProviders, animationObjects);

        // check that default face is archetype face
        Assert.assertNull(gameObject.getFaceName());

        // reset face
        gameObject.setObjectText("");
        Assert.assertNull(gameObject.getFaceName());

        // set face by text change
        gameObject.setAttributeString("face1", "face1");
        Assert.assertNull(gameObject.getFaceName());
        gameObject.setAttributeString("face", "face2");
        Assert.assertEquals("face2", gameObject.getFaceName());
        gameObject.setAttributeString("face2", "face3");
        Assert.assertEquals("face2", gameObject.getFaceName());

        // reset object text ==> face reverts to archetype face
        gameObject.setObjectText("");
        Assert.assertNull(gameObject.getFaceName());

        // set face by text change
        gameObject.setObjectText("abc 1\n" + "face face4\n" + "def 2\n");
        Assert.assertEquals("face4", gameObject.getFaceName());

        // unset face by text change
        gameObject.setObjectText("abc 1\n" + "def 2\n");
        Assert.assertNull(gameObject.getFaceName());

        // set face to archetype face
        gameObject.setObjectText("abc 1\n" + "face arch_face\n" + "def 2\n");
        Assert.assertNull(gameObject.getFaceName());
    }

    /**
     * Checks that a custom face may be changed.
     */
    @Test
    public void testSetFaceName2() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype = newArchetype("arch", "face arch_face", faceObjectProviders, animationObjects);
        final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject = newGameObject(archetype, "test", faceObjectProviders, animationObjects);

        Assert.assertNull(gameObject.getFaceName()); // default face
        gameObject.setObjectText("face face1\n");
        Assert.assertEquals("face1", gameObject.getFaceName());
        gameObject.setObjectText("face face2\n");
        Assert.assertEquals("face2", gameObject.getFaceName());
    }

    /**
     * Checks that {@link GameObject#setAttributeString(String, String)} does
     * work.
     */
    @Test
    public void testSetAttributeString() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype = newArchetype("arch", "key1 value1\n" + "key2 value2\n", faceObjectProviders, animationObjects);

        final BaseObject<?, ?, ?, ?> gameObject = new TestGameObject(archetype, faceObjectProviders, animationObjects);

        Assert.assertEquals("value1", gameObject.getAttributeString("key1"));
        Assert.assertEquals("value1", gameObject.getAttributeString("key1", true));
        Assert.assertEquals("", gameObject.getAttributeString("key1", false));
        Assert.assertEquals("value2", gameObject.getAttributeString("key2"));
        Assert.assertEquals("value2", gameObject.getAttributeString("key2", true));
        Assert.assertEquals("", gameObject.getAttributeString("key2", false));
        Assert.assertEquals("", gameObject.getAttributeString("key3"));
        Assert.assertEquals("", gameObject.getAttributeString("key3", true));
        Assert.assertEquals("", gameObject.getAttributeString("key3", false));
        Assert.assertEquals("", gameObject.getObjectText());

        gameObject.setAttributeString("key3", "value3");

        Assert.assertEquals("value1", gameObject.getAttributeString("key1"));
        Assert.assertEquals("value1", gameObject.getAttributeString("key1", true));
        Assert.assertEquals("", gameObject.getAttributeString("key1", false));
        Assert.assertEquals("value2", gameObject.getAttributeString("key2"));
        Assert.assertEquals("value2", gameObject.getAttributeString("key2", true));
        Assert.assertEquals("", gameObject.getAttributeString("key2", false));
        Assert.assertEquals("value3", gameObject.getAttributeString("key3"));
        Assert.assertEquals("value3", gameObject.getAttributeString("key3", true));
        Assert.assertEquals("value3", gameObject.getAttributeString("key3", false));
        Assert.assertEquals("key3 value3\n", gameObject.getObjectText());

        gameObject.setAttributeString("key2", "VALUE2");

        Assert.assertEquals("value1", gameObject.getAttributeString("key1"));
        Assert.assertEquals("value1", gameObject.getAttributeString("key1", true));
        Assert.assertEquals("", gameObject.getAttributeString("key1", false));
        Assert.assertEquals("VALUE2", gameObject.getAttributeString("key2"));
        Assert.assertEquals("VALUE2", gameObject.getAttributeString("key2", true));
        Assert.assertEquals("VALUE2", gameObject.getAttributeString("key2", false));
        Assert.assertEquals("value3", gameObject.getAttributeString("key3"));
        Assert.assertEquals("value3", gameObject.getAttributeString("key3", true));
        Assert.assertEquals("value3", gameObject.getAttributeString("key3", false));
        Assert.assertEquals("key3 value3\n" + "key2 VALUE2\n", gameObject.getObjectText());

        gameObject.setAttributeString("key3", "");

        Assert.assertEquals("value1", gameObject.getAttributeString("key1"));
        Assert.assertEquals("value1", gameObject.getAttributeString("key1", true));
        Assert.assertEquals("", gameObject.getAttributeString("key1", false));
        Assert.assertEquals("VALUE2", gameObject.getAttributeString("key2"));
        Assert.assertEquals("VALUE2", gameObject.getAttributeString("key2", true));
        Assert.assertEquals("VALUE2", gameObject.getAttributeString("key2", false));
        Assert.assertEquals("", gameObject.getAttributeString("key3"));
        Assert.assertEquals("", gameObject.getAttributeString("key3", true));
        Assert.assertEquals("", gameObject.getAttributeString("key3", false));
        Assert.assertEquals("key2 VALUE2\n", gameObject.getObjectText());

        gameObject.setAttributeString("key2", "value2");

        Assert.assertEquals("value1", gameObject.getAttributeString("key1"));
        Assert.assertEquals("value1", gameObject.getAttributeString("key1", true));
        Assert.assertEquals("", gameObject.getAttributeString("key1", false));
        Assert.assertEquals("value2", gameObject.getAttributeString("key2"));
        Assert.assertEquals("value2", gameObject.getAttributeString("key2", true));
        Assert.assertEquals("", gameObject.getAttributeString("key2", false));
        Assert.assertEquals("", gameObject.getAttributeString("key3"));
        Assert.assertEquals("", gameObject.getAttributeString("key3", true));
        Assert.assertEquals("", gameObject.getAttributeString("key3", false));
        Assert.assertEquals("", gameObject.getObjectText());
    }

    /**
     * Checks that caching the "direction" attribute does work.
     */
    @Test
    public void testDirection1() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype = newArchetype("arch", null, faceObjectProviders, animationObjects);
        final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject = newGameObject(archetype, "test", faceObjectProviders, animationObjects);
        checkDirection(gameObject, 0);
        gameObject.setAttributeString("direction", "3");
        checkDirection(gameObject, 3);
        gameObject.removeAttribute("direction");
        checkDirection(gameObject, 0);
        gameObject.setObjectText("direction 3");
        checkDirection(gameObject, 3);
    }

    /**
     * Checks that caching the "direction" attribute does work.
     */
    @Test
    public void testDirection2() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype = newArchetype("arch", "direction 2", faceObjectProviders, animationObjects);
        final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject = newGameObject(archetype, "test", faceObjectProviders, animationObjects);
        checkDirection(gameObject, 2);
        gameObject.setAttributeString("direction", "3");
        checkDirection(gameObject, 3);
        gameObject.removeAttribute("direction");
        checkDirection(gameObject, 2);
    }

    /**
     * Checks that caching the "direction" attribute does work.
     */
    @Test
    public void testDirection3() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype1 = newArchetype("arch", "direction 1", faceObjectProviders, animationObjects);
        final TestArchetype archetype2 = newArchetype("arch", "direction 2", faceObjectProviders, animationObjects);
        final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject = newGameObject(archetype1, "test", faceObjectProviders, animationObjects);
        checkDirection(gameObject, 1);
        gameObject.setArchetype(archetype2);
        checkDirection(gameObject, 2);
        gameObject.setAttributeString("direction", "4");
        checkDirection(gameObject, 4);
        gameObject.setArchetype(archetype1);
        checkDirection(gameObject, 4);
        gameObject.removeAttribute("direction");
        checkDirection(gameObject, 1);
    }

    /**
     * Checks that the "direction" attribute of a {@link GameObject} contains
     * the expected value.
     * @param gameObject the game object to check
     * @param direction the expected direction
     */
    private static void checkDirection(@NotNull final BaseObject<?, ?, ?, ?> gameObject, final int direction) {
        Assert.assertEquals(direction, gameObject.getDirection());
        Assert.assertEquals(direction, gameObject.getAttributeInt("direction"));
    }

    /**
     * Checks that caching the "type" attribute does work.
     */
    @Test
    public void testType1() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype = newArchetype("arch", null, faceObjectProviders, animationObjects);
        final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject = newGameObject(archetype, "test", faceObjectProviders, animationObjects);
        checkType(gameObject, 0);
        gameObject.setAttributeString("type", "3");
        checkType(gameObject, 3);
        gameObject.removeAttribute("type");
        checkType(gameObject, 0);
        gameObject.setObjectText("type 3");
        checkType(gameObject, 3);
    }

    /**
     * Checks that caching the "type" attribute does work.
     */
    @Test
    public void testType2() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype = newArchetype("arch", "type 2", faceObjectProviders, animationObjects);
        final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject = newGameObject(archetype, "test", faceObjectProviders, animationObjects);
        checkType(gameObject, 2);
        gameObject.setAttributeString("type", "3");
        checkType(gameObject, 3);
        gameObject.removeAttribute("type");
        checkType(gameObject, 2);
    }

    /**
     * Checks that caching the "type" attribute does work.
     */
    @Test
    public void testType3() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype1 = newArchetype("arch", "type 1", faceObjectProviders, animationObjects);
        final TestArchetype archetype2 = newArchetype("arch", "type 2", faceObjectProviders, animationObjects);
        final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject = newGameObject(archetype1, "test", faceObjectProviders, animationObjects);
        checkType(gameObject, 1);
        gameObject.setArchetype(archetype2);
        checkType(gameObject, 2);
        gameObject.setAttributeString("type", "4");
        checkType(gameObject, 4);
        gameObject.setArchetype(archetype1);
        checkType(gameObject, 4);
        gameObject.removeAttribute("type");
        checkType(gameObject, 1);
    }

    /**
     * Checks that the {@link BaseObject#TYPE} attribute of a {@link GameObject}
     * contains the expected value.
     * @param gameObject the game object to check
     * @param type the expected direction
     */
    private static void checkType(@NotNull final BaseObject<?, ?, ?, ?> gameObject, final int type) {
        Assert.assertEquals(type, gameObject.getTypeNo());
        Assert.assertEquals(type, gameObject.getAttributeInt(BaseObject.TYPE));
    }

    /**
     * Checks that caching the "name" attribute does work.
     */
    @Test
    public void testName1() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype = newArchetype("arch", null, faceObjectProviders, animationObjects);
        final BaseObject<?, ?, ?, ?> gameObject = new TestGameObject(archetype, faceObjectProviders, animationObjects);
        checkName(gameObject, "arch", "");
        gameObject.setAttributeString("name", "3");
        checkName(gameObject, "3", "3");
        gameObject.removeAttribute("name");
        checkName(gameObject, "arch", "");
        gameObject.setObjectText("name 3");
        checkName(gameObject, "3", "3");
    }

    /**
     * Checks that caching the "name" attribute does work.
     */
    @Test
    public void testName2() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype = newArchetype("arch", "name 2", faceObjectProviders, animationObjects);
        final BaseObject<?, ?, ?, ?> gameObject = new TestGameObject(archetype, faceObjectProviders, animationObjects);
        checkName(gameObject, "2", "2");
        gameObject.setAttributeString("name", "3");
        checkName(gameObject, "3", "3");
        gameObject.removeAttribute("name");
        checkName(gameObject, "2", "2");
    }

    /**
     * Checks that caching the "name" attribute does work.
     */
    @Test
    public void testName3() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype1 = newArchetype("arch", "name 1", faceObjectProviders, animationObjects);
        final TestArchetype archetype2 = newArchetype("arch", "name 2", faceObjectProviders, animationObjects);
        final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject = new TestGameObject(archetype1, faceObjectProviders, animationObjects);
        checkName(gameObject, "1", "1");
        gameObject.setArchetype(archetype2);
        checkName(gameObject, "2", "2");
        gameObject.setAttributeString("name", "4");
        checkName(gameObject, "4", "4");
        gameObject.setArchetype(archetype1);
        checkName(gameObject, "4", "4");
        gameObject.removeAttribute("name");
        checkName(gameObject, "1", "1");
    }

    /**
     * Checks that {@link AbstractBaseObject#setAttributeInt(String, int)} works
     * as expected.
     */
    @Test
    public void testSetAttributeInt1() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype1 = newArchetype("arch", "a 1\nb 0\nc 2", faceObjectProviders, animationObjects);
        final BaseObject<?, ?, ?, ?> gameObject = new TestGameObject(archetype1, faceObjectProviders, animationObjects);
        Assert.assertEquals("", gameObject.getAttributeString("a", false));
        Assert.assertEquals("", gameObject.getAttributeString("b", false));
        Assert.assertEquals("", gameObject.getAttributeString("c", false));
        Assert.assertEquals("", gameObject.getAttributeString("d", false));

        gameObject.setAttributeInt("a", 1);
        gameObject.setAttributeInt("b", 0);
        gameObject.setAttributeInt("c", 2);
        gameObject.setAttributeInt("d", 0);
        Assert.assertEquals("", gameObject.getAttributeString("a", false));
        Assert.assertEquals("", gameObject.getAttributeString("b", false));
        Assert.assertEquals("", gameObject.getAttributeString("c", false));
        Assert.assertEquals("", gameObject.getAttributeString("d", false));

        gameObject.setAttributeInt("a", 0);
        gameObject.setAttributeInt("b", 1);
        gameObject.setAttributeInt("c", 0);
        gameObject.setAttributeInt("d", 2);
        Assert.assertEquals("0", gameObject.getAttributeString("a", false));
        Assert.assertEquals("1", gameObject.getAttributeString("b", false));
        Assert.assertEquals("0", gameObject.getAttributeString("c", false));
        Assert.assertEquals("2", gameObject.getAttributeString("d", false));

        gameObject.setAttributeInt("a", 1);
        gameObject.setAttributeInt("b", 0);
        gameObject.setAttributeInt("c", 2);
        gameObject.setAttributeInt("d", 0);
        Assert.assertEquals("", gameObject.getAttributeString("a", false));
        Assert.assertEquals("", gameObject.getAttributeString("b", false));
        Assert.assertEquals("", gameObject.getAttributeString("c", false));
        Assert.assertEquals("", gameObject.getAttributeString("d", false));
    }

    /**
     * Checks that {@link AbstractBaseObject#toString(String)} works as
     * expected.
     */
    @Test
    public void testToString1() {
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final TestArchetype archetype1 = newArchetype("arch", "level 10", faceObjectProviders, animationObjects);
        final BaseObject<?, ?, ?, ?> gameObject = new TestGameObject(archetype1, faceObjectProviders, animationObjects);
        gameObject.setAttributeString("name", "Name");
        gameObject.setAttributeString("title", "Title");

        Assert.assertEquals("format", gameObject.toString("format"));
        Assert.assertEquals("Name TitleNameName TitleTitle", gameObject.toString("${NAME}${name}${NAME}${title}${other}"));
        Assert.assertEquals("", gameObject.toString("${other:abc}"));
        Assert.assertEquals("abc", gameObject.toString("${name:abc}"));
        Assert.assertEquals("abc", gameObject.toString("${level:abc}"));
        Assert.assertEquals("test", gameObject.toString("te${other: 1${other}2 }st"));
        Assert.assertEquals("te 1Name2 st", gameObject.toString("te${name: 1${name}2 }st"));
        Assert.assertEquals("te 1102 st", gameObject.toString("te${level: 1${level}2 }st"));
    }

    /**
     * Checks that {@link BaseObject#setObjectText(String)} ignores attribute
     * ordering.
     */
    @Test
    public void testSetObjectTextAttributeOrdering() {
        final InsertionMode<TestGameObject, TestMapArchObject, TestArchetype> topmostInsertionMode = new TopmostInsertionMode<TestGameObject, TestMapArchObject, TestArchetype>();
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);
        mapModel.beginTransaction("test");
        try {
            mapModelCreator.addGameObjectToMap(mapModel, "floor", "1", 0, 0, topmostInsertionMode);
        } finally {
            mapModel.endTransaction();
        }
        final TestGameObject gameObject = mapModel.getMapSquare(new Point(0, 0)).iterator().next();

        mapModel.resetModified();
        mapModel.beginTransaction("test");
        try {
            gameObject.setObjectText("a 1\nb 2\n");
        } finally {
            mapModel.endTransaction();
        }
        Assert.assertTrue(mapModel.isModified());

        mapModel.resetModified();
        mapModel.beginTransaction("test");
        try {
            gameObject.setObjectText("b 2\na 1\n");
        } finally {
            mapModel.endTransaction();
        }
        Assert.assertFalse(mapModel.isModified());
    }

    /**
     * Checks that the {@link BaseObject#NAME} attribute of an {@link
     * GameObject} contains the expected value.
     * @param gameObject the game object to check
     * @param name the expected name (cached value)
     * @param nameAttribute the expected name ("name" attribute)
     */
    private static void checkName(@NotNull final BaseObject<?, ?, ?, ?> gameObject, @NotNull final String name, @NotNull final String nameAttribute) {
        Assert.assertEquals(name, gameObject.getObjName());
        Assert.assertEquals(nameAttribute, gameObject.getAttributeString(BaseObject.NAME));
    }

    /**
     * Creates a new {@link TestArchetype} instance.
     * @param archetypeName the archetype name
     * @param objectText the object text to add or <code>null</code>
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     * @return the archetype instance
     */
    @NotNull
    private static TestArchetype newArchetype(@NotNull final String archetypeName, @Nullable final String objectText, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        final TestArchetype archetype = new TestDefaultArchetype(archetypeName, faceObjectProviders, animationObjects);
        if (objectText != null) {
            archetype.setObjectText(objectText);
        }
        return archetype;
    }

    /**
     * Creates a new game object instance.
     * @param archetype the archetype to create the game object from
     * @param name the game object's name
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     * @return the game object instance
     */
    @NotNull
    private static GameObject<TestGameObject, TestMapArchObject, TestArchetype> newGameObject(@NotNull final TestArchetype archetype, @NotNull final String name, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject = new TestGameObject(archetype, faceObjectProviders, animationObjects);
        gameObject.setAttributeString(BaseObject.NAME, name);
        return gameObject;
    }

    /**
     * Creates a new {@link FaceObjectProviders} instance.
     * @return the face object providers instance
     */
    private FaceObjectProviders newFaceObjectProviders() {
        final FaceObjects faceObjects = new TestFaceObjects();
        assert systemIcons != null;
        return new FaceObjectProviders(0, faceObjects, systemIcons);
    }

    /**
     * Sets up a test.
     */
    @Before
    public void setUp() {
        final GUIUtils guiUtils = new GUIUtils();
        systemIcons = new SystemIcons(guiUtils);
    }

}
