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
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.PaidItemShopSquareError;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link net.sf.gridarta.model.validation.MapValidator} to detect shop
 * squares that contain paid items.
 * @author Andreas Kirschbaum
 */
public class PaidItemShopSquareChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractShopSquareChecker<G, A, R> {

    /**
     * The {@link GameObjectMatcher} for finding shop squares.
     */
    @NotNull
    private final GameObjectMatcher shopMatcher;

    /**
     * The {@link GameObjectMatcher} for finding paid items.
     */
    @NotNull
    private final GameObjectMatcher paidItemMatcher;

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     * @param shopMatcher the game object matcher for finding shop squares
     * @param paidItemMatcher the game object matcher for finding paid items
     */
    public PaidItemShopSquareChecker(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final GameObjectMatcher shopMatcher, @NotNull final GameObjectMatcher paidItemMatcher) {
        super(validatorPreferences);
        this.shopMatcher = shopMatcher;
        this.paidItemMatcher = paidItemMatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMap(@NotNull final MapModel<G, A, R> mapModel, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final boolean[][] shopSquares = findMatchingSquares(mapModel, shopMatcher);
        final boolean[][] noSpellsSquares = findMatchingSquares(mapModel, paidItemMatcher);
        final Point point = new Point();
        for (point.x = 0; point.x < shopSquares.length && point.x < noSpellsSquares.length; point.x++) {
            for (point.y = 0; point.y < shopSquares[point.x].length && point.y < noSpellsSquares[point.x].length; point.y++) {
                if (shopSquares[point.x][point.y] && noSpellsSquares[point.x][point.y]) {
                    errorCollector.collect(new PaidItemShopSquareError<G, A, R>(mapModel.getMapSquare(point)));
                }
            }
        }
    }

}
