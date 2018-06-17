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
import java.util.regex.Pattern;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetype.TestDefaultArchetype;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.utils.Size2D;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

/**
 * Helper class for creating {@link MapModel} instances for regression tests.
 * @author Andreas Kirschbaum
 */
public class TestMapModelHelper {

    /**
     * The archetype type used for "exit" game objects.
     */
    public static final int EXIT_TYPE = 1;

    /**
     * The archetype type used for "floor" game objects.
     */
    private static final int FLOOR_TYPE = 2;

    /**
     * The archetype type used for "mob" game objects.
     */
    private static final int MOB_TYPE = 3;

    /**
     * An empty array of strings.
     */
    @NotNull
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * The "topmost" {@link InsertionMode} instance.
     */
    @NotNull
    private final InsertionMode<TestGameObject, TestMapArchObject, TestArchetype> topmostInsertionMode;

    /**
     * The {@link GameObjectFactory} instance.
     */
    @NotNull
    private final GameObjectFactory<TestGameObject, TestMapArchObject, TestArchetype> gameObjectFactory;

    /**
     * The archetype to create floor game objects.
     */
    @NotNull
    private final TestArchetype floorArchetype;

    /**
     * The archetype to create exit game objects.
     */
    @NotNull
    private final TestArchetype exitArchetype;

    /**
     * The archetype to create 2x1 mob game objects.
     */
    @NotNull
    private final TestArchetype mob21Archetype;

    /**
     * Creates a new instance.
     * @param topmostInsertionMode the "topmost" insertion mode instance
     * @param gameObjectFactory the game object factory to use
     * @param archetypeSet the archetype set to use
     * @param faceObjectProviders the face object providers to use
     * @param animationObjects the animation objects to use
     * @throws DuplicateArchetypeException if an internal error occurs
     */
    public TestMapModelHelper(@NotNull final InsertionMode<TestGameObject, TestMapArchObject, TestArchetype> topmostInsertionMode, @NotNull final GameObjectFactory<TestGameObject, TestMapArchObject, TestArchetype> gameObjectFactory, @NotNull final ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> archetypeSet, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) throws DuplicateArchetypeException {
        this.topmostInsertionMode = topmostInsertionMode;
        this.gameObjectFactory = gameObjectFactory;

        floorArchetype = new TestDefaultArchetype("floor", faceObjectProviders, animationObjects);
        floorArchetype.setAttributeInt(BaseObject.TYPE, FLOOR_TYPE);
        archetypeSet.addArchetype(floorArchetype);

        exitArchetype = new TestDefaultArchetype("exit", faceObjectProviders, animationObjects);
        exitArchetype.setAttributeInt(BaseObject.TYPE, EXIT_TYPE);
        archetypeSet.addArchetype(exitArchetype);

        mob21Archetype = new TestDefaultArchetype("mob21", faceObjectProviders, animationObjects);
        mob21Archetype.setAttributeInt(BaseObject.TYPE, MOB_TYPE);
        archetypeSet.addArchetype(mob21Archetype);
        final TestArchetype mob21bArchetype = new TestDefaultArchetype("mob21b", faceObjectProviders, animationObjects);
        mob21bArchetype.setMultiX(1);
        mob21Archetype.addTailPart(mob21bArchetype);
        archetypeSet.addArchetype(mob21bArchetype);
    }

    /**
     * Inserts a {@link #floorArchetype} game object into a map model.
     * @param mapModel the map model to insert into
     * @param point the position to insert at
     * @return the inserted game object
     * @throws CannotInsertGameObjectException if the archetype cannot be
     * inserted
     */
    @NotNull
    public TestGameObject insertFloor(@NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel, @NotNull final Point point) throws CannotInsertGameObjectException {
        return insertArchetype(mapModel, point, floorArchetype, false);
    }

    /**
     * Inserts an {@link #exitArchetype} game object into a map model.
     * @param mapModel the map model to insert into
     * @param point the position to insert at
     * @return the inserted game object
     * @throws CannotInsertGameObjectException if the archetype cannot be
     * inserted
     */
    @NotNull
    public TestGameObject insertExit(@NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel, @NotNull final Point point) throws CannotInsertGameObjectException {
        return insertArchetype(mapModel, point, exitArchetype, false);
    }

    /**
     * Inserts a {@link #mob21Archetype} game object into a map model.
     * @param mapModel the map model to insert into
     * @param point the position to insert at
     * @return the inserted game object
     * @throws CannotInsertGameObjectException if the archetype cannot be
     * inserted
     */
    @NotNull
    public TestGameObject insertMob21(@NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel, @NotNull final Point point) throws CannotInsertGameObjectException {
        return insertArchetype(mapModel, point, mob21Archetype, false);
    }

    /**
     * Inserts an archetype game object into a map model.
     * @param mapModel the map model to insert into
     * @param x the x coordinate to insert at
     * @param y the y coordinate to insert at
     * @param archetype the archetype to insert
     * @param join whether to perform autojoining
     * @return the inserted game object
     * @throws CannotInsertGameObjectException if the archetype cannot be
     * inserted
     */
    @NotNull
    public TestGameObject insertArchetype(@NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel, final int x, final int y, @NotNull final BaseObject<TestGameObject, TestMapArchObject, TestArchetype, ?> archetype, final boolean join) throws CannotInsertGameObjectException {
        return insertArchetype(mapModel, new Point(x, y), archetype, join);
    }

    /**
     * Inserts an archetype game object into a map model.
     * @param mapModel the map model to insert into
     * @param point the position to insert at
     * @param archetype the archetype to insert
     * @param join whether to perform autojoining
     * @return the inserted game object
     * @throws CannotInsertGameObjectException if the archetype cannot be
     * inserted
     */
    @NotNull
    public TestGameObject insertArchetype(@NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel, @NotNull final Point point, @NotNull final BaseObject<TestGameObject, TestMapArchObject, TestArchetype, ?> archetype, final boolean join) throws CannotInsertGameObjectException {
        final TestGameObject gameObject = mapModel.insertBaseObject(archetype, point, true, join, topmostInsertionMode);
        if (gameObject == null) {
            throw new CannotInsertGameObjectException(archetype, point);
        }
        return gameObject;
    }

    /**
     * Inserts an {@link #exitArchetype} game object into a game object.
     * @param gameObject the game object
     * @return the inserted game object
     */
    @NotNull
    public TestGameObject insertExit(@NotNull final GameObjectContainer<TestGameObject, TestMapArchObject, TestArchetype> gameObject) {
        return insertArchetype(gameObject, exitArchetype);
    }

    /**
     * Inserts a {@link #mob21Archetype} game object into a game object.
     * @param gameObject the game object
     * @return the inserted game object
     */
    @NotNull
    public TestGameObject insertMob21(@NotNull final GameObjectContainer<TestGameObject, TestMapArchObject, TestArchetype> gameObject) {
        return insertArchetype(gameObject, mob21Archetype);
    }

    /**
     * Inserts an archetype into a game object.
     * @param gameObject the game object
     * @param archetype the archetype to insert
     * @return the inserted game object
     */
    @NotNull
    public TestGameObject insertArchetype(@NotNull final GameObjectContainer<TestGameObject, TestMapArchObject, TestArchetype> gameObject, @NotNull final BaseObject<TestGameObject, TestMapArchObject, TestArchetype, ?> archetype) {
        final TestGameObject newGameObject = archetype.newInstance(gameObjectFactory);
        gameObject.addLast(newGameObject);
        return newGameObject;
    }

    /**
     * Checks for expected {@link MapModel}'s contents.
     * @param mapModel the map model
     * @param lines the expected contents
     */
    public static void checkMapContents(@NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel, @NotNull final String... lines) {
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        Assert.assertEquals(lines.length, mapSize.getHeight());
        final Pattern pattern1 = Pattern.compile("\\|");
        final Pattern pattern2 = StringUtils.PATTERN_COMMA;
        final Point pos = new Point();
        for (int y = 0; y < lines.length; y++) {
            final CharSequence line = lines[y];
            final String[] square = pattern1.split(line, -1);
            Assert.assertEquals(square.length, mapSize.getWidth());

            for (int x = 0; x < square.length; x++) {
                final String square2 = square[x];
                final String[] gameObjects = square2.isEmpty() ? EMPTY_STRING_ARRAY : pattern2.split(square2, -1);
                pos.x = x;
                pos.y = y;
                checkContentsString(mapModel.getMapSquare(pos), gameObjects);
            }
        }
    }

    /**
     * Checks that a {@link MapSquare} contains the given game objects.
     * @param mapSquare the map square
     * @param gameObjects the game object
     */
    private static void checkContentsString(@NotNull final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare, @NotNull final String... gameObjects) {
        int i = 0;
        for (final BaseObject<?, ?, ?, ?> gameObject : mapSquare) {
            final String gameObjectName = gameObject.getBestName();
            if (i >= gameObjects.length) {
                Assert.fail("map square " + mapSquare.getMapX() + "/" + mapSquare.getMapY() + " contains excess game object '" + gameObjectName + "'");
            } else if (!gameObjectName.equals(gameObjects[i])) {
                Assert.fail("map square " + mapSquare.getMapX() + "/" + mapSquare.getMapY() + " contains wrong game object '" + gameObjectName + "' at index " + i + ", expected '" + gameObjects[i] + "'");
            }
            i++;
        }
        if (i < gameObjects.length) {
            Assert.fail("map square " + mapSquare.getMapX() + "/" + mapSquare.getMapY() + " is missing game object '" + gameObjects[i] + "'");
        }
    }

    /**
     * Checks that a {@link MapSquare} contains the given game objects.
     * @param mapSquare the map square
     * @param gameObjects the game object
     */
    public static void checkContents(@NotNull final Iterable<TestGameObject> mapSquare, @NotNull final BaseObject<?, ?, ?, ?>... gameObjects) {
        int i = 0;
        for (final BaseObject<?, ?, ?, ?> gameObject : mapSquare) {
            final String gameObjectName = gameObject.getBestName();
            if (i >= gameObjects.length) {
                Assert.fail("map square contains excess game object '" + gameObjectName + "'");
            } else if (gameObject != gameObjects[i]) {
                Assert.fail("map square contains wrong game object '" + gameObjectName + "' at index " + i + ", expected '" + gameObjects[i].getBestName() + "'");
            }
            i++;
        }
        if (i < gameObjects.length) {
            Assert.fail("map square is missing game object '" + gameObjects[i].getBestName() + "'");
        }

        final boolean inContainer = mapSquare instanceof GameObject;
        for (final GameObject<?, ?, ?> gameObject : mapSquare) {
            Assert.assertEquals(inContainer, gameObject.isInContainer());
            if (inContainer) {
                // game objects within inventories must not contain tail parts
                Assert.assertFalse(gameObject.isMulti());
            } else {
                // game objects on the map must have expanded tail parts
                Assert.assertEquals(gameObject.getArchetype().isMulti(), gameObject.isMulti());
            }
        }
    }

}
