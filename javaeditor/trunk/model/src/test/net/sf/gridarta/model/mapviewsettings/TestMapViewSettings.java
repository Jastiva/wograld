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

/**
 * A {@link MapViewSettings} implementation for regression tests. The attribute
 * values are not retained.
 * @author Andreas Kirschbaum
 */
public class TestMapViewSettings extends AbstractMapViewSettings {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadGridVisible() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveGridVisible(final boolean gridVisible) {
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadLightVisible() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveLightVisible(final boolean lightVisible) {
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadSmoothing() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSmoothing(final boolean smoothing) {
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadDoubleFaces() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveDoubleFaces(final boolean doubleFaces) {
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int loadAlphaType() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveAlphaType(final int alphaType) {
        // ignore
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
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadAutojoin() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveAutojoin(final boolean autojoin) {
        // ignore
    }

}
