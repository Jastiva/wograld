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
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link Iterator} that iterates non-recursively over the contents of a
 * directory.
 * @author Andreas Kirschbaum
 */
public class FlatFileIterator implements Iterator<File> {

    /**
     * An empty array of {@link File Files}.
     */
    @NotNull
    private static final File[] EMPTY_FILE_ARRAY = new File[0];

    /**
     * The files to return.
     */
    @NotNull
    private final File[] files;

    /**
     * The current index into {@link #files}.
     */
    private int pos;

    /**
     * Creates a new instance.
     * @param dir the directory to list
     */
    public FlatFileIterator(final File dir) {
        final File[] tmp = dir.listFiles();
        files = tmp == null ? EMPTY_FILE_ARRAY : tmp;
        Arrays.sort(files);
        skipSpecialNames();
    }

    /* {@inheritDoc} */

    @Override
    public boolean hasNext() {
        return pos < files.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File next() {
        if (pos >= files.length) {
            throw new NoSuchElementException();
        }

        final File result = files[pos++];
        skipSpecialNames();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Skips special files that should be always ignored.
     */
    private void skipSpecialNames() {
        while (pos < files.length && files[pos].getName().equalsIgnoreCase(".svn")) {
            pos++;
        }
    }

}
