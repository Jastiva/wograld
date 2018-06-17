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

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract factory for creating {@link GameObject} instances.
 * @author Andreas Kirschbaum
 */
public interface GameObjectFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Creates a new {@link Archetype} instance.
     * @param archetypeName the name of the archetype
     * @return the new archetype
     */
    @NotNull
    R newArchetype(@NotNull String archetypeName);

    /**
     * Creates a new GameObject from an {@link Archetype}.
     * @param archetype the archetype
     * @return the new game object
     */
    @NotNull
    G createGameObject(@NotNull R archetype);

    /**
     * Creates a new GameObject from an {@link Archetype}.
     * @param archetype the archetype
     * @param head the head part to add to
     * @return the new game object
     */
    @NotNull
    G createGameObjectPart(@NotNull R archetype, @Nullable G head);

    /**
     * Creates a copy of a game object.
     * @param gameObject the game object to copy
     * @return the new game object
     */
    @NotNull
    G cloneGameObject(@NotNull G gameObject);

    /**
     * Creates a copy of a game object, including tail parts. This object must
     * be the head part.
     * @param gameObject the game object to copy
     * @return the new game object
     */
    @NotNull
    G cloneMultiGameObject(@NotNull G gameObject);

    /**
     * Copies inventory objects from an archetype into a game object.
     * @param gameObject the game object to add to
     * @param archetype the archetype to copy from
     */
    void createInventory(@NotNull GameObject<G, A, R> gameObject, @NotNull Iterable<G> archetype);

}
