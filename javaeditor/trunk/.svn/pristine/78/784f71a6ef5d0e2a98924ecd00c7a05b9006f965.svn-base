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
import javax.swing.JFileChooser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for {@link JFileChooser} related functions.
 * @author Andreas Kirschbaum
 */
public class FileChooserUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private FileChooserUtils() {
    }

    /**
     * Calls {@link JFileChooser#setCurrentDirectory(File)}. Makes sure the
     * passed directory exists to minimize the chance for {@link
     * NullPointerException NullPointerExceptions}. See <a
     * href="http://bugs.sun.com/view_bug.do?bug_id=4869950">Bugreport
     * 4869950</a>.
     * @param fileChooser the file chooser to affect
     * @param dir the directory to set; <code>null</code>=user's home directory
     */
    public static void setCurrentDirectory(@NotNull final JFileChooser fileChooser, @Nullable final File dir) {
        fileChooser.setCurrentDirectory(sanitize(dir));
    }

    /**
     * Makes sure the current directory of a {@link JFileChooser} is valid.
     * @param fileChooser the file chooser to check
     */
    public static void sanitizeCurrentDirectory(@NotNull final JFileChooser fileChooser) {
        setCurrentDirectory(fileChooser, fileChooser.getCurrentDirectory());
    }

    /**
     * Performs sanity checks on the given directory. Returns the given
     * directory or one of its parent directories if it doesn't exist.
     * @param dir the directory to check
     * @return the existing directory
     */
    @NotNull
    private static File sanitize(@Nullable final File dir) {
        File result = dir == null ? ConfigFileUtils.getHomeDir() : dir.getAbsoluteFile();
        while (!result.exists() || !result.isDirectory()) {
            result = result.getParentFile();
            if (result == null) {
                result = ConfigFileUtils.getHomeDir();
                if (!result.exists() || !result.isDirectory()) {
                    result = new File("/");
                }
                break;
            }
        }
        return result;
    }

}
