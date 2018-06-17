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

package net.sf.gridarta.model.mapmodel;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link FilterGameObjectIterator}.
 * @author Andreas Kirschbaum
 */
public class FilterGameObjectIteratorTest {

    /**
     * Checks that an empty iterator is filtered correctly.
     */
    @Test
    public void testEmptyIterator() {
        check(new AcceptAllMatcher(), new ArrayGameObjectIterator());
    }

    /**
     * Checks that a matcher that accepts all game objects returns all game
     * objects.
     */
    @Test
    public void testAllMatcher() {
        final TestMapModelCreator creator = new TestMapModelCreator(false);
        final TestGameObject o1 = creator.newGameObject("arch", "o1");
        final TestGameObject o2 = creator.newGameObject("arch", "o2");
        final TestGameObject o3 = creator.newGameObject("arch", "o3");
        check(new AcceptAllMatcher(), new ArrayGameObjectIterator(o1, o2, o3), o1, o2, o3);
    }

    /**
     * Checks that a matcher that accepts some game objects returns only the
     * accepted game objects.
     */
    @Test
    public void testAcceptMatcher() {
        final TestMapModelCreator creator = new TestMapModelCreator(false);
        final TestGameObject o1 = creator.newGameObject("arch", "o1");
        final TestGameObject o2 = creator.newGameObject("arch", "o2");
        final TestGameObject o3 = creator.newGameObject("arch", "o3");
        check(new AcceptMatcher(o1, o2), new ArrayGameObjectIterator(o1, o2, o3, o1, o2, o3, o1, o1, o2, o3, o3), o1, o2, o1, o2, o1, o1, o2);
    }

    /**
     * Creates a new {@link FilterGameObjectIterator} instance and checks that
     * it returns the expected game objects.
     * @param matcher the matcher for the filter game object iterator
     * @param iterator the underlying iterator for the filter game object
     * iterator
     * @param gameObjects the expected game objects to be returned from the
     * filter game object iterator
     */
    private static void check(@NotNull final GameObjectMatcher matcher, @NotNull final Iterator<TestGameObject> iterator, @NotNull final TestGameObject... gameObjects) {
        final Iterator<TestGameObject> filterGameObjectIterator = new FilterGameObjectIterator<TestGameObject, TestMapArchObject, TestArchetype>(iterator, matcher);
        for (final TestGameObject gameObject : gameObjects) {
            Assert.assertTrue(filterGameObjectIterator.hasNext());
            Assert.assertSame(gameObject, filterGameObjectIterator.next());
        }
        Assert.assertFalse(filterGameObjectIterator.hasNext());
    }

    /**
     * A {@link GameObjectMatcher} that accepts all game objects.
     */
    private static class AcceptAllMatcher implements GameObjectMatcher {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isMatching(@NotNull final GameObject<?, ?, ?> gameObject) {
            return true;
        }

    }

    /**
     * A {@link GameObjectMatcher} that accepts a set of game objects.
     */
    private static class AcceptMatcher implements GameObjectMatcher {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The accepted game objects.
         */
        @NotNull
        private final Collection<GameObject<?, ?, ?>> gameObjects = new HashSet<GameObject<?, ?, ?>>();

        /**
         * Creates a new instance.
         * @param gameObjects the game objects to accept
         */
        private AcceptMatcher(@NotNull final GameObject<?, ?, ?>... gameObjects) {
            this.gameObjects.addAll(Arrays.asList(gameObjects));
        }

        @Override
        public boolean isMatching(@NotNull final GameObject<?, ?, ?> gameObject) {
            return gameObjects.contains(gameObject);
        }

    }

    /**
     * An {@link Iterator} that returns a fixed list of {@link GameObject
     * GameObjects}.
     */
    private static class ArrayGameObjectIterator implements Iterator<TestGameObject> {

        /**
         * The {@link GameObject GameObjects} to return.
         */
        @NotNull
        private final TestGameObject[] gameObjects;

        /**
         * The current index into {@link #gameObjects}.
         */
        private int index;

        /**
         * Creates a new instance.
         * @param gameObjects the game objects to return
         */
        private ArrayGameObjectIterator(@NotNull final TestGameObject... gameObjects) {
            this.gameObjects = gameObjects.clone();
        }

        @Override
        public boolean hasNext() {
            return index < gameObjects.length;
        }

        @Override
        public TestGameObject next() {
            if (index >= gameObjects.length) {
                throw new NoSuchElementException();
            }
            return gameObjects[index++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
