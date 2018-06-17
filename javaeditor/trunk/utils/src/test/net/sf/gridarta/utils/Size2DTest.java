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

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link Size2D}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
@SuppressWarnings("FeatureEnvy")
// This is a test. It has feature envy by definition.
public class Size2DTest {

    /**
     * Test case for {@link Size2D#Size2D(int, int)}.
     */
    @Test
    public void testSize2D() {
        final Size2D size = new Size2D(100, 200);
        Assert.assertEquals("width MUST be stored", 100, size.getWidth());
        Assert.assertEquals("height MUST be stored", 200, size.getHeight());
    }

    /**
     * Test case for {@link Size2D#equals(Object)}.
     */
    @Test
    public void testEquals() {
        final Size2D size1 = new Size2D(100, 200);
        final Size2D size2 = new Size2D(100, 200);
        final Size2D size3 = new Size2D(100, 200);
        final Size2D differWidth = new Size2D(50, 200);
        final Size2D differHeight = new Size2D(100, 80);
        final Size2D differBoth = new Size2D(50, 80);
        Assert.assertEquals("Sizes with identical width and height MUST be equal.", size1, size2);
        Assert.assertEquals("Sizes with identical width and height MUST be equal.", size1, size3);
        Assert.assertEquals("Sizes with identical width and height MUST be equal.", size2, size1);
        Assert.assertEquals("Sizes with identical width and height MUST be equal.", size2, size3);
        Assert.assertEquals("Sizes with identical width and height MUST be equal.", size3, size1);
        Assert.assertEquals("Sizes with identical width and height MUST be equal.", size3, size2);
        Assert.assertFalse("Sizes with different width or height MUST be unequal", size1.equals(differBoth));
        Assert.assertFalse("Sizes with different width or height MUST be unequal", size1.equals(differWidth));
        Assert.assertFalse("Sizes with different width or height MUST be unequal", size1.equals(differHeight));
    }

    /**
     * Test case for {@link Size2D#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final Size2D size1 = new Size2D(100, 200);
        final Size2D size2 = new Size2D(100, 200);
        Assert.assertEquals("Equal sizes MUST return the same hashCode.", size1.hashCode(), size2.hashCode());
    }

    /**
     * Test case for {@link Size2D#getWidth()}.
     */
    @Test
    public void testGetWidth() {
        final Size2D size = new Size2D(100, 200);
        Assert.assertEquals("width MUST be stored", 100, size.getWidth());
    }

    /**
     * Test case for {@link Size2D#getHeight()}.
     */
    @Test
    public void testGetHeight() {
        final Size2D size = new Size2D(100, 200);
        Assert.assertEquals("height MUST be stored", 200, size.getHeight());
    }

}
