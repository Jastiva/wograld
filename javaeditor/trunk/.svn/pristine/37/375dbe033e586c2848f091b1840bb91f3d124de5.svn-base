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
 * A {@link TokenMarker} for the message field of Crossfire objects allowing.
 * @author Andreas Kirschbaum
 */
public class CrossfireDialogTokenMarker extends TokenMarker {

    /**
     * {@inheritDoc}
     */
    @Override
    public byte markTokensImpl(final byte token, final Segment line, final int lineIndex) {
        final char[] array = line.array;
        final int offset = line.offset;
        final int length = line.count;
        if (length >= 7 && array[offset] == '@' && array[offset + 1] == 'm' && array[offset + 2] == 'a' && array[offset + 3] == 't' && array[offset + 4] == 'c' && array[offset + 5] == 'h' && array[offset + 6] == ' ') {
            addToken(6, Token.KEYWORD1);
            int start = offset + 6;
            byte id = Token.NULL;
            for (int i = start + 1; i < offset + length; i++) {
                final byte thisId;
                if (array[i] == '|') {
                    thisId = Token.OPERATOR;
                } else if (Character.isUpperCase(array[i])) {
                    thisId = Token.INVALID;
                } else {
                    thisId = Token.LITERAL1;
                }
                if (thisId != id) {
                    addToken(i - start, id);
                    start = i;
                    id = thisId;
                }
            }
            addToken(offset + length - start, id);
        } else {
            addToken(length, Token.NULL);
        }

        return Token.NULL;
    }

}
