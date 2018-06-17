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

import java.util.MissingResourceException;
import javax.swing.JLabel;
import net.sf.japi.swing.action.ActionBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for {@link ActionBuilder} related functions.
 * @author Andreas Kirschbaum
 */
public class ActionBuilderUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private ActionBuilderUtils() {
    }

    /**
     * Returns the value of a key as a <code>boolean</code>.
     * @param actionBuilder the action builder to query
     * @param key the key to query
     * @return the value
     */
    public static boolean getBoolean(@NotNull final ActionBuilder actionBuilder, @NotNull final String key) {
        final String value = actionBuilder.getString(key);
        return value != null && Boolean.parseBoolean(value);
    }

    /**
     * Returns the value of a key as an <code>int</code>.
     * @param actionBuilder the action builder to query
     * @param key the key to query
     * @param defaultValue the default value if the key does not exist or if the
     * value is invalid
     * @return the value
     */
    public static int getInt(@NotNull final ActionBuilder actionBuilder, @NotNull final String key, final int defaultValue) {
        final String value = actionBuilder.getString(key);
        if (value == null) {
            return defaultValue;
        }

        return NumberUtils.parseInt(value, defaultValue);
    }

    /**
     * Returns the value of a key.
     * @param actionBuilder the action builder to query
     * @param key the key to query
     * @param defaultValue the default value if the key does not exist
     * @return the value
     */
    @NotNull
    public static String getString(@NotNull final ActionBuilder actionBuilder, @NotNull final String key, @NotNull final String defaultValue) {
        final String value = actionBuilder.getString(key);
        return value == null ? defaultValue : value;
    }

    /**
     * Returns the value of a key.
     * @param actionBuilder the action builder to query
     * @param key the key to query
     * @return the value
     */
    @NotNull
    public static String getString(@NotNull final ActionBuilder actionBuilder, @NotNull final String key) {
        final String value = actionBuilder.getString(key);
        if (value == null) {
            throw new MissingResourceException("missing resource key: " + key, ActionBuilderUtils.class.getName(), key);
        }
        return value;
    }

    /**
     * Returns the value of a key.
     * @param actionBuilder the action builder to query
     * @param key the key to query
     * @param args the arguments to replace in the format string
     * @return the value
     */
    @NotNull
    public static String format(@NotNull final ActionBuilder actionBuilder, @NotNull final String key, @NotNull final Object... args) {
        final String value = actionBuilder.format(key, args);
        if (value == null) {
            throw new MissingResourceException("missing resource key: " + key, ActionBuilderUtils.class.getName(), key);
        }
        return value;
    }

    /**
     * Creates a new {@link JLabel} from a resource key.
     * @param actionBuilder the action builder to query
     * @param key the resource key
     * @return the label
     * @noinspection TypeMayBeWeakened
     */
    @NotNull
    public static JLabel newLabel(@NotNull final ActionBuilder actionBuilder, @NotNull final String key) {
        return new JLabel(getString(actionBuilder, key));
    }

}
