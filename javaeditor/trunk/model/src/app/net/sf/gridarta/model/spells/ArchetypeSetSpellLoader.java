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
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Extract spell definitions from an archetype set.
 * @author Andreas Kirschbaum
 */
public class ArchetypeSetSpellLoader<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(ArchetypeSetSpellLoader.class);

    /**
     * The {@link GameObjectFactory} for creating {@link GameObject
     * GameObjects}.
     */
    @NotNull
    private final GameObjectFactory<G, A, R> gameObjectFactory;

    /**
     * Creates a new instance.
     * @param gameObjectFactory the game object factory for creating game
     * objects
     */
    public ArchetypeSetSpellLoader(@NotNull final GameObjectFactory<G, A, R> gameObjectFactory) {
        this.gameObjectFactory = gameObjectFactory;
    }

    /**
     * Find all game object that describe spells and add corresponding {@link
     * Spell} objects.
     * @param archetypeSet the archetype set to scan
     * @param typeNo the type number to search for
     * @param spells the <code>Spells</code> instance to add the spells to
     */
    public void load(@NotNull final ArchetypeSet<G, A, R> archetypeSet, final int typeNo, @NotNull final Spells<GameObjectSpell<G, A, R>> spells) {
        int numSpells = 0;
        for (final R archetype : archetypeSet.getArchetypes()) {
            if (archetype.getTypeNo() == typeNo) {
                spells.add(new GameObjectSpell<G, A, R>(archetype, gameObjectFactory));
                numSpells++;
            }
        }

        if (log.isInfoEnabled()) {
            log.info("Found " + numSpells + " defined spells from archetypes.");
        }
    }

}
