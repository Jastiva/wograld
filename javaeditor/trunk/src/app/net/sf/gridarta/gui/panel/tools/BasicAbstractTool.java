/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.panel.tools;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for the default provided tools.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public abstract class BasicAbstractTool<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractTool<G, A, R> {

    /**
     * The ID of this tool.
     */
    @NotNull
    @NonNls
    private final String id;

    /**
     * Create a BasicAbstractTool.
     * @param id the ID (used for {@link #getId()} as well as for the
     * ActionBuilder)
     */
    protected BasicAbstractTool(@NotNull final String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getId() {
        return id;
    }

}
