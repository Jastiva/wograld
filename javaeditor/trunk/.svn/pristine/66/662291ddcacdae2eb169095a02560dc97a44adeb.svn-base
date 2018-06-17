/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.mapfiles;

import java.io.File;
import java.util.Arrays;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.MapReaderFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Loader for pickmaps from a directory.
 * @author Andreas Kirschbaum
 */
public class Loader<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link ErrorView} for reporting errors.
     */
    @NotNull
    private final ErrorView errorView;

    /**
     * The model to add the loaded pickmaps to.
     */
    @NotNull
    private final MapFolderTree<G, A, R> mapFolderTree;

    /**
     * The {@link MapReaderFactory} for loading map files.
     */
    @NotNull
    private final MapReaderFactory<G, A> mapReaderFactory;

    /**
     * The {@link MapManager} for loading pickmaps.
     */
    @NotNull
    private final MapManager<G, A, R> pickmapManager;

    /**
     * The {@link MapViewsManager}.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * Creates a new instance.
     * @param errorView the error view for reporting error messages
     * @param mapFolderTree the model to add the loaded pickmaps to
     * @param mapReaderFactory the map reader factory to loading map files
     * @param pickmapManager the map manager for loading pickmaps
     * @param mapViewsManager the map views manager
     */
    public Loader(@NotNull final ErrorView errorView, @NotNull final MapFolderTree<G, A, R> mapFolderTree, @NotNull final MapReaderFactory<G, A> mapReaderFactory, @NotNull final MapManager<G, A, R> pickmapManager, @NotNull final MapViewsManager<G, A, R> mapViewsManager) {
        this.errorView = errorView;
        this.mapFolderTree = mapFolderTree;
        this.mapReaderFactory = mapReaderFactory;
        this.pickmapManager = pickmapManager;
        this.mapViewsManager = mapViewsManager;
    }

    /**
     * Loads all pickmap files from a directory and its sub-directories.
     */
    public void load() {
        final MapFolder<G, A, R> mapFolder = load(null, "");
        if (mapFolder != null) {
            mapFolderTree.setActiveMapFolder(mapFolder);
        }
    }

    /**
     * Loads all pickmap files from a directory and its sub-directories.
     * @param parent the parent folder, or <code>null</code>
     * @param folderName the folder name of <code>dir</code>
     * @return the default <code>Folder</code>, or <code>null</code> if this
     *         directory is empty
     */
    @Nullable
    private MapFolder<G, A, R> load(@Nullable final MapFolder<G, A, R> parent, @NotNull final String folderName) {
        final File baseDir = mapFolderTree.getBaseDir();
        final File dir = parent == null ? baseDir : new File(parent.getDir(), folderName);
        final ErrorViewCollector errorViewCollector = new ErrorViewCollector(errorView, dir);
        final File[] files = dir.listFiles();
        if (files == null) {
            errorViewCollector.addWarning(ErrorViewCategory.PICKMAPS_DIR_INVALID);
            return null;
        }

        final MapFolder<G, A, R> mapFolder;
        try {
            mapFolder = new MapFolder<G, A, R>(parent, folderName, baseDir, mapViewsManager);
        } catch (final InvalidNameException ex) {
            errorViewCollector.addWarning(ErrorViewCategory.PICKMAPS_DIR_INVALID, "ignoring pickmap folder with invalid name: " + ex.getMessage());
            return null;
        }
        try {
            mapFolderTree.addMapFolder(mapFolder);
        } catch (final DuplicateMapFolderException ex) {
            errorViewCollector.addWarning(ErrorViewCategory.PICKMAPS_DIR_INVALID, "cannot add folder: " + ex.getMessage());
            return null;
        }

        Arrays.sort(files);
        MapFolder<G, A, R> result = null;
        for (final File file : files) {
            if (file.isFile()) {
                try {
                    mapFolder.addPickmap(file.getName(), mapReaderFactory, pickmapManager);
                } catch (final InvalidNameException ex) {
                    errorViewCollector.addWarning(ErrorViewCategory.PICKMAPS_FILE_INVALID, "ignoring pickmap with invalid name: " + ex.getMessage());
                }
            } else if (file.isDirectory()) {
                if (!file.getName().startsWith(".")) {
                    final MapFolder<G, A, R> tmp = load(mapFolder, file.getName());
                    if (result == null && tmp != null && tmp.getPickmaps() > 0) {
                        result = tmp;
                    }
                }
            }
        }

        return mapFolder.getPickmaps() > 0 ? mapFolder : result;
    }

}
