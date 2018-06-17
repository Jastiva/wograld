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

package net.sf.gridarta.model.index;

import java.util.EventListener;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for listeners interested in {@link Index} related events.
 * @param <V> the index's value's type
 * @author Andreas Kirschbaum
 */
public interface IndexListener<V> extends EventListener {

    /**
     * A new value has been added to the index.
     * @param value the value
     */
    void valueAdded(@NotNull V value);

    /**
     * A value has been removed from the index.
     * @param value the value
     */
    void valueRemoved(@NotNull V value);

    /**
     * A name mapping has changed.
     */
    void nameChanged();

    /**
     * At least one pending value has become available.
     */
    void pendingChanged();

    /**
     * Called after indexing has finished.
     */
    void indexingFinished();

}
