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

package net.sf.gridarta.model.mapviewsettings;

import java.util.EventListener;

/**
 * Interface for event listeners that are interested in changes on {@link
 * MapViewSettings}. Usually you would implement this class in renderers for
 * Maps.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public interface MapViewSettingsListener extends EventListener {

    /**
     * This event handler is called when the grid visibility has changed.
     * @param gridVisible the new grid visibility
     */
    void gridVisibleChanged(boolean gridVisible);

    /**
     * This event handler is called when the .ight visibility has changed.
     * @param lightVisible the new light visibility
     */
    void lightVisibleChanged(boolean lightVisible);

    /**
     * This event handler is called when the smoothing setting has changed.
     * @param smoothing the new smoothing settings
     */
    void smoothingChanged(boolean smoothing);

    /**
     * This event handler is called when the double faces visibility has
     * changed.
     * @param doubleFaces the new double faces visibility
     */
    void doubleFacesChanged(boolean doubleFaces);

    /**
     * This event handler is called when the alpha type setting has changed.
     * @param alphaType the new alpha type setting
     */
    void alphaTypeChanged(int alphaType);

    /**
     * The edit types value has changed.
     * @param editType the new edit types value
     */
    void editTypeChanged(int editType);

    /**
     * The autojoin value has changed.
     * @param autojoin the new autojoin value
     */
    void autojoinChanged(boolean autojoin);

}
