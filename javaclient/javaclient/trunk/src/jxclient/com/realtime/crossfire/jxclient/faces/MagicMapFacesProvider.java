/*
 * This file is part of JXClient, the Fullscreen Java Crossfire Client.
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

package com.realtime.crossfire.jxclient.faces;

import javax.swing.ImageIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link FacesProvider} that returns faces scaled to 4x4 pixels.
 * @author Andreas Kirschbaum
 */
public class MagicMapFacesProvider implements FacesProvider {

    /**
     * The size of faces in pixels.
     */
    private static final int SIZE = 4;

    /**
     * The {@link FacesManager} to query.
     */
    @NotNull
    private final FacesManager facesManager;

    /**
     * Creates a new instance.
     * @param facesManager the faces manager to query
     */
    public MagicMapFacesProvider(@NotNull final FacesManager facesManager) {
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
        return facesManager.getMagicMapImageIcon(faceNum, isUnknownImage);
    }
    
    public ImageIcon getBlockedImageIcon() {
      //  return facesManager.getBlockedImageIcon();
        return facesManager.getMagicMapImageIcon(0, null);
    }
    
    public ImageIcon getDark1ImageIcon() {
       // return facesManager.getDark1ImageIcon();
        return facesManager.getMagicMapImageIcon(0,null);
        
    }
    
    public ImageIcon getDark2ImageIcon() {
        // return facesManager.getDark2ImageIcon();
        return facesManager.getMagicMapImageIcon(0,null);
    }
    
    public ImageIcon getDark3ImageIcon() {
      //  return facesManager.getDark3ImageIcon();
        return facesManager.getMagicMapImageIcon(0,null);
    }
    
    public ImageIcon getFogofwarImageIcon() {
      //  return facesManager.getFogofwarImageIcon();
        return facesManager.getMagicMapImageIcon(0,null);
    }

}
