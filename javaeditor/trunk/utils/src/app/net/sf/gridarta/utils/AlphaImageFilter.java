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

import java.awt.image.RGBImageFilter;

/**
 * An image filter creating transparent images.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class AlphaImageFilter extends RGBImageFilter {

    /**
     * Converts a pixel by increasing the transparency. {@inheritDoc}
     */
    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        // This is sufficient since alpha channel isn't used in the graphics this is required for.
        return rgb >>> 24 == 0 ? rgb : rgb & 0x00FFFFFF | 0x80000000;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() {
        return super.clone();
    }

}
