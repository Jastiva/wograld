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

package net.sf.gridarta.model.mappathnormalizer;

import java.io.File;
import java.io.IOException;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.settings.GlobalSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Normalizes map path specifications into {@link File Files}.
 * @author Andreas Kirschbaum
 */
public class MapPathNormalizer {

    /**
     * The global settings instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * Creates a new instance.
     * @param globalSettings the global settings instance
     */
    public MapPathNormalizer(@NotNull final GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    /**
     * Normalizes a map path relative to a {@link MapModel}.
     * @param mapModel the map model to start from
     * @param path the destination path
     * @return the normalized destination file
     * @throws InvalidPathException if the destination file is invalid
     * @throws IOErrorException if an I/O error occurs
     * @throws RelativePathOnUnsavedMapException if the path is relative and the
     * map has not yet been saved
     * @throws SameMapException if the destination path points to the source
     * map
     */
    @NotNull
    public File normalizeMapPath(@NotNull final MapModel<?, ?, ?> mapModel, @NotNull final String path) throws InvalidPathException, IOErrorException, RelativePathOnUnsavedMapException, SameMapException {
        return normalizeMapPath(mapModel.getMapFile(), path);
    }

    /**
     * Normalizes a map path relative to a {@link File}.
     * @param mapFile the file to start from
     * @param path the destination path
     * @return the normalized destination file
     * @throws InvalidPathException if the destination file is invalid
     * @throws IOErrorException if an I/O error occurs
     * @throws RelativePathOnUnsavedMapException if the path is relative and the
     * map has not yet been saved
     * @throws SameMapException if the destination path points to the source
     * map
     */
    @NotNull
    private File normalizeMapPath(@Nullable final File mapFile, @NotNull final String path) throws InvalidPathException, IOErrorException, RelativePathOnUnsavedMapException, SameMapException {
        @NotNull final File newFile;
        if (path.startsWith(File.pathSeparator) || path.startsWith("/")) {
            // we have an absolute path:
            newFile = new File(globalSettings.getMapsDirectory().getAbsolutePath(), path.substring(1));
        } else {
            // we have a relative path:
            if (mapFile == null) {
                throw new RelativePathOnUnsavedMapException(path);
            }
            newFile = new File(mapFile.getParent(), path);
        }
        if (path.length() == 0 || (mapFile != null && newFile.equals(mapFile))) {
            throw new SameMapException();
        }

        if (!newFile.exists() || newFile.isDirectory()) {
            // The path is wrong
            // TODO: Differ between non-existing paths, wrongly formatted paths and directories - give more info to the user.
            throw new InvalidPathException(newFile);
        }

        // its important to force the canonical file here or the
        // file path is added every time we use a ../ or a ./ .
        // This results in giant file names like "xx/../yy/../xx/../yy/.."
        // and after some times in buffer overflows.
        final File canonicalNewFile;
        try {
            canonicalNewFile = newFile.getCanonicalFile();
        } catch (final IOException ex) {
            throw new IOErrorException(newFile, ex);
        }

        return canonicalNewFile;
    }

}
