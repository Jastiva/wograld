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

import java.util.EventListener;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for listeners interested in {@link NamedFilter} related events.
 * @author tchize
 */
public interface NamedFilterListener extends EventListener {

    /**
     * The filter has changed.
     * @param type the change type
     * @param filterName the name of the sub-filter that has changed
     * @param filter the sub-filter that has changed
     */
    void nameFilterChanged(@NotNull NamedFilterChangeType type, @NotNull String filterName, @NotNull Filter<?, ?> filter);

}
