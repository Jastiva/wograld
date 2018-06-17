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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.util.IteratorEnumeration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for initializing the {@link ActionBuilder} infrastructure for
 * regression tests.
 * @author Andreas Kirschbaum
 */
public class TestActionBuilder {

    /**
     * The {@link Pattern} for matching validator keys for default values.
     */
    @NotNull
    private static final Pattern PATTERN_DEFAULT_KEY = Pattern.compile("Validator\\..*\\.default|MapValidator\\.All\\.default");

    /**
     * Private constructor to prevent instantiation.
     */
    private TestActionBuilder() {
    }

    /**
     * Initializes the {@link ActionBuilder} infrastructure for regression
     * tests.
     */
    public static void initialize() {
        final ActionBuilder actionBuilder = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");
        final ResourceBundle resourceBundle = new ResourceBundle() {

            /**
             * Maps key to associated object.
             */
            @NotNull
            private final Map<String, Object> objects = new HashMap<String, Object>();

            @Nullable
            @Override
            protected Object handleGetObject(@NotNull final String key) {
                final Object existingObject = objects.get(key);
                if (existingObject != null) {
                    return existingObject;
                }

                final Object object;
                if (PATTERN_DEFAULT_KEY.matcher(key).matches()) {
                    object = "true";
                } else {
                    return null;
                }
                objects.put(key, object);
                return object;
            }

            @NotNull
            @Override
            public Enumeration<String> getKeys() {
                return new IteratorEnumeration<String>(Collections.unmodifiableSet(objects.keySet()).iterator());
            }

        };
        actionBuilder.addBundle(resourceBundle);
    }

}
