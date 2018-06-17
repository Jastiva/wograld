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

import java.util.regex.Pattern;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.errors.EnvironmentSensorSlayingError;
import org.jetbrains.annotations.NotNull;

/**
 * An entry to match consisting of a {@link GameObjectMatcher} and a regular
 * expression which defined the allowed value of the "slaying" field.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class Entry<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Selects the game objects to check.
     */
    @NotNull
    private final GameObjectMatcher gameObjectMatcher;

    /**
     * The allowed values of the "slaying" field.
     */
    @NotNull
    private final Pattern regex;

    /**
     * Creates a new instance.
     * @param gameObjectMatcher selects the game objects to check
     * @param regex the allowed values of the "slaying" field
     */
    public Entry(@NotNull final GameObjectMatcher gameObjectMatcher, @NotNull final Pattern regex) {
        this.gameObjectMatcher = gameObjectMatcher;
        this.regex = regex;
    }

    /**
     * Validates one {@link GameObject} instance.
     * @param gameObject the game object instance
     * @param errorCollector the error collector to use
     * @return whether the game object did match
     */
    public boolean validate(@NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        if (!gameObjectMatcher.isMatching(gameObject)) {
            return false;
        }

        final CharSequence slaying = gameObject.getAttributeString(BaseObject.SLAYING, true);
        if (!regex.matcher(slaying).matches()) {
            errorCollector.collect(new EnvironmentSensorSlayingError<G, A, R>(gameObject));
        }
        return true;
    }

}
