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
 * A {@link MapValidationError} that indicates that the map checker script could
 * not be found.
 * @author Andreas Kirschbaum
 */
public class MapCheckerScriptMissingError<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends MapValidationError<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The command that could not be executed.
     */
    @NotNull
    private final String command;

    /**
     * The failure reason.
     */
    @NotNull
    private final String reason;

    /**
     * Create a MapValidationError.
     * @param mapModel the map on which the error occurred
     * @param command the command that could not be executed
     * @param reason the failure reason
     */
    protected MapCheckerScriptMissingError(@NotNull final MapModel<G, A, R> mapModel, @NotNull final String command, @NotNull final String reason) {
        super(mapModel);
        this.command = command;
        this.reason = reason;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getParameter(final int id) {
        switch (id) {
        case 0:
            return command;

        case 1:
            return reason;

        default:
            return null;
        }
    }

}
