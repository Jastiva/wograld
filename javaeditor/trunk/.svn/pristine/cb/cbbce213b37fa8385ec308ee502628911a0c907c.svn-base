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

/**
 * Validation error that's used when a map has wrong exit paths.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ExitError<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends GameObjectValidationError<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The exit path that was wrong.
     */
    @NotNull
    private final String exitPath;

    /**
     * Creates a new instance.
     * @param gameObject the GameObject on which the error occurred
     * @param exitPath exit path which was wrong
     */
    public ExitError(@NotNull final G gameObject, @NotNull final String exitPath) {
        super(gameObject);
        this.exitPath = exitPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameter(final int id) {
        switch (id) {
        case 0:
            return exitPath;

        default:
            return null;
        }
    }

}
