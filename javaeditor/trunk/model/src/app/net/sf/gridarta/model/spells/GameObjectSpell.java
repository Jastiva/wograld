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

package net.sf.gridarta.model.spells;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Describes a numbered in-game spell.
 * @author Andreas Kirschbaum
 */
public class GameObjectSpell<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends Spell {

    /**
     * The game object.
     */
    @NotNull
    private final R archetype;

    /**
     * The {@link GameObjectFactory} for creating {@link GameObject
     * GameObjects}.
     */
    @NotNull
    private final GameObjectFactory<G, A, R> gameObjectFactory;

    /**
     * Create a new instance.
     * @param archetype the spell template archetype
     * @param gameObjectFactory the game object factory for creating game
     * objects
     */
    public GameObjectSpell(@NotNull final R archetype, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory) {
        super(getName(archetype));
        this.archetype = archetype;
        this.gameObjectFactory = gameObjectFactory;
    }

    /**
     * Return a copy of the game object.
     * @return the copy of the game object
     */
    @NotNull
    public G createGameObject() {
        return gameObjectFactory.createGameObject(archetype);
    }

    /**
     * Return the archetype name of the spell object.
     * @return the archetype name
     */
    public String getArchetypeName() {
        return archetype.getArchetypeName();
    }

    /**
     * Return the name to use for a given archetype.
     * @param archetype the archetype
     * @return the name
     */
    private static String getName(final BaseObject<?, ?, ?, ?> archetype) {
        final String archObjName = archetype.getObjName();
        final String archetypeName = archetype.getArchetype().getArchetypeName();
        return (archObjName.length() > 0 ? archObjName : archetypeName) + " [" + archetypeName + "]";
    }

}
