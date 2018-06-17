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

package net.sf.gridarta.gui.mapmenu;

import java.io.Serializable;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for recent and bookmark menu entries.
 * @author Andreas Kirschbaum
 */
public abstract class MapMenuEntry implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The title.
     */
    @NotNull
    private String title;

    /**
     * Creates a new instance.
     * @param title the entry's title
     */
    protected MapMenuEntry(@NotNull final String title) {
        this.title = title;
    }

    /**
     * Returns the entry's title.
     * @return the title
     */
    @NotNull
    public String getTitle() {
        return title;
    }

    /**
     * Sets the entry's title.
     * @param title the title
     */
    public void setTitle(@NotNull final String title) {
        this.title = title;
    }

    /**
     * Returns whether this entry is a directory.
     * @return whether this entry is a directory
     */
    public abstract boolean allowsChildren();

    /**
     * Calls the <code>visit()</code> function of the {@link
     * MapMenuEntryVisitor} for this instance.
     * @param visitor the visitor to call
     */
    public abstract void visit(@NotNull final MapMenuEntryVisitor visitor);

}
