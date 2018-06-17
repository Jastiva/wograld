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

package net.sf.gridarta.gui.filter;

import javax.swing.JMenu;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.filter.Filter;
import net.sf.gridarta.model.filter.FilterConfigListener;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Andreas Kirschbaum
 */
public interface FilterControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    int MAX_HIGHLIGHT = 3;

    /**
     * Adds a {@link FilterConfigListener} to be notified about changes.
     * @param listener the config listener
     */
    void addConfigListener(@NotNull FilterConfigListener listener);

    /**
     * Removes a {@link FilterConfigListener} to be notified about changes.
     * @param listener the config listener
     */
    void removeConfigListener(@NotNull FilterConfigListener listener);

    void createMenuEntries(@NotNull JMenu menu);

    void newSquare(@NotNull FilterState filterState);

    boolean isHighlightedSquare(@NotNull FilterState filterState, int path);

    void objectInSquare(@NotNull FilterState filterState, @NotNull G gameObject);

    boolean canShow(@NotNull G gameObject);

    void addFilter(@NotNull String name, @NotNull Filter<?, ?> filter);

    void removeFilter(@NotNull String name);

}
