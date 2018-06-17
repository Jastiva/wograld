/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.mapmenu;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import net.sf.gridarta.MainControl;
import org.jetbrains.annotations.NotNull;

/**
 * Saves or restores {@link MapMenu} contents to {@link Preferences}.
 * @author Andreas Kirschbaum
 */
public class MapMenuLoader {

    /**
     * Preferences values for entries.
     * @noinspection PublicInnerClass
     */
    public enum Type {

        /**
         * Preferences value for entries representing directories.
         */
        DIR,

        /**
         * Preferences value for entries representing map files.
         */
        MAP

    }

    /**
     * The {@link Preferences}.
     */
    @NotNull
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * The preferences key prefix.
     */
    @NotNull
    private final String key;

    /**
     * Creates a new instance.
     * @param key the preferences key prefix
     */
    public MapMenuLoader(@NotNull final String key) {
        this.key = key;
    }

    /**
     * Returns the number of entries present in the preferences.
     * @return the number of entries
     */
    public int loadNumEntries() {
        return preferences.getInt(key + "Num", 0);
    }

    /**
     * Sets the number of entries present in the preferences.
     * @param num the number of entries
     */
    public void saveNumEntries(final int num) {
        preferences.putInt(key + "Num", num);
    }

    /**
     * Loads an entry from preferences.
     * @param index the preference index
     * @return the entry or <code>null</code>
     * @throws IOException if the entry cannot be loaded
     */
    @NotNull
    public Result loadEntry(final int index) throws IOException {
        final String suffix = "[" + index + "]";
        final String title = preferences.get(key + "Title" + suffix, "");
        final String filename = preferences.get(key + "Filename" + suffix, "");
        final String directory = preferences.get(key + "Directory" + suffix, "");
        final String typeString = preferences.get(key + "Type" + suffix, Type.MAP.toString());
        final Type type;
        try {
            type = Type.valueOf(typeString);
        } catch (final IllegalArgumentException ex) {
            throw new IOException("invalid type: " + typeString, ex);
        }
        if (type == Type.DIR) {
            final MapMenuEntry mapMenuEntry;
            try {
                mapMenuEntry = new MapMenuEntryDir(title);
            } catch (final IllegalArgumentException ex) {
                throw new IOException("invalid directory name: " + title, ex);
            }
            return new Result(directory, mapMenuEntry);
        } else if (type == Type.MAP) {
            if (filename.isEmpty()) {
                throw new IOException("bookmark without file name: " + title);
            }

            final MapMenuEntry mapMenuEntry = new MapMenuEntryMap(new File(filename), title);
            return new Result(directory, mapMenuEntry);
        } else {
            throw new IOException("invalid type: " + type);
        }
    }

    /**
     * Saves an entry to preferences.
     * @param index the preference index
     * @param title the entry's title
     * @param filename the entry's file name
     * @param directory the entry's directory
     * @param type the entry's type
     */
    public void saveEntry(final int index, @NotNull final String title, @NotNull final String filename, @NotNull final String directory, @NotNull final Type type) {
        final String suffix = "[" + index + "]";
        preferences.put(key + "Title" + suffix, title);
        preferences.put(key + "Filename" + suffix, filename);
        if (directory.isEmpty()) {
            preferences.remove(key + "Directory" + suffix);
        } else {
            preferences.put(key + "Directory" + suffix, directory);
        }
        if (type == Type.MAP) {
            preferences.remove(key + "Type" + suffix);
        } else {
            preferences.put(key + "Type" + suffix, type.toString());
        }
    }

    /**
     * Removes an entry.
     * @param index the preference index
     */
    public void removeEntry(final int index) {
        final String suffix = "[" + index + "]";
        preferences.remove(key + "Title" + suffix);
        preferences.remove(key + "Filename" + suffix);
        preferences.remove(key + "Directory" + suffix);
        preferences.remove(key + "Type" + suffix);
    }

    /**
     * Result value consisting of a {@link MapMenuEntry} and its location
     * (directory).
     * @noinspection PublicInnerClass
     */
    public static class Result {

        /**
         * The entry's directory.
         */
        @NotNull
        private final String directory;

        /**
         * The entry.
         */
        @NotNull
        private final MapMenuEntry mapMenuEntry;

        /**
         * Creates a new instance.
         * @param directory the entry's directory
         * @param mapMenuEntry the entry
         */
        public Result(@NotNull final String directory, @NotNull final MapMenuEntry mapMenuEntry) {
            this.directory = directory;
            this.mapMenuEntry = mapMenuEntry;
        }

        /**
         * Returns the entry's directory.
         * @return the entry's directory
         */
        @NotNull
        public String getDirectory() {
            return directory;
        }

        /**
         * Returns the entry.
         * @return the entry
         */
        @NotNull
        public MapMenuEntry getMapMenuEntry() {
            return mapMenuEntry;
        }

    }

}
