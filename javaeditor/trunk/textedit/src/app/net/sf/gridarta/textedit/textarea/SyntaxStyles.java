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

package net.sf.gridarta.textedit.textarea;

/**
 * A set of {@link SyntaxStyle} instances for painting colorized text.
 * @author Andreas Kirschbaum
 */
public class SyntaxStyles {

    /**
     * The styles; maps token id to style. The first entry is unused; it's value
     * is <code>null</code>.
     */
    private final SyntaxStyle[] styles;

    /**
     * Creates a new instance. The passed array maps token id to style. The
     * first entry is unused; all other entries must be non-<code>null</code>.
     * @param styles the styles to store
     */
    public SyntaxStyles(final SyntaxStyle[] styles) {
        this.styles = new SyntaxStyle[styles.length];
        System.arraycopy(styles, 1, this.styles, 1, styles.length - 1);
        for (int i = 1; i < this.styles.length; i++) {
            if (this.styles[i] == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Returns a style for a token id.
     * @param id the token id
     * @return the style
     */
    public SyntaxStyle getStyle(final byte id) {
        return styles[(int) id];
    }

}
