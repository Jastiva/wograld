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

package net.sf.gridarta.gui.mapimagecache;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import net.sf.gridarta.gui.map.renderer.MapRenderer;
import net.sf.gridarta.gui.map.renderer.RendererFactory;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.CacheFiles;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.DefaultMapControl;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.MapControlListener;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Caches icon and preview images for map files. Cached images are cached both
 * on disk and in memory using weak references. Icons have a fixed size ({@link
 * #ICON_WIDTH}x{@link #ICON_HEIGHT} pixels); previews are scaled down from the
 * normal size by the factor {@link #PREVIEW_SCALE}.
 * @author Andreas Kirschbaum
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class MapImageCache<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The width of icons in pixels.
     */
    private static final int ICON_WIDTH = 48;

    /**
     * The height of icons in pixels.
     */
    private static final int ICON_HEIGHT = 23;

    /**
     * The scale factor for preview images.
     */
    private static final int PREVIEW_SCALE = 8;

    /**
     * The {@link MapManager} for loading maps.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link RendererFactory} for creating renderers.
     */
    @NotNull
    private final RendererFactory<G, A, R> rendererFactory;

    /**
     * The registered event listeners.
     */
    @NotNull
    private final EventListenerList2<MapImageCacheListener<G, A, R>> listenerList = new EventListenerList2<MapImageCacheListener<G, A, R>>(MapImageCacheListener.class);

    /**
     * The cache entries. Maps {@link ImageType} to associated entry. All
     * entries exist.
     */
    @NotNull
    private final Map<ImageType, MapImageCacheEntry> entries = new EnumMap<ImageType, MapImageCacheEntry>(ImageType.class);

    /**
     * Maps {@link MapControl} instance to {@link MapRenderer} which is only
     * used to get images.
     */
    @NotNull
    private final Map<MapControl<G, A, R>, SoftReference<MapRenderer>> mapRendererReferences = new HashMap<MapControl<G, A, R>, SoftReference<MapRenderer>>();

    /**
     * The {@link MapControlListener} attached to all {@link MapControl
     * MapControls}. It updates the cached icon and preview for saved maps.
     */
    private final MapControlListener<G, A, R> mapControlListener = new MapControlListener<G, A, R>() {

        @Override
        public void saved(@NotNull final DefaultMapControl<G, A, R> mapControl) {
            final File file = mapControl.getMapModel().getMapFile();
            assert file != null;
            updateCaches(file, mapControl, ImageType.ICON);
            updateCaches(file, mapControl, ImageType.PREVIEW);
        }

    };

    /**
     * Creates a new instance.
     * @param mapManager the map manager for loading maps
     * @param defaultIcon the default icon to return if no icon can be created
     * for a map
     * @param defaultPreview the default preview image to return if no preview
     * can be created for a map
     * @param rendererFactory the renderer factory for creating renderers
     * @param cacheFiles the cache files for generating files for cached icons
     * and previews
     */
    public MapImageCache(@NotNull final MapManager<G, A, R> mapManager, @NotNull final Image defaultIcon, @NotNull final Image defaultPreview, @NotNull final RendererFactory<G, A, R> rendererFactory, @NotNull final CacheFiles cacheFiles) {
        this.mapManager = mapManager;
        this.rendererFactory = rendererFactory;
        entries.put(ImageType.ICON, new MapImageCacheEntry(cacheFiles, defaultIcon, ImageType.ICON.toString()));
        entries.put(ImageType.PREVIEW, new MapImageCacheEntry(cacheFiles, defaultPreview, ImageType.PREVIEW.toString()));

        final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

            @Override
            public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
                // ignore
            }

            @Override
            public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
                mapControl.addMapControlListener(mapControlListener);
            }

            @Override
            public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
                // ignore
            }

            /**
             * Purges cached renderers from {@link
             * MapImageCache#mapRendererReferences} when a map is closed.
             */
            @Override
            public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
                mapControl.addMapControlListener(mapControlListener);
                mapRendererReferences.remove(mapControl);
            }

        };
        mapManager.addMapManagerListener(mapManagerListener);
    }

    /**
     * Removes the cached images for a map file.
     * @param mapFile the map file to purge
     */
    public void flush(@NotNull final File mapFile) {
        for (final ImageType imageType : ImageType.values()) {
            entries.get(imageType).flush(mapFile);
        }
    }

    /**
     * Returns the icon {@link Image} for a given map file. If no image is
     * cached (or if the cached image is outdated), returns <code>null</code>.
     * @param mapFile the map file
     * @return the icon image or <code>null</code>
     */
    @Nullable
    public Image getIcon(@NotNull final File mapFile) {
        return entries.get(ImageType.ICON).lookupCache(mapFile);
    }

    /**
     * Returns an icon {@link Image} for a given map file. If no image is cached
     * (or if the cached image is outdated), loads the map and creates the
     * icon.
     * @param mapFile the map file
     * @return the icon image or <code>null</code> for directories
     */
    @Nullable
    public Image getOrCreateIcon(@NotNull final File mapFile) {
        return getOrCreate(mapFile, ImageType.ICON);
    }

    /**
     * Returns an icon {@link Image} for a given map. If no image is cached (or
     * if the cached image is outdated), (re-)creates it.
     * @param mapControl the map
     * @return the icon image
     */
    @NotNull
    public Image getOrCreateIcon(@NotNull final MapControl<G, A, R> mapControl) {
        return getOrCreate(mapControl, ImageType.ICON);
    }

    /**
     * Returns a preview {@link Image} for a given map file. If no image is
     * cached (or if the cached image is outdated), returns <code>null</code>.
     * @param mapFile the map file
     * @return the preview image or <code>null</code>
     */
    @Nullable
    public Image getPreview(@NotNull final File mapFile) {
        return entries.get(ImageType.PREVIEW).lookupCache(mapFile);
    }

    /**
     * Returns a preview {@link Image} for a given map file. If no image is
     * cached (or if the cached image is outdated), loads the map and creates
     * the image.
     * @param mapFile the map file
     * @return the preview image or <code>null</code> for directories
     */
    @Nullable
    public Image getOrCreatePreview(@NotNull final File mapFile) {
        return getOrCreate(mapFile, ImageType.PREVIEW);
    }

    /**
     * Returns a preview {@link Image} for a given map. If no image is cached
     * (or if the cached image is outdated), (re-)creates the image.
     * @param mapControl the map
     * @return the preview image
     */
    @NotNull
    public Image getOrCreatePreview(@NotNull final MapControl<G, A, R> mapControl) {
        return getOrCreate(mapControl, ImageType.PREVIEW);
    }

    /**
     * Updates the cached icon and preview {@link Image Images} for one map
     * file.
     * @param mapFile the map file
     * @param imageType the type of the image to return
     * @return the image or <code>null</code> if the image cannot be created
     */
    @Nullable
    private Image updateCaches(@NotNull final File mapFile, final ImageType imageType) {
        try {
            final MapControl<G, A, R> mapControl = mapManager.openMapFile(mapFile, false);
            try {
                return updateCaches(mapFile, mapControl, imageType);
            } finally {
                mapManager.release(mapControl);
            }
        } catch (final IOException ignored) {
            // ignore: do not update cached images
        }

        return entries.get(imageType).getDefaultImage();
    }

    /**
     * Updates the cached icon and preview {@link Image Images} for one map
     * file.
     * @param mapFile the map file or <code>null</code> for unsaved maps
     * @param mapControl the map control instance corresponding to
     * <code>mapFile</code>
     * @param imageType the type of the image to return
     * @return the image
     */
    @NotNull
    private Image updateCaches(@Nullable final File mapFile, @NotNull final MapControl<G, A, R> mapControl, final ImageType imageType) {
        final Image image;
        //noinspection ErrorNotRethrown
        try {
            image = getRenderer(mapControl).getFullImage();
        } catch (final OutOfMemoryError ignored) {
            return entries.get(imageType).getDefaultImage();
        }
        final Image[] images = new Image[2];
        images[0] = entries.get(ImageType.ICON).scaleImage(mapFile, image, ICON_WIDTH, ICON_HEIGHT);
        images[1] = entries.get(ImageType.PREVIEW).scaleImage(mapFile, image, (image.getWidth(null) + PREVIEW_SCALE - 1) / PREVIEW_SCALE, (image.getHeight(null) + PREVIEW_SCALE - 1) / PREVIEW_SCALE);

        if (mapFile != null) {
            for (final MapImageCacheListener<G, A, R> listener : listenerList.getListeners()) {
                listener.iconChanged(mapControl);
            }
        }

        return images[imageType == ImageType.ICON ? 0 : 1];
    }

    /**
     * Returns a {@link MapRenderer} for a map. If no renderer is cached a new
     * one is created and returned.
     * @param mapControl the map to get the renderer for
     * @return the map renderer
     */
    @NotNull
    private MapRenderer getRenderer(@NotNull final MapControl<G, A, R> mapControl) {
        final SoftReference<MapRenderer> mapRendererReference = mapRendererReferences.get(mapControl);
        final MapRenderer tmpMapRenderer = mapRendererReference == null ? null : mapRendererReference.get();
        final MapRenderer mapRenderer;
        if (tmpMapRenderer == null) {
            mapRenderer = rendererFactory.newSimpleMapRenderer(mapControl.getMapModel());
            mapRendererReferences.put(mapControl, new SoftReference<MapRenderer>(mapRenderer));
        } else {
            mapRenderer = tmpMapRenderer;
        }
        return mapRenderer;
    }

    /**
     * Adds a {@link MapImageCacheListener} to be notified.
     * @param listener the listener to add
     */
    public void addMapImageCacheListener(@NotNull final MapImageCacheListener<G, A, R> listener) {
        listenerList.add(listener);
    }

    /**
     * Removes a {@link MapControlListener} to be notified.
     * @param listener the listener to remove
     */
    public void removeMapImageCacheListener(@NotNull final MapImageCacheListener<G, A, R> listener) {
        listenerList.remove(listener);
    }

    /**
     * Returns an icon or preview {@link Image} for a given map. If no image is
     * cached (or if the cached image is outdated), (re-)creates the image.
     * @param mapControl the map
     * @param imageType the image type to use
     * @return the preview image
     */
    @NotNull
    private Image getOrCreate(@NotNull final MapControl<G, A, R> mapControl, @NotNull final ImageType imageType) {
        final File mapFile = mapControl.getMapModel().getMapFile();
        if (mapFile != null) {
            final Image image = entries.get(imageType).lookupCache(mapFile);
            if (image != null) {
                return image;
            }
        }

        return updateCaches(mapFile, mapControl, imageType);
    }

    /**
     * Returns a preview {@link Image} for a given map file. If no image is
     * cached (or if the cached image is outdated), loads the map and creates
     * the image.
     * @param mapFile the map file
     * @param imageType the image type to return
     * @return the preview image or <code>null</code> for directories
     */
    @Nullable
    private Image getOrCreate(@NotNull final File mapFile, @NotNull final ImageType imageType) {
        if (mapFile.isDirectory()) {
            return null;
        }

        final Image image = entries.get(imageType).lookupCache(mapFile);
        if (image != null) {
            return image;
        }

        return updateCaches(mapFile, imageType);
    }

}
