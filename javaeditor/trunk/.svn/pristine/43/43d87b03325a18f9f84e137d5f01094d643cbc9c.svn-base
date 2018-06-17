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
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.GameObjectValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.MobOutsideSpawnPointError;
import org.jetbrains.annotations.NotNull;

/**
 * GameObjectValidator to assert that mobs are not outside spawn points.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class MobOutsideSpawnPointChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements GameObjectValidator<G, A, R> {

    /**
     * The object types to check.
     */
    @NotNull
    private final Collection<Integer> typeNumbers = new HashSet<Integer>();

    /**
     * Create a new instance.
     * @param validatorPreferences the validator preferences to use
     * @param typeNumbers The object types to check.
     */
    public MobOutsideSpawnPointChecker(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final Integer... typeNumbers) {
        super(validatorPreferences);
        this.typeNumbers.addAll(Arrays.asList(typeNumbers));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGameObject(@NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        if (typeNumbers.contains(gameObject.getTypeNo())) {
            errorCollector.collect(new MobOutsideSpawnPointError<G, A, R>(gameObject));
        }
    }

}
