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

package net.sf.gridarta.var.atrinik.model.scripts;

import net.sf.gridarta.gui.scripts.ScriptedEventEditor;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.scripts.AbstractScriptedEventFactory;
import net.sf.gridarta.model.scripts.ScriptArchUtils;
import net.sf.gridarta.model.scripts.ScriptedEvent;
import net.sf.gridarta.model.scripts.UndefinedEventArchetypeException;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Implements an {@link AbstractScriptedEventFactory} for creating Crossfire
 * related instances.
 * @author Andreas Kirschbaum
 */
public class DefaultScriptedEventFactory extends AbstractScriptedEventFactory<GameObject, MapArchObject, Archetype> {

    /**
     * The script arch utils instance to use.
     */
    @NotNull
    private final ScriptArchUtils scriptArchUtils;

    /**
     * The attribute name for the subtype field.
     */
    @NotNull
    private final String subtypeAttribute;

    /**
     * The {@link ScriptedEventEditor} to use.
     */
    @NotNull
    private final ScriptedEventEditor<GameObject, MapArchObject, Archetype> scriptedEventEditor;

    /**
     * Creates a new instance.
     * @param scriptArchUtils the script arch utils instance to use
     * @param subtypeAttribute the attribute name for the subtype field
     * @param gameObjectFactory the game object factory for creating game
     * objects
     * @param scriptedEventEditor the scripted event editor to use
     * @param archetypeSet the archetype set to use
     */
    public DefaultScriptedEventFactory(@NotNull final ScriptArchUtils scriptArchUtils, @NotNull final String subtypeAttribute, @NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final ScriptedEventEditor<GameObject, MapArchObject, Archetype> scriptedEventEditor, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet) {
        super(scriptArchUtils, gameObjectFactory, archetypeSet);
        this.scriptArchUtils = scriptArchUtils;
        this.subtypeAttribute = subtypeAttribute;
        this.scriptedEventEditor = scriptedEventEditor;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ScriptedEvent<GameObject, MapArchObject, Archetype> newScriptedEvent(final int eventType, @NotNull final String pluginName, @NotNull final String scriptPath, @NotNull final String options) throws UndefinedEventArchetypeException {
        return new DefaultScriptedEvent(scriptArchUtils, eventType, subtypeAttribute, pluginName, scriptPath, options, this, scriptedEventEditor);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ScriptedEvent<GameObject, MapArchObject, Archetype> newScriptedEvent(final GameObject event) {
        return new DefaultScriptedEvent(scriptArchUtils, event, subtypeAttribute, scriptedEventEditor);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public GameObject newEventGameObject(final int eventType) throws UndefinedEventArchetypeException {
        final GameObject eventGameObject = super.newEventGameObject(eventType);
        eventGameObject.setAttributeInt(subtypeAttribute, eventType);
        return eventGameObject;
    }

}
