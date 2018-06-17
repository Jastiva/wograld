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
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating {@link ScriptedEvent} instances.
 * @author Andreas Kirschbaum
 */
public interface ScriptedEventFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Creates a new {@link ScriptedEvent} instance.
     * @param eventType type of the event
     * @param pluginName name of the plugin
     * @param scriptPath path to the file for this event
     * @param options the options for this event
     * @return the new instance
     * @throws UndefinedEventArchetypeException In case there is no Archetype to
     * create a ScriptedEvent.
     */
    @NotNull
    ScriptedEvent<G, A, R> newScriptedEvent(int eventType, @NotNull String pluginName, @NotNull String scriptPath, @NotNull String options) throws UndefinedEventArchetypeException;

    /**
     * Creates a new {@link ScriptedEvent} instance.
     * @param event the event game object
     * @return the new instance
     */
    @NotNull
    ScriptedEvent<G, A, R> newScriptedEvent(G event);

    /**
     * Return a new event game object for a given event type.
     * @param eventType the event type
     * @return the new event game object
     * @throws UndefinedEventArchetypeException if the event game object cannot
     * be created
     */
    @NotNull
    G newEventGameObject(int eventType) throws UndefinedEventArchetypeException;

}
