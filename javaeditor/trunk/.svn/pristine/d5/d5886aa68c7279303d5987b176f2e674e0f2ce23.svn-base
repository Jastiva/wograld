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

import java.util.prefs.Preferences;
import net.sf.gridarta.MainControl;
import org.jetbrains.annotations.NotNull;

/**
 * Default {@link MapViewSettings} implementation. Attributes are retained
 * across editor restarts. The attributes are stored in the {@link
 * Preferences}.
 * @author Andreas Kirschbaum
 */
public class DefaultMapViewSettings extends AbstractMapViewSettings {

    /**
     * Key for saving {@link #gridVisible} state in preferences.
     */
    @NotNull
    private static final String GRID_VISIBLE_KEY = "mapViewSettings.gridVisible";

    /**
     * Key for saving {@link #lightVisible} state in preferences.
     */
    @NotNull
    private static final String LIGHT_VISIBLE_KEY = "mapViewSettings.lightVisible";

    /**
     * Key for saving {@link #smoothing} state in preferences.
     */
    @NotNull
    private static final String SMOOTHING_KEY = "mapViewSettings.smoothing";

    /**
     * Key for saving {@link #doubleFaces} state in preferences.
     */
    @NotNull
    private static final String DOUBLE_FACES_KEY = "mapViewSettings.doubleFaces";

    /**
     * Key for saving {@link #alphaType} state in preferences.
     */
    @NotNull
    private static final String ALPHA_TYPE_KEY = "mapViewSettings.alphaType";

    /**
     * Key for saving {@link #autojoin} state in preferences.
     */
    @NotNull
    private static final String AUTOJOIN_KEY = "mapViewSettings.autojoin";

    /**
     * Preferences.
     */
    @NotNull
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadGridVisible() {
        return preferences.getBoolean(GRID_VISIBLE_KEY, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveGridVisible(final boolean gridVisible) {
        preferences.putBoolean(GRID_VISIBLE_KEY, gridVisible);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadLightVisible() {
        return preferences.getBoolean(LIGHT_VISIBLE_KEY, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveLightVisible(final boolean lightVisible) {
        preferences.putBoolean(LIGHT_VISIBLE_KEY, lightVisible);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadSmoothing() {
        return preferences.getBoolean(SMOOTHING_KEY, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSmoothing(final boolean smoothing) {
        preferences.putBoolean(SMOOTHING_KEY, smoothing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadDoubleFaces() {
        return preferences.getBoolean(DOUBLE_FACES_KEY, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveDoubleFaces(final boolean doubleFaces) {
        preferences.putBoolean(DOUBLE_FACES_KEY, doubleFaces);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int loadAlphaType() {
        return preferences.getInt(ALPHA_TYPE_KEY, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveAlphaType(final int alphaType) {
        preferences.putInt(ALPHA_TYPE_KEY, alphaType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int loadEditType() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveEditType(final int editType) {
        // ignore: retaining edit types would be confusing for the user
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadAutojoin() {
        return preferences.getBoolean(AUTOJOIN_KEY, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveAutojoin(final boolean autojoin) {
        preferences.putBoolean(AUTOJOIN_KEY, autojoin);
    }

}
