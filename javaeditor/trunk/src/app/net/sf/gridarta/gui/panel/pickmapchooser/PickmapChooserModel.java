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

package net.sf.gridarta.gui.panel.pickmapchooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.gridarta.gui.mapfiles.MapFile;
import net.sf.gridarta.gui.mapfiles.MapFileNameComparator;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Maintains loaded {@link MapFile} instances.
 * @author Andreas Kirschbaum
 */
public class PickmapChooserModel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterable<MapFile<G, A, R>> {

    /**
     * All open pickmaps.
     */
    @NotNull
    private final List<MapFile<G, A, R>> mapFiles = new ArrayList<MapFile<G, A, R>>();

    /**
     * The listeners to notify.
     */
    @NotNull
    private final Collection<PickmapChooserModelListener<G, A, R>> listeners = new LinkedList<PickmapChooserModelListener<G, A, R>>();

    /**
     * Maps {@link MapControl} instance to attached {@link MapModelListener}.
     */
    @NotNull
    private final Map<MapControl<G, A, R>, MapModelListener<G, A, R>> mapModelListeners = new HashMap<MapControl<G, A, R>, MapModelListener<G, A, R>>();

    /**
     * Adds a map file.
     * @param mapFile the map file to add
     * @return the insertion index
     */
    public int addMapFile(@NotNull final MapFile<G, A, R> mapFile) {
        final int tmp = Collections.binarySearch(mapFiles, mapFile, MapFileNameComparator.INSTANCE);
        final int index = tmp >= 0 ? tmp : -tmp - 1;
        mapFiles.add(index, mapFile);
        addMapModelListener(mapFile.getPickmap());
        return index;
    }

    /**
     * Removes a map file.
     * @param mapFile the map file to remove
     * @return the removed index or <code>-1</code> if the map file does not
     *         exist
     */
    public int removeMapFile(@NotNull final MapFile<G, A, R> mapFile) {
        final int index = mapFiles.indexOf(mapFile);
        if (index < 0) {
            return -1;             // might have been a pickmap that could not be loaded
        }
        removeMapModelListener(mapFile.getPickmap());
        mapFiles.remove(index);
        return index;
    }

    /**
     * Reverts a map file.
     * @param oldPickmap the old pickmap control
     * @param mapFile the map file to revert
     * @return the reverted index or <code>-1</code> if the map file does not
     *         exist
     */
    public int revertMapFile(@NotNull final MapControl<G, A, R> oldPickmap, @NotNull final MapFile<G, A, R> mapFile) {
        final int index = mapFiles.indexOf(mapFile);
        if (index < 0) {
            return -1;             // might have been a pickmap that could not be loaded
        }
        removeMapModelListener(oldPickmap);
        addMapModelListener(mapFile.getPickmap());
        firePickmapReverted(mapFile);
        return index;
    }

    /**
     * Returns the pickmap by file name.
     * @param file the file name
     * @return the pickmap, or <code>null</code> if the file is unknown
     */
    @Nullable
    public MapFile<G, A, R> getPickmap(@NotNull final File file) {
        for (final MapFile<G, A, R> mapFile : mapFiles) {
            if (mapFile.getFile().equals(file)) {
                return mapFile;
            }
        }
        return null;
    }

    /**
     * Returns whether no pickmaps exist in the current folder.
     * @return whether no pickmaps exist
     */
    public boolean isEmpty() {
        return mapFiles.isEmpty();
    }

    /**
     * Returns the index of a map file.
     * @param mapFile the map file to search
     * @return the index or <code>-1</code> if the map file does not exist
     */
    public int indexOf(@NotNull final MapFile<G, A, R> mapFile) {
        return mapFiles.indexOf(mapFile);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<MapFile<G, A, R>> iterator() {
        return Collections.unmodifiableList(mapFiles).iterator();
    }

    /**
     * Returns a map file by index.
     * @param index the index
     * @return the map file
     */
    @NotNull
    public MapFile<G, A, R> get(final int index) {
        return mapFiles.get(index);
    }

    /**
     * Removes all map files.
     */
    public void clear() {
        for (final MapFile<G, A, R> mapFile : mapFiles) {
            removeMapModelListener(mapFile.getPickmap());
        }
        mapFiles.clear();
    }

    /**
     * Adds a {@link PickmapChooserModelListener} to be notified.
     * @param pickmapChooserModelListener the listener to add
     */
    public void addPickmapChooserListener(@NotNull final PickmapChooserModelListener<G, A, R> pickmapChooserModelListener) {
        listeners.add(pickmapChooserModelListener);
    }

    /**
     * Notifies all listeners that the a pickmap has been reverted.
     * @param mapFile the reverted pickmap
     */
    private void firePickmapReverted(@NotNull final MapFile<G, A, R> mapFile) {
        for (final PickmapChooserModelListener<G, A, R> listener : listeners) {
            listener.pickmapReverted(mapFile);
        }
    }

    /**
     * Notifies all listeners that the active pickmap has changes.
     * @param mapFile the active pickmap
     */
    public void fireActivePickmapChanged(@Nullable final MapFile<G, A, R> mapFile) {
        for (final PickmapChooserModelListener<G, A, R> listener : listeners) {
            listener.activePickmapChanged(mapFile);
        }
    }

    /**
     * Notifies all listeners that a pickmap has been modified.
     * @param index the index of the modified pickmap
     * @param mapFile the modified pickmap
     */
    private void firePickmapModifiedChanged(final int index, @NotNull final MapFile<G, A, R> mapFile) {
        for (final PickmapChooserModelListener<G, A, R> listener : listeners) {
            listener.pickmapModifiedChanged(index, mapFile);
        }
    }

    /**
     * Tracks a {@link MapControl} instance for changes. The map control
     * instance must not be tracked.
     * @param mapControl the map control instance
     */
    private void addMapModelListener(@NotNull final MapControl<G, A, R> mapControl) {
        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        final MapModelListener<G, A, R> mapModelListener = new PickmapChooserMapModelListener(mapModel);
        mapModel.addMapModelListener(mapModelListener);
        mapModelListeners.put(mapControl, mapModelListener);
    }

    /**
     * Stops tracking a {@link MapControl} instance for changes. The map control
     * instance must be tracked.
     * @param mapControl the map control instance
     */
    private void removeMapModelListener(@NotNull final MapControl<G, A, R> mapControl) {
        final MapModelListener<G, A, R> mapModelListener = mapModelListeners.remove(mapControl);
        assert mapModelListener != null;
        mapControl.getMapModel().removeMapModelListener(mapModelListener);
    }

    /**
     * A {@link MapModelListener} which tracks a pickmap for modifications.
     */
    private class PickmapChooserMapModelListener implements MapModelListener<G, A, R> {

        /**
         * The tracked {@link MapModel}.
         */
        @NotNull
        private final MapModel<G, A, R> mapModel;

        /**
         * Creates a new instance.
         * @param mapModel the tracked map map model
         */
        private PickmapChooserMapModelListener(@NotNull final MapModel<G, A, R> mapModel) {
            this.mapModel = mapModel;
        }

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            // ignore
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
            // ignore
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
            // ignore
        }

        @Override
        public void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors) {
            // ignore
        }

        @Override
        public void mapFileChanged(@Nullable final File oldMapFile) {
            // ignore
        }

        @Override
        public void modifiedChanged() {
            int index = 0;
            for (final MapFile<G, A, R> mapFile : mapFiles) {
                if (mapFile.getPickmap().getMapModel() == mapModel) {
                    firePickmapModifiedChanged(index, mapFile);
                    return;
                }
                index++;
            }
        }

    }

}
