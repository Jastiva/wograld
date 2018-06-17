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

import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import javax.swing.ImageIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A FilterFaceProvider provides modified versions of icons provided by another
 * FaceProvider by applying a Filter.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author serpentshard
 */
public class FilterFaceProvider extends AbstractFaceProvider {

    /**
     * The FaceProvider to get the original icon from.
     */
    @Nullable
    private FaceProvider parent;

    /**
     * The Filter to apply.
     */
    @NotNull
    private final ImageFilter filter;

    /**
     * Create a new instance. The parent is unset. The parent must be set with
     * {@link #setParent(FaceProvider)} before invoking {@link
     * #getImageIconForFacename(String)} or {@link #createImage(String)} or
     * getting a face resp. creating an image will throw an {@link
     * IllegalStateException}.
     * @param filter ImageFilter to apply for creating the images
     */
    public FilterFaceProvider(@NotNull final ImageFilter filter) {
        this.filter = filter;
    }

    /**
     * Creates a new instance.
     * @param parent parent provider to get unfiltered images from
     * @param filter ImageFilter to apply for creating the images
     */
    public FilterFaceProvider(@Nullable final FaceProvider parent, @NotNull final ImageFilter filter) {
        this.parent = parent;
        this.filter = filter;
    }

    /**
     * Sets a new provider as parent.
     * @param parent parent provider to get unfiltered images from
     */
    public void setParent(@Nullable final FaceProvider parent) {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    protected ImageIcon createImage(@NotNull final String faceName) {
        if (parent == null) {
            throw new IllegalStateException("FilterFaceProvider in use but parent not set.");
        }
        final ImageIcon imageIcon = parent.getImageIconForFacename(faceName);
        if (imageIcon == null) {
            return null;
        }
        try {
            return new ImageIcon(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(imageIcon.getImage().getSource(), filter)));
        } catch (final Exception ignored) {
            return null;
        }
    }
    
     @Nullable
    @Override
    protected ImageIcon createImage2(@NotNull final String faceName) {
        if (parent == null) {
            throw new IllegalStateException("FilterFaceProvider in use but parent not set.");
        }
        final ImageIcon imageIcon = parent.getImageIconForFacename(faceName);
        if (imageIcon == null) {
            return null;
        }
        try {
            return new ImageIcon(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(imageIcon.getImage().getSource(), filter)));
        } catch (final Exception ignored) {
            return null;
        }
    }


}
