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
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link Iterator} that recursively returns all files from a directory.
 * @author Andreas Kirschbaum
 * @noinspection UnusedDeclaration // used by plugin scripts
 */
public class RecursiveFileIterator implements Iterator<File> {

    /**
     * The stack of active {@link Iterator Iterators}.
     */
    @NotNull
    private final Stack<Iterator<File>> iteratorStack = new Stack<Iterator<File>>();

    /**
     * The current {@link Iterator}.
     */
    @NotNull
    private Iterator<File> current;

    /**
     * Creates a new instance.
     * @param dir the directory to start the search at
     */
    public RecursiveFileIterator(@NotNull final File dir) {
        current = new FlatFileIterator(dir);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return current.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File next() {
        final File result = current.next();
        iteratorStack.push(current);
        current = new FlatFileIterator(result);
        try {
            //noinspection LoopConditionNotUpdatedInsideLoop
            while (!current.hasNext()) {
                current = iteratorStack.pop();
            }
        } catch (final EmptyStackException ignore) {
            /* ignore. */
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        current.remove();
    }

}
