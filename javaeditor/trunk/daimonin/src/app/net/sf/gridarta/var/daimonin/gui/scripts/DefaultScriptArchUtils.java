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

package net.sf.gridarta.var.daimonin.gui.scripts;

import net.sf.gridarta.gui.scripts.AbstractScriptArchUtils;
import net.sf.gridarta.utils.Pair;
import org.jetbrains.annotations.NotNull;

public class DefaultScriptArchUtils extends AbstractScriptArchUtils {

    /**
     * Creates a new instance.
     * @param eventTypes the event types
     * @param subtypeAttribute the attribute name for the subtype field
     * @param eventTypeNo the object type for event objects
     */
    public DefaultScriptArchUtils(@NotNull final Iterable<Pair<Integer, String>> eventTypes, @NotNull final String subtypeAttribute, final int eventTypeNo) {
        super(eventTypes, subtypeAttribute, eventTypeNo);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getArchetypeNameForEventType(final int eventType) {
        return "event_obj";
    }

}
