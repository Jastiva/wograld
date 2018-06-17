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

import java.io.Serializable;
import org.jetbrains.annotations.NotNull;

/**
 * Subclass: UserObject (= content object) for nodes in the CFTreasureListTree
 * These can be either treasurelists (containers), arches, or yes/no
 * containers.
 * @author unknown
 * @author Andreas Kirschbaum
 */
public abstract class TreasureObj implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Unset values.
     */
    public static final int UNSET = -1;

    /**
     * The name of this treasure object.
     * @serial
     */
    @NotNull
    private final String name;

    /**
     * Whether this treasure object is a "treasure" or "treasureone" object.
     * @serial
     */
    private final boolean isTreasureList;

    /**
     * Set for all except yes or no nodes.
     * @serial
     */
    private final boolean isRealChild;

    /**
     * The chance attribute.
     * @serial
     */
    private int chance;

    /**
     * The maximum number of generated items.
     * @serial
     */
    private int nrof;

    /**
     * The maximum magic bonus.
     * @serial
     */
    private int magic;

    /**
     * Set if thi list contains itself.
     */
    private boolean hasLoop;

    /**
     * Creates a new instance.
     * @param name the name of this treasure object
     * @param isTreasureList set for treasure or treasureone
     * @param isRealChild set for all except yes or no nodes
     */
    protected TreasureObj(@NotNull final String name, final boolean isTreasureList, final boolean isRealChild) {
        this.name = name;
        this.isTreasureList = isTreasureList;
        this.isRealChild = isRealChild;
        chance = UNSET;
        nrof = UNSET;
        magic = UNSET;
        hasLoop = false;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (nrof != UNSET) {
            sb.append(nrof).append(" ");
        }
        sb.append(name);
        appendToString(sb);
        if (magic != UNSET) {
            sb.append(" +").append(magic);
        }
        if (chance != UNSET) {
            sb.append(" (").append(chance).append(" %)");
        }
        return sb.toString();
    }

    /**
     * Appends a description of this treasure object to a {@link
     * StringBuilder}.
     * @param sb the string builder
     */
    protected abstract void appendToString(@NotNull final StringBuilder sb);

    /**
     * Sets the chance attribute.
     * @param value the chance attribute
     */
    public void setChance(final int value) {
        chance = value;
    }

    /**
     * Returns the chance attribute.
     * @return the chance attribute
     */
    public int getChance() {
        return chance;
    }

    /**
     * Initializes the chance attribute.
     * @return the chance attribute
     */
    public int initChance() {
        if (chance == UNSET) {
            chance = 100;
        }
        return chance;
    }

    /**
     * Updates the chance attribute by a corrector factor.
     * @param corrector the corrector factor
     */
    public void correctChance(final double corrector) {
        chance = (int) Math.round((double) chance * corrector);
    }

    /**
     * Sets the magic attribute.
     * @param magic the magic attribute
     */
    public void setMagic(final int magic) {
        this.magic = magic;
    }

    /**
     * Returns the magic attribute.
     * @return the magic attribute
     */
    public int getMagic() {
        return magic;
    }

    /**
     * Sets the maximum number of generated items.
     * @param nrof the maximum number
     */
    public void setNrof(final int nrof) {
        this.nrof = nrof;
    }

    /**
     * Returns whether this treasure object contains itself.
     * @return whether this treasure object contains itself
     */
    public boolean hasLoop() {
        return hasLoop;
    }

    /**
     * Sets whether this treasure object contains itself.
     * @param hasLoop whether this treasure object contains itself
     */
    public void setHasLoop(final boolean hasLoop) {
        this.hasLoop = hasLoop;
    }

    /**
     * Returns whether this treasure object is a "treasure" or "treasureone"
     * object.
     * @return whether this treasure object is a "treasure" or "treasureone"
     *         object
     */
    public boolean isTreasureList() {
        return isTreasureList;
    }

    /**
     * Returns whether this node is no a yes or no node.
     * @return whether this node is neither yes nor no
     */
    public boolean isRealChild() {
        return isRealChild;
    }

    /**
     * Returns the name of this treasure object.
     * @return the name of this treasure object
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Calls the {@link TreasureObjVisitor} callback function appropriate for
     * this instance.
     * @param visitor the visitor to call
     */
    public abstract void visit(@NotNull final TreasureObjVisitor visitor);

    /**
     * Copes the list type of a treasureone list.
     * @param treasureObj the node to copy from
     */
    public abstract void copyListType(@NotNull final TreasureObj treasureObj);

}
