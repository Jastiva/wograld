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

import net.sf.gridarta.model.baseobject.BaseObject;
import org.jetbrains.annotations.NotNull;

/**
 * Container for settings that affect the rendering of maps.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public interface MapViewSettings {

    /**
     * Register a MapViewSettingsListener.
     * @param listener MapViewSettingsListener to register
     */
    void addMapViewSettingsListener(@NotNull MapViewSettingsListener listener);

    /**
     * Unregister a MapViewSettingsListener.
     * @param listener MapViewSettingsListener to unregister
     */
    void removeMapViewSettingsListener(@NotNull MapViewSettingsListener listener);

    /**
     * Get the visibility of the grid.
     * @return visibility of the grid (<code>true</code> for visible,
     *         <code>false</code> for invisible)
     */
    boolean isGridVisible();

    /**
     * Set the visibility of the grid.
     * @param gridVisible new visibility of the grid (<code>true</code> for
     * making the grid visible, <code>false</code> for invisible)
     */
    void setGridVisible(boolean gridVisible);

    /**
     * Get the visibility of the light.
     * @return visibility of the light (<code>true</code> for visible,
     *         <code>false</code> for invisible)
     */
    boolean isLightVisible();

    /**
     * Set the visibility of the light.
     * @param lightVisible new visibility of the light (<code>true</code> for
     * making the light visible, <code>false</code> for invisible)
     */
    void setLightVisible(boolean lightVisible);

    /**
     * Returns the smoothing setting.
     * @return the smoothing setting
     */
    boolean isSmoothing();

    /**
     * Sets the smoothing setting.
     * @param smoothing the new smoothing setting
     */
    void setSmoothing(boolean smoothing);

    /**
     * Get whether double faces are drawn double height.
     * @return <code>true</code> if double faces are drawn double height,
     *         otherwise <code>false</code>
     */
    boolean isDoubleFaces();

    /**
     * Sets whether double faces should be drawn double height.
     * @param doubleFaces whether double faces should be drawn double height
     */
    void setDoubleFaces(final boolean doubleFaces);

    /**
     * Returns whether the specified edit type is to be shown transparent.
     * @param v edit type to check
     * @return <code>true</code> if to be displayed transparent, otherwise
     *         <code>false</code>
     */
    boolean isAlphaType(int v);

    /**
     * Sets whether the specified edit type is to be shown transparent.
     * @param v edit type to set
     * @param state state, <code>true</code> to make edit type <code>v</code>
     * transparent, <code>false</code> for opaque
     */
    void setAlphaType(int v, boolean state);

    /**
     * Clear the transparency.
     */
    void clearAlpha();

    /**
     * Returns the currently set edit type.
     * @return the currently set edit type
     */
    int getEditType();

    /**
     * Set the map view to hide squares of the given type. (If no edit type is
     * set, everything is displayed)
     * @param editType edit type bitmask of types to hide
     */
    void unsetEditType(int editType);

    /**
     * Get information on the current state of edit type. Are squares of type
     * 'editType' displayed?
     * @param editType are squares of this type displayed?
     * @return <code>true</code> if these squares are currently displayed
     */
    boolean isEditType(int editType);

    /**
     * Get information whether the gameObject is edited. Are squares of type 'v'
     * displayed?
     * @param gameObject are squares of this type displayed?
     * @return true if these squares are currently displayed
     */
    boolean isEditType(@NotNull BaseObject<?, ?, ?, ?> gameObject);

    /**
     * Returns whether a editType value is set so that not all squares are
     * displayed.
     * @return <code>true</code> if a editType value is set, otherwise
     *         <code>false</code>.
     */
    boolean isEditTypeSet();

    /**
     * Toggle an edit type.
     * @param editType the edit type to toggle
     */
    void toggleEditType(int editType);

    /**
     * Returns whether "autojoin" is enabled.
     * @return the autojoin state
     */
    boolean isAutojoin();

    /**
     * Sets the "autojoin" state.
     * @param autojoin if set, enable autojoining
     */
    void setAutojoin(boolean autojoin);

}
