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

import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.io.GameObjectParserFactory;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link GameObjectParserFactory} which creates Crossfire objects.
 * @author Andreas Kirschbaum
 */
public class DefaultGameObjectParserFactory implements GameObjectParserFactory<GameObject, MapArchObject, Archetype> {

    /**
     * The {@link GameObjectFactory} instance.
     */
    @NotNull
    private final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory;

    /**
     * The {@link ArchetypeSet} for looking up archetypes.
     */
    @NotNull
    private final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet;

    /**
     * Creates a new instance.
     * @param gameObjectFactory the game object factory to use
     * @param archetypeSet the archetype set for looking up archetypes
     */
    public DefaultGameObjectParserFactory(@NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet) {
        this.gameObjectFactory = gameObjectFactory;
        this.archetypeSet = archetypeSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameObjectParser<GameObject, MapArchObject, Archetype> newGameObjectParser() {
        return new DefaultGameObjectParser(gameObjectFactory, archetypeSet);
    }

}
