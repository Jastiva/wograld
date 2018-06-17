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
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Pattern;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.MapReader;
import net.sf.gridarta.model.io.MapReaderFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Maintains the state of a pickmap file. A <code>Pickmap</code> is part of a
 * {@link MapFolder} which is part of {@link MapFolderTree}. A pickmap has an
 * underlying file from which a {@link MapControl} instance may be created.
 * @author Andreas Kirschbaum
 */
public class MapFile<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link Pattern} that matches valid pickmap names.
     */
    @NotNull
    private static final Pattern PATTERN_VALID_PICKMAP_NAME = Pattern.compile("[-a-zA-Z_+ 0-9,]+");

    /**
     * The folder this pickmap is part of.
     */
    @NotNull
    private final MapFolder<G, A, R> mapFolder;

    /**
     * The underlying map file name.
     */
    @NotNull
    private final String name;

    /**
     * The {@link MapManager} for creating new pickmaps.
     */
    @NotNull
    private final MapManager<G, A, R> pickmapManager;

    /**
     * The {@link MapReaderFactory} to use.
     */
    @NotNull
    private final MapReaderFactory<G, A> mapReaderFactory;

    /**
     * The {@link MapControl} instance representing the map file, or
     * <code>null</code> if the map file is not loaded.
     */
    @Nullable
    private MapControl<G, A, R> pickmap;

    /**
     * The {@link MapView} or {@link #pickmap}, or <code>null</code> if
     * <code>pickmap == null</code>.
     */
    @Nullable
    private MapView<G, A, R> pickmapView;

    /**
     * The {@link MapViewsManager}.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The synchronization object for accessed to {@link #pickmap} and {@link
     * #pickmapView}.
     */
    @NotNull
    private final Object sync = new Object();

    /**
     * Creates a new instance.
     * @param mapFolder the folder ths pickmap is part of
     * @param name the underlying map file name
     * @param mapReaderFactory the map reader factory to use
     * @param pickmapManager the map manager for creating pickmaps
     * @param mapViewsManager the map views
     * @throws InvalidNameException if <code>name</code> is not valid
     */
    public MapFile(@NotNull final MapFolder<G, A, R> mapFolder, @NotNull final String name, @NotNull final MapReaderFactory<G, A> mapReaderFactory, @NotNull final MapManager<G, A, R> pickmapManager, @NotNull final MapViewsManager<G, A, R> mapViewsManager) throws InvalidNameException {
        if (!isValidPickmapName(name)) {
            throw new InvalidNameException(name);
        }

        this.mapFolder = mapFolder;
        this.name = name;
        this.pickmapManager = pickmapManager;
        this.mapReaderFactory = mapReaderFactory;
        this.mapViewsManager = mapViewsManager;
    }

    /**
     * Returns whether a pickmap name is valid.
     * @param name the name to check
     * @return whether the name is valid
     */
    public static boolean isValidPickmapName(@NotNull final CharSequence name) {
        return PATTERN_VALID_PICKMAP_NAME.matcher(name).matches();
    }

    /**
     * Returns the underlying map file.
     * @return the map file
     */
    @NotNull
    public File getFile() {
        return new File(mapFolder.getDir(), name);
    }

    /**
     * Returns the {@link MapControl} representing this pickmap. Returns
     * <code>null</code> unless {@link #loadPickmap()} was successfully called.
     * @return the <code>MapControl</code> instance, or <code>null if the map
     *         file has not been loaded
     */
    @Nullable
    public MapControl<G, A, R> getPickmap() {
        synchronized (sync) {
            return pickmap;
        }
    }

    /**
     * Returns the {@link MapView} instance for this pickmap. Returns
     * <code>null</code> unless {@link #loadPickmap()} was successfully called.
     * @return the <code>MapView</code> instance, or <code>null if the map file
     *         has not been loaded
     */
    @Nullable
    public MapView<G, A, R> getMapView() {
        synchronized (sync) {
            return pickmapView;
        }
    }

    /**
     * Loads the pickmap from the underlying map file.
     * @throws IOException if the map file cannot be loaded
     */
    public void loadPickmap() throws IOException {
        synchronized (sync) {
            if (pickmap != null) {
                return;
            }

            final File file = getFile();
            final MapReader<G, A> decoder = mapReaderFactory.newMapReader(file);
            pickmap = pickmapManager.newMap(decoder.getGameObjects(), decoder.getMapArchObject(), file, true);
            pickmapView = mapViewsManager.newMapView(pickmap, null, null);
        }
    }

    /**
     * Unloads the map file. Undoes the effect of {@link #loadPickmap()}.
     */
    public void freePickmap() {
        synchronized (sync) {
            if (pickmap != null) {
                assert pickmapView != null;
                mapViewsManager.closeView(pickmapView);
                assert pickmap != null;
                pickmapManager.release(pickmap);
                pickmap = null;
            }
        }
    }

    /**
     * Returns this pickmap if it has been loaded and is modified.
     * @param unsavedPickmaps the collection to add <code>this</code> to if
     * modified
     */
    public void getUnsavedPickmaps(@NotNull final Collection<MapControl<G, A, R>> unsavedPickmaps) {
        synchronized (sync) {
            if (pickmap != null && pickmap.getMapModel().isModified()) {
                unsavedPickmaps.add(pickmap);
            }
        }
    }

    /**
     * Removes this pickmap from its folder.
     * @param deleteFile if set, delete the map file as well
     */
    public void remove(final boolean deleteFile) {
        mapFolder.removePickmap(this, deleteFile);
    }

    /**
     * Saves this pickmap. Does nothing if the pickmap is not loaded or not
     * modified.
     * @throws IOException if saving fails
     */
    public void save() throws IOException {
        final MapControl<G, A, R> oldPickmap = getPickmap();
        if (oldPickmap != null && oldPickmap.getMapModel().isModified()) {
            oldPickmap.save();
        }
    }

    /**
     * Reverts this pickmap to its underlying map file. Does nothing if the
     * pickmap is not loaded.
     * @throws IOException if the pickmap cannot be loaded
     */
    public void revert() throws IOException {
        final MapControl<G, A, R> oldPickmap = getPickmap();
        if (oldPickmap != null) {
            freePickmap();
            loadPickmap();
            mapFolder.firePickmapReverted(this, oldPickmap);
        }
    }

}
