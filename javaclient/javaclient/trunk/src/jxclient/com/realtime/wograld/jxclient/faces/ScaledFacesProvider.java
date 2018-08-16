/*
 * This file is part of JXClient, the Fullscreen Java Wograld Client.
 *
 * JXClient is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JXClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JXClient; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Copyright (C) 2005-2008 Yann Chachkoff.
 * Copyright (C) 2006-2011 Andreas Kirschbaum.
 */

package com.realtime.wograld.jxclient.faces;

import javax.swing.ImageIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link FacesProvider} that returns faces scaled to 64x64 pixels.
 * @author Andreas Kirschbaum
 */
public class ScaledFacesProvider implements FacesProvider {

    /**
     * The size of faces in pixels.
     */
    private static final int SIZE = 128;

    /**
     * The {@link FacesManager} to query.
     */
    @NotNull
    private final FacesManager facesManager;

    /**
     * Creates a new instance.
     * @param facesManager the faces manager to query
     */
    public ScaledFacesProvider(@NotNull final FacesManager facesManager) {
        this.facesManager = facesManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return SIZE;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ImageIcon getImageIcon(final int faceNum, @Nullable final boolean[] isUnknownImage) {
      //  return facesManager.getScaledImageIcon(faceNum, isUnknownImage);
        return facesManager.getOriginalImageIcon(faceNum, isUnknownImage);
    }
    
    public ImageIcon getBlockedImageIcon() {
        return facesManager.getBlockedImageIcon();
    }
    
    public ImageIcon getDark1ImageIcon() {
        return facesManager.getDark1ImageIcon();
    }
    
    public ImageIcon getDark2ImageIcon() {
        return facesManager.getDark2ImageIcon();
    }
    
    public ImageIcon getDark3ImageIcon() {
        return facesManager.getDark3ImageIcon();
    }
    
    public ImageIcon getFogofwarImageIcon() {
        return facesManager.getFogofwarImageIcon();
    }

}
