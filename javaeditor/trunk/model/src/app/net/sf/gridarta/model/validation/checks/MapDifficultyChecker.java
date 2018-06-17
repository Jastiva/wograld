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

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.MapValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.MapDifficultyError;
import org.jetbrains.annotations.NotNull;

/**
 * Validator that checks whether the map has a valid difficulty.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class MapDifficultyChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements MapValidator<G, A, R> {

    /**
     * The minimal acceptable difficulty.
     */
    private final int minDifficulty;

    /**
     * The maximal acceptable difficulty.
     */
    private final int maxDifficulty;

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     * @param minDifficulty the minimal acceptable difficulty
     * @param maxDifficulty the maximal acceptable difficulty
     */
    public MapDifficultyChecker(@NotNull final ValidatorPreferences validatorPreferences, final int minDifficulty, final int maxDifficulty) {
        super(validatorPreferences);
        this.minDifficulty = minDifficulty;
        this.maxDifficulty = maxDifficulty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMap(@NotNull final MapModel<G, A, R> mapModel, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final MapArchObject<A> mapArch = mapModel.getMapArchObject();
        final int difficulty = mapArch.getDifficulty();
        if (difficulty < minDifficulty || difficulty > maxDifficulty) {
            errorCollector.collect(new MapDifficultyError<G, A, R>(mapModel, difficulty));
        }
    }

}
