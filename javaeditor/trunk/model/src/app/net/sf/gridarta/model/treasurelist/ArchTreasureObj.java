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

package net.sf.gridarta.model.treasurelist;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link TreasureObj} representing an "arch" entry.
 * @author Andreas Kirschbaum
 */
public class ArchTreasureObj extends TreasureObj {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * @param name name of this treasure object
     */
    public ArchTreasureObj(@NotNull final String name) {
        super(name, false, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void appendToString(@NotNull final StringBuilder sb) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final TreasureObjVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copyListType(@NotNull final TreasureObj treasureObj) {
        throw new UnsupportedOperationException();
    }

}
