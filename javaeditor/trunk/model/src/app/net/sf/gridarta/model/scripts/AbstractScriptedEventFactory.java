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

package net.sf.gridarta.model.scripts;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for{@link ScriptedEventFactory} implementations.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractScriptedEventFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements ScriptedEventFactory<G, A, R> {

    /**
     * The {@link ScriptArchUtils} instance to use.
     */
    @NotNull
    private final ScriptArchUtils scriptArchUtils;

    /**
     * The {@link GameObjectFactory} for creating {@link GameObject
     * GameObjects}.
     */
    @NotNull
    private final GameObjectFactory<G, A, R> gameObjectFactory;

    /**
     * The {@link ArchetypeSet} to use.
     */
    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;

    /**
     * Creates a new instance.
     * @param scriptArchUtils the script arch utils instance to use
     * @param gameObjectFactory the game object factory for creating game
     * objects
     * @param archetypeSet the archetype set to use
     */
    protected AbstractScriptedEventFactory(@NotNull final ScriptArchUtils scriptArchUtils, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final ArchetypeSet<G, A, R> archetypeSet) {
        this.scriptArchUtils = scriptArchUtils;
        this.gameObjectFactory = gameObjectFactory;
        this.archetypeSet = archetypeSet;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public G newEventGameObject(final int eventType) throws UndefinedEventArchetypeException {
        final String eventArchetypeName = scriptArchUtils.getArchetypeNameForEventType(eventType);
        final R eventArchetype;
        try {
            eventArchetype = archetypeSet.getArchetype(eventArchetypeName);
        } catch (final UndefinedArchetypeException ex) {
            throw new UndefinedEventArchetypeNameException(eventArchetypeName, ex);
        }
        return gameObjectFactory.createGameObject(eventArchetype);
    }

}
