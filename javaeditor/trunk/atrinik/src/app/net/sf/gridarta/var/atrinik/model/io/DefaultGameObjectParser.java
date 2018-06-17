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

package net.sf.gridarta.var.atrinik.model.io;

import java.util.LinkedHashMap;
import java.util.Map;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.io.AbstractGameObjectParser;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link net.sf.gridarta.model.io.GameObjectParser} for Atrinik game object
 * instances.
 * @author Andreas Kirschbaum
 */
public class DefaultGameObjectParser extends AbstractGameObjectParser<GameObject, MapArchObject, Archetype> {

    /**
     * Create a new instance.
     * @param gameObjectFactory the game object factory for creating new game
     * object instances
     * @param archetypeSet the archetype set for looking up archetypes
     */
    public DefaultGameObjectParser(@NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet) {
        super(gameObjectFactory, archetypeSet);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Map<String, String> getModifiedFields(@NotNull final GameObject gameObject) {
        final Map<String, String> fields = new LinkedHashMap<String, String>();
        addModifiedFields(gameObject, fields);
        return fields;
    }

}
