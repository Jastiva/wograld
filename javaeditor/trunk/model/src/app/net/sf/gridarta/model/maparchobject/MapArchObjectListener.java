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

package net.sf.gridarta.model.maparchobject;

import java.util.EventListener;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for listeners listening on map arch object changes.
 * @author Andreas Kirschbaum
 */
public interface MapArchObjectListener extends EventListener {

    /**
     * Meta information of a map has changed.
     */
    void mapMetaChanged();

    /**
     * The map size has changed. This callback ignores active map transactions.
     * @param mapSize the new map size
     */
    void mapSizeChanged(@NotNull Size2D mapSize);

}
