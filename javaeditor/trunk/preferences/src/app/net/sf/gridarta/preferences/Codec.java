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

package net.sf.gridarta.preferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class to encode arbitrary Strings to fit in a single text line. For
 * any string <code>s</code>, <code>encode(s)</code> is a string that does not
 * contain \r or \n characters and <code>s.equals(decode(encode(s)))</code>
 * holds.
 * @author Andreas Kirschbaum
 */
public class Codec {

    /**
     * Patterns that must be encoded. The corresponding replacement strings are
     * {@link #replacementsEncode}.
     */
    private static final Pattern[] patternsEncode = { Pattern.compile("\\\\"), Pattern.compile("\r"), Pattern.compile("\n"), };

    /**
     * The replacement strings for {@link #patternsEncode}.
     */
    private static final String[] replacementsEncode = { Matcher.quoteReplacement("\\\\"), Matcher.quoteReplacement("\\r"), Matcher.quoteReplacement("\\n"), };

    /**
     * Patterns that must be decoded. The corresponding replacement strings are
     * {@link #replacementsDecode}.
     */
    private static final Pattern[] patternsDecode = { Pattern.compile("\\\\n"), Pattern.compile("\\\\r"), Pattern.compile("\\\\\\\\"), };

    /**
     * The replacement strings for {@link #patternsDecode}.
     */
    private static final String[] replacementsDecode = { Matcher.quoteReplacement("\n"), Matcher.quoteReplacement("\r"), Matcher.quoteReplacement("\\"), };

    /**
     * Private constructor to prevent instantiation.
     */
    private Codec() {
    }

    /**
     * Encode a string to make it fit into one line.
     * @param str the string to be encoded
     * @return the encoded string
     * @see #decode(String)
     */
    @NotNull
    public static String encode(@NotNull final String str) {
        assert patternsEncode.length == replacementsEncode.length;
        String tmp = str;
        for (int i = 0; i < patternsEncode.length; i++) {
            tmp = patternsEncode[i].matcher(tmp).replaceAll(replacementsEncode[i]);
        }
        return tmp;
    }

    /**
     * Decode a string which was encoded by {@link #encode(String)}.
     * @param str the string to be decoded
     * @return the decoded string
     * @see #encode(String)
     */
    @NotNull
    public static String decode(@NotNull final String str) {
        assert patternsDecode.length == replacementsDecode.length;
        String tmp = str;
        for (int i = 0; i < patternsDecode.length; i++) {
            tmp = patternsDecode[i].matcher(tmp).replaceAll(replacementsDecode[i]);
        }
        return tmp;
    }

}
