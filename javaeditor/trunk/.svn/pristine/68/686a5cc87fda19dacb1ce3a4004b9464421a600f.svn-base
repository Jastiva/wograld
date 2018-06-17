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
 * A {@link TreasureObj} representing a "treasure" entry.
 * @author Andreas Kirschbaum
 */
public class TreasureListTreasureObj extends TreasureObj {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The list type.
     * @serial
     */
    @NotNull
    private TreasureListTreasureObjType listType;

    /**
     * Creates a new instance.
     * @param name name of this treasure object
     * @param listType the list type
     */
    public TreasureListTreasureObj(@NotNull final String name, @NotNull final TreasureListTreasureObjType listType) {
        super(name, true, true);
        this.listType = listType;
    }

    /**
     * Returns the list type.
     * @return the list type
     */
    @NotNull
    public TreasureListTreasureObjType getListType() {
        return listType;
    }

    /**
     * Sets the list type.
     * @param listType the list type
     */
    public void setListType(@NotNull final TreasureListTreasureObjType listType) {
        this.listType = listType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void appendToString(@NotNull final StringBuilder sb) {
        if (listType == TreasureListTreasureObjType.ONE) {
            sb.append(" [one]");
        }
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
        if (!(treasureObj instanceof TreasureListTreasureObj)) {
            return;
        }

        final TreasureListTreasureObj treasureListTreasureObj = (TreasureListTreasureObj) treasureObj;
        listType = treasureListTreasureObj.listType;
    }

}
