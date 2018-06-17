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

package net.sf.gridarta.model.mapmanager;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjectProvidersListener;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.MapReader;
import net.sf.gridarta.model.io.MapReaderFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.EventListenerList2;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for {@link MapManager} implementations.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractMapManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MapManager<G, A, R> {

    /**
     * The maximum number of deleted game objects to report.
     */
    private static final int DELETED_OBJECTS_TO_REPORT = 10;

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(AbstractMapManager.class);

    /**
     * The main control.
     */
    @NotNull
    private FileControl<G, A, R> fileControl;

    /**
     * The global settings instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The gridarta objects factory instance.
     */
    @NotNull
    private final MapReaderFactory<G, A> mapReaderFactory;

    /**
     * All open maps.
     */
    private final List<MapControl<G, A, R>> mapControls = new CopyOnWriteArrayList<MapControl<G, A, R>>();

    /**
     * The MapManagerListeners to inform of changes.
     */
    private final EventListenerList2<MapManagerListener<G, A, R>> listenerList = new EventListenerList2<MapManagerListener<G, A, R>>(MapManagerListener.class);

    /**
     * The current top map we are working with.
     */
    @Nullable
    private MapControl<G, A, R> currentMapControl;

    /**
     * Create a new map manager.
     * @param mapReaderFactory the map reader factory instance
     * @param globalSettings the global settings instance
     * @param faceObjectProviders the face object providers
     */
    protected AbstractMapManager(@NotNull final MapReaderFactory<G, A> mapReaderFactory, @NotNull final GlobalSettings globalSettings, @NotNull final FaceObjectProviders faceObjectProviders) {
        this.globalSettings = globalSettings;
        this.mapReaderFactory = mapReaderFactory;

        final FaceObjectProvidersListener faceObjectProvidersListener = new FaceObjectProvidersListener() {

            @Override
            public void facesReloaded() {
                for (final MapControl<G, A, R> mapControl : mapControls) {
                    mapControl.getMapModel().facesReloaded();
                }
            }

        };
        faceObjectProviders.addFaceObjectProvidersListener(faceObjectProvidersListener);
    }

    @Deprecated
    public void setFileControl(@NotNull final FileControl<G, A, R> fileControl) {
        this.fileControl = fileControl;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapControl<G, A, R> newMap(@Nullable final List<G> objects, @NotNull final A mapArchObject, @Nullable final File file, final boolean interactive) {
        final MapControl<G, A, R> mapControl = createMapControl(objects, mapArchObject, file);
        mapControls.add(mapControl);
        for (final MapManagerListener<G, A, R> listener : listenerList.getListeners()) {
            listener.mapCreated(mapControl, interactive);
        }
        if (interactive) {
            setCurrentMap(mapControl);
        }
        return mapControl;
    }

    /**
     * Creates a new {@link MapControl} instance.
     * @param objects the objects to insert into the new map
     * @param mapArchObject the map arch object to use for the new map
     * @param file the associated file
     * @return the new map control instance
     */
    @NotNull
    protected abstract MapControl<G, A, R> createMapControl(@Nullable final List<G> objects, @NotNull final A mapArchObject, @Nullable final File file);

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public MapControl<G, A, R> getOpenMap() {
        if (currentMapControl != null) {
            return currentMapControl;
        }

        if (!mapControls.isEmpty()) {
            return mapControls.get(mapControls.size() - 1);
        }

        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeMap(@NotNull final MapControl<G, A, R> mapControl) {
        for (final MapManagerListener<G, A, R> listener : listenerList.getListeners()) {
            listener.mapClosing(mapControl);
        }
        assert mapControl.getUseCounter() <= 0;
        mapControls.remove(mapControl);
        if (currentMapControl == mapControl) {
            setCurrentMap(mapControls.isEmpty() ? null : mapControls.get(0));
        }
        for (final MapManagerListener<G, A, R> listener : listenerList.getListeners()) {
            listener.mapClosed(mapControl);
        }
        mapControl.getMapModel().mapClosed();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapControl<G, A, R> openMapFile(@NotNull final File file, final boolean interactive) throws IOException {
        // First look whether the file isn't already open.
        // If so, return the previously loaded map.
        final File canonicalFile = getCanonicalFile(file);
        for (final MapControl<G, A, R> mapControl : mapControls) {
            final File mapFile = mapControl.getMapModel().getMapFile();
            if (mapFile != null && canonicalFile.equals(getCanonicalFile(mapFile))) {
                mapControl.acquire();
                return mapControl;
            }
        }

        final MapReader<G, A> decoder = decodeMapFile(file, interactive);
        return newMap(decoder.getGameObjects(), decoder.getMapArchObject(), file, interactive);
    }

    /**
     * Returns the canonical file name.
     * @param file the input file
     * @return the canonical file or the input file if an error occurs
     */
    @NotNull
    private static File getCanonicalFile(@NotNull final File file) {
        try {
            return file.getCanonicalFile();
        } catch (final IOException ex) {
            log.warn(ACTION_BUILDER.format("logCanonIOE", ex));
            return file;
        }
    }

    /**
     * Load a map file.
     * @param file the map file to load
     * @param interactive if set, may ask to delete undefined archetypes; if
     * unset, keep undefined archetypes
     * @return the decoder objects containing the loaded map data, or
     *         <code>null</code> if loading failed
     * @throws IOException if an I/O error occurs
     */
    @NotNull
    private MapReader<G, A> decodeMapFile(@NotNull final File file, final boolean interactive) throws IOException {
        final MapReader<G, A> decoder;
        //noinspection ErrorNotRethrown
        try {
            decoder = mapReaderFactory.newMapReader(file);
        } catch (final OutOfMemoryError ex) {
            throw new IOException("out of memory", ex);
        }

        int outOfMapBoundsDeleted = 0;
        final StringBuilder outOfMapBoundsObjects = new StringBuilder();
        final int mapWidth = decoder.getMapArchObject().getMapSize().getWidth();
        final int mapHeight = decoder.getMapArchObject().getMapSize().getHeight();
        final Iterator<G> it = decoder.getGameObjects().iterator();
        while (it.hasNext()) {
            final GameObject<G, A, R> gameObject = it.next();
            if (!gameObject.isInContainer()) {
                final int minX = gameObject.getHead().getMapX() + gameObject.getMinX();
                final int minY = gameObject.getHead().getMapY() + gameObject.getMinY();
                final int maxX = gameObject.getHead().getMapX() + gameObject.getMaxX();
                final int maxY = gameObject.getHead().getMapY() + gameObject.getMaxY();
                if (minX < 0 || minY < 0 || maxX >= mapWidth || maxY >= mapHeight) {
                    it.remove();
                    if (gameObject.isHead()) {
                        outOfMapBoundsDeleted++;
                        if (outOfMapBoundsDeleted <= DELETED_OBJECTS_TO_REPORT) {
                            outOfMapBoundsObjects.append('\n');
                            outOfMapBoundsObjects.append(gameObject.getBestName());
                            outOfMapBoundsObjects.append(" at ");
                            outOfMapBoundsObjects.append(minX).append('/').append(minY);
                            if (minX != maxX || minY != maxY) {
                                outOfMapBoundsObjects.append("..");
                                outOfMapBoundsObjects.append(maxX).append('/').append(maxY);
                            }
                        } else if (outOfMapBoundsDeleted == DELETED_OBJECTS_TO_REPORT + 1) {
                            outOfMapBoundsObjects.append("\n...");
                        }
                    }
                }
            }
        }
        if (interactive && outOfMapBoundsDeleted > 0) {
            fileControl.reportOutOfMapBoundsDeleted(file, outOfMapBoundsDeleted, outOfMapBoundsObjects);
        }

        return decoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentMap(@Nullable final MapControl<G, A, R> mapControl) {
        if (currentMapControl == mapControl) {
            return;
        }

        currentMapControl = mapControl;

        for (final MapManagerListener<G, A, R> listener : listenerList.getListeners()) {
            listener.currentMapChanged(currentMapControl);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<MapControl<G, A, R>> getOpenedMaps() {
        return Collections.unmodifiableList(mapControls);
    }

    /**
     * Returns the current top map we are working with.
     * @return the current top map we are working with, or <code>null</code> if
     *         no map is open
     */
    @Nullable
    @Override
    public MapControl<G, A, R> getCurrentMap() {
        return currentMapControl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMapManagerListener(@NotNull final MapManagerListener<G, A, R> listener) {
        listenerList.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMapManagerListener(@NotNull final MapManagerListener<G, A, R> listener) {
        listenerList.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revert(@NotNull final MapControl<G, A, R> mapControl) throws IOException {
        final File mapFile = mapControl.getMapModel().getMapFile();
        if (mapFile == null) {
            return;
        }

        final MapReader<G, A> decoder = decodeMapFile(mapFile, false);
        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        final A mapArchObject = mapModel.getMapArchObject();
        mapModel.beginTransaction("revert");
        try {
            mapArchObject.setMapSize(decoder.getMapArchObject().getMapSize());
            mapArchObject.beginTransaction();
            try {
                mapArchObject.setState(decoder.getMapArchObject());
            } finally {
                mapArchObject.endTransaction();
            }
            mapModel.clearMap();
            mapModel.addObjectListToMap(decoder.getGameObjects());
        } finally {
            mapModel.endTransaction();
        }
        mapModel.resetModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getLocalMapDir() {
        final MapControl<G, A, R> mapControl = currentMapControl;
        if (mapControl == null) {
            return globalSettings.getMapsDirectory();
        }

        final File mapFile = mapControl.getMapModel().getMapFile();
        if (mapFile == null) {
            return globalSettings.getMapsDirectory();
        }

        return mapFile.getParentFile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(@NotNull final MapControl<G, A, R> mapControl) {
        mapControl.release();
        if (mapControl.getUseCounter() <= 0) {
            closeMap(mapControl);
        }
    }

}
