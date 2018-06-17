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

package net.sf.gridarta.model.face;

import java.awt.image.RGBImageFilter;

/**
 * Class to filter images by simply applying a boolean OR operation.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ColourFilter extends RGBImageFilter {

    /**
     * The mask for selecting the red bits.
     */
    public static final int RED_MASK = 0x00FF0000;

    /**
     * The mask for selecting the green bits.
     */
    public static final int GREEN_MASK = 0x0000FF00;

    /**
     * The mask for selecting the blue bits.
     */
    public static final int BLUE_MASK = 0x000000FF;

    /**
     * The mask for selecting the red, green, and blue bits.
     */
    public static final int RED_GREEN_BLUE_MASK = RED_MASK | GREEN_MASK | BLUE_MASK;

    /**
     * The mask for selecting the alpha bits.
     */
    private static final int ALPHA_MASK = 0xFF000000;

    /**
     * The bit-offset for the red bits.
     */
    private static final int RED_SHIFT = 16;

    /**
     * The bit-offset for the green bits.
     */
    private static final int GREEN_SHIFT = 8;

    /**
     * The bit-offset for the blue bits.
     */
    private static final int BLUE_SHIFT = 0;

    /**
     * The positive mask to apply.
     */
    private final int positiveMask;

    /**
     * The negative mask to apply.
     */
    private final int negativeMask;

    /**
     * Create an ColourFilter.
     * @param mask Mask to apply
     */
    public ColourFilter(final int mask) {
        positiveMask = mask & RED_GREEN_BLUE_MASK;
        negativeMask = ~mask & RED_GREEN_BLUE_MASK;
    }

    /**
     * Converts a pixel by applying an or operation. {@inheritDoc}
     */
    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final int alpha = rgb & ALPHA_MASK;
        final float r = (float) (rgb >> RED_SHIFT & 0xFF);
        final float g = (float) (rgb >> GREEN_SHIFT & 0xFF);
        final float b = (float) (rgb >> BLUE_SHIFT & 0xFF);
        int gray1 = (int) (0.66F * r + 0.66F * g + 0.66F * b);
        int gray2 = gray1 - 256;
        if (gray1 < 0) {
            gray1 = 0;
        }
        if (gray1 > 255) {
            gray1 = 255;
        }
        if (gray2 < 0) {
            gray2 = 0;
        }
        if (gray2 > 255) {
            gray2 = 255;
        }
        gray1 = gray1 << BLUE_SHIFT | gray1 << GREEN_SHIFT | gray1 << RED_SHIFT;
        gray2 = gray2 << BLUE_SHIFT | gray2 << GREEN_SHIFT | gray2 << RED_SHIFT;

        return alpha | gray1 & positiveMask | gray2 & negativeMask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() {
        return super.clone();
    }

}
