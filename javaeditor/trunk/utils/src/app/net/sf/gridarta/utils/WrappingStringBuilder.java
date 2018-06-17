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

package net.sf.gridarta.utils;

/**
 * The class <code>WrappingStringBuilder</code> implements a string buffer that
 * separates words by "," and wraps lines at a given margin.
 * @author Andreas Kirschbaum
 */
public class WrappingStringBuilder {

    /**
     * The {@link StringBuilder} holding the string data.
     */
    private final StringBuilder sb = new StringBuilder();

    /**
     * The maximum line length.
     */
    private final int maxLineLength;

    /**
     * Set if no word was added yet, unset if at least one word was added.
     */
    private boolean firstWord = true;

    /**
     * The length of the last line in {@link #sb}.
     */
    private int thisLineLength;

    /**
     * Create a new instance.
     * @param maxLineLength the maximum line length
     */
    public WrappingStringBuilder(final int maxLineLength) {
        this.maxLineLength = maxLineLength;
    }

    /**
     * Append a word.
     * @param str the word to append
     */
    public void append(final String str) {
        if (!firstWord) {
            if (thisLineLength + str.length() + 1 > maxLineLength) {
                sb.append(",\n");
                thisLineLength = 0;
            } else {
                sb.append(", ");
                thisLineLength += 2;
            }
        }
        sb.append(str);
        thisLineLength += str.length();
        firstWord = false;
    }

    /**
     * Append an integer value.
     * @param value the integer value to append
     */
    public void append(final int value) {
        append(Integer.toString(value));
    }

    /**
     * Return the concatenated words as a string.
     * @return the concatenated words
     */
    @Override
    public String toString() {
        return sb.toString();
    }

}
