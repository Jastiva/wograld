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

package net.sf.gridarta.model.pickmapsettings;

import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for {@link PickmapSettings} implementations.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractPickmapSettings implements PickmapSettings {

    /**
     * The MapViewSettingsListeners to inform of changes.
     */
    @NotNull
    private final EventListenerList2<PickmapSettingsListener> listenerList = new EventListenerList2<PickmapSettingsListener>(PickmapSettingsListener.class);

    /**
     * The immutable state.
     */
    private boolean locked = loadLocked();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPickmapSettingsListener(@NotNull final PickmapSettingsListener listener) {
        listenerList.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePickmapSettingsListener(@NotNull final PickmapSettingsListener listener) {
        listenerList.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLocked() {
        return locked;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocked(final boolean locked) {
        if (this.locked == locked) {
            return;
        }

        this.locked = locked;
        saveLocked(locked);
        fireLockedChanged();
    }

    /**
     * Informs all registered listeners that the immutable state has changed.
     */
    private void fireLockedChanged() {
        for (final PickmapSettingsListener listener : listenerList.getListeners()) {
            listener.lockedChanged(locked);
        }
    }

    /**
     * Loads the default value for {@link #locked}.
     * @return the default value
     */
    protected abstract boolean loadLocked();

    /**
     * Saves the {@link #locked} value.
     * @param locked the locked value
     */
    protected abstract void saveLocked(final boolean locked);

}
