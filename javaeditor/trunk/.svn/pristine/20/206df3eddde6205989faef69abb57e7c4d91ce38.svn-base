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

package net.sf.gridarta.var.crossfire.model.validation.checks;

import net.sf.gridarta.model.maplocation.MapLocation;
import net.sf.gridarta.model.maplocation.NoExitPathException;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.GameObjectValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.checks.NonAbsoluteExitPathError;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link net.sf.gridarta.model.validation.MapValidator} that checks for
 * relative exit paths within unique maps. Such paths do not work reliably in
 * Crossfire and Atrinik.
 * @author Andreas Kirschbaum
 */
public class NonAbsoluteExitPathChecker extends AbstractValidator<GameObject, MapArchObject, Archetype> implements GameObjectValidator<GameObject, MapArchObject, Archetype> {

    /**
     * The {@link GameObjectMatcher} for matching exit objects.
     */
    @NotNull
    private final GameObjectMatcher exitGameObjectMatcher;

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     * @param exitGameObjectMatcher the game object matcher for matching exit
     * objects
     */
    public NonAbsoluteExitPathChecker(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final GameObjectMatcher exitGameObjectMatcher) {
        super(validatorPreferences);
        this.exitGameObjectMatcher = exitGameObjectMatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGameObject(@NotNull final GameObject gameObject, @NotNull final ErrorCollector<GameObject, MapArchObject, Archetype> errorCollector) {
        if (!exitGameObjectMatcher.isMatching(gameObject)) {
            return; // not an exit => skip
        }

        final MapSquare<GameObject, MapArchObject, Archetype> mapSquare = gameObject.getMapSquare();
        if (mapSquare == null) {
            return; // not on a map => skip
        }

        final MapModel<GameObject, MapArchObject, Archetype> mapModel = mapSquare.getMapModel();
        final MapArchObject mapArchObject = mapModel.getMapArchObject();
        if (!mapArchObject.isUnique()) {
            return; // not on an unique map => skip
        }

        final String exitPath;
        try {
            exitPath = MapLocation.getMapPath(gameObject, true);
        } catch (final NoExitPathException ignored) {
            return; // unset exit path => skip
        }
        if (exitPath.isEmpty() || exitPath.startsWith("/")) {
            return; // unset exit path or absolute exit path => ok
        }

        errorCollector.collect(new NonAbsoluteExitPathError<GameObject, MapArchObject, Archetype>(gameObject, exitPath));
    }

}
