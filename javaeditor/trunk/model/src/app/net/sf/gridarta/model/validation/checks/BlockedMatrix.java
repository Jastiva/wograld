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

package net.sf.gridarta.model.validation.checks;

import java.awt.Point;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * Determines whether a map square is blocked.
 * @author Andreas Kirschbaum
 */
public class BlockedMatrix<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The blocked grid of the currently processed map: <code>blocked[x][y]</code>
     * is set if and only if the corresponding map square contains at least one
     * object which has "no_pass" set.
     */
    private final boolean[][] blocked;

    /**
     * Creates a new instance.
     * @param mapModel the map model to operate on
     */
    public BlockedMatrix(@NotNull final MapModel<G, A, R> mapModel) {
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        final int width = mapSize.getWidth();
        final int height = mapSize.getHeight();
        blocked = new boolean[width][height];
        final Point pos = new Point();
        for (pos.y = 0; pos.y < height; pos.y++) {
            for (pos.x = 0; pos.x < width; pos.x++) {
                for (final BaseObject<G, A, R, ?> gameObject : mapModel.getMapSquare(pos)) {
                    if (gameObject.getAttributeInt(BaseObject.NO_PASS) != 0) {
                        blocked[pos.x][pos.y] = true;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Returns whether a given location is blocked.
     * @param x the x coordinate to check
     * @param y the y coordinate to check
     * @return whether the square is blocked
     */
    public boolean isBlocked(final int x, final int y) {
        try {
            return blocked[x][y];
        } catch (final IndexOutOfBoundsException ignored) {
            return true;
        }
    }

}
