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

package net.sf.gridarta.model.floodfill;

import java.awt.Point;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.utils.RandomUtils;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * Implements flood-filling. <p>Algorithmic notes: This algorithm replaces the
 * recursive algorithm used before. This algorithm might not look as efficient
 * as the previous recursive one, but it is. Especially, this algorithm uses
 * very low memory (map size bytes) and the stack amount used is very low and
 * constant. This algorithm behaves extremely fast filling from low x / low y to
 * high x / high y but not so fast when filling in the opposite direction. The
 * algorithm has between linear and quadratic performance, proportional to the
 * map size and depending on the start point for the fill. The effective
 * performance is nearly linear because the creation of arches is the far most
 * cost intensive part, and that's independent of the algorithm. The algorithm
 * needs 5-8 ms to fill an entire 24x24 map. The algorithm needs 250-700 ms to
 * fill an entire 240x240 map. The algorithm needs 1300-2800 ms to fill an
 * entire 480x480 map. More efficient algorithms are known to exist. This
 * algorithm works. But: <ul> <li>This algorithm is fast enough (usually, maps
 * are 24x24). <li>In contrast to the previous algorithm, this algorithm does
 * not cause a StackOverflow on large maps. </ul>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class FloodFill<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Constants for lookArea.
     */
    private static final byte NOT_LOOKED_AT = (byte) 0;

    private static final byte BORDER = (byte) 1;

    private static final byte WAS_EMPTY = (byte) 2;

    private static final byte BLOCKED = (byte) 3;

    /**
     * Flood-fill the map.
     * @param mapModel the map model to fill
     * @param start starting point for flood-fill
     * @param archList GameObject list to fill with
     * @param insertionModeSet the insertion mode set to use
     */
    public void floodFill(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Point start, @NotNull final List<? extends BaseObject<G, A, R, ?>> archList, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
        final MapArchObject<A> mapArchObject = mapModel.getMapArchObject();
        final Size2D mapSize = mapArchObject.getMapSize();
        final byte[][] area = new byte[mapSize.getWidth()][mapSize.getHeight()];
        area[start.x][start.y] = BORDER;
        mapModel.beginTransaction("Flood Fill"); // TODO: I18N/L10N
        try {
            int border = 1;
            while (border > 0) {
                final Point p = new Point();
                for (p.x = 0; p.x < mapSize.getWidth(); p.x++) {
                    for (p.y = 0; p.y < mapSize.getHeight(); p.y++) {
                        if (area[p.x][p.y] == BORDER) {
                            border--;
                            if (mapArchObject.isPointValid(p) && mapModel.getMapSquare(p).isEmpty()) {
                                area[p.x][p.y] = WAS_EMPTY;
                                final BaseObject<G, A, R, ?> gameObject = archList.get(RandomUtils.rnd.nextInt(archList.size()));
                                mapModel.insertBaseObject(gameObject, p, false, false, insertionModeSet.getTopmostInsertionMode());
                                //noinspection ProhibitedExceptionCaught
                                try {
                                    if (area[p.x - 1][p.y] == NOT_LOOKED_AT) {
                                        area[p.x - 1][p.y] = BORDER;
                                        border++;
                                    }
                                } catch (final ArrayIndexOutOfBoundsException ignored) {
                                    // ignore
                                }
                                //noinspection ProhibitedExceptionCaught
                                try {
                                    if (area[p.x + 1][p.y] == NOT_LOOKED_AT) {
                                        area[p.x + 1][p.y] = BORDER;
                                        border++;
                                    }
                                } catch (final ArrayIndexOutOfBoundsException ignored) {
                                    // ignore
                                }
                                //noinspection ProhibitedExceptionCaught
                                try {
                                    if (area[p.x][p.y - 1] == NOT_LOOKED_AT) {
                                        area[p.x][p.y - 1] = BORDER;
                                        border++;
                                    }
                                } catch (final ArrayIndexOutOfBoundsException ignored) {
                                    // ignore
                                }
                                //noinspection ProhibitedExceptionCaught
                                try {
                                    if (area[p.x][p.y + 1] == NOT_LOOKED_AT) {
                                        area[p.x][p.y + 1] = BORDER;
                                        border++;
                                    }
                                } catch (final ArrayIndexOutOfBoundsException ignored) {
                                    // ignore
                                }
                            } else {
                                area[p.x][p.y] = BLOCKED;
                            }
                        }
                    }
                }
            }
        } finally {
            mapModel.endTransaction();
        }
    }
}
