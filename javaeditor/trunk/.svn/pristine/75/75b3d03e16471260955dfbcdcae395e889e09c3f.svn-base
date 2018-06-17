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

import javax.swing.text.Segment;
import net.sf.gridarta.textedit.textarea.Token;

/**
 * Maps (parts of) {@link Segment Segments} to <code>byte</code> values. It
 * allows lookups of text sub-strings without the overhead of creating new
 * string objects. <p/> The keys are stored in a tree consisting of {@link Node
 * Nodes}. Each node represents one character of the key string. The root node
 * {@link #rootNode} represents the first character.
 * @author Andreas Kirschbaum
 */
public class KeywordMap {

    /**
     * The root node.
     */
    private final Node rootNode = new Node();

    /**
     * Whether case should be ignored when matching keys.
     */
    private final boolean ignoreCase;

    /**
     * Creates a new instance.
     * @param ignoreCase If set, ignore match keys case-insensitive.
     */
    public KeywordMap(final boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    /**
     * Looks up a key.
     * @param text The text segment to look up.
     * @param offset the starting offset of the key within the text segment
     * @param length the length of the key
     * @return the id registered for the key, or <code>Token.NULL</code> if no
     *         key matches
     */
    public byte lookup(final Segment text, final int offset, final int length) {
        Node node = rootNode;
        for (int i = offset, end = offset + length; i < end; i++) {
            node = node.lookup(ignoreCase ? Character.toUpperCase(text.array[i]) : text.array[i]);
            if (node == null) {
                return Token.NULL;
            }
        }
        return node.getId();
    }

    /**
     * Adds a key-value mapping.
     * @param keyword the key to add
     * @param id the value to map to
     */
    public void add(final CharSequence keyword, final byte id) {
        Node node = rootNode;
        for (int i = 0, len = keyword.length(); i < len; i++) {
            node = node.define(ignoreCase ? Character.toUpperCase(keyword.charAt(i)) : keyword.charAt(i));
        }
        node.setId(id);
    }

}
