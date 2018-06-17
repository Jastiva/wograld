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

package net.sf.gridarta.model.filter;

import net.sf.gridarta.model.gameobject.GameObject;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for Filters. <p/> <p> Interface used to filter and analyze maps.
 * Basically a filter gets an {@link GameObject} and tells if it got a match. It
 * also gets reset when the map square is changed, So more complex behavior can
 * be achieved.
 * @author tchize
 */
public interface Filter<F extends Filter<F, C>, C extends FilterConfig<F, C>> {

    /**
     * Tells whether we got a match on specific {@link GameObject}. The analysis
     * tool will call this function with every game object on a given map
     * square. The match function is responsible for analyzing inventories if it
     * needs to. Unless {@link #hasGlobalMatch(FilterConfig)} returns
     * <code>true</code>, when a match occurred, this function is not called
     * with the remaining game objects on the map square and a reset is issued.
     * @param config the filter configuration to use
     * @param gameObject the game object being analyzed on the map
     * @return whether it matches the criteria; this value is ignored if
     *         <code>hasGlobalMatch()</code> returns <code>true</code>
     */
    boolean match(@NotNull C config, @NotNull GameObject<?, ?, ?> gameObject);

    /**
     * This tells the filter we have finished with current map square and,
     * perhaps, we are jumping on next one. If {@link #hasGlobalMatch(FilterConfig)}
     * returns <code>true</code>, the returning value is used to know if we had
     * a match.
     * @param config the filter configuration to use
     * @return whether we had a global match on map square
     */
    boolean reset(@NotNull C config);

    /**
     * Tells if all the game objects on a square are to be analyzed before a
     * match result. If all game objects must be analyzed to decide the result
     * of a match, the return value of {@link #match(FilterConfig, GameObject)}
     * is ignored and the returning value of {@link #reset(FilterConfig)} is
     * used as replacement. This should mainly used by complex analyze filters.
     * @param config the filter configuration to use
     * @return whether a match can be decided only when all game objects on a
     *         map square are analyzed
     */
    boolean hasGlobalMatch(@NotNull C config);

    /**
     * Creates a new {@link FilterConfig} instance for this filter.
     * @return the new filter config instance
     */
    @NotNull
    C createConfig();

}
