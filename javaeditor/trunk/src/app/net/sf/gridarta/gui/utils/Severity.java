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

package net.sf.gridarta.gui.utils;

import java.awt.Color;
import org.jetbrains.annotations.NotNull;

/**
 * Severity levels for colors of tabs.
 * @author Andreas Kirschbaum
 */
public enum Severity {

    /**
     * The tab contents are unchanged from defaults.
     */
    DEFAULT(Color.BLACK, 0),

    /**
     * The tab contents are modified from defaults.
     */
    MODIFIED(Color.BLUE, 1),

    /**
     * The tab contents are invalid.
     */
    ERROR(Color.RED, 2);

    /**
     * The tab color.
     */
    @NotNull
    private final Color color;

    /**
     * The severity level.
     */
    private final int level;

    /**
     * Creates a new instance.
     * @param color the tab color
     * @param level the severity level
     */
    Severity(@NotNull final Color color, final int level) {
        this.color = color;
        this.level = level;
    }

    /**
     * Returns the tab color.
     * @return the tab color
     */
    @NotNull
    public Color getColor() {
        return color;
    }

    /**
     * Returns the severity level.
     * @return the severity level
     */
    public int getLevel() {
        return level;
    }

}
