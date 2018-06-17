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

import javax.swing.JList;
import net.sf.gridarta.model.gameobject.GameObject;
import org.jetbrains.annotations.NotNull;

public interface ScriptArchUtils {

    /**
     * Returns a human readable name for an event type.
     * @param eventType the event type
     * @return the event type name
     */
    @NotNull
    String typeName(int eventType);

    /**
     * Returns the archetype for an event type.
     * @param eventType the event type
     * @return the archetype name
     * @throws UndefinedEventArchetypeTypeException if the event type is
     * undefined
     */
    @NotNull
    String getArchetypeNameForEventType(int eventType) throws UndefinedEventArchetypeTypeException;

    /**
     * Converts a combo box index to an event type.
     * @param index the combo box index
     * @return the event type
     */
    int indexToEventType(int index);

    /**
     * Returns all event names.
     * @return all event names; the array may be modified
     */
    @NotNull
    String[] getEventNames();

    /**
     * Set all ScriptedEvents to appear in the given JList This method should be
     * fast because it may be executed when user clicks on map objects.
     * @param list the <code>JList</code>
     * @param gameObject the game object to operate on
     */
    void addEventsToJList(@NotNull final JList list, @NotNull final Iterable<? extends GameObject<?, ?, ?>> gameObject);

}
