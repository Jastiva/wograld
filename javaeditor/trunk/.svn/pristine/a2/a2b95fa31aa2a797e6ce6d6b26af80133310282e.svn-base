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

package net.sf.gridarta.gui.delayedmapmodel;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.SwingUtilities;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectListener;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.EventListenerList2;
import net.sf.gridarta.utils.Exiter;
import net.sf.gridarta.utils.ExiterListener;
import net.sf.gridarta.utils.Size2D;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides support for delayed notification of {@link MapModel} changes.
 * @author Andreas Kirschbaum
 */
public class DelayedMapModelListenerManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(DelayedMapModelListenerManager.class);

    /**
     * Notification delay in milliseconds. All listeners will be notified this
     * delay after the last map change has happened.
     */
    private static final long DELAY = 500L;

    /**
     * The {@link MapManager} to track.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * All known {@link MapModel} instances. Maps map model instance to attached
     * {@link MapModelListenerImpl} instance.
     */
    @NotNull
    private final Map<MapModel<G, A, R>, MapModelListenerImpl> mapModelListeners = new IdentityHashMap<MapModel<G, A, R>, MapModelListenerImpl>();

    /**
     * All known {@link MapModel} instances. Maps map model instance to attached
     * {@link MapArchObjectListenerImpl} instance.
     */
    @NotNull
    private final Map<MapModel<G, A, R>, MapArchObjectListenerImpl> mapArchObjectListeners = new IdentityHashMap<MapModel<G, A, R>, MapArchObjectListenerImpl>();

    /**
     * The timestamp when to deliver scheduled map models. Set to zero when not
     * active.
     */
    private long timeout;

    /**
     * The object used for synchronization.
     */
    @NotNull
    private final Object sync = new Object();

    /**
     * The {@link MapModel MapModels} having pending changes. Accesses are
     * synchronized with {@link #sync}.
     */
    @NotNull
    private final Map<MapModel<G, A, R>, Void> scheduledMapModels = new IdentityHashMap<MapModel<G, A, R>, Void>();

    /**
     * The listeners to notify.
     */
    @NotNull
    private final EventListenerList2<DelayedMapModelListener<G, A, R>> listeners = new EventListenerList2<DelayedMapModelListener<G, A, R>>(DelayedMapModelListener.class);

    /**
     * The {@link MapManagerListener} used to detect created and closed {@link
     * MapControl} instances.
     */
    @NotNull
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
            final MapModel<G, A, R> mapModel = mapControl.getMapModel();
            final MapModelListenerImpl mapModelListener = new MapModelListenerImpl(mapModel);
            mapModel.addMapModelListener(mapModelListener);
            mapModelListeners.put(mapModel, mapModelListener);
            final MapArchObjectListenerImpl mapArchObjectListener = new MapArchObjectListenerImpl(mapModel);
            mapModel.getMapArchObject().addMapArchObjectListener(mapArchObjectListener);
            mapArchObjectListeners.put(mapModel, mapArchObjectListener);
            scheduleMapModel(mapModel);
        }

        @Override
        public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
            final MapModel<G, A, R> mapModel = mapControl.getMapModel();
            final MapModelListener<G, A, R> mapModelListener = mapModelListeners.remove(mapModel);
            if (mapModelListener != null) {
                mapModel.removeMapModelListener(mapModelListener);
            }
            final MapArchObjectListener mapArchObjectListener = mapArchObjectListeners.remove(mapModel);
            if (mapArchObjectListener != null) {
                mapModel.getMapArchObject().removeMapArchObjectListener(mapArchObjectListener);
            }
            synchronized (sync) {
                scheduledMapModels.remove(mapControl.getMapModel());
            }
        }

    };

    /**
     * The {@link Thread} performing map validations.
     */
    private final Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                final Map<MapModel<G, A, R>, Void> mapModels = new IdentityHashMap<MapModel<G, A, R>, Void>();
                while (!Thread.currentThread().isInterrupted()) {
                    while (true) {
                        final long now = System.currentTimeMillis();
                        synchronized (sync) {
                            if (timeout == 0L) {
                                sync.wait();
                            } else {
                                final long diff = timeout - now;
                                if (diff <= 0L) {
                                    timeout = 0L;
                                    mapModels.putAll(scheduledMapModels);
                                    scheduledMapModels.clear();
                                    break;
                                }

                                sync.wait(diff);
                            }
                        }
                    }

                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {

                            @Override
                            public void run() {
                                for (final Map.Entry<MapModel<G, A, R>, Void> e : mapModels.entrySet()) {
                                    final MapModel<G, A, R> mapModel = e.getKey();
                                    for (final DelayedMapModelListener<G, A, R> listener : listeners.getListeners()) {
                                        listener.mapModelChanged(mapModel);
                                    }
                                }
                            }
                        });
                    } catch (final InvocationTargetException ex) {
                        log.error("InvocationTargetException: " + ex.getMessage(), ex);
                    }
                    mapModels.clear();
                }
            } catch (final InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }

    });

    /**
     * Creates a new instance.
     * @param mapManager the map manager to track
     * @param exiter the exiter instance
     */
    public DelayedMapModelListenerManager(@NotNull final MapManager<G, A, R> mapManager, @NotNull final Exiter exiter) {
        this.mapManager = mapManager;
        mapManager.addMapManagerListener(mapManagerListener);

        final ExiterListener exiterListener = new ExiterListener() {

            @Override
            public void preExitNotify() {
                thread.interrupt();
                try {
                    thread.join();
                } catch (final InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    log.warn("DelayedMapModelListenerManager was interrupted");
                }
            }

            @Override
            public void appExitNotify() {
                // ignore
            }

            @Override
            public void waitExitNotify() {
                // ignore
            }

        };
        exiter.addExiterListener(exiterListener);
    }

    /**
     * Starts execution.
     */
    public void start() {
        thread.start();
        scheduleAllMapModels();
    }

    /**
     * Adds a {@link DelayedMapModelListener} to be notified.
     * @param listener the listener
     */
    public void addDelayedMapModelListener(@NotNull final DelayedMapModelListener<G, A, R> listener) {
        listeners.add(listener);
        for (final MapControl<G, A, R> mapControl : mapManager.getOpenedMaps()) {
            listener.mapModelChanged(mapControl.getMapModel());
        }
    }

    /**
     * Removes a {@link DelayedMapModelListener} to be notified.
     * @param listener the listener
     */
    public void removeDelayedMapModelListener(@NotNull final DelayedMapModelListener<G, A, R> listener) {
        listeners.remove(listener);
    }

    /**
     * Schedules a {@link MapModel} which has been changed.
     * @param mapModel the map model to schedule
     */
    public void scheduleMapModel(@NotNull final MapModel<G, A, R> mapModel) {
        final long now = System.currentTimeMillis();
        synchronized (sync) {
            scheduledMapModels.put(mapModel, null);
            timeout = now + DELAY;
            sync.notifyAll();
        }
    }

    /**
     * Schedules all {@link MapModel MapModels} as changed.
     */
    public void scheduleAllMapModels() {
        final long now = System.currentTimeMillis();
        synchronized (sync) {
            for (final MapControl<G, A, R> mapControl : mapManager.getOpenedMaps()) {
                scheduledMapModels.put(mapControl.getMapModel(), null);
            }
            timeout = now + DELAY;
            sync.notifyAll();
        }
    }

    /**
     * A {@link MapModelListener} attached to all existing maps. It calls {@link
     * DelayedMapModelListenerManager#scheduleMapModel(MapModel)} for all map
     * changes.
     */
    private class MapModelListenerImpl implements MapModelListener<G, A, R> {

        /**
         * The tracked {@link MapModel} instance.
         */
        private final MapModel<G, A, R> mapModel;

        /**
         * Creates a new instance.
         * @param mapModel the map model to track
         */
        private MapModelListenerImpl(@NotNull final MapModel<G, A, R> mapModel) {
            this.mapModel = mapModel;
        }

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            scheduleMapModel(mapModel);
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
            scheduleMapModel(mapModel);
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
            scheduleMapModel(mapModel);
        }

        @Override
        public void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors) {
            // ignore
        }

        @Override
        public void mapFileChanged(@Nullable final File oldMapFile) {
            scheduleMapModel(mapModel);
        }

        @Override
        public void modifiedChanged() {
            // ignore
        }

    }

    /**
     * A {@link MapArchObjectListener} attached to all existing maps. It calls
     * {@link DelayedMapModelListenerManager#scheduleMapModel(MapModel)} for all
     * map changes.
     */
    private class MapArchObjectListenerImpl implements MapArchObjectListener {

        /**
         * The tracked {@link MapModel} instance.
         */
        private final MapModel<G, A, R> mapModel;

        /**
         * Creates a new instance.
         * @param mapModel the map model to track
         */
        private MapArchObjectListenerImpl(@NotNull final MapModel<G, A, R> mapModel) {
            this.mapModel = mapModel;
        }

        @Override
        public void mapMetaChanged() {
            scheduleMapModel(mapModel);
        }

        @Override
        public void mapSizeChanged(@NotNull final Size2D mapSize) {
            // ignore
        }

    }

}
