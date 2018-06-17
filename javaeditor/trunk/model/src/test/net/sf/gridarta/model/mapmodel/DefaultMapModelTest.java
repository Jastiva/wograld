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
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectListener;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.model.match.NamedGameObjectMatcher;
import net.sf.gridarta.model.match.TypeNrsGameObjectMatcher;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link DefaultMapModel}.
 * @author Andreas Kirschbaum
 */
@SuppressWarnings("FeatureEnvy")
// This is a test. It has feature envy by definition.
public class DefaultMapModelTest {

    /**
     * The map model listener registered to all map models to record map
     * changes.
     */
    @NotNull
    private final MapModelListener<TestGameObject, TestMapArchObject, TestArchetype> mapModelListener = new MapModelListener<TestGameObject, TestMapArchObject, TestArchetype>() {

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            log("mapSizeChanged", null, null);
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>> mapSquares) {
            log("mapSquaresChanged", mapSquares, null);
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<TestGameObject> gameObjects, @NotNull final Set<TestGameObject> transientGameObjects) {
            if (!gameObjects.isEmpty()) {
                log("mapObjectsChanged", null, gameObjects);
            }
            if (!transientGameObjects.isEmpty()) {
                log("mapObjectsTransientChanged", null, transientGameObjects);
            }
        }

        @Override
        public void errorsChanged(@NotNull final ErrorCollector<TestGameObject, TestMapArchObject, TestArchetype> errors) {
            result.append("errorsChanged");
        }

        @Override
        public void mapFileChanged(@Nullable final File oldMapFile) {
            // ignore
        }

        @Override
        public void modifiedChanged() {
            // ignore
        }

    };

    /**
     * The {@link MapArchObjectListener} registered to all map models to record
     * map changes.
     */
    @NotNull
    private final MapArchObjectListener mapArchObjectListener = new MapArchObjectListener() {

        @Override
        public void mapMetaChanged() {
            result.append("mapMetaChanged");
        }

        @Override
        public void mapSizeChanged(@NotNull final Size2D mapSize) {
            // ignore
        }

    };

    /**
     * Collects the map model changes.
     */
    @NotNull
    private final StringBuilder result = new StringBuilder();

    /**
     * Test case for an empty transaction.
     */
    @Test
    public void testEmpty() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);

        mapModel.beginTransaction("TEST");
        mapModel.endTransaction();
        Assert.assertEquals("", result.toString());
    }

    /**
     * Test case for {@link net.sf.gridarta.model.maparchobject.MapArchObject#setMapSize(Size2D)}.
     */
    @Test
    public void testResizeMap1() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);

        mapModel.beginTransaction("TEST");
        mapModel.getMapArchObject().setMapSize(new Size2D(4, 3));
        mapModel.endTransaction();
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("mapSizeChanged:\n");
        expectedResult.append("no squares\n");
        expectedResult.append("no game objects\n");
        Assert.assertEquals(expectedResult.toString(), result.toString());
    }

    /**
     * Test case for {@link net.sf.gridarta.model.maparchobject.MapArchObject#setMapSize(Size2D)}.
     */
    @Test
    public void testResizeMap2() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);

        mapModel.beginTransaction("TEST");
        mapModel.getMapArchObject().setMapSize(new Size2D(4, 3));
        mapModel.endTransaction();

        result.setLength(0);
        mapModel.beginTransaction("TEST");
        mapModel.getMapArchObject().setMapSize(new Size2D(4, 3));
        mapModel.endTransaction();
        Assert.assertEquals("", result.toString());
    }

    /**
     * Test case for {@link net.sf.gridarta.model.maparchobject.MapArchObject#setMapSize(Size2D)}.
     */
    @Test
    public void testResizeMap3() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);

        mapModel.beginTransaction("TEST");
        mapModel.getMapArchObject().setMapSize(new Size2D(4, 3));
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "1", 1, 2, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModel.endTransaction();

        result.setLength(0);
        mapModel.beginTransaction("TEST");
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "1", 2, 2, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModel.getMapArchObject().setMapSize(new Size2D(1, 2)); // cancels square changed event
        mapModel.endTransaction();
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("mapSizeChanged:\n");
        expectedResult.append("no squares\n");
        expectedResult.append("no game objects\n");
        Assert.assertEquals(expectedResult.toString(), result.toString());
    }

    /**
     * Test case for {@link net.sf.gridarta.model.maparchobject.MapArchObject#setMapSize(Size2D)}.
     */
    @Test
    public void testResizeMap4() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);

        mapModel.beginTransaction("TEST");
        mapModel.getMapArchObject().setMapSize(new Size2D(4, 3));
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "1", 1, 2, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModel.endTransaction();

        result.setLength(0);
        mapModel.beginTransaction("TEST");
        for (final BaseObject<TestGameObject, TestMapArchObject, TestArchetype, ?> gameObject : mapModel.getMapSquare(new Point(1, 2))) {
            gameObject.setAttributeString("key", "value");
        }
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "1", 2, 2, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModel.getMapArchObject().setMapSize(new Size2D(1, 2)); // cancels square changed event
        mapModel.endTransaction();
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("mapSizeChanged:\n");
        expectedResult.append("no squares\n");
        expectedResult.append("no game objects\n");
        Assert.assertEquals(expectedResult.toString(), result.toString());
    }

    /**
     * Test case for {@link DefaultMapModel#addGameObjectToMap(GameObject,
     * Point, InsertionMode)}.
     */
    @Test
    public void testAddGameObjectToMap1() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);

        mapModel.beginTransaction("TEST");
        mapModel.getMapArchObject().setMapSize(new Size2D(4, 3));
        mapModel.endTransaction();

        result.setLength(0);
        mapModel.beginTransaction("TEST");
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "1", 1, 2, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModel.endTransaction();
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("mapObjectsChanged:\n");
        expectedResult.append("no squares\n");
        expectedResult.append("game object 1 2 1\n");
        expectedResult.append("mapSquaresChanged:\n");
        expectedResult.append("square 1 2\n");
        expectedResult.append("no game objects\n");
        Assert.assertEquals(expectedResult.toString(), result.toString());
    }

    /**
     * Test case for {@link DefaultMapModel#addGameObjectToMap(GameObject,
     * Point, InsertionMode)}.
     */
    @Test
    public void testAddGameObjectToMap2() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);

        mapModel.beginTransaction("TEST");
        mapModel.getMapArchObject().setMapSize(new Size2D(4, 3));
        mapModel.endTransaction();

        result.setLength(0);
        mapModel.beginTransaction("TEST");
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "1", 1, 2, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "1", 1, 2, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "1", 2, 2, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModel.endTransaction();
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("mapObjectsChanged:\n");
        expectedResult.append("no squares\n");
        expectedResult.append("game object 1 2 1\n");
        expectedResult.append("game object 2 2 1\n");
        expectedResult.append("mapSquaresChanged:\n");
        expectedResult.append("square 1 2\n");
        expectedResult.append("square 2 2\n");
        expectedResult.append("no game objects\n");
        Assert.assertEquals(expectedResult.toString(), result.toString());
    }

    /**
     * Test case for changed objects.
     */
    @Test
    public void testModifiedGameObject1() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);

        mapModel.beginTransaction("TEST");
        mapModel.getMapArchObject().setMapSize(new Size2D(4, 3));
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "1", 1, 2, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModel.endTransaction();

        result.setLength(0);
        mapModel.beginTransaction("TEST");
        for (final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject : mapModel.getMapSquare(new Point(1, 2))) {
            mapModelCreator.insertGameObject(gameObject, "arch", "2");
        }
        mapModel.endTransaction();
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("mapObjectsChanged:\n");
        expectedResult.append("no squares\n");
        expectedResult.append("game object 0 0 2\n");
        expectedResult.append("game object 1 2 1\n");
        Assert.assertEquals(expectedResult.toString(), result.toString());
    }

    /**
     * Test case for changed objects.
     */
    @Test
    public void testModifiedGameObject2() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);

        mapModel.beginTransaction("TEST");
        mapModel.getMapArchObject().setMapSize(new Size2D(4, 3));
        mapModelCreator.addGameObjectToMap(mapModel, "arch", "1", 1, 2, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModel.endTransaction();

        result.setLength(0);
        mapModel.beginTransaction("TEST");
        for (final BaseObject<TestGameObject, TestMapArchObject, TestArchetype, ?> gameObject : mapModel.getMapSquare(new Point(1, 2))) {
            gameObject.setAttributeString("key", "value");
        }
        mapModel.endTransaction();
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("mapObjectsChanged:\n");
        expectedResult.append("no squares\n");
        expectedResult.append("game object 1 2 1\n");
        Assert.assertEquals(expectedResult.toString(), result.toString());
    }

    /**
     * Test case for {@link DefaultMapModel#getAllGameObjects()}: for a 1x2 game
     * object only the head should be returned.
     */
    @Test
    public void testGetAllGameObjects1() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);
        final BaseObject<TestGameObject, TestMapArchObject, TestArchetype, TestArchetype> archetype = mapModelCreator.getArchetype("arch");
        final TestArchetype archetype2 = mapModelCreator.getArchetype("arch2");
        archetype2.setMultiY(1);
        archetype.addTailPart(archetype2);

        mapModel.beginTransaction("TEST");
        mapModel.getMapArchObject().setMapSize(new Size2D(1, 2));
        final TestGameObject gameObject = mapModel.insertBaseObject(archetype, new Point(0, 0), true, false, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModel.endTransaction();

        final List<TestGameObject> gameObjects = mapModel.getAllGameObjects();
        Assert.assertEquals(1, gameObjects.size());
        Assert.assertEquals(gameObject, gameObjects.get(0));
    }

    /**
     * Test case for {@link DefaultMapModel#getAllGameObjects()}: the {@link
     * BaseObject#DIRECTION} attribute is set only if the inserted object
     * supports a direction.
     */
    @Test
    public void testUpdateDirectionOnInsert() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        mapModelCreator.getArchetypeChooserModel().setDirection(2);
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);
        final BaseObject<TestGameObject, TestMapArchObject, TestArchetype, TestArchetype> archetypeWithoutDirection = mapModelCreator.getArchetype("arch_without");
        final TestArchetype archetypeWithDirection = mapModelCreator.getArchetype("arch_with");
        archetypeWithDirection.setUsesDirection(true);

        mapModel.beginTransaction("TEST");
        mapModel.getMapArchObject().setMapSize(new Size2D(1, 1));
        final TestGameObject gameObjectWithout = mapModel.insertBaseObject(archetypeWithoutDirection, new Point(0, 0), true, false, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        final TestGameObject gameObjectWith = mapModel.insertBaseObject(archetypeWithDirection, new Point(0, 0), true, false, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModel.endTransaction();

        Assert.assertNotNull(gameObjectWithout);
        Assert.assertFalse(archetypeWithoutDirection.usesDirection());
        Assert.assertEquals(0, gameObjectWithout.getAttributeInt(BaseObject.DIRECTION));
        Assert.assertNotNull(gameObjectWith);
        Assert.assertTrue(archetypeWithDirection.usesDirection());
        Assert.assertEquals(2, gameObjectWith.getAttributeInt(BaseObject.DIRECTION));
    }

    /**
     * Checks that modifications correctly update edit types.
     */
    @Test
    public void testEditType1() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final GameObjectMatchers gameObjectMatchers = mapModelCreator.getGameObjectMatchers();
        gameObjectMatchers.addGameObjectMatcher(new NamedGameObjectMatcher(1, "m1", "1", true, null, new TypeNrsGameObjectMatcher(1)));
        gameObjectMatchers.addGameObjectMatcher(new NamedGameObjectMatcher(2, "m2", "2", true, null, new TypeNrsGameObjectMatcher(2)));
        gameObjectMatchers.addGameObjectMatcher(new NamedGameObjectMatcher(4, "e3", "3", true, new TypeNrsGameObjectMatcher(3), new TypeNrsGameObjectMatcher(4)));
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = newMapModel(mapModelCreator);
        final TestArchetype a1 = mapModelCreator.getArchetype("a1");
        a1.setAttributeInt(BaseObject.TYPE, 1);
        final TestArchetype a4 = mapModelCreator.getArchetype("a4");
        a4.setAttributeInt(BaseObject.TYPE, 4);

        mapModel.addActiveEditType(7);

        mapModel.beginTransaction("TEST");
        final TestGameObject g1 = mapModel.insertBaseObject(a1, new Point(0, 0), true, false, mapModelCreator.getInsertionModeSet().getAutoInsertionMode());
        mapModel.endTransaction();
        Assert.assertNotNull(g1);
        Assert.assertEquals(1, g1.getEditType());

        mapModel.beginTransaction("TEST");
        g1.setAttributeInt(BaseObject.TYPE, 2);
        mapModel.endTransaction();
        Assert.assertEquals(2, g1.getEditType());

        mapModel.beginTransaction("TEST");
        g1.setAttributeInt(BaseObject.TYPE, 3);
        mapModel.endTransaction();
        Assert.assertEquals(0, g1.getEditType());

        final TestGameObject g2 = a4.newInstance(mapModelCreator.getGameObjectFactory());
        mapModel.beginTransaction("TEST");
        g1.addLast(g2);
        mapModel.endTransaction();
        Assert.assertEquals(4, g1.getEditType());
        Assert.assertEquals(0, g2.getEditType());

        final TestGameObject g3 = a4.newInstance(mapModelCreator.getGameObjectFactory());
        mapModel.beginTransaction("TEST");
        g2.addLast(g3);
        mapModel.endTransaction();
        Assert.assertEquals(4, g1.getEditType());
        Assert.assertEquals(0, g2.getEditType());
        Assert.assertEquals(0, g3.getEditType());

        mapModel.beginTransaction("TEST");
        g2.setAttributeInt(BaseObject.TYPE, 2);
        mapModel.endTransaction();
        Assert.assertEquals(4, g1.getEditType());
        Assert.assertEquals(2, g2.getEditType());
        Assert.assertEquals(0, g3.getEditType());

        mapModel.beginTransaction("TEST");
        g3.setAttributeInt(BaseObject.TYPE, 2);
        mapModel.endTransaction();
        Assert.assertEquals(0, g1.getEditType());
        Assert.assertEquals(2, g2.getEditType());
        Assert.assertEquals(2, g3.getEditType());

        mapModel.beginTransaction("TEST");
        g3.setAttributeInt(BaseObject.TYPE, 4);
        mapModel.endTransaction();
        Assert.assertEquals(4, g1.getEditType());
        Assert.assertEquals(2, g2.getEditType());
        Assert.assertEquals(0, g3.getEditType());

        mapModel.beginTransaction("TEST");
        g2.setAttributeInt(BaseObject.TYPE, 3);
        mapModel.endTransaction();
        Assert.assertEquals(4, g1.getEditType());
        Assert.assertEquals(4, g2.getEditType());
        Assert.assertEquals(0, g3.getEditType());

        mapModel.beginTransaction("TEST");
        mapModel.removeGameObject(g3, false);
        mapModel.endTransaction();
        Assert.assertEquals(0, g1.getEditType());
        Assert.assertEquals(0, g2.getEditType());

        mapModel.beginTransaction("TEST");
        mapModel.removeGameObject(g2, false);
        mapModel.endTransaction();
        Assert.assertEquals(0, g1.getEditType());

        mapModel.beginTransaction("TEST");
        mapModel.removeGameObject(g1, false);
        mapModel.endTransaction();
    }

    /**
     * Records a change event.
     * @param name the event name
     * @param mapSquares the changed map squares
     * @param gameObjects the changed game objects
     */
    private void log(@NotNull final String name, @Nullable final Iterable<MapSquare<TestGameObject, TestMapArchObject, TestArchetype>> mapSquares, @Nullable final Iterable<TestGameObject> gameObjects) {
        result.append(name);
        result.append(":\n");
        if (mapSquares != null) {
            final Collection<String> lines = new TreeSet<String>();
            for (final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare : mapSquares) {
                lines.add("square " + mapSquare.getMapX() + " " + mapSquare.getMapY() + "\n");
            }
            for (final String line : lines) {
                result.append(line);
            }
        } else {
            result.append("no squares\n");
        }
        if (gameObjects != null) {
            final Collection<String> lines = new TreeSet<String>();
            for (final BaseObject<TestGameObject, TestMapArchObject, TestArchetype, ?> gameObject : gameObjects) {
                lines.add("game object " + gameObject.getMapX() + " " + gameObject.getMapY() + " " + gameObject.getBestName() + "\n");
            }
            for (final String line : lines) {
                result.append(line);
            }
        } else {
            result.append("no game objects\n");
        }
    }

    /**
     * Creates a new {@link MapModel} instance.
     * @param mapModelCreator the map model creator to use
     * @return the new map model instance
     */
    @NotNull
    private MapModel<TestGameObject, TestMapArchObject, TestArchetype> newMapModel(@NotNull final TestMapModelCreator mapModelCreator) {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelCreator.newMapModel(1, 1);

        result.setLength(0);
        mapModel.addMapModelListener(mapModelListener);
        mapModel.getMapArchObject().addMapArchObjectListener(mapArchObjectListener);
        Assert.assertEquals("", result.toString());

        return mapModel;
    }

}
