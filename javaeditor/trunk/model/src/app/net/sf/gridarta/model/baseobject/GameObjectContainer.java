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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.NotInsideContainerException;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for classes that contain GameObjects as children in the sense of
 * containment. The interface serves 2 main purposes: <ul> <li>{@link
 * GameObject} extends this class for containing other GameObjects, like
 * inventory contents e.g. of a bag or attached events.</li> <li>{@link
 * MapSquare} extends this class to list the squares on a MapSquare.</li> </ul>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @todo In case of MapSquares, this class is most likely bogus regarding
 * multi-part objects. This needs to be fixed.
 */
@SuppressWarnings("ClassReferencesSubclass")
public abstract class GameObjectContainer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Cloneable, Iterable<G>, Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The contents of this container.
     * @note the order of this container is bottom to top.
     * @serial
     */
    @NotNull
    private List<G> contents;

    /**
     * {@link Iterable} implementation for recursive traversal.
     */
    @NotNull
    private transient Iterable<G> recursive;

    /**
     * {@link Iterable} implementation for reverse traversal.
     */
    @NotNull
    private transient Iterable<G> reverse;

    /**
     * Create a new GameObjectContainer.
     */
    protected GameObjectContainer() {
        initData();
    }

    /**
     * Initialize the fields. This is needed because there are two ways of
     * object construction, <ul> <li>regular object construction via constructor
     * invocation and</li> <li>cloning of objects via {@link #clone()}.</li>
     * </ul>
     */
    private void initData() {
        contents = new ArrayList<G>(0);
        recursive = new Iterable<G>() {

            @Override
            public Iterator<G> iterator() {
                return new RecursiveGameObjectIterator<G, A, R>(GameObjectContainer.this);
            }

        };
        reverse = new Iterable<G>() {

            @Override
            public Iterator<G> iterator() {
                return new ReverseIterator<G>(contents);
            }

        };
    }

    /**
     * {@inheritDoc} The Iterator returned does not recurse, it only contains
     * objects on the first level. The Iterator returned is transparent, that
     * means modifying the iterator's collection actually modifies the
     * underlying GameObjectContainer.
     */
    @NotNull
    @Override
    public Iterator<G> iterator() {
        // Do not return contents.iterator() directly because otherwise the remove() operation will not properly unlink the removed GameObject from its container.
        return new Iterator<G>() {

            /** The basic iterator. */
            @NotNull
            private final Iterator<G> delegate = contents.iterator();

            /** Current element (last element returned by {@link #next()}). */
            @Nullable
            private G current;

            @Override
            public boolean hasNext() {
                return delegate.hasNext();
            }

            @Override
            public G next() {
                current = delegate.next();
                return current;
            }

            @Override
            public void remove() {
                // keep this in sync with GameObjectContainer#remove(G)
                // we can't simply invoke GameObjectContainer#remove(current) because that would result in a ConcurrentModificationException.
                notifyBeginChange();
                try {
                    if (contents.size() >= 2 && contents.get(0) == current) {
                        contents.get(1).propagateElevation(current);
                    }
                    delegate.remove();
                    current.setContainer(null, 0, 0);
                } finally {
                    notifyEndChange();
                }
            }
        };
    }

    /**
     * Return an object that is the reverse representation. Invoke this method
     * if you want to iterate over the contained GameObjects in reverse order.
     * @return reverse representation
     */
    @NotNull
    public Iterable<G> reverse() {
        return reverse;
    }

    /**
     * Return an object that is a recursive representation. Invoke this method
     * if you want to iterate over the contained GameObjects recursively.
     * @return recursive representation
     */
    @NotNull
    public Iterable<G> recursive() {
        return recursive;
    }

    /**
     * Check whether this square is empty.
     * @return <code>true</code> if this square is empty, otherwise
     *         <code>false</code>
     */
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    /**
     * Return the first GameObject contained in this container.
     * @return first GameObject contained or <code>null</code> if {@link
     *         #isEmpty()} returns <code>true</code>
     */
    @Nullable
    public G getFirst() {
        return contents.isEmpty() ? null : contents.get(0);
    }

    /**
     * Return the {@link GameObject} preceding a given game object.
     * @param gameObject the given game object
     * @return the preceding game object or <code>null</code> if no preceding
     *         game object exists.
     */
    @Nullable
    public G getPrev(@NotNull final G gameObject) {
        final Iterator<G> it = contents.iterator();
        while (it.hasNext()) {
            if (it.next() == gameObject) {
                return it.hasNext() ? it.next() : null;
            }
        }
        return null;
    }

    /**
     * Return the {@link GameObject} succeeding a given game object.
     * @param gameObject the given game object
     * @return the successor game object or <code>null</code> if no successor
     *         game object exists.
     */
    @Nullable
    public G getNext(@NotNull final G gameObject) {
        G prevGameObject = null;
        for (final G tmpGameObject : contents) {
            if (tmpGameObject == gameObject) {
                return prevGameObject;
            }
            prevGameObject = tmpGameObject;
        }
        return null;
    }

    /**
     * Return the last GameObject contained in this container. You should not
     * invoke this method to iterate over GameObjects, such invocation is
     * regarded deprecated.
     * @return last GameObject contained or <code>null</code> if {@link
     *         #isEmpty()} returns <code>true</code>
     * @noinspection TypeMayBeWeakened
     */
    @Nullable
    public G getLast() {
        return contents.isEmpty() ? null : contents.get(contents.size() - 1);
    }

    /**
     * Remove a GameObject from this container.
     * @param gameObject GameObject to remove
     * @throws IllegalArgumentException if <var>gameObject</var> isn't in this
     * container
     * @fixme this implementation does not take care of multi square objects.
     */
    public void remove(@NotNull final G gameObject) {
        // keep this in sync with iterator()
        notifyBeginChange();
        try {
            if (contents.size() >= 2 && contents.get(0) == gameObject) {
                contents.get(1).propagateElevation(gameObject);
            }
            if (!contents.remove(gameObject)) {
                throw new NotInsideContainerException(this, gameObject);
            }
            gameObject.setContainer(null, 0, 0);
        } finally {
            notifyEndChange();
        }
    }

    /**
     * Removes all GameObjects from this container.
     * @fixme this implementation does not take care of multi square objects.
     */
    public void removeAll() {
        if (contents.size() <= 0) {
            return;
        }

        notifyBeginChange();
        try {
            for (final GameObject<G, A, R> gameObject : contents) {
                gameObject.setContainer(null, 0, 0);
            }
            contents.clear();
        } finally {
            notifyEndChange();
        }
    }

    /**
     * Returns whether this game object is the top-most one.
     * @param gameObject item to move to top
     * @return whether this game object is the top-most one
     * @throws IllegalArgumentException if <var>gameObject</var> isn't in this
     * container
     */
    public boolean isTop(@NotNull final G gameObject) {
        return !contents.isEmpty() && contents.get(contents.size() - 1) == gameObject;
    }

    /**
     * Returns whether this game object is the bottom-most one.
     * @param gameObject item to move to top
     * @return whether this game object is the bottom-most one
     * @throws IllegalArgumentException if <var>gameObject</var> isn't in this
     * container
     */
    public boolean isBottom(@NotNull final G gameObject) {
        return !contents.isEmpty() && contents.get(0) == gameObject;
    }

    /**
     * Move an item to top.
     * @param gameObject item to move to top
     * @throws IllegalArgumentException if <var>gameObject</var> isn't in this
     * container
     */
    public void moveTop(@NotNull final G gameObject) {
        final int oldIndex = contents.indexOf(gameObject);
        if (oldIndex != contents.size() - 1) {
            notifyBeginChange();
            try {
                if (!contents.remove(gameObject)) {
                    throw new NotInsideContainerException(this, gameObject);
                }
                if (oldIndex == 0) {
                    assert contents.size() >= 1;
                    contents.get(0).propagateElevation(gameObject);
                }
                contents.add(gameObject);
            } finally {
                notifyEndChange();
            }
        }
    }

    /**
     * Move an item up.
     * @param gameObject item to move up
     * @throws IllegalArgumentException if <var>gameObject</var> isn't in this
     * container
     */
    public void moveUp(@NotNull final G gameObject) {
        final int oldIndex = contents.indexOf(gameObject);
        if (oldIndex < contents.size() - 1) {
            notifyBeginChange();
            try {
                if (!contents.remove(gameObject)) {
                    throw new NotInsideContainerException(this, gameObject);
                }
                if (oldIndex == 0) {
                    assert contents.size() >= 1;
                    contents.get(0).propagateElevation(gameObject);
                }
                contents.add(oldIndex + 1, gameObject);
            } finally {
                notifyEndChange();
            }
        }
    }

    /**
     * Move an item down.
     * @param gameObject item to move down
     * @throws IllegalArgumentException if <var>gameObject</var> isn't in this
     * container
     */
    public void moveDown(@NotNull final G gameObject) {
        final int oldIndex = contents.indexOf(gameObject);
        if (oldIndex > 0) {
            notifyBeginChange();
            try {
                if (!contents.remove(gameObject)) {
                    throw new NotInsideContainerException(this, gameObject);
                }
                if (oldIndex == 1) {
                    assert contents.size() >= 1;
                    gameObject.propagateElevation(contents.get(0));
                }
                contents.add(oldIndex - 1, gameObject);
            } finally {
                notifyEndChange();
            }
        }
    }

    /**
     * Move an item to bottom.
     * @param gameObject item to move to bottom
     * @throws IllegalArgumentException if <var>gameObject</var> isn't in this
     * container
     */
    public void moveBottom(@NotNull final G gameObject) {
        final int oldIndex = contents.indexOf(gameObject);
        if (oldIndex != 0) {
            notifyBeginChange();
            try {
                if (!contents.remove(gameObject)) {
                    throw new NotInsideContainerException(this, gameObject);
                }
                assert contents.size() >= 1;
                gameObject.propagateElevation(contents.get(0));
                contents.add(0, gameObject);
            } finally {
                notifyEndChange();
            }
        }
    }

    /**
     * Add the given GameObject at the end of this Container.
     * @param gameObject the free yet unlinked <code>GameObject</code> to be
     * placed in the inventory
     * @throws IllegalArgumentException if <var>gameObject</var> already is
     * inside another container
     */
    public void addLast(@NotNull final G gameObject) {
        if (gameObject.isInContainer()) {
            throw new IllegalArgumentException("Can't add " + gameObject + " to " + this + " because it's already inside " + gameObject.getContainer());
        }

        notifyBeginChange();
        try {
            contents.add(gameObject);
            setThisContainer(gameObject);
            gameObject.markModified();
        } finally {
            notifyEndChange();
        }
    }

    /**
     * Add the given GameObject at the end of this Container.
     * @param gameObject the free yet unlinked <code>GameObject</code> to be
     * placed in the inventory
     * @throws IllegalArgumentException if <var>gameObject</var> already is
     * inside another container
     */
    public void addFirst(@NotNull final G gameObject) {
        if (gameObject.isInContainer()) {
            throw new IllegalArgumentException("Can't add " + gameObject + " to " + this + " because it's already inside " + gameObject.getContainer());
        }

        notifyBeginChange();
        try {
            if (!contents.isEmpty()) {
                gameObject.propagateElevation(contents.get(0));
            }
            contents.add(0, gameObject);
            setThisContainer(gameObject);
            gameObject.markModified();
        } finally {
            notifyEndChange();
        }
    }

    /**
     * Add a GameObject after another.
     * @param previousGameObject previous anchor or <code>null</code> to insert
     * last
     * @param gameObject GameObject to insert
     * @throws IllegalArgumentException if <var>gameObject</var> already is
     * inside another container or <var>previousGameObject</var> isn't inside
     * this container
     * @noinspection TypeMayBeWeakened
     */
    public void insertAfter(@Nullable final G previousGameObject, @NotNull final G gameObject) {
        if (gameObject.isInContainer()) {
            throw new IllegalArgumentException("Can't add " + gameObject + " to " + this + " because it's already inside " + gameObject.getContainer());
        }

        if (previousGameObject == null) {
            addLast(gameObject);
            return;
        }

        if (!previousGameObject.isHead()) {
            throw new IllegalArgumentException();
        }

        notifyBeginChange();
        try {
            boolean added = false;
            int index = 0;
            for (final G tmpGameObject : contents) {
                if (tmpGameObject.getHead() == previousGameObject) {
                    if (index == 0) {
                        gameObject.propagateElevation(contents.get(0));
                    }
                    contents.add(index, gameObject);
                    added = true;
                    break;
                }
                index++;
            }
            if (!added) {
                throw new IllegalArgumentException("Can't add " + gameObject + " to " + this + " because " + previousGameObject + " is not inside");
            }
            setThisContainer(gameObject);
            gameObject.markModified();
        } finally {
            notifyEndChange();
        }
    }

    /**
     * Add a GameObject before another.
     * @param gameObject GameObject to insert
     * @param nextGameObject nextGameObject anchor or <code>null</code> to add
     * first
     * @throws IllegalArgumentException if <var>gameObject</var> already is
     * inside another container or <var>prev</var> isn't inside this container
     */
    public void insertBefore(@NotNull final G gameObject, @Nullable final G nextGameObject) {
        if (gameObject.isInContainer()) {
            throw new IllegalArgumentException("Can't add " + gameObject + " to " + this + " because it's already inside " + gameObject.getContainer());
        }

        if (nextGameObject == null) {
            addFirst(gameObject);
            return;
        }

        if (!nextGameObject.isHead()) {
            throw new IllegalArgumentException();
        }

        notifyBeginChange();
        try {
            final int insertIndex = contents.indexOf(nextGameObject);
            if (insertIndex == -1) {
                throw new IllegalArgumentException("Can't insert " + gameObject + " before " + nextGameObject + " because that isn't inside " + this);
            }
            contents.add(insertIndex + 1, gameObject);
            setThisContainer(gameObject);
            gameObject.markModified();
        } finally {
            notifyEndChange();
        }
    }

    /**
     * Replace an GameObject with another one.
     * @param oldGameObject old GameObject to be replaced
     * @param newGameObject new GameObject that replaces oldGameObject
     * @throws IllegalArgumentException if <var>oldGameObject</var> isn't in
     * this container or is a multi-part object
     */
    public void replace(@NotNull final G oldGameObject, @NotNull final G newGameObject) {
        if (oldGameObject.isMulti()) {
            throw new IllegalArgumentException();
        }

        notifyBeginChange();
        try {
            final int insertIndex = contents.indexOf(oldGameObject);
            if (insertIndex == -1) {
                throw new NotInsideContainerException(this, oldGameObject);
            }
            if (insertIndex == 0) {
                newGameObject.propagateElevation(oldGameObject);
            }
            contents.remove(oldGameObject);
            oldGameObject.setContainer(null, 0, 0);
            contents.add(insertIndex, newGameObject);
            setThisContainer(newGameObject);
            newGameObject.markModified();
        } finally {
            notifyEndChange();
        }
    }

    /**
     * Returns the {@link MapSquare} of this container.
     * @return the map square of this container or <code>null</code> if this
     *         GameObjectContainer is not (yet?) connected to a map (a {@link
     *         MapSquare} would return itself)
     */
    @Nullable
    public abstract MapSquare<G, A, R> getMapSquare();

    /**
     * Notify the map model that this container is about to change.
     */
    protected abstract void notifyBeginChange();

    /**
     * Notify the map model that this container has changed.
     */
    protected abstract void notifyEndChange();

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    protected Object clone() {
        try {
            final GameObjectContainer<G, A, R> clone = (GameObjectContainer<G, A, R>) super.clone();
            clone.initData();
            for (final BaseObject<G, A, R, G> gameObject : contents) {
                final G clonedGameObject = gameObject.clone();
                clone.contents.add(clonedGameObject);
                clone.setThisContainer(clonedGameObject);
            }
            return clone;
        } catch (final CloneNotSupportedException e) {
            assert false : "This class must be cloneable" + e;
            throw new AssertionError(e);
        }
    }

    // writeObject() is not required because this class doesn't require special handling during serialization.

    private void readObject(@NotNull final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        // initialize transients
        initData();
    }

    /**
     * Compare this object to another game object container.
     * @param gameObjectContainer the other game object container
     * @return <code>true</code> if this object equals the other object
     */
    public boolean hasSameContents(@NotNull final GameObjectContainer<?, ?, ?> gameObjectContainer) {
        if (gameObjectContainer.contents.size() != contents.size()) {
            return false;
        }

        for (int i = 0; i < contents.size(); i++) {
            if (!gameObjectContainer.contents.get(i).isEqual(contents.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Sets a {@link GameObject}'s container to this container.
     * @param gameObject the game object
     */
    protected abstract void setThisContainer(@NotNull final G gameObject);

    /**
     * Returns this instance as a {@link GameObject} or <code>null</code> if
     * this instance is not a game object.
     * @return this instance or <code>null</code>
     */
    @Nullable
    public abstract G asGameObject();

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        final Iterator<G> it = contents.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(",");
                sb.append(it.next());
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * An iterator for iterating over a list in reverse order.
     * @todo move this class to JAPI
     */
    private static class ReverseIterator<T extends GameObject<?, ?, ?>> implements Iterator<T> {

        /**
         * The iterator used for delegation.
         */
        @NotNull
        private final ListIterator<T> delegate;

        /**
         * The list being iterated over.
         */
        @NotNull
        private final List<T> list;

        /**
         * Create a reverse iterator.
         * @param list to iterate over in reverse order
         */
        private ReverseIterator(@NotNull final List<T> list) {
            this.list = list;
            delegate = list.listIterator(list.size());
        }

        @Override
        public boolean hasNext() {
            return delegate.hasPrevious();
        }

        // previous can throw NoSuchElementException but InspectionGadgets doesn't know about that.
        @SuppressWarnings("IteratorNextCanNotThrowNoSuchElementException")
        @NotNull
        @Override
        public T next() {
            return delegate.previous();
        }

        @Override
        public void remove() {
            if (delegate.nextIndex() == 0 && list.size() >= 2) {
                list.get(1).propagateElevation(list.get(0));
            }
            delegate.remove();
        }

    }

}
