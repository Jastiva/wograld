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

package net.sf.gridarta.model.validation.errors;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Validation error that's used when the EnvironmentChecker detected a possible
 * error on the map.
 * @author Andreas Kirschbaum
 */
public class EnvironmentInvError<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends GameObjectValidationError<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The type's description.
     */
    @NotNull
    private final String typeDescription;

    /**
     * The environment type's description.
     */
    @NotNull
    private final String envTypeDescription;

    /**
     * Creates a new instance.
     * @param gameObject the game object on which the error occurred
     * @param typeDescription the type's description
     * @param envTypeDescription the environment type's description
     */
    public EnvironmentInvError(@NotNull final G gameObject, @NotNull final String typeDescription, @NotNull final String envTypeDescription) {
        super(gameObject);
        this.typeDescription = typeDescription;
        this.envTypeDescription = envTypeDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getParameter(final int id) {
        switch (id) {
        case 0:
            return typeDescription;

        case 1:
            return envTypeDescription;

        default:
            return null;
        }
    }

}
