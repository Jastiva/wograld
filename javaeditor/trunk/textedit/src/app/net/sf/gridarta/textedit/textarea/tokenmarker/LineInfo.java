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

/**
 * Stores information about tokenized lines.
 * @author Slava Pestov
 * @author Andreas Kirschbaum
 */
public class LineInfo {

    /**
     * Creates a new LineInfo object with token = Token.NULL and obj = null.
     */
    public LineInfo() {
    }

    /**
     * Creates a new LineInfo object with the specified parameters.
     */
    public LineInfo(final byte token, final Object obj) {
        this.token = token;
        this.obj = obj;
    }

    /**
     * The id of the last token of the line.
     */
    private byte token;

    /**
     * This is for use by the token marker implementations themselves. It can be
     * used to store anything that is an object and that needs to exist on a
     * per-line basis.
     */
    private Object obj;

    public byte getToken() {
        return token;
    }

    public void setToken(final byte token) {
        this.token = token;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(final Object obj) {
        this.obj = obj;
    }

}
