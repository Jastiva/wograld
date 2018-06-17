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

import java.util.HashMap;
import java.util.Map;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.SquareValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.DoubleTypeError;
import org.jetbrains.annotations.NotNull;

/**
 * A SquareValidator to assert that there are no two arches of the same type on
 * the same square.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DoubleTypeChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements SquareValidator<G, A, R> {

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     */
    public DoubleTypeChecker(@NotNull final ValidatorPreferences validatorPreferences) {
        super(validatorPreferences);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateSquare(@NotNull final MapSquare<G, A, R> mapSquare, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final Map<Integer, G> gameObjects = new HashMap<Integer, G>();
        final Map<Integer, DoubleTypeError<G, A, R>> errors = new HashMap<Integer, DoubleTypeError<G, A, R>>();
        for (final G gameObject : mapSquare) {
            final Integer typeNo = gameObject.getTypeNo();
            final DoubleTypeError<G, A, R> existingError = errors.get(typeNo);
            if (existingError != null) {
                existingError.addGameObject(gameObject);
            } else {
                final G existingGameObject = gameObjects.get(typeNo);
                if (existingGameObject != null) {
                    final DoubleTypeError<G, A, R> error = new DoubleTypeError<G, A, R>(mapSquare, existingGameObject);
                    errors.put(typeNo, error);
                    errorCollector.collect(error);
                } else {
                    gameObjects.put(typeNo, gameObject);
                }
            }
        }
    }

}
