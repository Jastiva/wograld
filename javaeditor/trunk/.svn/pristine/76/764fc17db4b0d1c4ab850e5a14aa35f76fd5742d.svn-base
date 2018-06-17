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

package net.sf.gridarta.model.undo;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.SavedSquares;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The class <code>UndoState</code> holds information to undo/redo one edit
 * operation. It consists of the map model state, the map arch object state, and
 * the name of the edit operation.
 * @author Andreas Kirschbaum
 */
public class UndoState<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The name of the operation.
     */
    @NotNull
    private final String name;

    /**
     * The map arch object before the operation started.
     */
    @NotNull
    private final A mapArchObject;

    /**
     * The affected map squares. The contents represent the state before the
     * operation started.
     */
    @Nullable
    private SavedSquares<G, A, R> savedSquares;

    /**
     * Create a new instance.
     * @param name the name of the operation
     * @param mapArchObject the map arch object to store; the value is
     * <em>not</em> copied
     */
    public UndoState(@NotNull final String name, @NotNull final A mapArchObject) {
        this.name = name;
        this.mapArchObject = mapArchObject;
    }

    /**
     * Return the name of the operation.
     * @return the name of the operation
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Records the affected map squares.
     * @param savedSquares the map squares to record
     * @throws IllegalStateException if changed map squares have been recorded
     * before
     */
    @SuppressWarnings("NullableProblems")
    public void setSavedSquares(@NotNull final SavedSquares<G, A, R> savedSquares) {
        if (this.savedSquares != null) {
            throw new IllegalStateException();
        }

        this.savedSquares = savedSquares;
    }

    /**
     * Returns the map squares that have changed in the operation.
     * @return the changed map squares
     * @throws IllegalStateException if no saved squares have been recorded
     */
    @NotNull
    @SuppressWarnings("NullableProblems")
    public SavedSquares<G, A, R> getSavedSquares() {
        if (savedSquares == null) {
            throw new IllegalStateException();
        }
        return savedSquares;
    }

    /**
     * Returns the map arch object before the operation started.
     * @return the map arch object
     */
    @NotNull
    public A getMapArchObject() {
        return mapArchObject;
    }

}
