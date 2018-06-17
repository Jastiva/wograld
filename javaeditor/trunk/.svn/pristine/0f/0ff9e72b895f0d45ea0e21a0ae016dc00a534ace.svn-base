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

import java.awt.image.ColorModel;
import java.awt.image.ImageFilter;

/**
 * An {@link ImageFilter} that produces double faces: the source image is drawn
 * twice with a vertical shift.
 * @author Andreas Kirschbaum
 */
public class DoubleImageFilter extends ImageFilter {

    /**
     * The default RGB color model.
     */
    private static final ColorModel RGB_DEFAULT_COLOR_MODEL = ColorModel.getRGBdefault();

    /**
     * The offset for shifting the two images.
     */
    private final int doubleFaceOffset;

    /**
     * The image width.
     */
    private int width;

    /**
     * The destination image's pixels.
     */
    private int[] raster;

    /**
     * Creates a new instance.
     * @param doubleFaceOffset the offset for shifting the two images
     */
    public DoubleImageFilter(final int doubleFaceOffset) {
        this.doubleFaceOffset = doubleFaceOffset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDimensions(final int width, final int height) {
        this.width = width;
        final int newHeight = height + doubleFaceOffset;
        raster = new int[width * newHeight];
        super.setDimensions(width, newHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHints(final int hints) {
        super.setHints((hints & ~(SINGLEPASS | TOPDOWNLEFTRIGHT)) | COMPLETESCANLINES | RANDOMPIXELORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPixels(final int x, final int y, final int w, final int h, final ColorModel model, final byte[] pixels, final int off, final int scansize) {
        int srcOff = off;
        int dstOff = y * width + x;
        for (int yc = 0; yc < h; yc++) {
            for (int xc = 0; xc < w; xc++) {
                raster[dstOff++] = model.getRGB(pixels[srcOff++] & 0xff);
            }
            srcOff += scansize - w;
            dstOff += width - w;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPixels(final int x, final int y, final int w, final int h, final ColorModel model, final int[] pixels, final int off, final int scansize) {
        int srcOff = off;
        int dstOff = y * width + x;
        for (int yc = 0; yc < h; yc++) {
            for (int xc = 0; xc < w; xc++) {
                raster[dstOff++] = model.getRGB(pixels[srcOff++]);
            }
            srcOff += scansize - w;
            dstOff += width - w;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void imageComplete(final int status) {
        if (status != IMAGEERROR && status != IMAGEABORTED) {
            final int offset = width * doubleFaceOffset;
            for (int i = raster.length - 1; i >= offset; i--) {
                if ((raster[i] & 0xFF000000) == 0) {
                    raster[i] = raster[i - offset];
                }
            }
            super.setPixels(0, 0, width, raster.length / width, RGB_DEFAULT_COLOR_MODEL, raster, 0, width);
        }
        super.imageComplete(status);
    }

}
