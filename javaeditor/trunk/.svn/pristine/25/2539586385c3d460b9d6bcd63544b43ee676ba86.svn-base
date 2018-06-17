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

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for image related functions.
 * @author Andreas Kirschbaum
 */
public class ImageUtils {

    /**
     * The component for the media {@link #tracker}.
     */
    private static final Component component = new Component() {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

    };

    /**
     * The {@link MediaTracker} for loading scaled images.
     */
    private static final MediaTracker tracker = new MediaTracker(component);

    /**
     * Private constructor to prevent instantiation.
     */
    private ImageUtils() {
    }

    /**
     * Returns a scaled version of an {@link Image}. Other than {@link
     * Image#getScaledInstance(int, int, int)}, this function drops the
     * reference to the source image.
     * @param image the source image to scale
     * @param w the scaled width
     * @param h the scaled height
     * @return the scaled image
     * @throws InterruptedException if scaling failed because the current thread
     * was interrupted
     */
    @NotNull
    public static Image scaleImage(@NotNull final Image image, final int w, final int h) throws InterruptedException {
        final Image scaledImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);

        final int id = 0;
        tracker.addImage(scaledImage, id);
        try {
            tracker.waitForID(id);
        } finally {
            tracker.removeImage(scaledImage, id);
        }

        return scaledImage;
    }

}
