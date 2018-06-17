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

package net.sf.gridarta.preferences;

import org.jetbrains.annotations.NotNull;

/**
 * The type of a {@link FilePreferences} node.
 * @author Andreas Kirschbaum
 */
public enum NodeType {

    /**
     * User Node.
     */
    USER("user"),

    /**
     * System Node.
     */
    SYSTEM("system");

    /**
     * Prefix for node tree.
     */
    @NotNull
    private final String prefix;

    /**
     * Creates a new instance.
     * @param prefix the Prefix for the node tree
     */
    NodeType(@NotNull final String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the prefix for building key names.
     * @return the prefix for key names
     */
    @NotNull
    public String getPrefix() {
        return prefix;
    }

}
