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

package net.sf.gridarta.plugin.parameter;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parameter for a Plugin.
 * @author tchize
 */
public abstract class AbstractPluginParameter<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>, V> implements PluginParameter<G, A, R> {

    @Nullable
    private V value;

    @NotNull
    private String description = "[description]";

    @NotNull
    private String name = "[name]";

    @NotNull
    private final EventListenerList2<PluginParameterListener<G, A, R>> listeners = new EventListenerList2<PluginParameterListener<G, A, R>>(PluginParameterListener.class);

    protected AbstractPluginParameter() {
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDescription(@NotNull final String description) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(@NotNull final String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public V getValue() {
        return value;
    }

    public boolean setValue(@Nullable final V value) {
        if (this.value == null ? value == null : this.value.equals(value)) {
            return false;
        }

        this.value = value;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPluginParameterListener(@NotNull final PluginParameterListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePluginParameterListener(@NotNull final PluginParameterListener<G, A, R> listener) {
        listeners.remove(listener);
    }

    protected void fireDataChanged() {
        for (final PluginParameterListener<G, A, R> listener : listeners.getListeners()) {
            listener.stateChanged(this);
        }
    }

}
