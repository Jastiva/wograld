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
import org.jetbrains.annotations.Nullable;

/**
 * Stores and manages information about scripted events. This data is only
 * needed for those arches with one or more events defined.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @xxx This class is designed very badly, as it combines model and
 * presentation.
 */
public interface ScriptArchData<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Search the owner game object for an event object of the specified event
     * type.
     * @param eventSubtype look for a ScriptedEvent of this type
     * @param gameObject the game object to operate on
     * @return the event game object, or <code>null</code> if no event of this
     *         type exists
     */
    @Nullable
    G getScriptedEvent(int eventSubtype, @NotNull G gameObject);

    /**
     * Returns whether this ScriptArchData is empty (contains no events). (Note
     * that empty ScriptArchData objects always are removed ASAP.)
     * @param gameObject the game object to operate on
     * @return <code>true</code> if this ScriptArchData contains no events,
     *         otherwise <code>false</code>
     */
    boolean isEmpty(@NotNull G gameObject);

}
