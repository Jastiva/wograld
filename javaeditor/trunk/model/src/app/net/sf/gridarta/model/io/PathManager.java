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

package net.sf.gridarta.model.io;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class contains methods for converting relative map paths to absolute map
 * paths and vice versa. Server-side, paths to maps always are URIs with the
 * maps directory being the root directory for the maps. Therefore it makes
 * sense to treat them as URIs in the editor as well.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @todo make more use of URI
 */
public class PathManager {

    /**
     * A {@link Pattern} that matches redundant directory parts like "dir/../".
     */
    @NotNull
    private static final Pattern PATTERN_REDUNDANT = Pattern.compile("[^/]+/\\.\\./");

    /**
     * A {@link Pattern} that matches redundant directory separators.
     */
    @NotNull
    private static final Pattern PATTERN_SLASHES = Pattern.compile("//*");


    // NOTE: There are certainly faster algorithms. But these methods are rarely used.

    /**
     * Reference to MainControl for obtaining folder information.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * Creates a new instance.
     * @param globalSettings the global settings instance for obtaining folder
     * information
     */
    public PathManager(@NotNull final GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    /**
     * Returns a map path for a {@link File}.
     * @param file the file
     * @return the map path
     */
    @NotNull
    public String getMapPath(@NotNull final File file) {
        return getMapPath(file.getPath());
    }

    /**
     * Create map path. Replaces all occurrences of '\' with '/' and removes the
     * path to the map directory (absolute, relative or canonical) from the
     * path.
     * @param path to create map path from
     * @return fixed path
     */
    @NotNull
    public String getMapPath(@NotNull final String path) {
        return getMapPath(path, globalSettings.getMapsDirectory());
    }

    /**
     * Create map path. Replaces all occurrences of '\' with '/' and removes the
     * path to the map directory (absolute, relative or canonical) from the
     * path.
     * @param mapDir the map dir
     * @param path to create map path from
     * @return fixed path
     */
    @NotNull
    public static String getMapPath(@NotNull final String path, @NotNull final File mapDir) {
        @NotNull final String mapDirName = mapDir.getPath();
        String mapPath = path.replace('\\', '/');
        if (mapPath.startsWith(mapDirName)) {
            mapPath = mapPath.substring(mapDirName.length());
        }
        if (mapPath.startsWith(new File(mapDirName).getAbsolutePath())) {
            mapPath = mapPath.substring(new File(mapDirName).getAbsolutePath().length());
        }
        try {
            if (mapPath.startsWith(new File(mapDirName).getCanonicalPath())) {
                mapPath = mapPath.substring(new File(mapDirName).getCanonicalPath().length());
            }
        } catch (final IOException ignored) {
            // ignore
        }
        mapPath = mapPath.replace('\\', '/');
        return mapPath;
    }

    /**
     * Returns a map path for a {@link File}.
     * @param file the file
     * @return the map path or <code>null</code> if the file is not within the
     *         maps directory
     */
    @Nullable
    public String getMapPath2(@NotNull final File file) {
        final String canonicalFile;
        final String canonicalMaps;
        try {
            canonicalFile = file.getCanonicalPath();
            canonicalMaps = globalSettings.getMapsDirectory().getCanonicalPath();
        } catch (final IOException ignored) {
            return null;
        }
        if (!canonicalFile.startsWith(canonicalMaps)) {
            return null;
        }
        final String mapPath = canonicalFile.substring(canonicalMaps.length());
        return mapPath.replace('\\', '/');
    }

    /**
     * Check whether a path is absolute. Paths starting with "/" are absolute,
     * paths starting with other characters are relative. Empty paths are
     * relative.
     * @param path the path to check
     * @return <code>true</code> if <var>path</var> is absolute,
     *         <code>false</code> otherwise
     * @see #isRelative(String) which nearly is the opposite of this method
     */
    public static boolean isAbsolute(@NotNull final String path) {
        return path.startsWith("/") || new File(path).isAbsolute();
    }

    /**
     * Check whether a path is relative. Paths not starting with "/" are
     * relative, paths starting with are absolute. Empty paths are relative.
     * @param path the path to check
     * @return <code>true</code> if <var>path</var> is relative,
     *         <code>false</code> otherwise
     * @see #isAbsolute(String) which nearly is the opposite of this method
     */
    public static boolean isRelative(@NotNull final String path) {
        return !path.startsWith("/") && !new File(path).isAbsolute();
    }

    /**
     * Converts a relative path to an absolute path. If the path already is
     * absolute, this method simply returns the path. If the reference ends on
     * "/", it is treated as being a base directory, otherwise a file that's
     * directory is taken as base.
     * @param reference the reference file the relative path works on
     * @param relative destination file with relative path
     * @return absolute path from the base directory
     */
    @NotNull
    public static String relativeToAbsolute(@NotNull final String reference, @NotNull final String relative) {
        final String normalizedReference = reference.replace('\\', '/');
        final String normalizedRelative = relative.replace('\\', '/');
        if (isAbsolute(normalizedRelative)) {
            return normalizedRelative;
        }
        String work = normalizedReference.substring(0, normalizedReference.lastIndexOf('/') + 1) + normalizedRelative; // + 1 to include the "/"
        String work2;
        do {
            work2 = work;
            work = PATTERN_REDUNDANT.matcher(work2).replaceAll("");
        } while (!work2.equals(work));
        return work;
    }

    /**
     * Converts an absolute path to a relative path. If the path already is
     * relative, this method simply returns the path. If the reference ends on
     * "/", it is treated as being a base directory, otherwise a file that's
     * directory is taken as base.
     * @param reference the reference file the relative path works on
     * @param absolute destination file with relative path
     * @return absolute path from the base directory
     */
    @NotNull
    public static String absoluteToRelative(@NotNull final String reference, @NotNull final String absolute) {
        final String normalizedReference = reference.replace('\\', '/');
        final String normalizedAbsolute = absolute.replace('\\', '/');
        if (isRelative(normalizedAbsolute)) {
            return normalizedAbsolute;
        }
        //String reference2 =
        // First: Strip equal path parts
        final int difference = findDifference(normalizedReference, normalizedAbsolute);
        final StringBuilder relative = new StringBuilder(normalizedAbsolute.substring(difference));
        final CharSequence referencePath = normalizedReference.substring(difference);
        for (int i = 0, l = findOccurrences(referencePath, '/'); i < l; i++) {
            relative.insert(0, "../");
        }
        return relative.toString();
    }

    /**
     * Helper method that returns the first string index at which two strings
     * denoting paths aren't identical. That is, the index always points at the
     * beginning or a '/'-character. The return value of this method is in every
     * case good for a String.substring() invocation start index.
     * @param s1 first string to compare
     * @param s2 second string to compare
     * @return index of first / after which s1 and s2 aren't identical or 0 if
     *         they differ from start
     */
    private static int findDifference(@NotNull final CharSequence s1, @NotNull final CharSequence s2) {
        int s1slash = -1;
        int s2slash = -1;
        char c1 = (char) 0; // initialize for equality for first loop cycle
        char c2 = (char) 0;
        for (int index = 0, l1 = s1.length(), l2 = s2.length(); index < l1 && index < l2 && c1 == c2; index++) {
            c1 = s1.charAt(index);
            c2 = s2.charAt(index);
            if (c1 == '/') {
                s1slash = index;
            }
            if (c2 == '/') {
                s2slash = index;
            }
        }
        return Math.min(s1slash, s2slash) + 1;
    }

    /**
     * Counts the occurrences of a character within a string.
     * @param s the string to count in
     * @param c character to count
     * @return number of occurrences of <var>c</var> in <var>s</var>
     */
    private static int findOccurrences(@NotNull final CharSequence s, final char c) {
        int occurrences = 0;
        for (int i = 0, l = s.length(); i < l; i++) {
            if (s.charAt(i) == c) {
                occurrences++;
            }
        }
        return occurrences;
    }

    /**
     * Create a reasonable path.
     * @param str the string to create path from
     * @return path
     */
    @NotNull
    public static String path(@NotNull final CharSequence str) {
        String path = StringUtils.PATTERN_BACKSLASH.matcher(str).replaceAll("/");
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        path = path.startsWith("file:") ? path.substring("file:".length()) : path;
        path = PATTERN_SLASHES.matcher(path).replaceAll("/");
        return path;
    }

    /**
     * Returns the given path in absolute form.
     * @param path the path to convert
     * @return the absolute path
     */
    @NotNull
    public static String getAbsolutePath(@NotNull final CharSequence path) {
        return relativeToAbsolute(System.getProperty("user.dir") + "/dummy", path(path));
    }

}
