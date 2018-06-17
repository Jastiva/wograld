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

import java.io.File;
import org.jetbrains.annotations.NotNull;

/**
 * Loader for loading resources the user's home directory.
 * @author tchize
 */
public class ConfigFileUtils {

    /**
     * Name of directory with settings file.
     */
    @NotNull
    private static final String APP_SETTINGS_DIR = ".gridarta";

    /**
     * Private constructor to prevent instantiation.
     */
    private ConfigFileUtils() {
    }

    /**
     * Return the filename to use when dealing with this application's and
     * current users' home directory. For example, if called like this
     * <code>CResourceLoader.getHomeFile("file");</code> will return something
     * like /home/user/.gridarta/file under Linux.
     * @param filename the name of the requested file
     * @return the full to user home directory, appended with application
     *         directory and the filename.
     */
    @NotNull
    public static File getHomeFile(@NotNull final String filename) {
        final StringBuilder buf = new StringBuilder();
        buf.append(getHomeDir());
        buf.append(File.separator).append(APP_SETTINGS_DIR);
        final File rc = new File(buf.toString());
        if (!rc.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            rc.mkdirs();
        }
        buf.append(File.separator);
        buf.append(filename);
        return new File(buf.toString());
    }

    /**
     * Returns the user's home directory.
     * @return the home directory
     */
    @NotNull
    public static File getHomeDir() {
        return new File(System.getProperty("user.home"));
    }

}
