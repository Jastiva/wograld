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

import java.io.Serializable;
import org.jetbrains.annotations.Nullable;

/**
 * The class Size2D represents a 2d rectangular area. Sizes always are positive.
 * This class doesn't allow zero or negative sizes.
 * @author unknown (please fill in!!!)
 */
public class Size2D implements Serializable {

    /**
     * Serial Version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * One size object.
     */
    public static final Size2D ONE = new Size2D(1, 1);

    /**
     * The width of the area. It is always greater than zero.
     * @invariant width > 0
     * @serial include
     */
    private final int width;

    /**
     * The height of the area. It is always greater than zero.
     * @invariant height > 0
     * @serial include
     */
    private final int height;

    /**
     * Create a new Size2D.
     * @param width the width of the area; must be greater than zero
     * @param height the height of the area; must be greater than zero
     * @throws IllegalArgumentException in case <var>width</var> or
     * <var>height</var> is less than 1.
     */
    public Size2D(final int width, final int height) {
        if (width < 1) {
            throw new IllegalArgumentException("Width must be > 0");
        }
        if (height < 1) {
            throw new IllegalArgumentException("Height must be > 0");
        }

        this.width = width;
        this.height = height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        final Size2D other = (Size2D) obj;
        return height == other.height && width == other.width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return height ^ width << 16 ^ width >> 16;
    }

    /**
     * Returns the width of the area. It is always greater than zero.
     * @return the width of the area
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the area. It is always greater than zero.
     * @return the height of the area
     */
    public int getHeight() {
        return height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return width + "x" + height;
    }

}
