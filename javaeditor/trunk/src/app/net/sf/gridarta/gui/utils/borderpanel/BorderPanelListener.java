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

package net.sf.gridarta.gui.utils.borderpanel;

import java.awt.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for listeners interested in {@link BorderPanel} related events.
 * @author Andreas Kirschbaum
 */
public interface BorderPanelListener {

    /**
     * The size of a {@link Component} has changed.
     * @param component the component
     * @param size the new size
     */
    void sizeChanged(@NotNull Component component, int size);

    /**
     * The position of a the inner split pane has changed.
     * @param location the location that has changed
     * @param size2 the new size
     */
    void size2Changed(@NotNull Location location, int size2);

    /**
     * Returns the preferred position of the inner split pane.
     * @param location the location
     * @return the preferred position or <code>-1</code>
     */
    int getSize2(Location location);

}
