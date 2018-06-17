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
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Default implementation of {@link GlobalSettings}.
 * @author Andreas Kirschbaum
 */
public abstract class DefaultGlobalSettings extends AbstractGlobalSettings {

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The preferences key for the archetype directory.
     */
    @NotNull
    private static final String MAP_DIRECTORY_KEY = "mapDirectory";

    /**
     * The preferences key for the archetype directory.
     */
    @NotNull
    private static final String ARCH_DIRECTORY_KEY = "archDirectory";

    /**
     * The preferences key for the media directory.
     */
    @NotNull
    private static final String MEDIA_DIRECTORY_KEY = "mediaDirectory";

    /**
     * The preferences key for the selected image set.
     */
    @NotNull
    private static final String IMAGE_SET_KEY = "useImageSet";

    /**
     * The preferences key for storing the last known documentation version.
     */
    @NotNull
    private static final String DOCUMENTATION_VERSION_KEY = "docuVersion";

    /**
     * Key for info whether the main window's toolbar is shown.
     */
    @NotNull
    private static final String SHOW_MAIN_TOOLBAR_KEY = "ShowMainToolbar";

    /**
     * Preferences key for user name.
     */
    @NotNull
    private static final String PREFERENCES_USER_NAME = "username";

    /**
     * Preferences default value for user name.
     */
    @NotNull
    private static final String PREFERENCES_USER_NAME_DEFAULT = System.getProperty("user.name");

    /**
     * The preferences key for configuration source.
     */
    @NotNull
    private static final String CONFIG_SOURCE_KEY = "configSource";

    /**
     * Preferences.
     */
    @NotNull
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * The default value for the maps directory.
     */
    @NotNull
    private final String mapsDirectoryDefault;

    /**
     * Whether a media directory is used.
     */
    private final boolean hasMediaDefaultDirectory;

    /**
     * The default value for the media directory.
     */
    @NotNull
    private final String mediaDirectoryDefault;

    /**
     * Whether an image set is used.
     */
    private final boolean hasImageSet;

    /**
     * The default value for the image set.
     */
    @NotNull
    private final String imageSetDefault;

    /**
     * The default value for the archetype directory.
     */
    @NotNull
    private final String archDirectoryDefault;

    /**
     * The archetype directory.
     */
    @NotNull
    private File archDirectory;

    /**
     * The default maps directory.
     */
    @NotNull
    private File mapsDirectory;

    /**
     * The media directory.
     */
    @NotNull
    private File mediaDirectory;

    /**
     * The image set.
     */
    @NotNull
    private String imageSet;

    /**
     * The image directory. By default, created images are stored in this
     * directory.
     */
    @NotNull
    private File imageDirectory;

    /**
     * Do we load arches from the collected archives.
     */
    @NotNull
    private String configSourceName;

    /**
     * Time for an automated documentation popup.
     */
    private boolean autoPopupDocumentation;

    /**
     * The the default directory for saving maps.
     */
    @NotNull
    private File currentSaveMapDirectory;

    /**
     * Creates a new instance.
     */
    protected DefaultGlobalSettings() {
        archDirectoryDefault = ActionBuilderUtils.getString(ACTION_BUILDER, "archDirectoryDefault", "");
        mapsDirectoryDefault = ActionBuilderUtils.getString(ACTION_BUILDER, "mapsDirectoryDefault", "");
        hasMediaDefaultDirectory = ActionBuilderUtils.getBoolean(ACTION_BUILDER, "mediaDirectory");
        mediaDirectoryDefault = ActionBuilderUtils.getString(ACTION_BUILDER, "mediaDirectoryDefault", "");
        hasImageSet = ActionBuilderUtils.getBoolean(ACTION_BUILDER, "imageSet");
        imageSetDefault = ActionBuilderUtils.getString(ACTION_BUILDER, "imageSetDefault", "none");

        final PreferenceChangeListener preferenceChangeListener = new PreferenceChangeListener() {

            @Override
            public void preferenceChange(final PreferenceChangeEvent evt) {
                if (evt.getKey().equals(SHOW_MAIN_TOOLBAR_KEY)) {
                    fireShowMainToolbarChanged();
                } else if (evt.getKey().equals(ARCH_DIRECTORY_KEY)) {
                    archDirectory = new File(preferences.get(ARCH_DIRECTORY_KEY, archDirectoryDefault));
                } else if (evt.getKey().equals(MAP_DIRECTORY_KEY)) {
                    setMapsDirectoryInt(new File(preferences.get(MAP_DIRECTORY_KEY, mapsDirectoryDefault)), false);
                } else if (evt.getKey().equals(MEDIA_DIRECTORY_KEY)) {
                    mediaDirectory = new File(preferences.get(MEDIA_DIRECTORY_KEY, mediaDirectoryDefault));
                } else if (evt.getKey().equals(IMAGE_SET_KEY)) {
                    imageSet = preferences.get(IMAGE_SET_KEY, imageSetDefault);
                } else if (evt.getKey().equals(CONFIG_SOURCE_KEY)) {
                    configSourceName = preferences.get(CONFIG_SOURCE_KEY, "");
                }
            }

        };
        preferences.addPreferenceChangeListener(preferenceChangeListener);

        archDirectory = new File(preferences.get(ARCH_DIRECTORY_KEY, archDirectoryDefault));
        mapsDirectory = new File(preferences.get(MAP_DIRECTORY_KEY, mapsDirectoryDefault));
        mediaDirectory = new File(preferences.get(MEDIA_DIRECTORY_KEY, mediaDirectoryDefault));
        imageSet = preferences.get(IMAGE_SET_KEY, imageSetDefault);
        imageDirectory = mapsDirectory;
        configSourceName = preferences.get(CONFIG_SOURCE_KEY, "");
        final int documentationVersion = ActionBuilderUtils.getInt(ACTION_BUILDER, "docuVersion", 0);
        if (documentationVersion > 0 && preferences.getInt(DOCUMENTATION_VERSION_KEY, 0) < documentationVersion) {
            // remember to open documentation
            autoPopupDocumentation = true;
            // update documentation version right now, because we want the help popup only one time
            preferences.putInt(DOCUMENTATION_VERSION_KEY, documentationVersion);
        }

        currentSaveMapDirectory = mapsDirectory.exists() ? mapsDirectory : new File(System.getProperty("user.dir"));
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getArchDirectoryDefault() {
        return new File(archDirectoryDefault);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getArchDirectory() {
        return archDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArchDirectory(@NotNull final File archDirectory) {
        if (this.archDirectory.equals(archDirectory)) {
            return;
        }

        this.archDirectory = archDirectory;
        preferences.put(ARCH_DIRECTORY_KEY, archDirectory.toString());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getMapsDirectoryDefault() {
        return new File(mapsDirectoryDefault);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getMapsDirectory() {
        return mapsDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMapsDirectory(@NotNull final File mapsDirectory) {
        setMapsDirectoryInt(mapsDirectory, true);
    }

    /**
     * Sets the {@link #mapsDirectory}.
     * @param mapsDirectory the new maps directory
     * @param updatePreferences whether the preferences should be updated
     */
    private void setMapsDirectoryInt(@NotNull final File mapsDirectory, final boolean updatePreferences) {
        if (this.mapsDirectory.equals(mapsDirectory)) {
            return;
        }

        this.mapsDirectory = mapsDirectory;
        if (updatePreferences) {
            preferences.put(MAP_DIRECTORY_KEY, mapsDirectory.toString());
        }
        fireMapsDirectoryChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasMediaDirectory() {
        return hasMediaDefaultDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getMediaDirectoryDefault() {
        return new File(mediaDirectoryDefault);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getMediaDirectory() {
        return mediaDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMediaDirectory(@NotNull final File mediaDirectory) {
        if (!hasMediaDefaultDirectory) {
            return;
        }
        if (this.mediaDirectory.equals(mediaDirectory)) {
            return;
        }

        this.mediaDirectory = mediaDirectory;
        preferences.put(MEDIA_DIRECTORY_KEY, mediaDirectory.getPath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasImageSet() {
        return hasImageSet;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getImageSetDefault() {
        return imageSetDefault;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getImageSet() {
        return imageSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setImageSet(@NotNull final String imageSet) {
        if (!hasImageSet) {
            return;
        }
        if (this.imageSet.equals(imageSet)) {
            return;
        }

        this.imageSet = imageSet;
        preferences.put(IMAGE_SET_KEY, imageSet);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getImageDirectory() {
        return imageDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setImageDirectory(@NotNull final File imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getConfigSourceName() {
        return configSourceName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfigSourceName(@NotNull final String configSourceName) {
        if (this.configSourceName.equals(configSourceName)) {
            return;
        }

        this.configSourceName = configSourceName;
        preferences.put(CONFIG_SOURCE_KEY, configSourceName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getCurrentSaveMapDirectory() {
        return currentSaveMapDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentSaveMapDirectory(@NotNull final File currentSaveMapDirectory) {
        this.currentSaveMapDirectory = currentSaveMapDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutoPopupDocumentation() {
        return autoPopupDocumentation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoPopupDocumentation(final boolean autoPopupDocumentation) {
        this.autoPopupDocumentation = autoPopupDocumentation;
    }

    /**
     * Returns whether the main toolbar should be shown.
     * @return whether the main toolbar should be shown
     */
    @Override
    public boolean isShowMainToolbar() {
        return preferences.getBoolean(SHOW_MAIN_TOOLBAR_KEY, SHOW_MAIN_TOOLBAR_DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShowMainToolbar(final boolean selected) {
        preferences.putBoolean(SHOW_MAIN_TOOLBAR_KEY, selected);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getUserName() {
        return preferences.get(PREFERENCES_USER_NAME, PREFERENCES_USER_NAME_DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getUserNameDefault() {
        return PREFERENCES_USER_NAME_DEFAULT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserName(@NotNull final String userName) {
        preferences.put(PREFERENCES_USER_NAME, userName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveIndices() {
        return true;
    }

}
