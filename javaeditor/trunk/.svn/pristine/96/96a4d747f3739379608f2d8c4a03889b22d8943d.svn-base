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
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Automatically guess the insertion position. May replace rather than insert
 * the object.
 * @author Andreas Kirschbaum
 */
public class AutoInsertionMode<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements InsertionMode<G, A, R> {

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
     * A {@link GameObjectMatcher} matching wall game objects.
     * @serial
     */
    @Nullable
    private final GameObjectMatcher wallGameObjectMatcher;

    /**
     * A {@link GameObjectMatcher} matching monster game objects.
     * @serial
     */
    @Nullable
    private final GameObjectMatcher belowFloorGameObjectMatcher;

    /**
     * A {@link GameObjectMatcher} matching system objects that should stay on
     * top.
     * @serial
     */
    @Nullable
    private final GameObjectMatcher systemObjectGameObjectMatcher;

    /**
     * Initializes the class.
     * @param floorGameObjectMatcher the floor matcher to use
     * @param wallGameObjectMatcher the wall matcher to use
     * @param belowFloorGameObjectMatcher the game object to insert below the
     * floor
     * @param systemObjectGameObjectMatcher matches system objects that should
     * stay on top
     */
    public AutoInsertionMode(@Nullable final GameObjectMatcher floorGameObjectMatcher, @Nullable final GameObjectMatcher wallGameObjectMatcher, @Nullable final GameObjectMatcher belowFloorGameObjectMatcher, @Nullable final GameObjectMatcher systemObjectGameObjectMatcher) {
        this.floorGameObjectMatcher = floorGameObjectMatcher;
        this.wallGameObjectMatcher = wallGameObjectMatcher;
        this.belowFloorGameObjectMatcher = belowFloorGameObjectMatcher;
        this.systemObjectGameObjectMatcher = systemObjectGameObjectMatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(@NotNull final G gameObject, @NotNull final MapSquare<G, A, R> mapSquare) {
        if (floorGameObjectMatcher != null && floorGameObjectMatcher.isMatching(gameObject)) {
            replaceFloor(gameObject, mapSquare, mapSquare.getLast(floorGameObjectMatcher));
        } else if (wallGameObjectMatcher != null && wallGameObjectMatcher.isMatching(gameObject) && replaceWall(gameObject, mapSquare, mapSquare.getLast(wallGameObjectMatcher))) {
        } else if (belowFloorGameObjectMatcher != null && belowFloorGameObjectMatcher.isMatching(gameObject)) {
            mapSquare.addFirst(gameObject);
        } else if (systemObjectGameObjectMatcher != null && !systemObjectGameObjectMatcher.isMatching(gameObject)) {
            insertNonSystemObject(gameObject, mapSquare, mapSquare.getFirst(systemObjectGameObjectMatcher));
        } else {
            mapSquare.addLast(gameObject);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "auto";
    }

    /**
     * Replace a floor game object.
     * @param gameObject the game object to insert with
     * @param mapSquare the map square to modify
     * @param lastFloor the last floor game object within
     * <code>mapSquare</code>
     */
    private void replaceFloor(@NotNull final G gameObject, @NotNull final MapSquare<G, A, R> mapSquare, @Nullable final G lastFloor) {
        // floor exists ==> replace it
        if (lastFloor != null && !lastFloor.isMulti()) {
            mapSquare.replace(lastFloor, gameObject);
            return;
        }

        // "below floor" objects exist ==> insert afterwards
        if (belowFloorGameObjectMatcher != null) {
            final G lastBelowFloor = mapSquare.getLastOfLeadingSpan(belowFloorGameObjectMatcher);
            if (lastBelowFloor != null) {
                mapSquare.insertAfter(lastBelowFloor, gameObject);
                return;
            }
        }

        // fallback ==> insert bottommost
        mapSquare.addFirst(gameObject);
    }

    /**
     * Replaces a wall game object.
     * @param gameObject the game object to insert with
     * @param mapSquare the map square to modify
     * @param lastWall the last wall game object within <code>mapSquare</code>
     * @return whether an existing wall was replaced
     */
    private boolean replaceWall(@NotNull final G gameObject, @NotNull final GameObjectContainer<G, A, R> mapSquare, @Nullable final G lastWall) {
        if (lastWall != null && !lastWall.isMulti()) {
            mapSquare.replace(lastWall, gameObject);
            return true;
        }

        return false;
    }

    /**
     * Inserts a non-system game object.
     * @param gameObject the game object to insert
     * @param mapSquare the map square to modify
     * @param firstSystemObject the first system game object within
     * <code>mapSquare</code>
     */
    private void insertNonSystemObject(@NotNull final G gameObject, @NotNull final GameObjectContainer<G, A, R> mapSquare, @Nullable final G firstSystemObject) {
        mapSquare.insertAfter(firstSystemObject, gameObject);
    }

}
