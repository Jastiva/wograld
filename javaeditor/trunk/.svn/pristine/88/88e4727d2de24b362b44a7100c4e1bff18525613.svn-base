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

package net.sf.gridarta.model.pickmapsettings;

import org.jetbrains.annotations.NotNull;

/**
 * Container for settings that affect pickmaps.
 * @author Andreas Kirschbaum
 */
public interface PickmapSettings {

    /**
     * Adds a {@link PickmapSettingsListener} to be notified about changes.
     * @param listener the listener to add
     */
    void addPickmapSettingsListener(@NotNull PickmapSettingsListener listener);

    /**
     * Removes a {@link PickmapSettingsListener} to be notified about changes.
     * @param listener the listener to remove
     */
    void removePickmapSettingsListener(@NotNull PickmapSettingsListener listener);

    /**
     * Returns whether pickmaps are immutable.
     * @return whether pickmaps are immutable
     */
    boolean isLocked();

    /**
     * Sets whether pickmaps are immutable.
     * @param locked whether pickmaps are immutable
     */
    void setLocked(boolean locked);

}
