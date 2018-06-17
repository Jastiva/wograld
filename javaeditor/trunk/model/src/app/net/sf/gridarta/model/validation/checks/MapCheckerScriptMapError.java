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
import net.sf.gridarta.model.validation.errors.MapValidationError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link MapValidationError} that represents a per map error of the {@link
 * MapCheckerScriptChecker}.
 * @author Andreas Kirschbaum
 */
public class MapCheckerScriptMapError<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends MapValidationError<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The error message.
     */
    @NotNull
    private final String message;

    /**
     * Creates a new instance.
     * @param mapModel the map model
     * @param message the error message
     */
    public MapCheckerScriptMapError(@NotNull final MapModel<G, A, R> mapModel, @NotNull final String message) {
        super(mapModel);
        this.message = message;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getParameter(final int id) {
        switch (id) {
        case 0:
            return message;

        default:
            return null;
        }
    }

}
