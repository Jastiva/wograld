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

import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import net.sf.japi.util.EnumerationIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Subclass: Nodes in the CFTreasureListTree. Each node contains a {@link
 * TreasureObj} as content.
 * @author unknown
 */
public class TreasureTreeNode extends DefaultMutableTreeNode {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The treasure object of this node.
     */
    @NotNull
    private final TreasureObj content;

    /**
     * Construct tree node with specified content object.
     * @param content the treasure object of this node
     */
    public TreasureTreeNode(@NotNull final TreasureObj content) {
        this.content = content;
    }

    /**
     * Return a new cloned instance of this object.
     * @return a new cloned instance of this object
     */
    public MutableTreeNode getClone(final boolean processSecondLinking, @Nullable final List<TreasureTreeNode> needSecondLink) {
        // clone this object
        final TreasureTreeNode clone = new TreasureTreeNode(content);

        // also clone all children nodes and link them properly
        for (final TreasureTreeNode treasureTreeNode : new EnumerationIterator<TreasureTreeNode>(children())) {
            clone.add(treasureTreeNode.getClone(processSecondLinking, needSecondLink));
        }

        // if this is a list without children it will need second linking
        if (!processSecondLinking && content.isTreasureList() && !content.getName().equalsIgnoreCase("NONE") && !content.hasLoop()) {
            // this is a list, let's see if there are children
            boolean hasChildren = false; // true when this list has real children
            for (final TreasureTreeNode treasureTreeNode : new EnumerationIterator<TreasureTreeNode>(children())) {
                if (treasureTreeNode.content.isRealChild()) {
                    hasChildren = true; // found a real child
                    break;
                }
            }
            if (!hasChildren) {
                // this is a list with nothing but YES/NO in it, needs linking
                assert needSecondLink != null;
                needSecondLink.add(clone);
            }
        }

        return clone;
    }

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @Override
    public String toString() {
        return content.toString();
    }

    public TreasureObj getTreasureObj() {
        return content;
    }

    /**
     * Recalculate the chances of objects in a treasureone list. The new chances
     * always sum up to 100% total.
     */
    public void recalculateChances() {
        if (!(content instanceof TreasureListTreasureObj)) {
            return;
        }

        final TreasureListTreasureObj treasureListTreasureObj = (TreasureListTreasureObj) content;
        if (treasureListTreasureObj.getListType() != TreasureListTreasureObjType.ONE) {
            return;
        }

        int sumChances = 0;

        // calculate the sum of all chances
        for (final TreasureTreeNode treasureTreeNode : new EnumerationIterator<TreasureTreeNode>(children())) {
            final TreasureObj treasureObj = treasureTreeNode.content;
            sumChances += treasureObj.initChance();
        }

        final double corrector = 100.0 / (double) sumChances;

        // now apply the correcting factor to all chances
        for (final TreasureTreeNode treasureTreeNode : new EnumerationIterator<TreasureTreeNode>(children())) {
            final TreasureObj treasureObj = treasureTreeNode.content;
            treasureObj.correctChance(corrector);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Enumeration<TreasureTreeNode> children() {
        //DefaultMutableTreeNode does not use type parameters
        @SuppressWarnings("unchecked")
        final Enumeration<TreasureTreeNode> tmp = (Enumeration<TreasureTreeNode>) super.children();
        return tmp;
    }

}
