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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Stores all known {@link MapFile MapFiles}. The map files are grouped into
 * {@link MapFolder MapFolders}. One of these folder can be the "active folder";
 * this is the folder the GUI operates on.
 * @author Andreas Kirschbaum
 */
public class MapFolderTree<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterable<MapFolder<G, A, R>> {

    /**
     * The registered event listeners.
     */
    private final EventListenerList2<MapFolderTreeListener<G, A, R>> listeners = new EventListenerList2<MapFolderTreeListener<G, A, R>>(MapFolderTreeListener.class);

    /**
     * The base directory for creating new map folders.
     */
    @NotNull
    private final File baseDir;

    /**
     * The active map folder.
     */
    @Nullable
    private MapFolder<G, A, R> activeMapFolder;

    /**
     * The folders. Maps folder name to folder instance.
     */
    private final TreeMap<String, MapFolder<G, A, R>> mapFolders = new TreeMap<String, MapFolder<G, A, R>>();

    /**
     * Creates a new instance.
     * @param baseDir the base directory for creating new map folders
     */
    public MapFolderTree(@NotNull final File baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * Returns the base directory for creating new map folders.
     * @return the base directory for creating new map folders
     */
    @NotNull
    public File getBaseDir() {
        return baseDir;
    }

    /**
     * Adds a map folder to this model. The added map folder's name must be
     * different from all existing map folder names.
     * @param mapFolder the map folder to add
     * @throws DuplicateMapFolderException if the map folder's name is not
     * unique
     */
    public synchronized void addMapFolder(@NotNull final MapFolder<G, A, R> mapFolder) throws DuplicateMapFolderException {
        final MapFolder<G, A, R> parent = mapFolder.getParent();
        if (parent != null && !mapFolders.containsKey(parent.getName())) {
            throw new IllegalArgumentException();
        }
        final String folderName = mapFolder.getName();
        if (mapFolders.containsKey(folderName)) {
            throw new DuplicateMapFolderException(folderName);
        }
        mapFolders.put(folderName, mapFolder);
        for (final MapFolderTreeListener<G, A, R> listener : listeners.getListeners()) {
            listener.folderAdded(mapFolder);
        }
    }

    /**
     * Removes a map folder from this model. If the active map folder is
     * removed, the next or previous map folder becomes the active folder.
     * @param mapFolder the map folder to remove
     * @param deleteFile if set, also remove the map files
     * @throws MapFolderNotEmptyException if the map folder contains
     * sub-folders
     */
    public synchronized void removeMapFolder(@NotNull final MapFolder<G, A, R> mapFolder, final boolean deleteFile) throws MapFolderNotEmptyException {
        if (mapFolders.get(mapFolder.getName()) != mapFolder) {
            throw new IllegalArgumentException();
        }
        for (final MapFolder<G, A, R> child : mapFolders.values()) {
            if (child.getParent() == mapFolder) {
                throw new MapFolderNotEmptyException(mapFolder.getName());
            }
        }
        mapFolder.removeAllPickmaps(true);
        if (deleteFile) {
            mapFolder.getDir().delete();
        }
        if (mapFolder == activeMapFolder) {
            final Map.Entry<String, MapFolder<G, A, R>> higherEntry = mapFolders.higherEntry(mapFolder.getName());
            if (higherEntry != null) {
                activeMapFolder = higherEntry.getValue();
            } else {
                final Map.Entry<String, MapFolder<G, A, R>> lowerEntry = mapFolders.lowerEntry(mapFolder.getName());
                if (lowerEntry != null) {
                    activeMapFolder = lowerEntry.getValue();
                } else {
                    activeMapFolder = null;
                }
            }
            fireActiveMapFolderChanged();
        }
        mapFolders.remove(mapFolder.getName());
        for (final MapFolderTreeListener<G, A, R> listener : listeners.getListeners()) {
            listener.folderRemoved(mapFolder);
        }
    }

    /**
     * Returns the active map folder.
     * @return the active map folder or <code>null</code> if no folder is
     *         active
     */
    @Nullable
    public MapFolder<G, A, R> getActiveMapFolder() {
        return activeMapFolder;
    }

    /**
     * Sets the active map folder. The passed map folder must be part of this
     * model.
     * @param mapFolder the active map folder or <code>null</code> if no folder
     * should be active
     */
    @SuppressWarnings("NullableProblems")
    public synchronized void setActiveMapFolder(@NotNull final MapFolder<G, A, R> mapFolder) {
        if (activeMapFolder == mapFolder) {
            return;
        }

        activeMapFolder = mapFolder;
        fireActiveMapFolderChanged();
    }

    /**
     * Notifies all listeners that the active folder has changed.
     */
    private void fireActiveMapFolderChanged() {
        for (final MapFolderTreeListener<G, A, R> listener : listeners.getListeners()) {
            listener.activeMapFolderChanged(activeMapFolder);
        }
    }

    /**
     * Returns an {@link Iterator} returning all map folders. The folders are
     * sorted by folder name.
     */
    @Override
    public Iterator<MapFolder<G, A, R>> iterator() {
        return Collections.unmodifiableCollection(mapFolders.values()).iterator();
    }

    /**
     * Adds a {@link MapFolderTreeListener} to be informed about changes.
     * @param listener the listener to add
     */
    public void addModelListener(@NotNull final MapFolderTreeListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a {@link MapFolderTreeListener} to be informed about changes.
     * @param listener the listener to remove
     */
    public void removeModelListener(@NotNull final MapFolderTreeListener<G, A, R> listener) {
        listeners.remove(listener);
    }

    /**
     * Returns all unsaved map controls of this model.
     * @param unsavedMaps the collection to add the unsaved pickmaps to
     */
    public void getUnsavedPickmaps(@NotNull final Collection<MapControl<G, A, R>> unsavedMaps) {
        for (final MapFolder<G, A, R> mapFolder : mapFolders.values()) {
            mapFolder.getUnsavedPickmaps(unsavedMaps);
        }
    }

}
