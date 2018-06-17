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
import net.sf.gridarta.model.baseobject.BaseObject;
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
public class DefaultScriptArchData<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements ScriptArchData<G, A, R> {

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
     * Create a ScriptArchData.
     * @param subtypeAttribute the attribute name for the subtype field
     * @param eventTypeNo the object type for event objects
     */
    public DefaultScriptArchData(@NotNull final String subtypeAttribute, final int eventTypeNo) {
        this.subtypeAttribute = subtypeAttribute;
        this.eventTypeNo = eventTypeNo;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public G getScriptedEvent(final int eventSubtype, @NotNull final G gameObject) {
        for (final G tmp : gameObject) {
            if (tmp.getTypeNo() == eventTypeNo && tmp.getAttributeInt(subtypeAttribute) == eventSubtype) {
                return tmp;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty(@NotNull final G gameObject) {
        for (final BaseObject<G, A, R, ?> tmp : gameObject) {
            if (tmp.getTypeNo() == eventTypeNo) {
                return false;
            }
        }
        return true;
    }

}
