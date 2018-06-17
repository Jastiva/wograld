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

package net.sf.gridarta.model.index;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link MapsIndex}.
 * @author Andreas Kirschbaum
 */
public class MapsIndexTest {

    /**
     * Checks that values are correctly marked as pending.
     */
    @Test
    public void test1() {
        final Index<File> index = new MapsIndex();
        index.add(new File("a"), 1L);
        index.add(new File("b"), 1L);
        index.add(new File("c"), 1L);
        Assert.assertEquals("a,b,c", getPending(index));
        Assert.assertEquals("", getPending(index));
        index.add(new File("d"), 1L);
        Assert.assertEquals("d", getPending(index));
        index.add(new File("b"), 1L);
        index.add(new File("d"), 1L);
        Assert.assertEquals("", getPending(index));
        index.add(new File("b"), 2L);
        index.add(new File("d"), 2L);
        Assert.assertEquals("b,d", getPending(index));
        index.setPending(new File("a"));
        Assert.assertEquals("a", getPending(index));
        index.setPending(new File("a"));
        index.add(new File("b"), 3L);
        index.add(new File("c"), 3L);
        index.remove(new File("b"));
        Assert.assertEquals("a,c", getPending(index));
    }

    /**
     * Checks that {@link Index#findPartialName(String)} works as expected.
     */
    @Test
    public void testFind1() {
        final Index<File> index = new MapsIndex();
        Assert.assertEquals("", findPartialName(index, "a"));
        index.add(new File("abc"), 1L);
        index.setName(new File("abc"), 1L, "abC");
        Assert.assertEquals("abc", findPartialName(index, "a"));
        Assert.assertEquals("abc", findPartialName(index, "b"));
        Assert.assertEquals("", findPartialName(index, "x"));
        index.add(new File("bcd"), 1L);
        index.setName(new File("bcd"), 1L, "BcD");
        Assert.assertEquals("abc", findPartialName(index, "a"));
        Assert.assertEquals("abc,bcd", findPartialName(index, "b"));
        Assert.assertEquals("abc,bcd", findPartialName(index, "Bc"));
        Assert.assertEquals("", findPartialName(index, "x"));
    }

    /**
     * Checks that {@link Index#beginUpdate()} and {@link Index#endUpdate()}
     * works as expected.
     */
    @Test
    public void testTransaction() {
        final Index<File> index = new MapsIndex();
        index.add(new File("a"), 1L);
        index.add(new File("b"), 1L);
        index.add(new File("c"), 1L);
        index.setName(new File("a"), 2L, "a");
        index.setName(new File("b"), 2L, "a");
        index.setName(new File("c"), 2L, "a");
        index.beginUpdate();
        index.add(new File("b"), 1L);
        index.add(new File("c"), 2L);
        index.add(new File("d"), 2L);
        index.endUpdate();
        index.setName(new File("b"), 2L, "a");
        index.setName(new File("c"), 2L, "a");
        index.setName(new File("d"), 2L, "a");
        Assert.assertEquals("b,c,d", findPartialName(index, "a"));
    }

    /**
     * Checks that listeners are notified.
     */
    @Test
    public void testListener() {
        final Index<File> index = new MapsIndex();
        final Listener listener = new Listener();
        index.addIndexListener(listener);

        index.add(new File("a"), 1L);
        Assert.assertEquals("add a\n" + "pending changed\n", listener.getAndClearEvents());

        index.add(new File("a"), 1L);
        Assert.assertEquals("", listener.getAndClearEvents());

        index.add(new File("a"), 2L);
        Assert.assertEquals("", listener.getAndClearEvents());

        index.add(new File("b"), 2L);
        index.add(new File("c"), 2L);
        Assert.assertEquals("add b\n" + "add c\n", listener.getAndClearEvents());

        index.remove(new File("b"));
        Assert.assertEquals("del b\n" + "name changed\n", listener.getAndClearEvents());

        index.setName(new File("a"), 2L, "name1");
        index.setName(new File("b"), 2L, "name2");
        index.setName(new File("c"), 2L, "name3");
        Assert.assertEquals("name changed\n" + "name changed\n" + "name changed\n", listener.getAndClearEvents());
    }

    /**
     * Returns all pending values of a {@link MapsIndex}. Afterwards the pending
     * entries have been removed from the index.
     * @param index the index
     * @return the pending values
     */
    @NotNull
    private static String getPending(@NotNull final Index<File> index) {
        final Collection<File> pendingFiles = new HashSet<File>();
        while (true) {
            final File pendingFile = index.removePending();
            if (pendingFile == null) {
                break;
            }
            pendingFiles.add(pendingFile);
        }
        return format(pendingFiles);
    }

    /**
     * Calls {@link Index#findPartialName(String)} on the given {@link Index}
     * and name and returns a string representation of the result.
     * @param index the index
     * @param name the name
     * @return the string representation
     */
    @NotNull
    private static String findPartialName(@NotNull final Index<File> index, @NotNull final String name) {
        return format(index.findPartialName(name));
    }

    /**
     * Returns a text representation of a {@link Collection}.
     * @param files the collection
     * @return the text representation
     */
    @NotNull
    private static String format(@NotNull final Collection<File> files) {
        final TreeSet<File> tmp = new TreeSet<File>(files);
        final StringBuilder sb = new StringBuilder();
        final Iterator<File> it = tmp.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(',');
                sb.append(it.next());
            }
        }
        return sb.toString();
    }

    /**
     * An {@link IndexListener} that records a text representation of all
     * generated events.
     */
    private static class Listener implements IndexListener<File> {

        /**
         * The recorded events in text representation.
         */
        @NotNull
        private final StringBuilder stringBuilder = new StringBuilder();

        @Override
        public void valueAdded(@NotNull final File value) {
            stringBuilder.append("add ").append(value).append("\n");
        }

        @Override
        public void valueRemoved(@NotNull final File value) {
            stringBuilder.append("del ").append(value).append("\n");
        }

        @Override
        public void nameChanged() {
            stringBuilder.append("name changed\n");
        }

        @Override
        public void pendingChanged() {
            stringBuilder.append("pending changed\n");
        }

        @Override
        public void indexingFinished() {
            stringBuilder.append("indexing finished");
        }

        /**
         * Returns the accumulated events.
         * @return the events
         */
        @NotNull
        public String getAndClearEvents() {
            final String result = stringBuilder.toString();
            stringBuilder.setLength(0);
            return result;
        }

    }

}
