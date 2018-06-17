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
import javax.swing.filechooser.FileFilter;
import net.sf.japi.util.filter.file.AbstractFileFilter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link HideFileFilterProxy}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class HideFileFilterProxyTest {

    /**
     * Test case for {@link HideFileFilterProxy#HideFileFilterProxy(FileFilter)}.
     */
    @Test
    public void testHideFileFilterProxy() {
        //noinspection ResultOfObjectAllocationIgnored
        new HideFileFilterProxy(new MockFileFilter(true));
    }

    /**
     * Test case for {@link HideFileFilterProxy#getDescription()}.
     */
    @Test
    public void testGetDescription() {
        final FileFilter oUT = new HideFileFilterProxy(new MockFileFilter(true));
        Assert.assertEquals("MOCK", oUT.getDescription());
    }

    /**
     * Test case for {@link HideFileFilterProxy#accept(File)}.
     */
    @Test
    public void testAccept() {
        final FileFilter oUT1 = new HideFileFilterProxy(new MockFileFilter(true));
        Assert.assertTrue(oUT1.accept(new File("foobar")));
        //Assert.assertFalse(oUT1.accept(new File("CVS")));
        Assert.assertFalse(oUT1.accept(new File(".svn")));

        final FileFilter oUT2 = new HideFileFilterProxy(new MockFileFilter(false));
        Assert.assertFalse(oUT2.accept(new File("foobar")));
        //Assert.assertFalse(oUT2.accept(new File("CVS")));
        Assert.assertFalse(oUT2.accept(new File(".svn")));
    }

    /**
     * Mock File Filter that's used as parent file filter for the
     * HideFileFilterProxy under test.
     * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
     */
    private static class MockFileFilter extends AbstractFileFilter {

        /**
         * Whether this file filter will accept or reject files.
         */
        private final boolean accepts;

        /**
         * Create a MockFileFilter.
         * @param accepts whether this file filter will accept or reject files
         */
        private MockFileFilter(final boolean accepts) {
            this.accepts = accepts;
        }

        @Override
        public boolean accept(final File f) {
            return accepts;
        }

        @Override
        public String getDescription() {
            return "MOCK";
        }

    }

}
