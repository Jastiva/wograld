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
import java.util.ArrayList;
import java.util.Collection;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.MapValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.BlockedSquareError;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link MapValidator} to assert that a square that's blocked and no_pass is
 * not surrounded by blocked and no_pass only.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @fixme this implementation does not take care of multi square objects.
 */
public class BlockedSquareChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements MapValidator<G, A, R> {

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     */
    public BlockedSquareChecker(@NotNull final ValidatorPreferences validatorPreferences) {
        super(validatorPreferences);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMap(@NotNull final MapModel<G, A, R> mapModel, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        for (final MapSquare<G, A, R> completelyBlockedSquare : findCompletelyBlockedSquares(mapModel)) {
            errorCollector.collect(new BlockedSquareError<G, A, R>(completelyBlockedSquare));
        }
    }

    /**
     * Returns the completely blocked squares of a map.
     * @param mapModel Map Model to search for completely blocked squares
     * @return list with all completely blocked squares
     */
    public static <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> Iterable<MapSquare<G, A, R>> findCompletelyBlockedSquares(@NotNull final MapModel<G, A, R> mapModel) {
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        final int mapWidth = mapSize.getWidth();
        final int mapHeight = mapSize.getHeight();
        // The blockedMatrix is 1 row larger than the map on each border to ease two things:
        // First, it's not necessary to do a bounds check then.
        // Second, it will cope with the future requirement to perform this check with respect of tiled maps.
        final boolean[][] blockedMatrix = new boolean[mapWidth + 2][mapHeight + 2];
        final Point pos = new Point();
        for (int x = 0; x < mapWidth; x++) {
            pos.x = x;
            for (int y = 0; y < mapHeight; y++) {
                pos.y = y;
                if (isBlocked(mapModel.getMapSquare(pos))) {
                    blockedMatrix[x + 1][y + 1] = true;
                }
            }
        }
        final Collection<MapSquare<G, A, R>> completelyBlockedSquares = new ArrayList<MapSquare<G, A, R>>();
        for (int x = 0; x < mapWidth; x++) {
            pos.x = x;
            for (int y = 0; y < mapHeight; y++) {
                pos.y = y;
                if (and9x9(blockedMatrix, x + 1, y + 1)) {
                    completelyBlockedSquares.add(mapModel.getMapSquare(pos));
                }
            }
        }
        return completelyBlockedSquares;
    }

    /**
     * Returns the <code>AND</code> of a 9x9 field denoted by <var>x</var> and
     * <var>y</var> in <var>matrix</var>.
     * @param matrix Matrix to look at
     * @param x x coordinate of 9x9 center
     * @param y y coordinate of 9x9 center
     * @return <code>AND</code> result of the 9x9 matrix around and with
     *         <var>matrix[x][y]</var>.
     * @throws ArrayIndexOutOfBoundsException if <code><var>x</var> &lt;
     * 1</code>, <code><var>y</var> &lt; 1</code>, <code><var>x</var> >= width -
     * 1</code> or <code><var>y</var> >= height - 1</code>.
     */
    private static boolean and9x9(@NotNull final boolean[][] matrix, final int x, final int y) {
        return matrix[x - 1][y - 1] && matrix[x - 1][y] && matrix[x - 1][y + 1] && matrix[x][y - 1] && matrix[x][y] && matrix[x][y + 1] && matrix[x + 1][y - 1] && matrix[x + 1][y] && matrix[x + 1][y + 1];
    }

    /**
     * Return whether a square is blocked and impassable. A square is blocked if
     * it contains a GameObject with "<code>blocksview 1</code>". A square is
     * impassable if it contains a GameObject with "<code>no_pass 1</code>".
     * @param square MapSquare to check
     * @return <code>true</code> if the square is blocking view and passage
     */
    private static boolean isBlocked(@NotNull final Iterable<? extends GameObject<?, ?, ?>> square) {
        boolean noPass = false;
        boolean blocksView = false;
        for (final BaseObject<?, ?, ?, ?> gameObject : square) {
            if (!noPass) {
                noPass = gameObject.getAttributeInt(BaseObject.NO_PASS) == 1;
            }
            if (!blocksView) {
                blocksView = gameObject.getAttributeInt(BaseObject.BLOCKSVIEW) == 1;
            }
            if (noPass && blocksView) {
                return true;
            }
        }
        return false;
    }

}
