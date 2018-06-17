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
 * An {@link GlobalSettings} implementation for testing purposes.
 * @author Andreas Kirschbaum
 */
public class TestGlobalSettings extends AbstractGlobalSettings {

    /**
     * The maps directory.
     */
    @NotNull
    private File mapsDirectory = new File("maps");

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getArchDirectoryDefault() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getArchDirectory() {
        return new File("arch");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArchDirectory(@NotNull final File archDirectory) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getMapsDirectoryDefault() {
        throw new AssertionError();
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
        if (this.mapsDirectory.equals(mapsDirectory)) {
            return;
        }

        this.mapsDirectory = mapsDirectory;
        fireMapsDirectoryChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setImageDirectory(@NotNull final File imageDirectory) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getImageDirectory() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getConfigSourceName() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfigSourceName(@NotNull final String configSourceName) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getCurrentSaveMapDirectory() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentSaveMapDirectory(@NotNull final File currentSaveMapDirectory) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutoPopupDocumentation() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoPopupDocumentation(final boolean autoPopupDocumentation) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public File getPickmapDir() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasMediaDirectory() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getMediaDirectoryDefault() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public File getMediaDirectory() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMediaDirectory(@NotNull final File mediaDirectory) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasImageSet() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getImageSetDefault() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getImageSet() {
        throw new AssertionError();
    }

    @Override
    public void setImageSet(@NotNull final String imageSet) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public File getConfigurationDirectory() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public File getCollectedDirectory() {
        return new File("collected");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShowMainToolbar() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShowMainToolbar(final boolean selected) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getUserName() {
        return "user";
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getUserNameDefault() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserName(@NotNull final String userName) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveIndices() {
        return false;
    }

}
