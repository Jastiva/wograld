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
 * Validation error indicating a game object with a custom type.
 * @author Andreas Kirschbaum
 */
public class CustomTypeError<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends GameObjectValidationError<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The archetype's type number.
     */
    private final int archetypeType;

    /**
     * The game object's type number.
     */
    private final int gameObjectType;

    /**
     * Create a new instance.
     * @param gameObject The GameObject on which the error occurred.
     * @param archetypeType The archetype's type number.
     * @param gameObjectType The game object's type number.
     */
    public CustomTypeError(@NotNull final G gameObject, final int archetypeType, final int gameObjectType) {
        super(gameObject);
        this.archetypeType = archetypeType;
        this.gameObjectType = gameObjectType;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getParameter(final int id) {
        switch (id) {
        case 0:
            return Integer.toString(archetypeType);

        case 1:
            return Integer.toString(gameObjectType);

        default:
            return null;
        }
    }

}
