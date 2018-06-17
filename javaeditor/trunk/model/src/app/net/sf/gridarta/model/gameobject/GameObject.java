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

package net.sf.gridarta.model.gameobject;

import java.util.Iterator;
import javax.swing.ImageIcon;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A <code>GameObject</code> instance reflects a game object (object on a map).
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public interface GameObject<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends BaseObject<G, A, R, G> {

    /**
     * The editor folder name for server-internal archetypes. These archetypes
     * are not available to users.
     */
    String EDITOR_FOLDER_INTERN = "intern";

    /**
     * Removes all GameObjects from this container.
     * @fixme this implementation does not take care of multi square objects.
     */
    void removeAll();

    /**
     * Add the given GameObject at the end of this Container.
     * @param gameObject the free yet unlinked <code>GameObject</code> to be
     * placed in the inventory
     * @throws IllegalArgumentException if <var>gameObject</var> already is
     * inside another container
     */
    void addLast(@NotNull G gameObject);

    /**
     * Return the last GameObject contained in this container.
     * @return first GameObject contained or <code>null</code> if {@link
     *         #isEmpty()} returns <code>true</code>
     */
    @Nullable
    G getLast();

    /**
     * Add the given GameObject at the beginning of this Container.
     * @param gameObject the free yet unlinked <code>GameObject</code> to be
     * placed in the inventory
     * @throws IllegalArgumentException if <var>gameObject</var> already is
     * inside another container
     */
    void addFirst(@NotNull G gameObject);

    /**
     * Return the first GameObject contained in this container.
     * @return first GameObject contained or <code>null</code> if {@link
     *         #isEmpty()} returns <code>true</code>
     */
    @Nullable
    G getFirst();

    /**
     * If there is elevation data in the other game object, move it to here.
     * @param gameObject the other game object
     */
    void propagateElevation(@NotNull BaseObject<?, ?, ?, ?> gameObject);

    /**
     * {@inheritDoc} The Iterator returned does not recurse, it only contains
     * objects on the first level. The Iterator returned is transparent, that
     * means modifying the iterator's collection actually modifies the
     * underlying GameObjectContainer.
     */
    @NotNull
    @Override
    Iterator<G> iterator();

    /**
     * Return an object that is the reverse representation. Invoke this method
     * if you want to iterate over the contained GameObjects in reverse order.
     * @return reverse representation
     */
    @NotNull
    Iterable<G> reverse();

    /**
     * Return an object that is a recursive representation. Invoke this method
     * if you want to iterate over the contained GameObjects recursively.
     * @return recursive representation
     */
    @NotNull
    Iterable<G> recursive();

    /**
     * Returns whether this GameObject has one or more scripted events defined.
     * @return <code>true</code> if this GameObject has one or more scripted
     *         events, otherwise <code>false</code>
     */
    boolean isScripted();

    /**
     * Check whether this square is empty.
     * @return <code>true</code> if this square is empty, otherwise
     *         <code>false</code>
     */
    boolean isEmpty();

    /**
     * Returns the face for this game object according to the settings of a
     * {@link MapViewSettings} instance.
     * @param mapViewSettings the map view settings instance
     * @return the face
     */
    @NotNull
    ImageIcon getImage(@NotNull MapViewSettings mapViewSettings);

    /**
     * Remove this GameObject from its container. Does nothing if the object has
     * no container. This method also takes perfectly well care of multi-part
     * GameObjects.
     */
    void remove();

    /**
     * Returns container of this GameObject. There are two possibilities for the
     * container: <ul> <li>Another GameObject, which means this object is in the
     * inventory of that GameObject.</li> <li>A MapSquare, which means that this
     * GameObject is top level on that MapSquare (Crossfire returns
     * <code>null</code> for this).</li> </ul>
     * @return the container of this game object
     * @see #getTopContainer()
     */
    @Nullable
    GameObjectContainer<G, A, R> getContainer();

    /**
     * Returns whether this game object is the top-most one. Returns true if it
     * has no container.
     * @return whether this game object is the top-most one
     */
    boolean isTop();

    /**
     * Returns whether this game object is the bottom-most one. Returns true if
     * it has no container.
     * @return whether this game object is the bottom-most one
     */
    boolean isBottom();

    /**
     * Move this GameObject top. Does nothing if the object has no container.
     */
    void moveTop();

    /**
     * Move this GameObject up. Does nothing if the object has no container.
     */
    void moveUp();

    /**
     * Move this GameObject down. Does nothing if the object has no container.
     */
    void moveDown();

    /**
     * Move this GameObject bottom. Does nothing if the object has no
     * container.
     */
    void moveBottom();

    /**
     * Insert a GameObject before this GameObject.
     * @param node GameObject to append
     */
    void insertBefore(@NotNull G node);

    /**
     * Insert a GameObject after this GameObject.
     * @param node GameObject to append
     */
    void insertAfter(@NotNull G node);

    /**
     * Returns the environment game object if this game object is in the
     * inventory or <code>null</code>.
     * @return the game object this game object is part of or <code>null</code>
     */
    @Nullable
    G getContainerGameObject();

    /**
     * Get the topmost container of this GameObject (in Game sense, which means
     * being in a MapSquare isn't, but being in an GameObject is).
     * @return the topmost container but always GameObject, never a map square;
     *         if this object is topmost itself, it returns itself
     * @see #getContainer()
     */
    @NotNull
    G getTopContainer();

    /**
     * Sets container of this GameObject. There are two possibilities for the
     * container: <ul> <li>Another GameObject, which means this object is in the
     * inventory of that GameObject.</li> <li>A MapSquare, which means that this
     * GameObject is top level on that MapSquare.</li> </ul>
     * @param container the container of this game object
     * @param mapX the x coordinate on the map or <code>0</code>
     * @param mapY the y coordinate on the map or <code>0</code>
     * @throws IllegalStateException in case this GameObject is an Archetype
     * (Archetypes must not be added to a map).
     */
    void setContainer(@Nullable GameObjectContainer<G, A, R> container, int mapX, int mapY);

    /**
     * Check whether this GameObject is in a Container (in Gridarta sense, which
     * means being in a MapSquare isn't, but being in an GameObject is).
     * @return <code>true</code> if this GameObject has a Container and the
     *         Container is an GameObject, otherwise (no Container or Container
     *         is not an GameObject) false
     */
    boolean isInContainer();

    /**
     * Get the MapSquare of this {@link GameObjectContainer}.
     * @return the map square of this container or <code>null</code> if this
     *         GameObjectContainer is not (yet?) connected to a map (a
     *         <code>MapSquare</code> would return itself)
     */
    @Nullable
    MapSquare<G, A, R> getMapSquare();

    /**
     * Returns the game object preceding this game object.
     * @return the successor game object or <code>null</code> if no preceding
     *         game object exists.
     */
    @Nullable
    G getPrev();

    /**
     * Returns the game object succeeding this game object.
     * @return the preceding game object or <code>null</code> if no successor
     *         game object exists.
     */
    @Nullable
    G getNext();

    /**
     * Set the Archetype of this GameObject.
     * @param archetype new Archetype of this GameObject.
     * @see #getArchetype()
     */
    void setArchetype(@NotNull R archetype);

    /**
     * Return whether this instance references an undefined archetype.
     * @return <code>true</code> if this instance references an undefined
     *         archetype
     */
    boolean hasUndefinedArchetype();

    /**
     * Marks this game object as "modified".
     */
    void markModified();

    /**
     * Returns the effective light radius of this game object.
     * @return the effective light radius or <code>0</code> if this object does
     *         not emit light
     */
    int getLightRadius();

}
