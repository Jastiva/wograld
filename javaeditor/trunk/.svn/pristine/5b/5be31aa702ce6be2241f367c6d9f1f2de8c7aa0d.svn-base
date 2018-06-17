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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.NotNull;

/**
 * A Map Validator that delegates to other MapValidators.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DelegatingMapValidator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements GameObjectValidator<G, A, R>, SquareValidator<G, A, R>, MapValidator<G, A, R>, Iterable<Validator<G, A, R>> {

    /**
     * Map Validators to validate against.
     */
    @NotNull
    private final List<Validator<G, A, R>> validators = new ArrayList<Validator<G, A, R>>();

    /**
     * The default key.
     */
    @NotNull
    public static final String DEFAULT_KEY = "Validator.All";

    /**
     * Create a DelegatingMapValidator for the default key.
     * @param validatorPreferences the validator preferences to use
     */
    public DelegatingMapValidator(@NotNull final ValidatorPreferences validatorPreferences) {
        this(validatorPreferences, DEFAULT_KEY);
    }

    /**
     * Create a DelegatingMapValidator for a specific key.
     * @param validatorPreferences the validator preferences to use
     * @param key key
     */
    private DelegatingMapValidator(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final String key) {
        super(validatorPreferences, key);
    }

    /**
     * Perform all validations on a map.
     * @param mapModel map to validate
     */
    public void validateAll(@NotNull final MapModel<G, A, R> mapModel) {
        final ErrorCollector<G, A, R> errorCollector = new DefaultErrorCollector<G, A, R>();
        validateMap(mapModel, errorCollector);
        for (final MapSquare<G, A, R> mapSquare : mapModel) {
            validateSquare(mapSquare, errorCollector);
            for (final G archObject : mapSquare) {
                validateGameObject(archObject, errorCollector);
                for (final G invObject : archObject.recursive()) {
                    validateGameObject(invObject, errorCollector);
                }
            }
        }
        mapModel.setErrors(errorCollector);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMap(@NotNull final MapModel<G, A, R> mapModel, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        for (final Validator<G, A, R> validator : validators) {
            if (validator.isEnabled()) {
                if (validator instanceof MapValidator) {
                    ((MapValidator<G, A, R>) validator).validateMap(mapModel, errorCollector);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateSquare(@NotNull final MapSquare<G, A, R> mapSquare, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        for (final Validator<G, A, R> validator : validators) {
            if (validator.isEnabled() && validator instanceof SquareValidator) {
                ((SquareValidator<G, A, R>) validator).validateSquare(mapSquare, errorCollector);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGameObject(@NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        for (final Validator<G, A, R> validator : validators) {
            if (validator.isEnabled() && validator instanceof GameObjectValidator) {
                ((GameObjectValidator<G, A, R>) validator).validateGameObject(gameObject, errorCollector);
            }
        }
    }

    /**
     * Validate a set of game objects.
     * @param gameObjects game objects to validate
     * @param errorCollector the rror collector to report errors to
     */
    public void validate(@NotNull final G[] gameObjects, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        for (final G gameObject : gameObjects) {
            validateGameObject(gameObject, errorCollector);
        }
    }

    /**
     * Add a Validator that might to be queried. The Validator is queried for a
     * KeyName and the preferences are queried whether this validator module
     * should or shouldn't be active.
     * @param validator to be queried
     */
    public void addValidator(@NotNull final Validator<G, A, R> validator) {
        validators.add(validator);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<Validator<G, A, R>> iterator() {
        return validators.iterator();
    }

}
