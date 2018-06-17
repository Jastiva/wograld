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

package net.sf.gridarta.textedit.textarea.tokenmarker;

import net.sf.gridarta.textedit.textarea.Token;
import org.jetbrains.annotations.Nullable;

/**
 * A node in the tree that is used to store all key-value pairs.
 * @author Andreas Kirschbaum
 */
public class Node {

    /**
     * The minimal character code usable in keys.
     */
    private static final char MIN = 'A';

    /**
     * The maximal character code usable in keys.
     */
    private static final char MAX = 'z';

    /**
     * The id for the key matching the path between the root node and this
     * node.
     */
    private byte id = Token.NULL;

    /**
     * The next nodes, or <code>null</code> if no next node exists. The index is
     * <code>0</code> for {@link #MIN}.
     */
    private final Node[] nodes = new Node[(int) MAX - (int) MIN + 1];

    /**
     * Looks up or defines the next node for a given character.
     * @param ch The character.
     * @return the next node
     * @throws IndexOutOfBoundsException if the character is not withing
     * <code>MIN</code>..<code>MAX</code>
     */
    public Node define(final char ch) {
        final int index = (int) ch - (int) MIN;
        if (nodes[index] == null) {
            nodes[index] = new Node();
        }
        return nodes[index];
    }

    /**
     * Looks up the next node for a given character.
     * @param ch the character
     * @return the next node, or <code>null</code> if no key matching exists
     */
    @Nullable
    public Node lookup(final char ch) {
        return MIN <= ch && ch <= MAX ? nodes[(int) ch - (int) MIN] : null;
    }

    /**
     * Sets the id to return for this node.
     * @param id the ide to set
     */
    public void setId(final byte id) {
        this.id = id;
    }

    /**
     * Returns the id for this node.
     * @return this id, or <code>token.NULL</code> if no key matches this node
     */
    public byte getId() {
        return id;
    }

}
