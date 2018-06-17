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

package net.sf.gridarta.model.settings;

import java.io.File;
import org.jetbrains.annotations.NotNull;

/**
 * Common settings values.
 */
public interface GlobalSettings {

    /**
     * Default value for whether the main window's toolbar is shown.
     */
    boolean SHOW_MAIN_TOOLBAR_DEFAULT = true;

    /**
     * Returns the default archetype directory.
     * @return the default archetype directory
     */
    @NotNull
    File getArchDirectoryDefault();

    /**
     * Returns the archetype directory.
     * @return the archetype directory
     */
    @NotNull
    File getArchDirectory();

    /**
     * Sets the archetype directory.
     * @param archDirectory the archetype directory
     */
    void setArchDirectory(@NotNull File archDirectory);

    /**
     * Returns the default maps directory.
     * @return the default maps directory
     */
    @NotNull
    File getMapsDirectoryDefault();

    /**
     * Returns the default maps directory.
     * @return the default maps directory
     */
    @NotNull
    File getMapsDirectory();

    /**
     * Sets the default maps directory.
     * @param mapsDirectory the default maps directory
     */
    void setMapsDirectory(@NotNull File mapsDirectory);

    /**
     * Sets the directory to save created image to. It defaults to {@link
     * #getMapsDirectory()} and is not retained across editor restarts.
     * @param imageDirectory the image directory
     */
    void setImageDirectory(@NotNull File imageDirectory);

    /**
     * Returns the directory to save images to.
     * @return the image directory
     */
    @NotNull
    File getImageDirectory();

    /**
     * Returns the name of the configuration source.
     * @return the name of the configuration source
     */
    @NotNull
    String getConfigSourceName();

    /**
     * Sets the name of the configuration source.
     * @param configSourceName the name
     */
    void setConfigSourceName(@NotNull String configSourceName);

    /**
     * Returns the default directory for saving maps.
     * @return the default directory for saving maps
     */
    @NotNull
    File getCurrentSaveMapDirectory();

    /**
     * Sets the default directory for saving maps.
     * @param currentSaveMapDirectory the default directory for saving maps
     */
    void setCurrentSaveMapDirectory(@NotNull File currentSaveMapDirectory);

    boolean isAutoPopupDocumentation();

    void setAutoPopupDocumentation(boolean autoPopupDocumentation);

    /**
     * Returns the pickmap directory.
     * @return the pickmap directory
     */
    @NotNull
    File getPickmapDir();

    /**
     * Returns whether a media directory is used.
     * @return whether a media directory is used
     */
    boolean hasMediaDirectory();

    /**
     * Returns the default media directory.
     * @return the default media directory
     */
    @NotNull
    File getMediaDirectoryDefault();

    /**
     * Returns the media directory. It contains background music files.
     * @return the media directory
     */
    @NotNull
    File getMediaDirectory();

    /**
     * Sets the media directory. It contains background music files.
     * @param mediaDirectory the media directory
     */
    void setMediaDirectory(@NotNull File mediaDirectory);

    /**
     * Returns whether an image set is used.
     * @return whether an image set is used
     */
    boolean hasImageSet();

    /**
     * Returns the default image set.
     * @return the default image set
     */
    @NotNull
    String getImageSetDefault();

    /**
     * Returns the image set.
     * @return the image set
     */
    @NotNull
    String getImageSet();

    /**
     * Sets the image set.
     * @param imageSet the image set
     */
    void setImageSet(@NotNull String imageSet);

    /**
     * Returns the configuration directory which is used to load configuration
     * information like types.xml.
     * @return the directory
     */
    @NotNull
    File getConfigurationDirectory();

    /**
     * Returns the directory where collected archetypes are stored.
     * @return the directory
     */
    @NotNull
    File getCollectedDirectory();

    /**
     * Returns whether the main toolbar should be shown.
     * @return whether the main toolbar should be shown
     */
    boolean isShowMainToolbar();

    /**
     * Sets whether the main toolbar should be shown.
     * @param selected whether the main toolbar should be shown
     */
    void setShowMainToolbar(boolean selected);

    /**
     * Returns the user name.
     * @return the user name
     */
    @NotNull
    String getUserName();

    /**
     * Returns the default user name.
     * @return the default user name
     */
    @NotNull
    String getUserNameDefault();

    /**
     * Sets the user name.
     * @param userName the user name
     */
    void setUserName(@NotNull String userName);

    /**
     * Adds a {@link GlobalSettingsListener} to be notified of changes.
     * @param listener the listener
     */
    void addGlobalSettingsListener(@NotNull GlobalSettingsListener listener);

    /**
     * Removes a {@link GlobalSettingsListener} to be notified of changes.
     * @param listener the listener
     */
    void removeGlobalSettingsListener(@NotNull GlobalSettingsListener listener);

    /**
     * Returns whether indices should be saved to disk.
     * @return whether indices should be saved to disk
     */
    boolean saveIndices();

}
