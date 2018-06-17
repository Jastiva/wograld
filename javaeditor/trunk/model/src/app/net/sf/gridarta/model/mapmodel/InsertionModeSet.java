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
 * A set of {@link InsertionMode InsertionModes}.
 * @author Andreas Kirschbaum
 */
public class InsertionModeSet<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The "auto" insertion mode.
     */
    @NotNull
    private final InsertionMode<G, A, R> autoInsertionMode;

    /**
     * The "topmost" insertion mode.
     */
    @NotNull
    private final InsertionMode<G, A, R> topmostInsertionMode;

    /**
     * The "above floor" insertion mode.
     */
    @NotNull
    private final InsertionMode<G, A, R> aboveFloorInsertionMode;

    /**
     * The "below floor" insertion mode.
     */
    @NotNull
    private final InsertionMode<G, A, R> belowFloorInsertionMode;

    /**
     * The "bottommost" insertion mode.
     */
    @NotNull
    private final InsertionMode<G, A, R> bottommostInsertionMode = new BottommostInsertionMode<G, A, R>();

    /**
     * Creates a new instance.
     * @param topmostInsertionMode the "topmost" insertion mode
     * @param floorGameObjectMatcher the game object matcher for selecting floor
     * game objects
     * @param wallGameObjectMatcher the game object matcher for selecting wall
     * game objects
     * @param belowFloorGameObjectMatcher the game object matcher for selecting
     * below floor game objects
     * @param systemObjectGameObjectMatcher the game object matcher for
     * selecting game objects that should stay on top
     */
    public InsertionModeSet(@NotNull final InsertionMode<G, A, R> topmostInsertionMode, @Nullable final GameObjectMatcher floorGameObjectMatcher, @Nullable final GameObjectMatcher wallGameObjectMatcher, @Nullable final GameObjectMatcher belowFloorGameObjectMatcher, @Nullable final GameObjectMatcher systemObjectGameObjectMatcher) {
        this.topmostInsertionMode = topmostInsertionMode;
        autoInsertionMode = new AutoInsertionMode<G, A, R>(floorGameObjectMatcher, wallGameObjectMatcher, belowFloorGameObjectMatcher, systemObjectGameObjectMatcher);
        aboveFloorInsertionMode = new AboveFloorInsertionMode<G, A, R>(floorGameObjectMatcher);
        belowFloorInsertionMode = new BelowFloorInsertionMode<G, A, R>(floorGameObjectMatcher);
    }

    /**
     * Returns the "auto" insertion mode.
     * @return the insertion mode
     */
    @NotNull
    public InsertionMode<G, A, R> getAutoInsertionMode() {
        return autoInsertionMode;
    }

    /**
     * Returns the "topmost" insertion mode.
     * @return the insertion mode
     */
    @NotNull
    public InsertionMode<G, A, R> getTopmostInsertionMode() {
        return topmostInsertionMode;
    }

    /**
     * Returns the "above floor" insertion mode.
     * @return the insertion mode
     */
    @NotNull
    public InsertionMode<G, A, R> getAboveFloorInsertionMode() {
        return aboveFloorInsertionMode;
    }

    /**
     * Returns the "below floor" insertion mode.
     * @return the insertion mode
     */
    @NotNull
    public InsertionMode<G, A, R> getBelowFloorInsertionMode() {
        return belowFloorInsertionMode;
    }

    /**
     * Returns the "bottommost" insertion mode.
     * @return the insertion mode
     */
    @NotNull
    public InsertionMode<G, A, R> getBottommostInsertionMode() {
        return bottommostInsertionMode;
    }

}
