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

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author tchize
 * @author Andreas Kirschbaum
 */
public class PluginModel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterable<Plugin<G, A, R>> {

    @NotNull
    private final Map<String, Plugin<G, A, R>> plugins = new TreeMap<String, Plugin<G, A, R>>();

    /**
     * The {@link PluginModelListener PluginModelListeners} to inform of
     * changes.
     */
    @NotNull
    private final EventListenerList2<PluginModelListener<G, A, R>> listeners = new EventListenerList2<PluginModelListener<G, A, R>>(PluginModelListener.class);

    @Nullable
    public Plugin<G, A, R> getPlugin(@NotNull final String name) {
        return plugins.get(name);
    }

    public int getPluginCount() {
        return plugins.size();
    }

    @Nullable
    public Plugin<G, A, R> getPlugin(final int index) {
        final Iterator<Plugin<G, A, R>> it = plugins.values().iterator();
        Plugin<G, A, R> m = null;
        int i = index;
        while (it.hasNext() && i-- >= 0) {
            m = it.next();
        }
        return i >= 0 ? null : m;
    }

    /**
     * Add a new plugin.
     * @param plugin the plugin to add
     * @return <code>true</code> if the plugin was added, or <code>false</code>
     *         if the plugin name already exists
     */
    public boolean addPlugin(@NotNull final Plugin<G, A, R> plugin) {
        if (plugins.containsKey(plugin.getName())) {
            return false;
        }

        plugins.put(plugin.getName(), plugin);
        firePluginCreatedEvent(plugin);
        firePluginRegisteredEvent(plugin);
        return true;
    }

    public void removePlugin(@NotNull final Plugin<G, A, R> plugin) {
        if (!plugins.containsKey(plugin.getName())) {
            throw new IllegalArgumentException();
        }

        firePluginUnregisteredEvent(plugin);
        firePluginDeletedEvent(plugin);
        plugins.remove(plugin.getName());
    }

    /**
     * Adds a listener to be informed of changes.
     * @param listener the listener
     */
    public void addPluginModelListener(@NotNull final PluginModelListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener to be informed of changes.
     * @param listener the listener
     */
    public void removePluginModelListener(@NotNull final PluginModelListener<G, A, R> listener) {
        listeners.remove(listener);
    }

    /**
     * Notify all listeners about an added plugin.
     * @param plugin the added plugin
     */
    private void firePluginCreatedEvent(@NotNull final Plugin<G, A, R> plugin) {
        for (final PluginModelListener<G, A, R> listener : listeners.getListeners()) {
            listener.pluginCreated(plugin);
        }
    }

    /**
     * Notifies all listeners about a removed plugin.
     * @param plugin the removed plugin
     */
    private void firePluginDeletedEvent(@NotNull final Plugin<G, A, R> plugin) {
        for (final PluginModelListener<G, A, R> listener : listeners.getListeners()) {
            listener.pluginDeleted(plugin);
        }
    }

    /**
     * Notifies all listeners about a registered plugin.
     * @param plugin the registered plugin
     */
    private void firePluginRegisteredEvent(@NotNull final Plugin<G, A, R> plugin) {
        for (final PluginModelListener<G, A, R> listener : listeners.getListeners()) {
            listener.pluginRegistered(plugin);
        }
    }

    /**
     * Notifies all listeners about an unregistered plugin.
     * @param plugin the unregistered plugin
     */
    private void firePluginUnregisteredEvent(@NotNull final Plugin<G, A, R> plugin) {
        for (final PluginModelListener<G, A, R> listener : listeners.getListeners()) {
            listener.pluginUnregistered(plugin);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<Plugin<G, A, R>> iterator() {
        return Collections.unmodifiableCollection(plugins.values()).iterator();
    }

    public void reRegister(@NotNull final Plugin<G, A, R> plugin) {
        firePluginUnregisteredEvent(plugin);
        firePluginRegisteredEvent(plugin);
    }

}
