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

package net.sf.gridarta.model.exitconnector;

import java.awt.Point;
import java.io.File;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.io.PathManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Stores information about a remembered exit location.
 * @author Andreas Kirschbaum
 */
public class ExitLocation {

    /**
     * The file of the map that contains the remembered exit.
     */
    @NotNull
    private final File file;

    /**
     * The {@link PathManager} for converting path names.
     */
    @NotNull
    private final PathManager pathManager;

    /**
     * The x-coordinate of the remembered exit.
     */
    private final int x;

    /**
     * The y-coordinate of the remembered exit.
     */
    private final int y;

    /**
     * The map name of the remembered exit.
     */
    @NotNull
    private final String mapName;

    /**
     * Creates a new instance.
     * @param file the file of the map that contains the remembered exit
     * @param point the coordinate of the remembered exit
     * @param mapName the map name of the remembered exit
     * @param pathManager the path manager for converting path names
     */
    public ExitLocation(@NotNull final File file, @NotNull final Point point, @NotNull final String mapName, @NotNull final PathManager pathManager) {
        this.file = file;
        this.pathManager = pathManager;
        x = point.x;
        y = point.y;
        this.mapName = mapName;
    }

    /**
     * Returns the coordinate of the remembered exit.
     * @return the coordinate
     */
    @NotNull
    public Point getMapCoordinate() {
        return new Point(x, y);
    }

    /**
     * Updates exit information.
     * @param gameObject the exit game object to update
     * @param updateName whether the exit's name should be updated
     * @param sourceMapFile the file of the map containing
     * <code>gameObject</code> or <code>null</code> if on an unsaved map
     */
    public void updateExitObject(@NotNull final BaseObject<?, ?, ?, ?> gameObject, final boolean updateName, @Nullable final File sourceMapFile) {
        final String targetMapPath = pathManager.getMapPath(file);
        final String mapPath;
        if (sourceMapFile == null) {
            // unsaved source map => absolute reference
            mapPath = targetMapPath;
        } else {
            final String sourceMapPath = pathManager.getMapPath(sourceMapFile);
            final String sourceMapComponent = getMapComponent(sourceMapPath);
            final String targetMapComponent = getMapComponent(targetMapPath);
            if (sourceMapComponent == null ? targetMapComponent == null : sourceMapComponent.equals(targetMapComponent)) {
                // relative reference
                mapPath = PathManager.absoluteToRelative(sourceMapPath, targetMapPath);
            } else {
                // absolute reference
                mapPath = targetMapPath;
            }
        }
        gameObject.setAttributeString(BaseObject.SLAYING, mapPath);
        gameObject.setAttributeInt(BaseObject.HP, x);
        gameObject.setAttributeInt(BaseObject.SP, y);
        if (updateName) {
            gameObject.setAttributeString(BaseObject.NAME, mapName);
        }
    }

    /**
     * Returns the initial path component of a map path. "/path/to/map" returns
     * "path", "/map" returns <code>null</code>.
     * @param mapPath the map path
     * @return the initial path component
     */
    @Nullable
    private static String getMapComponent(@NotNull final String mapPath) {
        if (!mapPath.startsWith("/")) {
            return null;
        }

        final int index = mapPath.indexOf('/', 1);
        if (index == -1) {
            return null;
        }

        return mapPath.substring(1, index);
    }

    /**
     * Returns The file of the map that contains the remembered exit.
     * @return the file
     */
    @NotNull
    public File getFile() {
        return file;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        final ExitLocation exitLocation = (ExitLocation) obj;
        return x == exitLocation.x && y == exitLocation.y && file.equals(exitLocation.file) && mapName.equals(exitLocation.mapName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return x ^ (y << 16) ^ (y >> 16) ^ file.hashCode() ^ mapName.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return file + " " + x + "/" + y + " " + mapName;
    }

}
