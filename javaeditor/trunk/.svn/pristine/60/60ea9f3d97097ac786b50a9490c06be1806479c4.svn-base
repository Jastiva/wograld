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
import net.sf.gridarta.model.gameobject.FaceSource;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.GameObjectValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.UndefinedAnimError;
import net.sf.gridarta.model.validation.errors.UndefinedFaceError;
import org.jetbrains.annotations.NotNull;

/**
 * Checks that a game object does not reference an undefined face.
 * @author Andreas Kirschbaum
 */
public class UndefinedFaceChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements GameObjectValidator<G, A, R> {

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     */
    public UndefinedFaceChecker(@NotNull final ValidatorPreferences validatorPreferences) {
        super(validatorPreferences);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGameObject(@NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        if (gameObject.getFaceObjSource() == FaceSource.FACE_NOT_FOUND) {
            final String animName = gameObject.getAnimName();
            if (animName != null && !animName.equals("NONE")) {
                errorCollector.collect(new UndefinedAnimError<G, A, R>(gameObject, animName));
            } else {
                final String faceName = gameObject.getFaceName();
                if (faceName != null) {
                    errorCollector.collect(new UndefinedFaceError<G, A, R>(gameObject, faceName));
                }
            }
        }
    }

}
