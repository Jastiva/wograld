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

import java.util.Arrays;

/**
 * The highlighted state while using a {@link FilterControl} instance.
 * @author Andreas Kirschbaum
 */
public class FilterState {

    /**
     * The current highlight state.
     */
    private final boolean[] highlightedSquare = new boolean[FilterControl.MAX_HIGHLIGHT];

    /**
     * Resets the state to no highlights.
     */
    public void reset() {
        Arrays.fill(highlightedSquare, false);
    }

    /**
     * Returns whether the given path should be highlighted.
     * @param path the path
     * @return the highlighted state
     */
    public boolean isHighlightedSquare(final int path) {
        return highlightedSquare[path];
    }

    /**
     * Sets the highlighted state for a given path.
     * @param path the path
     * @param highlightedSquare the highlighted state
     */
    public void setHighlightedSquare(final int path, final boolean highlightedSquare) {
        this.highlightedSquare[path] = highlightedSquare;
    }

}
