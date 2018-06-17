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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of FaceProvider which reads images from the arch directory.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author serpentshard
 * @todo Move scanning for face files to this class, so the faces can be easily
 * reloaded while the application is running.
 * 
 */
public class ArchFaceProvider extends AbstractFaceProvider {

    /**
     * The icon filename. The value is a String with the full reachable path of
     * the png file.
     */
    private final Map<String, String> files = new HashMap<String, String>();
    
    // names of extended images for wograld
    private final Map<String, String> files2 = new HashMap<String, String>();

    /**
     * Returns the number of faces.
     * @return the number of faces
     */
    public int size() {
        return files.size();
    }

    /**
     * Report position of a face for loading it later.
     * @param faceName face name to get image for, excluding path and ending
     * @param fileName filename with full reachable path of the png file (maybe
     * relative but must be loadable).
     */
    public void addInfo(@NotNull final String faceName, @NotNull final String fileName) {
        files.put(faceName, fileName);
    }
    
    public void addInfo2(@NotNull final String faceName, @NotNull final String fileName) {
        files2.put(faceName, fileName);
        // names of extended images for wograld
    }

    /**
     * Get the filename for a face.
     * @param faceName face name to get filename for
     * @return filename for <var>faceName</var> or <code>null</code> if no face
     */
    @Nullable
    public String getFilename(@NotNull final String faceName) {
        return files.get(faceName);
    }
    
     public String getFilename2(@NotNull final String faceName) {
        return files2.get(faceName);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    protected ImageIcon createImage(@NotNull final String faceName) {
        final String filename = files.get(faceName);
        if (filename == null) {
            return null;
        }

        final File file = new File(filename);
        final long length = file.length();
        if (length <= 0L || length >= (long) Integer.MAX_VALUE) {
            return null;
        }

        // It has been checked that the value will fit into an int.
        //noinspection NumericCastThatLosesPrecision
        final int intLength = (int) length;
        final byte[] buf = new byte[intLength];
        try {
            final FileInputStream fis = new FileInputStream(file);
            try {
                if (fis.read(buf) != intLength) {
                    return null;
                }
            } finally {
                fis.close();
            }
        } catch (final IOException ex) {
            return null;
        }

        return new ImageIcon(buf);
    }
    
    protected ImageIcon createImage2(@NotNull final String faceName) {
        final String filename = files2.get(faceName);
        if (filename == null) {
            return null;
        }

        final File file = new File(filename);
        final long length = file.length();
        if (length <= 0L || length >= (long) Integer.MAX_VALUE) {
            return null;
        }

        // It has been checked that the value will fit into an int.
        //noinspection NumericCastThatLosesPrecision
        final int intLength = (int) length;
        final byte[] buf = new byte[intLength];
        try {
            final FileInputStream fis = new FileInputStream(file);
            try {
                if (fis.read(buf) != intLength) {
                    return null;
                }
            } finally {
                fis.close();
            }
        } catch (final IOException ex) {
            return null;
        }

        return new ImageIcon(buf);
    }
    
    

}
