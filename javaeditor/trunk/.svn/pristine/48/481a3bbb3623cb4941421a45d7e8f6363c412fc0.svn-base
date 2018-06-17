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

import java.io.File;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link PreferencesFactory} which creates {@link FilePreferences}
 * instances.
 * @author Andreas Kirschbaum
 */
public class FilePreferencesFactory implements PreferencesFactory {

    /**
     * The user preferences as returned by {@link #userRoot()}.
     */
    private static Preferences userRoot;

    /**
     * The system preferences as returned by {@link #userRoot()}.
     */
    private static Preferences systemRoot;

    /**
     * Initialize the module. This function must be called before an instance is
     * used.
     * @param defaultName the default key name for loading/saving values
     * @param file the file for loading/saving values or <code>null</code> to
     * not use a backing file
     */
    public static void initialize(@NotNull final String defaultName, @Nullable final File file) {
        if (userRoot != null || systemRoot != null) {
            throw new IllegalStateException();
        }

        final Storage storage = new Storage(defaultName, file);
        userRoot = new FilePreferencesRoot(NodeType.USER, storage);
        systemRoot = new FilePreferencesRoot(NodeType.SYSTEM, storage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Preferences userRoot() {
        if (userRoot == null) {
            throw new IllegalStateException();
        }
        return userRoot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Preferences systemRoot() {
        if (systemRoot == null) {
            throw new IllegalStateException();
        }
        return systemRoot;
    }

}
