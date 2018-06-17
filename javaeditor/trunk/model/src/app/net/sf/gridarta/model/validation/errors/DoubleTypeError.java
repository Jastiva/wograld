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
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.NotNull;

/**
 * Validation error that's used when the DoubleTypeChecker detected a possible
 * error on the map.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DoubleTypeError<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends GameObjectsValidationError<G, A, R> implements CorrectableError {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create an DoubleTypeError.
     * @param mapSquare the square on which the error occurred
     * @param gameObject the game object on which the error occurred
     */
    public DoubleTypeError(@NotNull final MapSquare<G, A, R> mapSquare, @NotNull final G gameObject) {
        super(mapSquare, gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void correct(@NotNull final Component parentComponent) {
        // TODO: Display the list of all archObjects and ask the user which archObject to keep.
        // Then delete all arches except the selected one.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addGameObject(@NotNull final G gameObject) {
        super.addGameObject(gameObject);
    }

}
