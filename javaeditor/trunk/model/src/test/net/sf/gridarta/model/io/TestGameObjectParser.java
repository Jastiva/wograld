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

package net.sf.gridarta.model.io;

import java.util.LinkedHashMap;
import java.util.Map;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link GameObjectParser} for regression tests.
 * @author Andreas Kirschbaum
 */
public class TestGameObjectParser extends AbstractGameObjectParser<TestGameObject, TestMapArchObject, TestArchetype> {

    /**
     * Create a new instance.
     * @param gameObjectFactory the game object factory for creating new game
     * object instances
     * @param archetypeSet the archetype set for looking up archetypes
     */
    public TestGameObjectParser(@NotNull final GameObjectFactory<TestGameObject, TestMapArchObject, TestArchetype> gameObjectFactory, @NotNull final ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> archetypeSet) {
        super(gameObjectFactory, archetypeSet);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Map<String, String> getModifiedFields(@NotNull final TestGameObject gameObject) {
        final Map<String, String> fields = new LinkedHashMap<String, String>();
        addModifiedFields(gameObject, fields);
        return fields;
    }

}
