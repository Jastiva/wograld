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

package net.sf.gridarta.var.crossfire.model.gameobject;

import java.util.Iterator;
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Checks that {@link GameObjectContainer GameObjectContainers} correctly
 * propagate elevation information when being modified.
 * @author Andreas Kirschbaum
 */
public class PropagateElevationTest {

    /**
     * Checks that {@link GameObjectContainer#addFirst(net.sf.gridarta.model.gameobject.GameObject)}
     * does work correctly.
     */
    @Test
    public void testAddFirst() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> gameObjectContainer = gameObjectCreator.newGameObject(0);

        // first game object => keep
        gameObjectContainer.addFirst(gameObjectCreator.newGameObject(123));
        check(gameObjectContainer, 123);

        // without elevation => propagate
        gameObjectContainer.addFirst(gameObjectCreator.newGameObject(0));
        check(gameObjectContainer, 123);

        // with elevation => ignore but propagate
        gameObjectContainer.addFirst(gameObjectCreator.newGameObject(321));
        check(gameObjectContainer, 123);
    }

    /**
     * Checks that {@link GameObjectContainer#addLast(net.sf.gridarta.model.gameobject.GameObject)}
     * does work correctly.
     */
    @Test
    public void testAddLast() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> gameObjectContainer = gameObjectCreator.newGameObject(0);

        // first game object => keep
        gameObjectContainer.addLast(gameObjectCreator.newGameObject(123));
        check(gameObjectContainer, 123);

        // without elevation => keep
        gameObjectContainer.addLast(gameObjectCreator.newGameObject(0));
        check(gameObjectContainer, 123, 0);

        // with elevation => keep
        gameObjectContainer.addLast(gameObjectCreator.newGameObject(321));
        check(gameObjectContainer, 123, 0, 321);
    }

    /**
     * Checks that {@link GameObjectContainer#insertBefore(net.sf.gridarta.model.gameobject.GameObject,
     * net.sf.gridarta.model.gameobject.GameObject)} does work correctly.
     */
    @Test
    public void testInsertBefore() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> gameObjectContainer = gameObjectCreator.newGameObject(0);

        // first game object => keep
        gameObjectContainer.insertBefore(gameObjectCreator.newGameObject(1), null);
        check(gameObjectContainer, 1);

        // without elevation => propagate
        gameObjectContainer.insertBefore(gameObjectCreator.newGameObject(0), null);
        check(gameObjectContainer, 1, 0);

        // with elevation => ignore but propagate
        final GameObject ob1 = gameObjectCreator.newGameObject(2);
        gameObjectContainer.insertBefore(ob1, null);
        check(gameObjectContainer, 1, 0, 0);

        // with elevation => keep
        gameObjectContainer.insertBefore(gameObjectCreator.newGameObject(3), ob1);
        check(gameObjectContainer, 1, 3, 0, 0);

        // with elevation => ignore but propagate
        gameObjectContainer.insertBefore(gameObjectCreator.newGameObject(4), null);
        check(gameObjectContainer, 1, 0, 3, 0, 0);
    }

    /**
     * Checks that {@link GameObjectContainer#insertAfter(net.sf.gridarta.model.gameobject.GameObject,
     * net.sf.gridarta.model.gameobject.GameObject)} does work correctly.
     */
    @Test
    public void testInsertAfter() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> gameObjectContainer = gameObjectCreator.newGameObject(0);

        // first game object => keep
        gameObjectContainer.insertAfter(null, gameObjectCreator.newGameObject(1));
        check(gameObjectContainer, 1);

        // at end => keep
        final GameObject ob1 = gameObjectCreator.newGameObject(2);
        gameObjectContainer.insertAfter(null, ob1);
        check(gameObjectContainer, 1, 2);

        // at front, without elevation => propagate
        gameObjectContainer.insertAfter(ob1, gameObjectCreator.newGameObject(0));
        check(gameObjectContainer, 1, 0, 2);

        // middle => keep
        gameObjectContainer.insertAfter(ob1, gameObjectCreator.newGameObject(3));
        check(gameObjectContainer, 1, 0, 3, 2);

        // at front, with elevation => ignore but propagate
        gameObjectContainer.insertAfter(gameObjectContainer.getFirst(), gameObjectCreator.newGameObject(4));
        check(gameObjectContainer, 1, 0, 0, 3, 2);
    }

    /**
     * Checks that {@link GameObjectContainer#moveBottom(net.sf.gridarta.model.gameobject.GameObject)}
     * does work correctly.
     */
    @Test
    public void testMoveBottom() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> container1 = newContainer(gameObjectCreator, 1, 0, 0, 0);
        container1.moveBottom(get(container1, 0));
        check(container1, 1, 0, 0, 0);
        container1.moveBottom(get(container1, 1));
        check(container1, 1, 0, 0, 0);
        container1.moveBottom(get(container1, 3));
        check(container1, 1, 0, 0, 0);
    }

    /**
     * Checks that {@link GameObjectContainer#moveDown(net.sf.gridarta.model.gameobject.GameObject)}
     * does work correctly.
     */
    @Test
    public void testMoveDown() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> container1 = newContainer(gameObjectCreator, 1, 0, 0, 0);
        container1.moveDown(get(container1, 0));
        check(container1, 1, 0, 0, 0);
        container1.moveDown(get(container1, 1));
        check(container1, 1, 0, 0, 0);
        container1.moveDown(get(container1, 3));
        check(container1, 1, 0, 0, 0);
    }

    /**
     * Checks that {@link GameObjectContainer#moveUp(net.sf.gridarta.model.gameobject.GameObject)}
     * does work correctly.
     */
    @Test
    public void testMoveUp() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> container1 = newContainer(gameObjectCreator, 1, 0, 0, 0);
        container1.moveUp(get(container1, 0));
        check(container1, 1, 0, 0, 0);
        container1.moveUp(get(container1, 1));
        check(container1, 1, 0, 0, 0);
        container1.moveUp(get(container1, 3));
        check(container1, 1, 0, 0, 0);
    }

    /**
     * Checks that {@link GameObjectContainer#moveTop(net.sf.gridarta.model.gameobject.GameObject)}
     * does work correctly.
     */
    @Test
    public void testMoveTop() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> container1 = newContainer(gameObjectCreator, 1, 0, 0, 0);
        container1.moveTop(get(container1, 0));
        check(container1, 1, 0, 0, 0);
        container1.moveTop(get(container1, 1));
        check(container1, 1, 0, 0, 0);
        container1.moveTop(get(container1, 3));
        check(container1, 1, 0, 0, 0);
    }

    /**
     * Checks that {@link GameObjectContainer#remove(net.sf.gridarta.model.gameobject.GameObject)}
     * does work correctly.
     */
    @Test
    public void testRemove() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> container1 = newContainer(gameObjectCreator, 1, 0, 0, 0);
        container1.remove(get(container1, 0));
        check(container1, 1, 0, 0);
        container1.remove(get(container1, 2));
        check(container1, 1, 0);
        container1.remove(get(container1, 0));
        check(container1, 1);
        container1.remove(get(container1, 0));
        check(container1);
    }

    /**
     * Checks that {@link GameObjectContainer#replace(net.sf.gridarta.model.gameobject.GameObject,
     * net.sf.gridarta.model.gameobject.GameObject)} does work correctly.
     */
    @Test
    public void testReplace() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> container1 = newContainer(gameObjectCreator, 1, 0, 0, 0);
        container1.replace(get(container1, 0), gameObjectCreator.newGameObject(2));
        check(container1, 1, 0, 0, 0);
        container1.replace(get(container1, 1), gameObjectCreator.newGameObject(3));
        check(container1, 1, 3, 0, 0);
        container1.replace(get(container1, 0), gameObjectCreator.newGameObject(4));
        check(container1, 1, 3, 0, 0);
    }

    /**
     * Checks that {@link GameObjectContainer#iterator()}'s {@link
     * Iterator#remove()} does work correctly.
     */
    @Test
    public void testIterator() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> container1 = newContainer(gameObjectCreator, 1, 0, 0, 0, 0, 0);
        final Iterator<GameObject> it = container1.iterator();
        it.next();
        it.remove();
        check(container1, 1, 0, 0, 0, 0);
        it.next();
        it.remove();
        check(container1, 1, 0, 0, 0);
        it.next();
        it.next();
        it.remove();
        check(container1, 1, 0, 0);
    }

    /**
     * Checks that {@link GameObjectContainer#reverse()}'s {@link
     * Iterator#remove()} does work correctly.
     */
    @Test
    public void testReverse() {
        final GameObjectCreator gameObjectCreator = new GameObjectCreator();
        final GameObjectContainer<GameObject, MapArchObject, Archetype> container1 = newContainer(gameObjectCreator, 1, 0, 0, 0, 0, 0);
        final Iterator<GameObject> it = container1.reverse().iterator();
        it.next();
        it.remove();
        check(container1, 1, 0, 0, 0, 0);
        it.next();
        it.remove();
        check(container1, 1, 0, 0, 0);
        it.next();
        it.next();
        it.remove();
        check(container1, 1, 0, 0);
        it.next();
        it.remove();
        check(container1, 1, 0);
        it.next();
        it.remove();
        check(container1, 1);
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Creates a new {@link GameObjectContainer} that contains game objects with
     * the given elevation values.
     * @param gameObjectCreator the game object creator to use
     * @param elevations the elevations
     * @return the game object container
     */
    @NotNull
    private static GameObjectContainer<GameObject, MapArchObject, Archetype> newContainer(@NotNull final GameObjectCreator gameObjectCreator, final int... elevations) {
        final GameObjectContainer<GameObject, MapArchObject, Archetype> gameObject = gameObjectCreator.newGameObject(0);
        for (final int elevation : elevations) {
            gameObject.addLast(gameObjectCreator.newGameObject(elevation));
        }
        return gameObject;
    }

    /**
     * Returns the game object at a given index.
     * @param gameObjects the game objects to search
     * @param index the index
     * @return the game object at <code>index</code>
     */
    @NotNull
    private static GameObject get(@NotNull final Iterable<GameObject> gameObjects, final int index) {
        int left = index;
        for (final GameObject gameObject : gameObjects) {
            if (left == 0) {
                return gameObject;
            }
            left--;
        }

        Assert.fail("index " + index + " not found");
        throw new AssertionError();
    }

    /**
     * Checks some game objects for expected elevation values.
     * @param gameObjects the game objects
     * @param elevation the expected elevation value for the first game object
     */
    private static void check(@NotNull final Iterable<GameObject> gameObjects, final int... elevation) {
        int i = 0;
        for (final GameObject gameObject : gameObjects) {
            final int thisElevation = gameObject.getAttributeInt(GameObject.ELEVATION);
            final int expectedElevation = i < elevation.length ? elevation[i] : 0;
            Assert.assertEquals(gameObject.getBestName(), expectedElevation, thisElevation);
            i++;
        }
    }

}
