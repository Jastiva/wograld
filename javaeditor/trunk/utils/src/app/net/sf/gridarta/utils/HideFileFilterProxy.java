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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.filechooser.FileFilter;
import net.sf.japi.util.filter.file.AbstractFileFilter;
import org.jetbrains.annotations.NotNull;

/**
 * A FileFilter that wraps another FileFilter and filters out CVS and .xvpics
 * files. The description and other filtering are taken from the wrapped file
 * filter.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class HideFileFilterProxy extends AbstractFileFilter {

    /**
     * The other file filter.
     */
    @NotNull
    private final FileFilter other;

    /**
     * The default rejected directory names.
     */
    @NotNull
    private static final Collection<String> REJECTED_DIRECTORY_NAMES = Arrays.asList("CVS");

    /**
     * The rejected directory names.
     */
    @NotNull
    private final Collection<String> rejectedDirectoryNames = new ArrayList<String>();

    /**
     * Creates a new instance.
     * @param other the file filter to wrap
     */
    public HideFileFilterProxy(@NotNull final FileFilter other) {
        this.other = other;
        rejectedDirectoryNames.addAll(REJECTED_DIRECTORY_NAMES);
    }

    /**
     * {@inheritDoc} Returns the description from the {@link #other}
     * FileFilter.
     */
    @NotNull
    @Override
    public String getDescription() {
        return other.getDescription();
    }

    /**
     * {@inheritDoc} First checks whether the file should be hidden, if not,
     * checks the other FileFilter.
     */
    @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
    @Override
    public boolean accept(@NotNull final File pathName) {
        if (pathName.getName().startsWith(".")) {
            return false;
        }
        // XXX suppression for Bug in IDEA
        //noinspection RedundantCast
        return !(pathName.isDirectory() && rejectedDirectoryNames.contains(pathName.getName())) && ((java.io.FileFilter) other).accept(pathName);
    }

}
