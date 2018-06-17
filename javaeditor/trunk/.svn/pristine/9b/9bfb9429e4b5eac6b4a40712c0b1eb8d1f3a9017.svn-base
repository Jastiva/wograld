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

package net.sf.gridarta.var.crossfire.model.archetype;

import net.sf.gridarta.model.archetype.AbstractArchetypeBuilder;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link AbstractArchetypeBuilder} for Daimonin archetypes.
 * @author Andreas Kirschbaum
 */
public class DefaultArchetypeBuilder extends AbstractArchetypeBuilder<GameObject, MapArchObject, Archetype> {

    /**
     * Creates a new instance.
     * @param gameObjectFactory the game object factory for creating the new
     * archetype
     */
    public DefaultArchetypeBuilder(@NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory) {
        super(gameObjectFactory);
    }

    public void setLoreText(@NotNull final CharSequence line) {
        getArchetype().setLoreText(line);
    }

}
