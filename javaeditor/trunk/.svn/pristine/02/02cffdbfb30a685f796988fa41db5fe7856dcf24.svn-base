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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.MapReaderFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Model class representing a folder of {@link MapFile MapFiles}. A folder has a
 * name and a base directory. The name is a unique identifier in the model; the
 * base directory is the directory where all contained pickmaps are stored.
 * @author Andreas Kirschbaum
 */
public class MapFolder<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterable<MapFile<G, A, R>> {

    /**
     * The {@link Pattern} that matches valid map folder names.
     */
    @NotNull
    private static final Pattern PATTERN_VALID_MAP_FOLDER_NAME = Pattern.compile("[-a-zA-Z_+ 0-9,]+");

    /**
     * The registered event listeners.
     */
    private final EventListenerList2<MapFolderListener<G, A, R>> listeners = new EventListenerList2<MapFolderListener<G, A, R>>(MapFolderListener.class);

    /**
     * The parent folder, or <code>null</code> if it is a top-level folder.
     */
    @Nullable
    private final MapFolder<G, A, R> parent;

    /**
     * The synchronization object for accesses to {@link #mapFiles}.
     */
    @NotNull
    private final Object sync = new Object();

    /**
     * The folder's name.
     */
    @NotNull
    private final String name;

    /**
     * The base directory for folder file names.
     */
    @NotNull
    private final File baseDir;

    /**
     * The {@link MapViewsManager}.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The pickmaps of this folder.
     */
    private final List<MapFile<G, A, R>> mapFiles = new ArrayList<MapFile<G, A, R>>();

    /**
     * Creates a new instance.
     * @param parent the parent folder, or <code>null</code> if it is a
     * top-level folder
     * @param name the name
     * @param baseDir the base directory for folder file names
     * @param mapViewsManager the map views
     * @throws InvalidNameException if <code>name</code> is not valid
     */
    public MapFolder(@Nullable final MapFolder<G, A, R> parent, @NotNull final String name, @NotNull final File baseDir, @NotNull final MapViewsManager<G, A, R> mapViewsManager) throws InvalidNameException {
        this.baseDir = baseDir;
        this.mapViewsManager = mapViewsManager;
        if (parent == null ? name.length() > 0 : !PATTERN_VALID_MAP_FOLDER_NAME.matcher(name).matches()) {
            throw new InvalidNameException(name);
        }

        this.parent = parent;
        this.name = name;
    }

    /**
     * Returns the parent folder.
     * @return the parent folder, or <code>null</code> if this folder is a
     *         top-level folder
     */
    @Nullable
    public MapFolder<G, A, R> getParent() {
        return parent;
    }

    /**
     * Returns the name.
     * @return the name
     */
    @NotNull
    public String getName() {
        if (parent == null) {
            return "*default*";
        }

        return parent.parent == null ? name : parent.getName() + "/" + name;
    }

    /**
     * Returns the base directory.
     * @return the base directory
     */
    @NotNull
    public File getDir() {
        return parent != null ? new File(parent.getDir(), name) : baseDir;
    }

    /**
     * Adds a new {@link MapFile} to this folder.
     * @param name the map file name
     * @param mapReaderFactory the map reader factory instance
     * @param pickmapManager the map manager for loading pickmaps
     * @return the newly created pickmap
     * @throws InvalidNameException if the pickmap name is invalid
     */
    public MapFile<G, A, R> addPickmap(@NotNull final String name, @NotNull final MapReaderFactory<G, A> mapReaderFactory, @NotNull final MapManager<G, A, R> pickmapManager) throws InvalidNameException {
        final MapFile<G, A, R> mapFile;
        synchronized (sync) {
            mapFile = new MapFile<G, A, R>(this, name, mapReaderFactory, pickmapManager, mapViewsManager);
            mapFiles.add(mapFile);
        }
        for (final MapFolderListener<G, A, R> listenerMap : listeners.getListeners()) {
            listenerMap.pickmapAdded(mapFile);
        }
        return mapFile;
    }

    /**
     * Removes a {@link MapFile} from this folder.
     * @param mapFile the pickmap to remove
     * @param deleteFile if set, also remove the map files
     */
    public void removePickmap(@NotNull final MapFile<G, A, R> mapFile, final boolean deleteFile) {
        synchronized (sync) {
            final int index = mapFiles.indexOf(mapFile);
            if (index == -1) {
                throw new IllegalArgumentException();
            }
            if (deleteFile) {
                final File file = mapFile.getFile();
                file.delete();
            }
            mapFiles.remove(index);
        }
        for (final MapFolderListener<G, A, R> listenerMap : listeners.getListeners()) {
            listenerMap.pickmapRemoved(mapFile);
        }
        mapFile.freePickmap();
    }

    /**
     * Removes all {@link MapFile} from this folder.
     * @param deleteFile if set, also remove the map file
     */
    public void removeAllPickmaps(final boolean deleteFile) {
        while (true) {
            final MapFile<G, A, R> mapFile;
            synchronized (sync) {
                if (mapFiles.isEmpty()) {
                    break;
                }
                mapFile = mapFiles.get(mapFiles.size() - 1);
            }
            removePickmap(mapFile, deleteFile);
        }
    }

    /**
     * Returns an {@link Iterator} returning all pickmaps of this folder in
     * arbitrary order.
     * @return the iterator
     */
    @Override
    public Iterator<MapFile<G, A, R>> iterator() {
        return Collections.unmodifiableList(mapFiles).iterator();
    }

    /**
     * Returns the number of pickmaps in this folder.
     * @return the number of pickmaps
     */
    public int getPickmaps() {
        return mapFiles.size();
    }

    /**
     * Adds a {@link MapFolderListener} to be notified of events.
     * @param listenerMap the listener to add
     */
    public void addFolderListener(@NotNull final MapFolderListener<G, A, R> listenerMap) {
        listeners.add(listenerMap);
    }

    /**
     * Removes a {@link MapFolderListener} to be notified of events.
     * @param listenerMap the listener to remove
     */
    public void removeFolderListener(@NotNull final MapFolderListener<G, A, R> listenerMap) {
        listeners.remove(listenerMap);
    }

    /**
     * Returns all unsaved {@link MapFile MapFiles} in this folder.
     * @param unsavedPickmaps the collection to add the unsaved pickmaps to
     */
    public void getUnsavedPickmaps(@NotNull final Collection<MapControl<G, A, R>> unsavedPickmaps) {
        for (final MapFile<G, A, R> mapFile : mapFiles) {
            mapFile.getUnsavedPickmaps(unsavedPickmaps);
        }
    }

    /**
     * Notifies all listeners about a reverted pickmap.
     * @param mapFile the pickmap that was reverted
     * @param oldPickmap the map control of the pickmap before reverting
     */
    public void firePickmapReverted(@NotNull final MapFile<G, A, R> mapFile, @NotNull final MapControl<G, A, R> oldPickmap) {
        for (final MapFolderListener<G, A, R> listenerMap : listeners.getListeners()) {
            listenerMap.pickmapReverted(mapFile, oldPickmap);
        }
    }

}
