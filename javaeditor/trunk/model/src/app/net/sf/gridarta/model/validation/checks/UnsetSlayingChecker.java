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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.GameObjectValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.UnsetSlayingError;
import org.jetbrains.annotations.NotNull;

/**
 * Checks that a game object does not set a custom type.
 * @author Andreas Kirschbaum
 */
public class UnsetSlayingChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements GameObjectValidator<G, A, R> {

    /**
     * The object types to check.
     */
    @NotNull
    private final Collection<Integer> typeNumbers = new HashSet<Integer>();

    /**
     * The values which do not trigger a warning.
     */
    @NotNull
    private final Collection<String> allowedValues = new HashSet<String>();

    /**
     * Create a new instance.
     * @param validatorPreferences the validator preferences to use
     * @param typeNumbers The object types to check.
     */
    public UnsetSlayingChecker(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final Integer... typeNumbers) {
        super(validatorPreferences);
        allowedValues.add("");
        this.typeNumbers.addAll(Arrays.asList(typeNumbers));
    }

    /**
     * Adds a value which does not trigger a warning.
     * @param value the allowed value
     */
    public void addAllowedValue(@NotNull final String value) {
        allowedValues.add(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGameObject(@NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        if (typeNumbers.contains(gameObject.getTypeNo())) {
            final String slayingArchetype = gameObject.getArchetype().getAttributeString(BaseObject.SLAYING);
            if (!allowedValues.contains(slayingArchetype)) {
                final String slayingObject = gameObject.getAttributeString(BaseObject.SLAYING);
                if (slayingArchetype.equals(slayingObject)) {
                    errorCollector.collect(new UnsetSlayingError<G, A, R>(gameObject));
                }
            }
        }
    }

}
