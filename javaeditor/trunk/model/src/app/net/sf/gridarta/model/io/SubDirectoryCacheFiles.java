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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link CacheFiles} implementation that stores all files in sub-directories
 * right next to the original files.
 * @author Andreas Kirschbaum
 */
public class SubDirectoryCacheFiles implements CacheFiles {

    /**
     * The name of the sub-directories.
     */
    @NotNull
    private final String directory;

    /**
     * Creates a new instance.
     * @param directory the name of the sub-directories
     */
    public SubDirectoryCacheFiles(@NotNull final String directory) {
        this.directory = directory;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getCacheFile(@NotNull final File file, @Nullable final String prefix) {
        final String name = file.getName();
        return new File(new File(file.getParent(), directory), prefix == null ? name : name + "." + prefix);
    }

}
