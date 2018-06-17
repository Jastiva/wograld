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

import java.awt.Component;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Validation error that's used when the EmptySpawnPointChecker detected a
 * possible error on the map.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class EmptySpawnPointError<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends GameObjectValidationError<G, A, R> implements CorrectableError {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create an EmptySpawnPointError.
     * @param gameObject the GameObject on which the error occurred
     */
    public EmptySpawnPointError(@NotNull final G gameObject) {
        super(gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void correct(@NotNull final Component parentComponent) {
        // TODO: Ask the user whether the spawn point should be deleted or whether he wants to add a mob inside.
        // In case a mob was found on the same square, the user should be asked whether it is this mob that he wants to move inside
    }

}
