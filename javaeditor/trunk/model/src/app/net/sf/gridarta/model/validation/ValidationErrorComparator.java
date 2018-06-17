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

package net.sf.gridarta.model.validation;

import java.io.Serializable;
import java.util.Comparator;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.errors.ValidationError;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Comparator} that compares {@link ValidationError} instances for
 * display ordering.
 * @author Andreas Kirschbaum
 */
public class ValidationErrorComparator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Comparator<ValidationError<G, A, R>>, Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(@NotNull final ValidationError<G, A, R> o1, @NotNull final ValidationError<G, A, R> o2) {
        final MapModel<G, A, R> mapModel1 = o1.getMapModel();
        final MapModel<G, A, R> mapModel2 = o2.getMapModel();
        if (mapModel1 == mapModel2) {
            final int minY1 = getMinY(o1);
            final int minY2 = getMinY(o2);
            if (minY1 != minY2) {
                return minY1 - minY2;
            }

            final int minX1 = getMinX(o1, minY1);
            final int minX2 = getMinX(o2, minY2);
            if (minX1 != minX2) {
                return minX1 - minX2;
            }
        }

        return System.identityHashCode(mapModel1) - System.identityHashCode(mapModel2);
    }

    /**
     * Returns the minimal y coordinate of a {@link ValidationError}.
     * @param validationError the validation error
     * @return the minimal y coordinate
     */
    private int getMinY(@NotNull final ValidationError<G, A, R> validationError) {
        int result = Integer.MAX_VALUE;
        for (final MapSquare<G, A, R> mapSquare : validationError.getMapSquares()) {
            final int y = mapSquare.getMapY();
            if (result > y) {
                result = y;
            }
        }
        for (final G gameObject : validationError.getGameObjects()) {
            final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
            assert mapSquare != null;
            final int y = mapSquare.getMapY();
            if (result > y) {
                result = y;
            }
        }
        return result;
    }

    /**
     * Returns the minimal x coordinate of a {@link ValidationError}. Only map
     * squares having the given y coordinate are considered.
     * @param validationError the validation error
     * @param y the y coordinate
     * @return the minimal x coordinate
     */
    private int getMinX(@NotNull final ValidationError<G, A, R> validationError, final int y) {
        int result = Integer.MAX_VALUE;
        for (final MapSquare<G, A, R> mapSquare : validationError.getMapSquares()) {
            if (mapSquare.getMapY() == y) {
                final int x = mapSquare.getMapX();
                if (result > x) {
                    result = x;
                }
            }
        }
        for (final G gameObject : validationError.getGameObjects()) {
            final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
            assert mapSquare != null;
            if (mapSquare.getMapY() == y) {
                final int x = mapSquare.getMapX();
                if (result > x) {
                    result = x;
                }
            }
        }
        return result;
    }

}
