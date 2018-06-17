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

package net.sf.gridarta.var.atrinik.model.archetype;

import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * <code>Archetype</code> implements Atrinik archetypes.
 * @author Andreas Kirschbaum
 */
public interface Archetype extends net.sf.gridarta.model.archetype.Archetype<GameObject, MapArchObject, Archetype> {

    int TYPE_PEDESTAL = 17;

    int TYPE_ALTAR = 18;

    int TYPE_LOCKED_DOOR = 20;

    int TYPE_SPECIAL_KEY = 21;

    int TYPE_TIMED_GATE = 26;

    int TYPE_HANDLE_TRIGGER = 27;

    int TYPE_TRIGGER_BUTTON = 30;

    int TYPE_ALTAR_TRIGGER = 31;

    int TYPE_TRIGGER_PEDESTAL = 32;

    int TYPE_TELEPORTER = 41;

    int TYPE_CREATOR = 42;

    int TYPE_DETECTOR = 51;

    int TYPE_MARKER = 55;

    int TYPE_MAGIC_WALL = 62;

    int TYPE_INVENTORY_CHECKER = 64;

    int TYPE_MOOD_FLOOR = 65;

    int TYPE_EXIT = 66;

    int TYPE_SHOP_FLOOR = 68;

    int TYPE_FLOOR = 71;

    int TYPE_APPLYABLE_LIGHT = 74;

    int TYPE_LIGHT_SOURCE = 78;

    int TYPE_MOB = 80;

    int TYPE_SPAWN_POINT = 81;

    int TYPE_SPAWN_POINT_MOB = 83;

    int TYPE_GATE = 91;

    int TYPE_BUTTON = 92;

    int TYPE_HANDLE = 93;

    int TYPE_PIT = 94;

    int TYPE_SIGN_MAGIC_MOUTH = 98;

    int TYPE_DIRECTOR = 112;

    int TYPE_EVENT_OBJECT = 118;

    int TYPE_CONTAINER = 122;

    int TYPE_TIMER = 132;

    int TYPE_ENVIRONMENT_SENSOR = 133;

    int TYPE_CONNECTION_SENSOR = 134;

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    Archetype clone();

}
