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

package net.sf.gridarta.gui.dialog.shrinkmapsize;

import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class to remove empty squares from a map's border.
 * @author Andreas Kirschbaum
 */
public class ShrinkMapSizeUtils {

    /**
     * Flag value: remove empty space from east border.
     */
    public static final int SHRINK_EAST = 2;

    /**
     * Flag value: remove empty space from south border.
     */
    public static final int SHRINK_SOUTH = 4;

    /**
     * Private constructor to prevent instantiation.
     */
    private ShrinkMapSizeUtils() {
    }

    /**
     * Removes empty squares from a map's border.
     * @param mapModel the map to modify
     * @param shrinkFlags the borders to modify
     */
    public static void shrinkMap(@NotNull final MapModel<?, ?, ?> mapModel, final int shrinkFlags) {
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        int filledWidth = mapSize.getWidth();
        int filledHeight = mapSize.getHeight();
        if ((shrinkFlags & SHRINK_EAST) != 0) {
            while (filledWidth > 1 && mapModel.isAreaEmpty(filledWidth - 1, 0, 1, filledHeight)) {
                filledWidth--;
            }
        }
        if ((shrinkFlags & SHRINK_SOUTH) != 0) {
            while (filledHeight > 1 && mapModel.isAreaEmpty(0, filledHeight - 1, filledWidth, 1)) {
                filledHeight--;
            }
        }
        mapModel.beginTransaction("Shrink Map Size");
        try {
            mapModel.getMapArchObject().setMapSize(new Size2D(filledWidth, filledHeight));
        } finally {
            mapModel.endTransaction();
        }
    }

    /**
     * Returns which borders contain empty squares.
     * @param mapModel the map to check
     * @return the borders having empty squares
     */
    public static int getShrinkFlags(@NotNull final MapModel<?, ?, ?> mapModel) {
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        final int mapWidth = mapSize.getWidth();
        final int mapHeight = mapSize.getHeight();
        int shrinkFlags = 0;
        /*
                if (mapModel.isAreaEmpty(0, 0, mapWidth, 1)) {
                    shrinkFlags |= SHRINK_NORTH;
                }
        */
        if (mapModel.isAreaEmpty(mapWidth - 1, 0, 1, mapHeight)) {
            shrinkFlags |= SHRINK_EAST;
        }
        if (mapModel.isAreaEmpty(0, mapHeight - 1, mapWidth, 1)) {
            shrinkFlags |= SHRINK_SOUTH;
        }
        /*
                if (mapModel.isAreaEmpty(0, 0, 1, mapHeight)) {
                    shrinkFlags |= SHRINK_WEST;
                }
        */
        return shrinkFlags;
    }

}
