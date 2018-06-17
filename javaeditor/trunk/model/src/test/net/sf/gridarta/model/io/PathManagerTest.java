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

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link PathManager}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class PathManagerTest {

    /**
     * Test case for {@link PathManager#path(CharSequence)}.
     */
    @Test
    public void testPath() {
        Assert.assertEquals("Expecting trailing slash from directories being removed.", "/foo", PathManager.path("/foo/"));
        Assert.assertEquals("Expecting file: URIs being converted to URIs without scheme.", "/foo", PathManager.path("file:/foo/"));
        Assert.assertEquals("Expecting multiple // characters to be collapsed.", "/foo/bar", PathManager.path("//foo///bar"));
    }

    /**
     * Test case for {@link PathManager#absoluteToRelative(String, String)}.
     */
    @Test
    public void testAbsoluteToRelative() {
        Assert.assertEquals("../stoneglow/stoneglow_0000", PathManager.absoluteToRelative("/relic/castle_0000", "/stoneglow/stoneglow_0000"));
        Assert.assertEquals("../stoneglow/stoneglow_0000", PathManager.absoluteToRelative("/relic/", "/stoneglow/stoneglow_0000"));
    }

    /**
     * Test case for {@link PathManager#absoluteToRelative(String, String)}.
     */
    @Test
    public void testRelativeToAbsolute() {
        Assert.assertEquals("/stoneglow/stoneglow_0000", PathManager.relativeToAbsolute("/relic/castle_0000", "../stoneglow/stoneglow_0000"));
        Assert.assertEquals("/stoneglow/stoneglow_0000", PathManager.relativeToAbsolute("/relic/", "../stoneglow/stoneglow_0000"));
        Assert.assertEquals("/stoneglow/stoneglow_0000", PathManager.relativeToAbsolute("/relic/castle_0000", "/stoneglow/stoneglow_0000"));
        Assert.assertEquals("/stoneglow/stoneglow_0000", PathManager.relativeToAbsolute("/relic/", "/stoneglow/stoneglow_0000"));
    }

    /**
     * Test case for {@link PathManager#isRelative(String)}.
     */
    @Test
    public void testIsRelative() {
        Assert.assertTrue(PathManager.isRelative("../stoneglow/stoneglow_0000"));
        Assert.assertTrue(PathManager.isRelative("stoneglow/stoneglow_0000"));
        Assert.assertFalse(PathManager.isRelative("/stoneglow/stoneglow_0000"));
        Assert.assertTrue(PathManager.isRelative(""));
    }

    /**
     * Test case for {@link PathManager#isAbsolute(String)}.
     */
    @Test
    public void testIsAbsolute() {
        Assert.assertFalse(PathManager.isAbsolute("../stoneglow/stoneglow_0000"));
        Assert.assertFalse(PathManager.isAbsolute("stoneglow/stoneglow_0000"));
        Assert.assertTrue(PathManager.isAbsolute("/stoneglow/stoneglow_0000"));
        Assert.assertFalse(PathManager.isAbsolute(""));
    }

}
