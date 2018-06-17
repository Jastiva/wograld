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

package net.sf.gridarta.model.maplocation;

import java.awt.Point;
import java.io.File;
import java.util.regex.Pattern;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a location on a map consisting of a map path and a map
 * coordinate.
 * @author Andreas Kirschbaum
 */
public class MapLocation implements Comparable<MapLocation> {

    /**
     * The {@link Pattern} that matches end of lines in random map parameters.
     */
    @NotNull
    private static final Pattern PATTERN_END_OF_LINE = Pattern.compile("[\r\n]+");

    /**
     * The map path.
     */
    @NotNull
    private final String mapPath;

    /**
     * The map coordinate.
     */
    @NotNull
    private final Point mapCoordinate;

    /**
     * Creates a new instance.
     * @param mapPath the map path
     * @param mapXCoordinate the map x-coordinate
     * @param mapYCoordinate the map y-coordinate
     */
    private MapLocation(@NotNull final String mapPath, final int mapXCoordinate, final int mapYCoordinate) {
        this.mapPath = mapPath;
        mapCoordinate = new Point(mapXCoordinate, mapYCoordinate);
    }

    /**
     * Creates a new instance from a {@link BaseObject} instance.
     * @param gameObject the game object
     * @param allowRandomMapParameters whether random map parameters should be
     * considered
     * @throws NoExitPathException if the game object is not a valid exit
     */
    public MapLocation(@NotNull final BaseObject<?, ?, ?, ?> gameObject, final boolean allowRandomMapParameters) throws NoExitPathException {
        this(getMapPath(gameObject, allowRandomMapParameters), getMapX(gameObject), getMapY(gameObject));
    }

    /**
     * Creates a new instance from a {@link BaseObject} instance. The new
     * <code>MapLocation</code> instance includes an absolute map path.
     * @param gameObject the game object
     * @param allowRandomMapParameters whether random map parameters should be
     * considered
     * @param pathManager the path manager for converting relative path names
     * @return the new map location instance
     * @throws NoExitPathException if the game object is not a valid exit
     */
    @NotNull
    public static MapLocation newAbsoluteMapLocation(@NotNull final GameObject<?, ?, ?> gameObject, final boolean allowRandomMapParameters, @NotNull final PathManager pathManager) throws NoExitPathException {
        final String mapPath = getMapPath(gameObject, allowRandomMapParameters);
        if (mapPath.isEmpty()) {
            throw new NoExitPathException(gameObject);
        }
        final String baseMapPath;
        final MapSquare<?, ?, ?> mapSquare = gameObject.getMapSquare();
        if (mapSquare == null) {
            baseMapPath = "/";
        } else {
            final MapModel<?, ?, ?> mapModel = mapSquare.getMapModel();
            final File mapFile = mapModel.getMapFile();
            if (mapFile == null) {
                baseMapPath = "/";
            } else {
                baseMapPath = pathManager.getMapPath(mapFile);
            }
        }
        final String canonicalMapPath = PathManager.relativeToAbsolute(baseMapPath, mapPath);
        final int mapX = getMapX(gameObject);
        final int mapY = getMapY(gameObject);
        return new MapLocation(canonicalMapPath, mapX, mapY);
    }

    /**
     * Returns the map path.
     * @return the map path
     */
    @NotNull
    public String getMapPath() {
        return mapPath;
    }

    /**
     * Returns the map coordinate.
     * @return the map coordinate
     */
    @NotNull
    public Point getMapCoordinate() {
        return new Point(mapCoordinate);
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
        final MapLocation mapLocation = (MapLocation) obj;
        return mapLocation.mapPath.equals(mapPath) && mapLocation.mapCoordinate.equals(mapCoordinate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return mapPath.hashCode() ^ mapCoordinate.hashCode();
    }

    /**
     * Returns the exit x coordinate of a {@link BaseObject}.
     * @param gameObject the game object
     * @return the exit x coordinate
     */
    private static int getMapY(@NotNull final BaseObject<?, ?, ?, ?> gameObject) {
        return gameObject.getAttributeInt(BaseObject.SP);
    }

    /**
     * Returns the exit y coordinate of a {@link BaseObject}.
     * @param gameObject the game object
     * @return the exit y coordinate
     */
    private static int getMapX(@NotNull final BaseObject<?, ?, ?, ?> gameObject) {
        return gameObject.getAttributeInt(BaseObject.HP);
    }

    /**
     * Returns the exit map path of a {@link BaseObject}.
     * @param gameObject the game object
     * @param allowRandomMapParameters whether random maps should be considered
     * @return the exit map path
     * @throws NoExitPathException if the game object is not a valid exit
     */
    public static String getMapPath(@NotNull final BaseObject<?, ?, ?, ?> gameObject, final boolean allowRandomMapParameters) throws NoExitPathException {
        String path = gameObject.getAttributeString(BaseObject.SLAYING);
        if (allowRandomMapParameters && (path.equals("/!") || path.startsWith("/random/"))) {
            // destination is a random map; extract the final non-random map
            path = getRandomMapParameter(gameObject, "final_map");
            if (path == null) {
                throw new NoExitPathException(gameObject);
            }
        }

        return path;
    }

    /**
     * Extracts a parameter value for an exit to a random map.
     * @param gameObject the exit object containing the parameters
     * @param parameterName the parameter name to use
     * @return the value of the given parameter name, or <code>null</code> if
     *         the parameter does not exist
     */
    @Nullable
    private static String getRandomMapParameter(@NotNull final BaseObject<?, ?, ?, ?> gameObject, @NotNull final String parameterName) {
        final String msg = gameObject.getMsgText();
        if (msg == null) {
            return null;
        }

        final String[] lines = PATTERN_END_OF_LINE.split(msg);
        for (final String line : lines) {
            final String[] tmp = StringUtils.PATTERN_SPACES.split(line, 2);
            if (tmp.length == 2 && tmp[0].equals(parameterName)) {
                return tmp[1];
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        return mapCoordinate.x + "/" + mapCoordinate.y + "@" + mapPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("CompareToUsesNonFinalVariable")
    public int compareTo(@NotNull final MapLocation o) {
        final int cmp = mapPath.compareTo(o.mapPath);
        if (cmp != 0) {
            return cmp;
        }
        if (mapCoordinate.x < o.mapCoordinate.x) {
            return -1;
        }
        if (mapCoordinate.x > o.mapCoordinate.x) {
            return -1;
        }
        if (mapCoordinate.y < o.mapCoordinate.y) {
            return -1;
        }
        if (mapCoordinate.y > o.mapCoordinate.y) {
            return -1;
        }
        return 0;
    }

}
