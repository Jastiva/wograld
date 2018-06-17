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

package net.sf.gridarta.model.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.DefaultMapControl;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.MapControlListener;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.settings.GlobalSettingsListener;
import net.sf.gridarta.utils.ConfigFileUtils;
import net.sf.gridarta.utils.Xtea;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Maintains a {@link MapsIndex} for the maps directory. Changed maps are
 * tracked and the index is updated.
 * @author Andreas Kirschbaum
 */
public class MapsIndexer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(MapsIndexer.class);

    /**
     * A pending map may exist in {@link #mapsIndex} if a permit is available.
     */
    @NotNull
    private final Semaphore mapsIndexSemaphore = new Semaphore(1);

    /**
     * The {@link MapsIndex} being updated.
     */
    @NotNull
    private final MapsIndex mapsIndex;

    /**
     * The {@link MapManager} for loading map files.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link GlobalSettings} instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The object for synchronizing access to {@link #mapsDirectory} and {@link
     * #newMapsDirectory}.
     */
    @NotNull
    private final Object syncMapsDirectory = new Object();

    /**
     * The currently indexed maps directory.
     */
    @Nullable
    private File mapsDirectory;

    /**
     * The maps directory to index. If <code>null</code>, {@link #mapsDirectory}
     * is valid.
     */
    @Nullable
    private File newMapsDirectory;

    /**
     * The object for synchronizing access to {@link #state}.
     */
    @NotNull
    private final Object syncState = new Object();

    /**
     * The indexer state.
     */
    @NotNull
    private State state = State.INIT;

    /**
     * Indexer states.
     */
    private enum State {

        /**
         * Indexer has been created but is not yet running.
         */
        INIT,

        /**
         * Indexer is scanning the maps directory searching for files differing
         * to the current index.
         */
        SCAN,

        /**
         * Indexer is indexing maps differing from the current index.
         */
        INDEX,

        /**
         * Indexer is idle; the current index is up-to-date.
         */
        IDLE

    }

    /**
     * The {@link MapManagerListener} attached to {@link #mapManager} to detect
     * current map changes.
     */
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
            if (!mapControl.isPickmap()) {
                mapControl.addMapControlListener(mapControlListener);
            }
        }

        @Override
        public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
            if (!mapControl.isPickmap()) {
                mapControl.removeMapControlListener(mapControlListener);
            }
        }

    };

    /**
     * The {@link MapControlListener} attached to all opened maps.
     */
    @NotNull
    private final MapControlListener<G, A, R> mapControlListener = new MapControlListener<G, A, R>() {

        @Override
        public void saved(@NotNull final DefaultMapControl<G, A, R> mapControl) {
            final File file = mapControl.getMapModel().getMapFile();
            if (file != null) {
                mapsIndex.setPending(file);
            }
        }

    };

    /**
     * The {@link GlobalSettingsListener} attached to {@link #globalSettings}
     * for tracking changed maps directories.
     */
    @NotNull
    private final GlobalSettingsListener globalSettingsListener = new GlobalSettingsListener() {

        @Override
        public void showMainToolbarChanged(final boolean visible) {
            // ignore
        }

        @Override
        public void mapsDirectoryChanged(@NotNull final File mapsDirectory) {
            synchronized (syncMapsDirectory) {
                newMapsDirectory = globalSettings.getMapsDirectory();
            }
        }

    };

    /**
     * The {@link IndexListener} attached to {@link #mapsIndex} to detect newly
     * added pending maps.
     */
    @NotNull
    private final IndexListener<File> indexListener = new IndexListener<File>() {

        @Override
        public void valueAdded(@NotNull final File value) {
            // ignore
        }

        @Override
        public void valueRemoved(@NotNull final File value) {
            // ignore
        }

        @Override
        public void nameChanged() {
            // ignore
        }

        @Override
        public void pendingChanged() {
            mapsIndexSemaphore.release();
        }

        @Override
        public void indexingFinished() {
            // ignore
        }

    };

    /**
     * The {@link Runnable} scanning the maps directory and updating the index.
     */
    @NotNull
    private final Runnable runnable = new Runnable() {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                updateMapsDirectory();
                indexPendingMaps();
                if (state == State.IDLE) {
                    try {
                        mapsIndexSemaphore.acquire();
                    } catch (final InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

    };

    /**
     * The {@link Thread} executing {@link #runnable}.
     */
    @NotNull
    private final Thread thread = new Thread(runnable, "indexer");

    /**
     * Creates a new instance.
     * @param mapsIndex the maps index to update
     * @param mapManager the map manager for loading map files
     * @param globalSettings the global settings instance; defines the indexed
     * maps directory
     */
    public MapsIndexer(@NotNull final MapsIndex mapsIndex, @NotNull final MapManager<G, A, R> mapManager, @NotNull final GlobalSettings globalSettings) {
        this.mapsIndex = mapsIndex;
        this.mapManager = mapManager;
        this.globalSettings = globalSettings;
        reportStateChange();
    }

    /**
     * Starts indexing. Must not be called more than once.
     */
    public void start() {
        globalSettings.addGlobalSettingsListener(globalSettingsListener);
        mapManager.addMapManagerListener(mapManagerListener);
        synchronized (syncMapsDirectory) {
            newMapsDirectory = globalSettings.getMapsDirectory();
        }
        mapsIndex.addIndexListener(indexListener);
        thread.start();
    }

    /**
     * Stops indexing. Must not be called more than once or before {@link
     * #start()} has been called.
     * @throws InterruptedException if the current thread was interrupted
     */
    public void stop() throws InterruptedException {
        try {
            thread.interrupt();
            thread.join();
        } finally {
            globalSettings.removeGlobalSettingsListener(globalSettingsListener);
            mapManager.removeMapManagerListener(mapManagerListener);
            for (final MapControl<G, A, R> mapControl : mapManager.getOpenedMaps()) {
                if (!mapControl.isPickmap()) {
                    mapControl.removeMapControlListener(mapControlListener);
                }
            }
            mapsIndex.removeIndexListener(indexListener);
        }

        synchronized (syncMapsDirectory) {
            saveMapsIndex();
        }
    }

    /**
     * Blocks the calling thread until all pending maps have been indexed.
     * @throws InterruptedException if the current thread was interrupted during
     * wait
     */
    public void waitForIdle() throws InterruptedException {
        synchronized (syncState) {
            while (state != State.IDLE) {
                syncState.wait(1000L);
            }
        }
    }

    /**
     * Checks whether {@link #newMapsDirectory} has been set and updates {@link
     * #mapsDirectory} accordingly.
     */
    private void updateMapsDirectory() {
        final File tmpMapsDirectory;
        synchronized (syncMapsDirectory) {
            if (newMapsDirectory == null || (mapsDirectory != null && mapsDirectory.equals(newMapsDirectory))) {
                return;
            }

            setState(State.SCAN);

            saveMapsIndex();

            tmpMapsDirectory = newMapsDirectory;
            assert tmpMapsDirectory != null;
            mapsDirectory = tmpMapsDirectory;
            newMapsDirectory = null;

            loadMapsIndex();
        }
        mapsIndex.beginUpdate();
        scanMapsDirectoryInt(tmpMapsDirectory, "");
        mapsIndex.endUpdate();
    }

    /**
     * Saves {@link #mapsIndex} to its cache file if modified.
     */
    private void saveMapsIndex() {
        assert Thread.holdsLock(syncMapsDirectory);
        if (mapsDirectory == null) {
            return;
        }

        if (!globalSettings.saveIndices()) {
            return;
        }

        if (!mapsIndex.isModified()) {
            return;
        }

        assert mapsDirectory != null;
        final File cacheFile = getCacheFile(mapsDirectory);
        try {
            final OutputStream outputStream = new FileOutputStream(cacheFile);
            try {
                final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                try {
                    mapsIndex.save(objectOutputStream);
                } finally {
                    objectOutputStream.close();
                }
            } finally {
                outputStream.close();
            }
            if (log.isInfoEnabled()) {
                log.info(cacheFile + ": saved " + mapsIndex.size() + " entries");
            }
        } catch (final IOException ex) {
            log.warn(cacheFile + ": cannot save cache file: " + ex.getMessage());
        }
    }

    /**
     * Loads {@link #mapsIndex} from its cache file.
     */
    private void loadMapsIndex() {
        assert Thread.holdsLock(syncMapsDirectory);
        assert mapsDirectory != null;
        final File cacheFile = getCacheFile(mapsDirectory);
        try {
            final InputStream inputStream = new FileInputStream(cacheFile);
            try {
                final ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                try {
                    mapsIndex.load(objectInputStream);
                } finally {
                    objectInputStream.close();
                }
            } finally {
                inputStream.close();
            }
            if (log.isInfoEnabled()) {
                log.info(cacheFile + ": loaded " + mapsIndex.size() + " entries");
            }
        } catch (final FileNotFoundException ex) {
            if (log.isDebugEnabled()) {
                log.debug(cacheFile + ": cache file does not exist: " + ex.getMessage());
            }
            mapsIndex.clear();
        } catch (final IOException ex) {
            log.warn(cacheFile + ": cannot load cache file: " + ex.getMessage());
            mapsIndex.clear();
        }
    }

    /**
     * Scans a directory for files. Adds all files found to {@link #mapsIndex}.
     * @param dir the directory to scan
     * @param mapPath the map path corresponding to <code>dir</code>
     */
    private void scanMapsDirectoryInt(@NotNull final File dir, @NotNull final String mapPath) {
        final File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (final File file : files) {
            if (file.isFile() && !file.getName().endsWith("~")) {
                mapsIndex.add(file, file.lastModified());
            } else if (file.isDirectory() && !file.getName().equalsIgnoreCase(".svn") && !file.getName().equalsIgnoreCase(".dedit")) {
                scanMapsDirectoryInt(file, mapPath + "/" + file.getName());
            }
        }
    }

    /**
     * Indexes one pending map from {@link #mapsIndex}. Does nothing if no
     * pending map exists.
     */
    private void indexPendingMaps() {
        final File file = mapsIndex.removePending();
        if (file == null) {
            setState(State.IDLE);
            return;
        }

        setState(State.INDEX);
        final long timestamp = file.lastModified();
        final MapControl<G, A, R> mapControl;
        try {
            mapControl = mapManager.openMapFile(file, false);
        } catch (final IOException ex) {
            if (log.isInfoEnabled()) {
                log.info(file + ": load failed:" + ex.getMessage());
            }
            return;
        }
        try {
            final String mapName = mapControl.getMapModel().getMapArchObject().getMapName();
            if (log.isDebugEnabled()) {
                log.debug(file + ": indexing as '" + mapName + "'");
            }
            mapsIndex.setName(file, timestamp, mapName);
        } finally {
            mapManager.release(mapControl);
        }
    }

    /**
     * Returns the cache file for a given maps directory.
     * @param mapsDirectory the maps directory
     * @return the cache file
     */
    @NotNull
    private static File getCacheFile(@NotNull final File mapsDirectory) {
        final byte[] key = new byte[16];
        final Xtea xtea = new Xtea(key);
        final byte[] data;
        try {
            data = mapsDirectory.getAbsoluteFile().toString().getBytes("UTF-8");
        } catch (final UnsupportedEncodingException ex) {
            throw new AssertionError(ex); // UTF-8 must be supported
        }
        final byte[] hash = new byte[8];
        final byte[] in = new byte[8];
        final byte[] out = new byte[8];
        int i;
        for (i = 0; i + 8 < data.length; i++) {
            System.arraycopy(data, i, in, 0, 8);
            xtea.encrypt(in, out);
            for (int j = 0; j < 8; j++) {
                hash[j] ^= out[j];
            }
        }
        final int len = data.length % 8;
        System.arraycopy(data, i, in, 0, len);
        in[len] = (byte) 1;
        Arrays.fill(in, len + 1, 8, (byte) 0);
        xtea.encrypt(in, out);
        for (int j = 0; j < 8; j++) {
            hash[j] ^= out[j];
        }

        final StringBuilder sb = new StringBuilder("index/maps/");
        for (int j = 0; j < 8; j++) {
            sb.append(String.format("%02x", hash[j] & 0xff));
        }
        sb.append(mapsDirectory.getName());
        final File file = ConfigFileUtils.getHomeFile(sb.toString());
        final File dir = file.getParentFile();
        if (dir != null && !dir.exists() && !dir.mkdirs()) {
            log.warn("cannot create directory: " + dir);
        }
        return file;
    }

    /**
     * Sets a new value to {@link #state}. Logs changed values.
     * @param state the new state
     */
    private void setState(@NotNull final State state) {
        synchronized (syncState) {
            if (this.state == state) {
                return;
            }

            this.state = state;
            reportStateChange();
            syncState.notifyAll();
        }
    }

    /**
     * Logs a changed value of {@link #state}.
     */
    private void reportStateChange() {
        if (log.isDebugEnabled()) {
            log.debug("state=" + state);
        }

        mapsIndex.indexingFinished();
    }

}
