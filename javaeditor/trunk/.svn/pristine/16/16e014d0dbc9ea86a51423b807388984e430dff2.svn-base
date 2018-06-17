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
 * Interface for filter configurations.
 * @author tchize
 */
public interface FilterConfig<F extends Filter<F, C>, C extends FilterConfig<F, C>> {

    /**
     * Returns the {@link Filter} this filter config belongs to.
     * @return the filter
     */
    @NotNull
    F getFilter();

    /**
     * Enables or disables the filter.
     * @param enabled whether the filter should be enabled
     */
    void setEnabled(boolean enabled);

    /**
     * Returns whether the filter is enabled.
     * @return whether the filter is enabled
     */
    boolean isEnabled();

    /**
     * Adds a {@link FilterConfigListener} to be notified about changes.
     * @param listener the listener
     */
    void addConfigChangeListener(FilterConfigListener listener);

    /**
     * Removes a {@link FilterConfigListener} to be notified about changes.
     * @param listener the listener
     */
    void removeConfigChangeListener(FilterConfigListener listener);

    /**
     * Visits the appropriate <code>visit()</code> function of a {@link
     * FilterConfigVisitor}.
     * @param visitor the visitor to call
     */
    void accept(@NotNull FilterConfigVisitor visitor);

    /**
     * Tells whether we got a match on specific {@link GameObject}. The analysis
     * tool will call this function with every game object on a given map
     * square. The match function is responsible for analyzing inventories if it
     * needs to.
     * @param gameObject the game object being analyzed on the map
     * @return whether it matches the criteria
     */
    boolean match(@NotNull GameObject<?, ?, ?> gameObject);

    /**
     * Tells the filter we have finished with current map square and, perhaps,
     * we are jumping on next one.
     * @return whether we had a global match on the map square
     */
    boolean reset();

}
