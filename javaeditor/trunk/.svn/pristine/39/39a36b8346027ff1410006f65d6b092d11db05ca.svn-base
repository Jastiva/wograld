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

package net.sf.gridarta.model.mapmodel;

import java.awt.Point;
import net.sf.gridarta.model.baseobject.BaseObject;
import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown if a game object cannot be inserted into a map.
 * @author Andreas Kirschbaum
 */
public class CannotInsertGameObjectException extends Exception {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * @param archetype the archetypes that cannot be inserted
     * @param point the insertion location
     */
    public CannotInsertGameObjectException(@NotNull final BaseObject<?, ?, ?, ?> archetype, @NotNull final Point point) {
        super(archetype.getBestName() + " at " + point.x + "/" + point.y);
    }

}
