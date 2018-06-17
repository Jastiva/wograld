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

import java.util.Arrays;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for string manipulation.
 * @author Andreas Kirschbaum
 */
public class StringUtils {

    /**
     * Pattern to match whitespace.
     */
    @NotNull
    public static final Pattern PATTERN_WHITESPACE = Pattern.compile("[\\x00-\\x09\\x0b\\x20]+");

    /**
     * Pattern to match trailing whitespace.
     */
    @NotNull
    private static final Pattern PATTERN_TRAILING_WHITESPACE = Pattern.compile("[\\x00-\\x09\\x0b\\x20]+$");

    /**
     * Pattern to match trailing whitespace in a multi line string.
     */
    @NotNull
    private static final Pattern PATTERN_MULTI_LINE_TRAILING_WHITESPACE = Pattern.compile("(?m)[\\x00-\\x09\\x0b\\x20]+$");

    /**
     * The pattern to match end of line characters separating lines.
     */
    @NotNull
    public static final Pattern PATTERN_END_OF_LINE = Pattern.compile("\\s*\n");

    /**
     * The pattern that matches a single space.
     */
    @NotNull
    public static final Pattern PATTERN_SPACE = Pattern.compile(" ");

    /**
     * The pattern that matches a non-empty sequence of spaces.
     */
    @NotNull
    public static final Pattern PATTERN_SPACES = Pattern.compile(" +");

    /**
     * The pattern that matches a single slash ("/").
     */
    @NotNull
    public static final Pattern PATTERN_SLASH = Pattern.compile("/");

    /**
     * The pattern that matches a single backslash ("\").
     */
    @NotNull
    public static final Pattern PATTERN_BACKSLASH = Pattern.compile("\\\\");

    /**
     * The pattern that matches a single colon (":").
     */
    @NotNull
    public static final Pattern PATTERN_COLON = Pattern.compile(":");

    /**
     * The pattern that matches a single comma (",").
     */
    @NotNull
    public static final Pattern PATTERN_COMMA = Pattern.compile(",");

    /**
     * The pattern that matches a single equal sign ("=").
     */
    @NotNull
    public static final Pattern PATTERN_EQUAL = Pattern.compile("=");

    /**
     * The pattern that matches a single newline ("\n").
     */
    @NotNull
    public static final Pattern PATTERN_NEWLINE = Pattern.compile("\n");

    /**
     * Private constructor to prevent instantiation.
     */
    private StringUtils() {
    }

    /**
     * Removes trailing whitespace from a string.
     * @param str the string
     * @return the trimmed string
     */
    public static String removeTrailingWhitespace(@NotNull final CharSequence str) {
        return PATTERN_TRAILING_WHITESPACE.matcher(str).replaceFirst("");
    }

    /**
     * Removes trailing whitespace from all lines of a string.
     * @param str the string
     * @return the trimmed string
     */
    public static String removeTrailingWhitespaceFromLines(@NotNull final CharSequence str) {
        return PATTERN_MULTI_LINE_TRAILING_WHITESPACE.matcher(str).replaceAll("");
    }

    /**
     * Returns a given string which ends with a trailing newline character;
     * empty strings remain empty. If the input strings ends in a newline
     * character or if it is empty, it is returned; else a newline character is
     * appended first.
     * @param str the input string
     * @return the string with a trailing newline character
     */
    public static CharSequence ensureTrailingNewline(@NotNull final String str) {
        return str.length() <= 0 ? "" : str.endsWith("\n") ? str : str + "\n";
    }

    /**
     * Helper function for 'diffArchText()': Looks for occurrence of the
     * attribute 'str' in 'base' and if found, returns the full line where 'str'
     * occurs in base. Only occurrences of 'str' at the beginning of a new line
     * are counted as valid. If not found, null is returned.
     * @param base full text to search
     * @param str string (attribute) to look for
     * @param ignoreValues if true, lines are matched against 'str' only till
     * the first space (" ")
     * @return the text that differs
     */
    @Nullable
    public static CharSequence diffTextString(@NotNull final CharSequence base, @NotNull final String str, final boolean ignoreValues) {
        for (final String line : PATTERN_END_OF_LINE.split(base, 0)) {
            if (ignoreValues ? line.startsWith(str) : line.equals(str)) {
                return line;
            }
        }
        return null;
    }

    /**
     * Returns an attribute line from a set of attribute definitions. If the set
     * contains more than one instance of <code>attributeName</code>, only the
     * first instance is returned.
     * @param attributes the attribute set to search
     * @param attributeName the attribute name to search for
     * @return the set of attribute value or <code>null</code> if the attribute
     *         was not found
     */
    @Nullable
    public static String getAttribute(@NotNull final CharSequence attributes, @NotNull final String attributeName) {
        final String str = attributeName + " ";
        for (final String line : PATTERN_END_OF_LINE.split(attributes, 0)) {
            if (line.startsWith(str)) {
                return line.substring(str.length());
            }
        }
        return null;
    }

    /**
     * Sorts newline separated lines in a string.
     * @param string the string to sort
     * @return the sorted string
     */
    @NotNull
    public static String sortLines(@NotNull final CharSequence string) {
        final String[] lines = PATTERN_NEWLINE.split(string, -1);
        Arrays.sort(lines);
        final StringBuilder sb = new StringBuilder();
        for (final String line : lines) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

}
