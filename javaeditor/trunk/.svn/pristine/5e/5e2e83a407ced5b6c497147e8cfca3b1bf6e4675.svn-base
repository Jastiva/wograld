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
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.MapValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link MapValidator} to detect shop squares which allow magic or prayers,
 * or which have adjacent squares that allow magic or prayers.
 * @author Andreas Kirschbaum
 * @fixme this implementation does not take care of multi square objects.
 */
public abstract class AbstractShopSquareChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements MapValidator<G, A, R> {

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     */
    protected AbstractShopSquareChecker(@NotNull final ValidatorPreferences validatorPreferences) {
        super(validatorPreferences);
    }

    /**
     * Returns a <code>boolean</code> array of the map size that marks squares
     * that contain game objects matching a given {@link GameObjectMatcher}.
     * @param mapModel the map to check
     * @param matcher the matcher to use
     * @return the array
     */
    @NotNull
    protected boolean[][] findMatchingSquares(@NotNull final MapModel<G, A, R> mapModel, @NotNull final GameObjectMatcher matcher) {
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        final int mapWidth = mapSize.getWidth();
        final int mapHeight = mapSize.getHeight();
        final boolean[][] matchingSquares = new boolean[mapWidth][mapHeight];
        final Point point = new Point();
        for (point.x = 0; point.x < mapWidth; point.x++) {
            for (point.y = 0; point.y < mapHeight; point.y++) {
                matchingSquares[point.x][point.y] = isMatching(mapModel.getMapSquare(point), matcher);
            }
        }
        return matchingSquares;
    }

    /**
     * Returns whether a map square contains a game object matching a given
     * {@link GameObjectMatcher}.
     * @param mapSquare the map square to check
     * @param matcher the matcher to use
     * @return whether a matching game object was found
     */
    private boolean isMatching(@NotNull final Iterable<G> mapSquare, @NotNull final GameObjectMatcher matcher) {
        for (final GameObject<G, A, R> gameObject : mapSquare) {
            if (matcher.isMatching(gameObject.getHead())) {
                return true;
            }
        }
        return false;
    }

}
