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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of FaceProvider which reads images from the collected PNG
 * archive.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author serpentshard
 * @todo Construction of this class should succeed even in case the face file is
 * unavailable.
 * @todo Move parsing of the face files to this class, so the faces can be
 * easily reloaded while the application is running.
 */
@SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
public class CollectedFaceProvider extends AbstractFaceProvider {

    /**
     * The icon position and size are stored here. The value is a long with the
     * high-int being the position and the low-int being the size.
     */
    private final Map<String, Long> positions = new HashMap<String, Long>();

    // wograld, for reading the topimg
    private final Map<String, Long> positions2 = new HashMap<String, Long>();
    
    /**
     * The file to read from.
     */
    @NotNull
    private final RandomAccessFile file;
    
    // wograld, for reading the topimg
    private RandomAccessFile file2 = null;
    

    /**
     * Creates a new instance.
     * @param file file where icons are found
     * @throws FileNotFoundException in case the File <var>file</var> wasn't
     * found
     */
    public CollectedFaceProvider(final File file) throws FileNotFoundException {
        this.file = new RandomAccessFile(file, "r");
    }

    /**
     * Creates a new instance.
     * @param fileName name of file where icons are found
     * @throws FileNotFoundException in case the File with name
     * <var>fileName</var> wasn't found
     */
    public CollectedFaceProvider(final String fileName) throws FileNotFoundException {
        file = new RandomAccessFile(fileName, "r");
    }
    
    public void assignSecondFile(final File file) throws FileNotFoundException {
        this.file2 = new RandomAccessFile(file, "r");
    }

    /**
     * Report position and size of a face for loading it later.
     * @param faceName face name to get image for, excluding path and ending
     * @param pos position of image
     * @param size Size of image
     */
    public void addInfo(@NotNull final String faceName, final int pos, final int size) {
        positions.put(faceName, (long) pos << 32 | (long) size);
    }
    
    public void addInfo2(@NotNull final String faceName, final int pos, final int size) {
        positions2.put(faceName, (long) pos << 32 | (long) size);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    protected ImageIcon createImage(@NotNull final String faceName) {
        final Long position = positions.get(faceName);
        if (position == null) {
            return null;
        }
        final long posI = position;
        final byte[] buf;
        try {
            file.seek(posI >> 32);
            buf = new byte[(int) posI];
            file.readFully(buf);
        } catch (final IOException e) {
            return null;
        }
        return new ImageIcon(buf);
    }
    
    @Nullable
    @Override
    protected ImageIcon createImage2(@NotNull final String faceName) {
        final Long position = positions2.get(faceName);
        if (position == null) {
            return null;
        }
        final long posI = position;
        final byte[] buf;
        try {
            file2.seek(posI >> 32);
            buf = new byte[(int) posI];
            file2.readFully(buf);
        } catch (final IOException e) {
            return null;
        }
        return new ImageIcon(buf);
    }

}
