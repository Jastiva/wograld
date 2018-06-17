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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.prefs.Preferences;
import net.sf.gridarta.MainControl;
import net.sf.japi.util.filter.file.EndingFileFilter;
import org.jetbrains.annotations.NotNull;

/**
 * Swing FileFilter implementation that filters Daimonin map files.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class MapFileFilter extends EndingFileFilter {

    /**
     * Whether to actually perform real checks or just file endings.
     * <code>true</code> = real checks, <code>false</code> = file endings only.
     */
    private static boolean performingRealChecks = Preferences.userNodeForPackage(MainControl.class).getBoolean("filterRealMaps", true);

    /**
     * Already filtered files. Key: File Value: Boolean information whether or
     * not the file should be accepted (<code>true</code> == accept,
     * <code>false</code> == reject)
     */
    @NotNull
    private final Map<File, Boolean> cache = new WeakHashMap<File, Boolean>();

    /**
     * Creates a new instance.
     * @param acceptDirectories whether the file filter should accept
     * directories
     * @param description the description to use for Swing
     * @param endings the endings to accept, including their period
     */
    public MapFileFilter(final boolean acceptDirectories, @NotNull final String description, @NotNull final String... endings) {
        super(acceptDirectories, true, description, endings);
    }

    /**
     * Set whether to actually perform real checks or just file endings.
     * @param performingRealChecks <code>true</code> for performing real checks,
     * <code>false</code> to compare only file endings
     */
    public static void setPerformingRealChecks(final boolean performingRealChecks) {
        MapFileFilter.performingRealChecks = performingRealChecks;
        Preferences.userNodeForPackage(MainControl.class).putBoolean("filterRealMaps", performingRealChecks);
    }

    /**
     * Get whether to actually perform real checks or just file endings.
     * @return <code>true</code> for performing real checks, <code>false</code>
     *         to compare only file endings
     */
    public static boolean isPerformingRealChecks() {
        return performingRealChecks;
    }

    /**
     * {@inheritDoc}
     * @noinspection ParameterNameDiffersFromOverriddenParameter
     */
    @Override
    public boolean accept(@NotNull final File pathName) {
        if (pathName.isDirectory()) {
            return true;
        }

        //NullPointerException is expected when no mapping exists
        //noinspection ProhibitedExceptionCaught
        try {
            return cache.get(pathName);
        } catch (final NullPointerException ignored) {
        }

        if (!performingRealChecks) {
            return super.accept(pathName);
        }

        try {
            final FileInputStream fis = new FileInputStream(pathName);
            try {
                final InputStreamReader isr = new InputStreamReader(fis);
                try {
                    final BufferedReader in = new BufferedReader(isr);
                    try {
                        final String line = in.readLine();
                        final boolean ret = line != null && line.equals("arch map");
                        cache.put(pathName, ret ? Boolean.TRUE : Boolean.FALSE);
                        return ret;
                    } finally {
                        in.close();
                    }
                } finally {
                    isr.close();
                }
            } finally {
                fis.close();
            }
        } catch (final IOException ignored) {
            return super.accept(pathName);
        }
    }

}
