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
 * Test for {@link WrappingStringBuilder}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
@SuppressWarnings("FeatureEnvy")
public class WrappingStringBuilderTest {

    /**
     * Test for {@link WrappingStringBuilder#append(String)}.
     */
    @Test
    public void testAppend() {
        final WrappingStringBuilder sb = new WrappingStringBuilder(16);
        sb.append("hello");
        sb.append("foo");
        sb.append("bar");
        sb.append("buzz");
        Assert.assertEquals("hello, foo, bar,\n" + "buzz", sb.toString());
        sb.append(10);
        Assert.assertEquals("hello, foo, bar,\n" + "buzz, 10", sb.toString());
    }

}
