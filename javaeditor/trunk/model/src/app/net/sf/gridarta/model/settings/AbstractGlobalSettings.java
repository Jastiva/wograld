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

package net.sf.gridarta.model.settings;

import java.io.File;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for {@link GlobalSettings} implementations.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractGlobalSettings implements GlobalSettings {

    /**
     * The {@link GlobalSettingsListener GlobalSettingsListeners} to inform of
     * changes.
     */
    @NotNull
    private final EventListenerList2<GlobalSettingsListener> listenerList = new EventListenerList2<GlobalSettingsListener>(GlobalSettingsListener.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void addGlobalSettingsListener(@NotNull final GlobalSettingsListener listener) {
        listenerList.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeGlobalSettingsListener(@NotNull final GlobalSettingsListener listener) {
        listenerList.remove(listener);
    }

    /**
     * Notifies all listeners about a changed visibility of the main toolbar.
     */
    protected void fireShowMainToolbarChanged() {
        final boolean visible = isShowMainToolbar();
        for (final GlobalSettingsListener listener : listenerList.getListeners()) {
            listener.showMainToolbarChanged(visible);
        }
    }

    /**
     * Notifies all listeners about a changed maps directory.
     */
    protected void fireMapsDirectoryChanged() {
        final File mapsDirectory = getMapsDirectory();
        for (final GlobalSettingsListener listener : listenerList.getListeners()) {
            listener.mapsDirectoryChanged(mapsDirectory);
        }
    }

}
