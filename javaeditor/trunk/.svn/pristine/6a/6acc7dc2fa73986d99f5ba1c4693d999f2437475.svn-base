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

package net.sf.gridarta.model.match;

import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.TestAnimationObjects;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetype.TestDefaultArchetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.TestFaceObjects;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.gameobject.TestGameObjectFactory;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Regression tests for {@link NamedGameObjectMatcher}.
 * @author Andreas Kirschbaum
 */
public class NamedGameObjectMatcherTest {

    /**
     * The {@link SystemIcons} instance.
     */
    @Nullable
    private SystemIcons systemIcons;

    /**
     * Checks that a {@link NamedGameObjectMatcher} works correctly when not
     * using an environment check.
     */
    @Test
    public void testTypeNrsGameObjectMatcher1() {
        final GameObjectMatcher invGameObjectMatcher = new TypeNrsGameObjectMatcher(1);
        final GameObjectMatcher namedGameObjectMatcher = new NamedGameObjectMatcher(0, "namedGameObjectMatcher", "namedGameObjectMatcher", false, null, invGameObjectMatcher);
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final TestGameObjectFactory gameObjectFactory = new TestGameObjectFactory(faceObjectProviders, animationObjects);
        Assert.assertTrue(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 1)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2)));
        Assert.assertTrue(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 1, 1)));
        Assert.assertTrue(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2, 1)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 1, 2)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2, 2)));
        Assert.assertTrue(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 1, 1, 1)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 1, 2, 2)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2, 1, 2)));
        Assert.assertTrue(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2, 2, 1)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2, 2, 2)));
    }

    /**
     * Checks that a {@link NamedGameObjectMatcher} works correctly when using
     * an environment check.
     */
    @Test
    public void testTypeNrsGameObjectMatcher2() {
        final GameObjectMatcher invGameObjectMatcher = new TypeNrsGameObjectMatcher(1);
        final GameObjectMatcher envGameObjectMatcher = new TypeNrsGameObjectMatcher(2);
        final GameObjectMatcher namedGameObjectMatcher = new NamedGameObjectMatcher(0, "namedGameObjectMatcher", "namedGameObjectMatcher", false, envGameObjectMatcher, invGameObjectMatcher);
        final FaceObjectProviders faceObjectProviders = newFaceObjectProviders();
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final TestGameObjectFactory gameObjectFactory = new TestGameObjectFactory(faceObjectProviders, animationObjects);
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 1)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 1, 1)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2, 1)));
        Assert.assertTrue(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 1, 2)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2, 2)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 1, 1, 1)));
        Assert.assertTrue(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 1, 2, 2)));
        Assert.assertTrue(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2, 1, 2)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2, 2, 1)));
        Assert.assertFalse(namedGameObjectMatcher.isMatching(newGameObjects(gameObjectFactory, faceObjectProviders, animationObjects, 2, 2, 2)));
    }

    /**
     * Creates a new {@link TestArchetype} instance.
     * @param archetypeName the archetype's name
     * @param typeNo the archetype's type number
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     * @return the new instance
     */
    @NotNull
    private static TestArchetype newArchetype(@NotNull final String archetypeName, final int typeNo, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        final TestArchetype archetype = new TestDefaultArchetype(archetypeName, faceObjectProviders, animationObjects);
        archetype.setAttributeInt(BaseObject.TYPE, typeNo);
        return archetype;
    }

    /**
     * Creates a new {@link TestGameObject} instance.
     * @param gameObjectFactory the game object factory to use
     * @param archetype the game object's archetype
     * @param gameObjectName the game object's name
     * @return the new instance
     */
    @NotNull
    private static TestGameObject newGameObject(@NotNull final TestGameObjectFactory gameObjectFactory, @NotNull final TestArchetype archetype, @NotNull final String gameObjectName) {
        final TestGameObject gameObject = gameObjectFactory.createGameObject(archetype);
        gameObject.setAttributeString(BaseObject.NAME, gameObjectName);
        return gameObject;
    }

    /**
     * Creates a chain of {@link TestGameObject} instances having the given type
     * numbers. The first type number is used for the game object; following
     * type numbers are used for the parent objects.
     * @param gameObjectFactory the game object factory to use
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     * @param typeNumbers the type numbers
     * @return the child game object
     */
    @NotNull
    private static GameObject<?, ?, ?> newGameObjects(@NotNull final TestGameObjectFactory gameObjectFactory, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects, @NotNull final int... typeNumbers) {
        TestGameObject gameObject = null;
        for (final int typeNumber : typeNumbers) {
            final String name = Integer.toString(typeNumber);
            final TestArchetype archetype = newArchetype("arch" + name, typeNumber, faceObjectProviders, animationObjects);
            final TestGameObject tmp = newGameObject(gameObjectFactory, archetype, name);
            if (gameObject != null) {
                tmp.addLast(gameObject);
            }
            gameObject = tmp;
        }
        if (gameObject == null) {
            throw new IllegalArgumentException();
        }
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
