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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * Replaces placeholders in strings.
 * @author Andreas Kirschbaum
 */
public class StringParameterBuilder {

    /**
     * The {@link Pattern} for parameters.
     */
    @NotNull
    private static final Pattern PATTERN = Pattern.compile("\\$\\{([a-zA-Z]+)}");

    /**
     * Maps parameter key to value.
     */
    @NotNull
    private final Map<String, String> values = new HashMap<String, String>();

    /**
     * Adds a parameter key/value pair. The key "PATH" is handled differently:
     * the value is a list of path names to search for the argument. The first
     * match is used; if no match is found, a syntax error occurs.
     * @param key the parameter's key
     * @param value the parameter's value
     */
    public void addParameter(@NotNull final String key, @NotNull final String value) {
        values.put(key, value);
    }

    /**
     * Replaces all parameters in a string.
     * @param spec the input string
     * @return the output string
     * @throws SyntaxErrorException if a parameter is invalid
     */
    @NotNull
    public String replace(@NotNull final CharSequence spec) throws SyntaxErrorException {
        final Matcher matcher = PATTERN.matcher(spec);
        final StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            final String key = matcher.group(1);
            final String value = values.get(key);
            if (value == null) {
                throw new SyntaxErrorException("unknown variable '${" + key + "}");
            }
            matcher.appendReplacement(sb, "");
            sb.append(value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
