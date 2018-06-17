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

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Insert right above the topmost floor square. Inserts bottommost if no floor
 * square exists.
 * @author Andreas Kirschbaum
 */
public class AboveFloorInsertionMode<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements InsertionMode<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * A {@link GameObjectMatcher} matching floor game objects.
     * @serial
     */
    @Nullable
    private final GameObjectMatcher floorGameObjectMatcher;

    /**
     * Initializes the class.
     * @param floorGameObjectMatcher the floor matcher to use
     */
    public AboveFloorInsertionMode(@Nullable final GameObjectMatcher floorGameObjectMatcher) {
        this.floorGameObjectMatcher = floorGameObjectMatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(@NotNull final G gameObject, @NotNull final MapSquare<G, A, R> mapSquare) {
        if (floorGameObjectMatcher == null) {
            mapSquare.addFirst(gameObject);
            return;
        }

        final G lastFloor = mapSquare.getLast(floorGameObjectMatcher);
        if (lastFloor == null) {
            mapSquare.addFirst(gameObject);
            return;
        }

        mapSquare.insertBefore(gameObject, lastFloor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "above floor";
    }

}
