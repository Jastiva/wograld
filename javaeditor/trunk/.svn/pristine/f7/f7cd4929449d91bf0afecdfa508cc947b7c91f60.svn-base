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

package net.sf.gridarta.plugin;

import java.util.EventListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Listener interface for scripting related events.
 * @author Andreas Kirschbaum
 */
public interface PluginModelListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends EventListener {

    /**
     * Notifies about the creation of a new plugin.
     * @param plugin the created plugin
     */
    void pluginCreated(@NotNull Plugin<G, A, R> plugin);

    /**
     * Notifies about the deletion of a plugin.
     * @param plugin the deleted plugin
     */
    void pluginDeleted(@NotNull Plugin<G, A, R> plugin);

    /**
     * Notifies about the registering of a plugin.
     * @param plugin the registered plugin
     */
    void pluginRegistered(@NotNull Plugin<G, A, R> plugin);

    /**
     * Notifies about the unregistering of a plugin.
     * @param plugin the unregistered plugin
     */
    void pluginUnregistered(@NotNull Plugin<G, A, R> plugin);

}
