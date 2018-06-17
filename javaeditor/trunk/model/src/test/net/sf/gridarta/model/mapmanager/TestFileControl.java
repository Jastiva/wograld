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
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link FileControl} implementation for testing purposes.
 * @author Andreas Kirschbaum
 */
public class TestFileControl implements FileControl<TestGameObject, TestMapArchObject, TestArchetype> {

    /**
     * Collects the performed actions.
     */
    @NotNull
    private final StringBuilder sb = new StringBuilder();

    /**
     * {@inheritDoc}
     */
    @Override
    public void editScript() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openFile() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(@NotNull final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAllMaps() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean closeAllMaps() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveAs(@NotNull final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean confirmSaveChanges(@NotNull final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportSaveError(@NotNull final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl, @NotNull final String message) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportSaveError(@NotNull final File mapFile, @NotNull final String message) {
        sb.append("reportSaveError: ");
        sb.append(mapFile);
        sb.append(" ");
        sb.append(message);
        sb.append("\n");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportLoadError(@Nullable final File file, @NotNull final String message) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportOutOfMapBoundsDeleted(@NotNull final File file, final int outOfMapBoundsDeleted, @NotNull final StringBuilder outOfMapBoundsObjects) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportOutOfMemory(@NotNull final File file) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportTeleportCharacterError(@NotNull final String mapPath, @NotNull final String message) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return sb.toString();
    }

}
