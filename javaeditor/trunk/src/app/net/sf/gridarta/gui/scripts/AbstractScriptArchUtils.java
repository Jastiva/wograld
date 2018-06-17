/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.scripts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JList;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.scripts.ScriptArchUtils;
import net.sf.gridarta.utils.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractScriptArchUtils implements ScriptArchUtils {

    /**
     * The attribute name for the subtype field.
     * @serial
     */
    @NotNull
    private final String subtypeAttribute;

    /**
     * The object type for event objects.
     * @serial
     */
    private final int eventTypeNo;

    /**
     * Maps event type to event name.
     */
    @NotNull
    private final Map<Integer, String> allEventTypes = new HashMap<Integer, String>();

    /**
     * Maps index into {@link #eventNames} to event type.
     */
    @NotNull
    private final Map<Integer, Integer> indexToEventType = new HashMap<Integer, Integer>();

    /**
     * All event names.
     */
    @NotNull
    private final List<String> eventNames = new ArrayList<String>();

    /**
     * Creates a new instance.
     * @param eventTypes the event types
     * @param subtypeAttribute the attribute name for the subtype field
     * @param eventTypeNo the object type for event objects
     */
    protected AbstractScriptArchUtils(@NotNull final Iterable<Pair<Integer, String>> eventTypes, @NotNull final String subtypeAttribute, final int eventTypeNo) {
        this.subtypeAttribute = subtypeAttribute;
        this.eventTypeNo = eventTypeNo;
        for (final Pair<Integer, String> pair : eventTypes) {
            final int eventType = pair.getFirst();
            final String eventName = pair.getSecond();
            add(eventType, eventName);
        }
    }

    /**
     * Adds on event description.
     * @param eventType the event type
     * @param eventName the event name
     */
    private void add(final int eventType, @NotNull final String eventName) {
        assert !allEventTypes.containsKey(eventType);
        allEventTypes.put(eventType, eventName);
        indexToEventType.put(eventNames.size(), eventType);
        eventNames.add(eventName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String typeName(final int eventType) {
        final String typeName = allEventTypes.get(eventType);
        return typeName != null ? typeName : "<invalid type>";
    }

    /**
     * Returns the type name for an event type.
     * @param eventType the event type
     * @return the type name or <code>null</code> for invalid event types
     */
    @Nullable
    protected String getEventType(final int eventType) {
        return allEventTypes.get(eventType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexToEventType(final int index) {
        //noinspection ProhibitedExceptionCaught
        try {
            return indexToEventType.get(index);
        } catch (final NullPointerException ignored) {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String[] getEventNames() {
        return eventNames.toArray(new String[eventNames.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEventsToJList(@NotNull final JList list, @NotNull final Iterable<? extends GameObject<?, ?, ?>> gameObject) {
        //cher: JList expects Vector, so we MUST use an obsolete concrete collection.
        // noinspection UseOfObsoleteCollectionType
        final Vector<String> content = new Vector<String>();
        for (final BaseObject<?, ?, ?, ?> tmp : gameObject) {
            if (tmp.getTypeNo() == eventTypeNo) {
                content.add(" " + typeName(tmp.getAttributeInt(subtypeAttribute)));
            }
        }

        list.setListData(content);
        list.setSelectedIndex(0);
    }

}
