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

package net.sf.gridarta.maincontrol;

/**
 * Main run mode of the editor.
 * @author Andreas Kirschbaum
 */
public enum GridartaRunMode {

    /**
     * Normal operation: Start editor with GUI.
     */
    NORMAL(false),

    /**
     * Batch PNG: Create PNG files for all given maps in their directories.
     */
    BATCH_PNG(false),

    /**
     * Single PNG: Create a PNG file from the specified map.
     */
    SINGLE_PNG(false),

    /**
     * Collect archetypes.
     */
    COLLECT_ARCHES(true);

    /**
     * Whether this mode uses console-mode.
     */
    private final boolean consoleMode;

    /**
     * Creates a new instance.
     * @param consoleMode whether this mode uses console mode
     */
    GridartaRunMode(final boolean consoleMode) {
        this.consoleMode = consoleMode;
    }

    /**
     * Returns whether this mode uses console-mode.
     * @return whether this mode uses console-mode
     */
    public boolean isConsoleMode() {
        return consoleMode;
    }

}
