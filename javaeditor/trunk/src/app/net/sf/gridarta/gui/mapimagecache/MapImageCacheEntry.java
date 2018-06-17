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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.sf.gridarta.model.io.CacheFiles;
import net.sf.gridarta.utils.ImageUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An image cache entry for one {@link ImageType}.
 * @author Andreas Kirschbaum
 */
public class MapImageCacheEntry {

    /**
     * The logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(MapImageCache.class);

    /**
     * The {@link CacheFiles} for generating files for cached icons and
     * previews.
     */
    @NotNull
    private final CacheFiles cacheFiles;

    /**
     * The default image.
     */
    @NotNull
    private final Image defaultImage;

    /**
     * The directory prefix name for the cache directory.
     */
    private final String prefix;

    /**
     * The cached icons. Maps map file to {@link Image}.
     */
    @NotNull
    private final Map<File, WeakReference<Image>> images = new HashMap<File, WeakReference<Image>>();

    /**
     * The timestamps of the cached icons. Maps map file to timestamp.
     */
    @NotNull
    private final Map<File, Long> timestamps = new HashMap<File, Long>();

    /**
     * Creates a new instance.
     * @param cacheFiles the cache files for generating files for caching icons
     * and previews
     * @param defaultImage the default image
     * @param prefix the directory prefix for the cache directory
     */
    public MapImageCacheEntry(@NotNull final CacheFiles cacheFiles, @NotNull final Image defaultImage, @NotNull final String prefix) {
        this.cacheFiles = cacheFiles;
        this.defaultImage = defaultImage;
        this.prefix = prefix;
    }

    /**
     * Removes the cached image for a map file. Both in-memory and on-disk cache
     * are purged.
     * @param mapFile the map file to flush
     */
    public void flush(@NotNull final File mapFile) {
        images.remove(mapFile);
        final File cacheFile = cacheFiles.getCacheFile(mapFile, prefix);
        deleteCacheFile(cacheFile);
    }

    /**
     * Deletes a cache file if it exists. Logs a warning message if the file
     * cannot be deleted.
     * @param cacheFile the cache file to delete
     */
    private static void deleteCacheFile(@NotNull final File cacheFile) {
        if (cacheFile.exists() && !cacheFile.delete()) {
            log.warn("cannot delete cache file: " + cacheFile);
        }
    }

    /**
     * Returns the {@link Image} for a given map file.
     * @param mapFile the map file
     * @return the image icon or <code>null</code> if no image is available
     */
    @Nullable
    private Image getImageIcon(@NotNull final File mapFile) {
        final WeakReference<Image> ref = images.get(mapFile);
        if (ref != null) {
            final Image image = ref.get();
            if (image != null && mapFile.lastModified() <= timestamps.get(mapFile)) {
                return image;
            }

            images.remove(mapFile);
        }

        return null;
    }

    /**
     * Saves an PNG image file for a map file.
     * @param mapFile the map file to save the image of
     * @param image the image to save
     */
    private void saveImageFile(@NotNull final File mapFile, @NotNull final Image image) {
        final File imageFile = cacheFiles.getCacheFile(mapFile, prefix);

        final File imageDir = imageFile.getParentFile();
        if (imageDir != null && !imageDir.exists() && !imageDir.mkdirs()) {
            log.warn("cannot create cache directory: " + imageDir);
        }

        final int width = image.getWidth(null);
        if (width == -1) {
            return;
        }
        final int height = image.getHeight(null);
        if (height == -1) {
            return;
        }
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics graphics = bufferedImage.getGraphics();
        try {
            graphics.drawImage(image, 0, 0, null);
        } finally {
            graphics.dispose();
        }

        try {
            ImageIO.write(bufferedImage, "png", imageFile);
            timestamps.put(mapFile, imageFile.lastModified());
        } catch (final IOException e) {
            log.warn("Cannot create icon file " + imageFile + ": " + e.getMessage());
        }
    }

    /**
     * Loads an image file from disk. If the file does not exists, cannot be
     * loaded, or is outdated, return <code>null</code>.
     * @param mapFile the map file name
     * @return the image file, or <code>null</code>
     */
    @Nullable
    private Image loadImageFile(@NotNull final File mapFile) {
        final File imageFile = cacheFiles.getCacheFile(mapFile, prefix);

        if (!imageFile.exists()) {
            // image file does not exist
            return null;
        }

        final long imageLastModified = imageFile.lastModified();
        if (imageLastModified == 0L) {
            // image file does not exist or is unreadable
            return null;
        }

        final long mapLastModified = mapFile.lastModified();
        if (mapLastModified == 0L || mapLastModified >= imageLastModified) {
            // image file is older than map file ==> outdated
            deleteCacheFile(imageFile);
            return null;
        }

        final Image image;
        try {
            image = ImageIO.read(imageFile);
        } catch (final IOException ignored) {
            return null;
        }
        if (image.getWidth(null) <= 0 || image.getHeight(null) <= 0) {
            // image file is invalid
            deleteCacheFile(imageFile);
            return null;
        }

        timestamps.put(mapFile, imageFile.lastModified());
        return image;
    }

    /**
     * Looks up an image from the cache.
     * @param mapFile the map file to look up
     * @return the image, or <code>null</code> if the cache does not contain an
     *         entry for <code>mapFile</code> or if the entry is outdated
     */
    @Nullable
    public Image lookupCache(@NotNull final File mapFile) {
        if (mapFile.isDirectory()) {
            return null;
        }

        final Image image = getImageIcon(mapFile);
        if (image != null) {
            return image;
        }

        final Image image2 = loadImageFile(mapFile);
        if (image2 == null) {
            return null;
        }

        if (mapFile.lastModified() <= timestamps.get(mapFile)) {
            images.put(mapFile, new WeakReference<Image>(image2));
        }

        return image2;
    }

    /**
     * Returns the default image.
     * @return the default image
     */
    @NotNull
    public Image getDefaultImage() {
        return defaultImage;
    }

    /**
     * Scales an {@link Image} to the given size and updates the cache. If an
     * out of memory error occurs the default image is returned.
     * @param mapFile the map file for the image
     * @param image the image to scale
     * @param width the scaled width
     * @param height the scaled height
     * @return the scaled image
     */
    @NotNull
    public Image scaleImage(@Nullable final File mapFile, @NotNull final Image image, final int width, final int height) {
        Image result;
        //noinspection ErrorNotRethrown
        try {
            result = ImageUtils.scaleImage(image, width, height);
        } catch (final InterruptedException ignored) {
            Thread.currentThread().interrupt();
            result = defaultImage;
        } catch (final OutOfMemoryError ignored) {
            result = defaultImage;
        }
        if (mapFile != null) {
            images.put(mapFile, new WeakReference<Image>(result));
            timestamps.put(mapFile, mapFile.lastModified());
            saveImageFile(mapFile, result);
        }
        return result;
    }

}
